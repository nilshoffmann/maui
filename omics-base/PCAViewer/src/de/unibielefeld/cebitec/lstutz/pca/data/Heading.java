package de.unibielefeld.cebitec.lstutz.pca.data;

public class Heading {
	
	private String heading_x;
	private String heading_y;
	private String heading_z;
	
	public Heading(String x, String y, String z){
		this.heading_x=x;
		this.heading_y=y;
		this.heading_z=z;
	}
	
	public Heading(){
		
	}

	public String getHeading_x() {
		return heading_x;
	}
	
	public String getHeading_y() {
		return heading_y;
	}
	
	public String getHeading_z() {
		return heading_z;
	}

	public void setHeading_x(String heading_x) {
		this.heading_x = heading_x;
	}
	
	public void setHeading_y(String heading_y) {
		this.heading_y = heading_y;
	}
	
	public void setHeading_z(String heading_z) {
		this.heading_z = heading_z;
	}
}
