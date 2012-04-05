/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Actions",
id = "net.sf.maltcms.chromaui.project.spi.actions.RenamePeakGroup")
@ActionRegistration(displayName = "#CTL_RenamePeakGroup")
@ActionReferences({@ActionReference(path="Actions/DescriptorNodeActions/IPeakGroupDescriptor")})
@Messages("CTL_RenamePeakGroup=Rename")
public final class RenamePeakGroup implements ActionListener {

    private final IPeakGroupDescriptor context;

    public RenamePeakGroup(IPeakGroupDescriptor context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        NotifyDescriptor.InputLine nd = new NotifyDescriptor.InputLine("Peak Group Name","Rename");
        nd.setInputText(context.getName());
        // let's display the dialog now...
        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            String newName = nd.getInputText().trim();
            RenameRunnable rr = new RenameRunnable(newName,context);
            RenameRunnable.createAndRun(newName, rr);
        } 
    }
    
    class RenameRunnable extends AProgressAwareRunnable {

        private final String newName;
        private final IPeakGroupDescriptor context;
        
        public RenameRunnable(String newName, IPeakGroupDescriptor context) {
            this.newName = newName;
            this.context = context;
        }
        
        @Override
        public void run() {
            ProgressHandle ph = getProgressHandle();
            ph.start(context.getPeakAnnotationDescriptors().size());
            int i = 1;
            try{
                ph.progress("Renaming peaks...");
                for(IPeakAnnotationDescriptor peak:context.getPeakAnnotationDescriptors()) {
                    ph.progress(i++);
                    peak.setName(newName+" ");
                    peak.setDisplayName(newName+" (User Defined)");
                    peak.setCas("");
                    peak.setFormula("");
                    peak.setLibrary("User Defined");
                    peak.setSimilarity(Double.NaN);
                    peak.setMethod("Manual Annotation");
                }
                context.setDisplayName(newName+" (User Defined)");
                context.setName(newName);
                context.setShortDescription(context.createDisplayName(context.getPeakAnnotationDescriptors()).toString());
            }finally{
                ph.finish();
            }
        }    
    }
}
