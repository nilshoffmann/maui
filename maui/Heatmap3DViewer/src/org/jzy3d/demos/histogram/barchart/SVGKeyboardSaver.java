/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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
package org.jzy3d.demos.histogram.barchart;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jzy3d.chart.Chart;

/**
 *
 * @author ao
 */
public class SVGKeyboardSaver extends KeyAdapter {

    private final Chart chart;

    public SVGKeyboardSaver(Chart chart) {
        this.chart = chart;
    }

	@Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()) {
            JFileChooser jfc = new JFileChooser(".");
            jfc.setFileFilter(new FileNameExtensionFilter("PNG file", new String[]{".png"}));
            jfc.showSaveDialog(e.getComponent());
            
            if(jfc.getSelectedFile()!=null){
		if(!jfc.getSelectedFile().getParentFile().exists())
			jfc.getSelectedFile().mkdirs();
                try {
					File temp = new File(jfc.getSelectedFile().toString()+".png");
					chart.screenshot(temp);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(e.getComponent(), "Error saving file.");
                    Logger.getLogger(SVGKeyboardSaver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
