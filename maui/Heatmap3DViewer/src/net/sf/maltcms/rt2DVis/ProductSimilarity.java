/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.rt2DVis;

/**
 *
 * @author Nils Hoffmann
 */
public class ProductSimilarity {
	private IScalarSimilarity[] similarities;
	public ProductSimilarity(IScalarSimilarity ... similarities) {
		this.similarities = similarities;
	}
	
	public double apply(double[] mu, double[] values) {
		if(values.length!=similarities.length || mu.length!=similarities.length) {
			throw new IllegalArgumentException();
            }
		double product = 1.0d;
		for (int i = 0; i < values.length; i++) {
			product*=similarities[i].apply(values[i],mu[i]);
		}
		return product;
	}
}
