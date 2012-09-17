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
