package de.unibielefeld.cebitec.lstutz.pca.visual;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.vecmath.Color3f;

import de.unibielefeld.cebitec.lstutz.pca.data.DataModel;
import de.unibielefeld.cebitec.lstutz.pca.data.Heading;

public class GroupPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public GroupPanel(HashMap<String,ArrayList<DataModel>> hash, StandardGUI gui){
		this.setPreferredSize(new Dimension(840,30));
		this.setBackground(new Color(51,51,51));
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		for(String s : hash.keySet()){
			Checkbox bla = new Checkbox();
			bla.setState(true);
			bla.setBackground(get_color(s));
			bla.setName(s);
			bla.addItemListener(gui);
			this.add(bla);
		}
		this.set_colors();
	}
	
	private Color get_color(String s){
		String[] split = s.split(",");
		return new Color(Float.parseFloat(split[0]),
						 Float.parseFloat(split[1]),
						 Float.parseFloat(split[2]));
	}
	
	private Color create_complement(Color c){
		return new Color(255-c.getRed(),255-c.getGreen(),255-c.getBlue());
	}
	
	public void set_background(Color3f c){
		this.setBackground(new Color((int)(c.x*255),(int)(c.y*255),(int)(c.z*255)));
		this.repaint();
	}
	
	public void set_colors(){
		//achsen.setForeground(create_complement(this.getBackground()));
		this.repaint();
	}
}
