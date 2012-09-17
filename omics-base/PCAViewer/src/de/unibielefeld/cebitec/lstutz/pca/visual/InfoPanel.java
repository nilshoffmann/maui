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
package de.unibielefeld.cebitec.lstutz.pca.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.vecmath.Color3f;

import de.unibielefeld.cebitec.lstutz.pca.data.DataModel;
import de.unibielefeld.cebitec.lstutz.pca.data.Heading;
import org.openide.util.NotImplementedException;

public class InfoPanel extends JPanel implements MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel achsen, info, label, x, y, z, color, view, export;
	private Link link;
	
	public InfoPanel(Heading h, StandardGUI g){
		this.setPreferredSize(new Dimension(200,480));
		this.setBackground(new Color(51,51,51));
		this.setLayout(new GridLayout(19,1,0,2));
		achsen = new JLabel("Axis labeling:");
		this.add(achsen);
		JLabel heading_x = new JLabel(h.getHeading_x());
		heading_x.setForeground(Color.green);
		this.add(heading_x);
		JLabel heading_y = new JLabel(h.getHeading_y());
		heading_y.setForeground(Color.red);
		this.add(heading_y);
		JLabel heading_z = new JLabel(h.getHeading_z());
		heading_z.setForeground(Color.blue);
		this.add(heading_z);
		info = new JLabel("Current Selection:");
		this.add(info);
		label = new JLabel("Nothing selected");
		this.add(label);
		link = new Link("Nothing selected", "http://");
		link.addMouseListener(this);
		this.add(link);
		x = new JLabel(" ");
		this.add(x);
		y = new JLabel(" ");
		this.add(y);
		z = new JLabel(" ");
		this.add(z);
		view = new JLabel("Change view:");
		this.add(view);
		JButton xy = new JButton("View XY");
		xy.setActionCommand("xy");
		xy.addActionListener(g);
		this.add(xy);
		JButton xz = new JButton("View XZ");
		xz.setActionCommand("xz");
		xz.addActionListener(g);
		this.add(xz);
		JButton yz = new JButton("View YZ");
		yz.setActionCommand("yz");
		yz.addActionListener(g);
		this.add(yz);
		color = new JLabel("Colors:");
		this.add(color);
		JButton change_background = new JButton("Change background color");
		change_background.setActionCommand("change background");
		change_background.addActionListener(g);
		this.add(change_background);
		export = new JLabel("Export:");
		this.add(export);
		JButton export_button = new JButton("Export to png file");
		export_button.setActionCommand("export");
		export_button.addActionListener(g);
		this.add(export_button);

		JButton clip_button = new JButton("Export info to clipboard");
		clip_button.setActionCommand("clip");
		clip_button.addActionListener(g);
		this.add(clip_button);
		
		this.set_colors();
	}
	
	public void set_coords(DataModel da){
		this.label.setText("Label = "+da.getLabel());
		this.label.setToolTipText(da.getAnnotation());
		this.x.setText("x = "+da.getCoordinates().get(0));
		this.y.setText("y = "+da.getCoordinates().get(1));
		this.z.setText("z = "+da.getCoordinates().get(2));
		this.link.setText("<html><head></head><body><u>Link to...</u></body></html>");
		this.link.setLink(da.getLink());
	}
	
	private Color create_complement(Color c){
		return new Color(255-c.getRed(),255-c.getGreen(),255-c.getBlue());
	}
	
	public void set_background(Color3f c){
		this.setBackground(new Color((int)(c.x*255),(int)(c.y*255),(int)(c.z*255)));
		this.repaint();
	}
	
	public void set_colors(){
		achsen.setForeground(create_complement(this.getBackground()));
		label.setForeground(create_complement(this.getBackground()));
		color.setForeground(create_complement(this.getBackground()));
		view.setForeground(create_complement(this.getBackground()));
		x.setForeground(create_complement(this.getBackground()));
		y.setForeground(create_complement(this.getBackground()));
		z.setForeground(create_complement(this.getBackground()));
		info.setForeground(create_complement(this.getBackground()));
		export.setForeground(create_complement(this.getBackground()));
		link.setForeground(create_complement(this.getBackground()));
		this.repaint();
	}


	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == link){
                    throw new NotImplementedException();
//			try {
//				//Desktop.getDesktop().browse(new URI( ((Link)e.getSource()).getLink() ));
//		        BasicService bs = (BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
//		        // Invoke the showDocument method
//		        bs.showDocument(new URL(((Link)e.getSource()).getLink())); 
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
		}
	}


	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
