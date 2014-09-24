/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
package net.sf.maltcms.chromaui.ui.support.api;

import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.ListView;

/**
 *
 * @author Nils Hoffmann
 */
public class DialogPanel extends JPanel implements ExplorerManager.Provider {

    private ExplorerManager em = new ExplorerManager();

    /**
     *
     * @param label
     * @param singleSelectionOnly
     */
    public void init(String label, boolean singleSelectionOnly) {
        removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel jlabel = new JLabel(label);
        jlabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(jlabel);
        add(Box.createVerticalStrut(10));
        ListView cv = new ListView();
        if (singleSelectionOnly) {
            cv.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        } else {
            cv.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        }
        JScrollPane jsp = new JScrollPane(cv);
        jsp.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(jsp);
    }

    /**
     *
     * @return
     */
    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
}
