/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.ui;

import java.awt.FlowLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.ListView;

/**
 *
 * @author hoffmann
 */
public class DialogPanel extends JPanel implements ExplorerManager.Provider {

    private ExplorerManager em = new ExplorerManager();

    public void init(String label, boolean singleSelectionOnly) {
        removeAll();
        setLayout(new FlowLayout(FlowLayout.LEADING));
        add(new JLabel(label));
        ListView cv = new ListView();
        if(singleSelectionOnly) {
            cv.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        }else{
            cv.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        }
        add(cv);
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
}
