/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.actions;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;

import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.ListView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;

@ActionID(category = "Maui",
id = "net.sf.maltcms.chromaui.project.spi.actions.DeletePeakAnnotationsAction")
@ActionRegistration(displayName = "#CTL_DeletePeakAnnotationsAction")
@ActionReferences({
    @ActionReference(path = "Actions/ChromAUIProjectLogicalView")})
@Messages("CTL_DeletePeakAnnotationsAction=Remove Peak Annotations")
public final class DeletePeakAnnotationsAction implements ActionListener {

    private final IChromAUIProject context;

    public DeletePeakAnnotationsAction(IChromAUIProject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        List<Peak1DContainer> peakContainers = new ArrayList<Peak1DContainer>();
        final Set<IToolDescriptor> itd = new LinkedHashSet<IToolDescriptor>();
        for (IChromatogramDescriptor chrom : context.getChromatograms()) {
            for (Peak1DContainer container : context.getPeaks(chrom)) {
                itd.add(container.getTool());
            }
        }

        DialogPanel dp = new DialogPanel();
        dp.init("Peak Annotations of Tool: ");
        dp.getExplorerManager().setRootContext(new AbstractNode(Children.create(new ChildFactory<IToolDescriptor>() {

            @Override
            protected boolean createKeys(List list) {
                list.addAll(itd);
                return true;
            }

            @Override
            protected Node createNodeForKey(IToolDescriptor key) {
                Node customerNode = new AbstractNode(
                        Children.LEAF, Lookups.singleton(key));
                customerNode.setDisplayName(key.getName());
                return customerNode;
            }
        }, true)));

        NotifyDescriptor.Confirmation nd = new NotifyDescriptor.Confirmation(dp, "Select Tool Results for Deletion");
        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            Collection<? extends IToolDescriptor> tools = dp.getExplorerManager().getSelectedNodes()[0].getLookup().lookupAll(IToolDescriptor.class);
            itd.clear();
            itd.addAll(tools);
            for (IChromatogramDescriptor chrom : context.getChromatograms()) {
                for (Peak1DContainer container : context.getPeaks(chrom)) {
                    if (tools.contains(container.getTool())) {
                        peakContainers.add(container);
                    }
                }
            }

            context.removeContainer(peakContainers.toArray(new Peak1DContainer[peakContainers.size()]));
            context.refresh();
        }
    }
    
    private class DialogPanel extends JPanel implements ExplorerManager.Provider {

        private ExplorerManager em = new ExplorerManager();

        public void init(String label) {
            removeAll();
            setLayout(new FlowLayout(FlowLayout.LEADING));
            add(new JLabel(label));
            ListView cv = new ListView();
            cv.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            add(cv);
        }
        
        @Override
        public ExplorerManager getExplorerManager() {
            return em;
        }
        
    }

}
