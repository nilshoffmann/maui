/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.heatmap3Dviewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.sf.maltcms.maui.heatmapViewer.HeatmapViewer;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Tools",
id = "maltcms.ui.heatmap3Dviewer.TestJzy3D")
@ActionRegistration(displayName = "#CTL_TestJzy3D")
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 1250, separatorBefore = 1225)
})
@Messages("CTL_TestJzy3D=Test Jzy3D")
public final class TestJzy3D implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        HeatmapViewer.main(new String[0]);
    }
}
