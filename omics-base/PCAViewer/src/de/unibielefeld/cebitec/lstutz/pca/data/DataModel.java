package de.unibielefeld.cebitec.lstutz.pca.data;

import java.util.ArrayList;

import javax.vecmath.Color3f;

public class DataModel{
	
	private String label;
	private Color3f color;
	private String shape;
	private ArrayList<Double> coordinates = new ArrayList<Double>();
	private Heading heading;
	private Double factor;
	private String annotation, link;

	public DataModel(){
		
	}
	
	public DataModel(String label, Color3f color, String shape, ArrayList<Double> coordinates){
		this.label=label;
		this.color=color;
		this.shape=shape;
		this.coordinates=coordinates;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Color3f getColor() {
		return color;
	}

	public void setColor(Color3f color) {
		this.color = color;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public ArrayList<Double> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(ArrayList<Double> coordinates) {
		this.coordinates = coordinates;
	}
	
	public Heading getHeading() {
		return heading;
	}
	
	public void setHeading(Heading heading) {
		this.heading = heading;
	}

	public Double getFactor() {
		return factor;
	}

	public void setFactor(Double factor) {
		this.factor = factor;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	
}
