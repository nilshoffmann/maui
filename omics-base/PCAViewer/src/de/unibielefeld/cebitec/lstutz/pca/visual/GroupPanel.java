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
package de.unibielefeld.cebitec.lstutz.pca.visual;

import de.unibielefeld.cebitec.lstutz.pca.data.DataModel;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.vecmath.Color3f;

public class GroupPanel extends JPanel {

    /**
     *
     *
     *
     */
    private static final long serialVersionUID = 1L;

    private JLabel label = null;

    public GroupPanel(HashMap<String, ArrayList<DataModel>> hash, StandardGUI gui) {
//        this.setPreferredSize(new Dimension(840, 30));
//        this.setBackground(new Color(51, 51, 51));
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        for (String s : hash.keySet()) {
            Checkbox bla = new Checkbox();
            bla.setState(true);
            bla.setBackground(get_color(s));
            bla.setName(s);
            bla.addItemListener(gui);
            this.add(bla);
        }
        label = new JLabel("Selected:");
        label.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
        this.add(new JSeparator(JSeparator.VERTICAL));
        this.add(Box.createHorizontalGlue());
        this.add(label);
        this.set_colors();
    }

    private Color get_color(String s) {
        String[] split = s.split(",");
        return new Color(Float.parseFloat(split[0]),
                Float.parseFloat(split[1]),
                Float.parseFloat(split[2]));
    }

    private Color create_complement(Color c) {
        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
    }

    public void set_background(Color3f c) {
        this.setBackground(new Color((int) (c.x * 255), (int) (c.y * 255), (int) (c.z * 255)));
        this.repaint();
    }

    public void set_coords(DataModel da) {
        this.label.setText("Selected: "+get40CharString(da.getLabel()));
        this.label.setToolTipText("<html><p>" + da.getLabel() + "</p></html>");
    }

    public String get40CharString(String label) {
        if (label.length() > 37) {
            String shortString = label.substring(0, Math.min(37, label.length()));
            return shortString + "...";
        }
        return label;
    }

    public void set_colors() {
        label.setForeground(create_complement(this.getBackground()));
        //achsen.setForeground(create_complement(this.getBackground()));
        this.repaint();
    }
}
