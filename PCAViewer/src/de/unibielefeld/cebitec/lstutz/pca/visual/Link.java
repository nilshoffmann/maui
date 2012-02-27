package de.unibielefeld.cebitec.lstutz.pca.visual;

import javax.swing.JLabel;

public class Link extends JLabel{
	
	private String link;
	
	public Link(String label, String link){
		super(label);
		this.link = link;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
}
