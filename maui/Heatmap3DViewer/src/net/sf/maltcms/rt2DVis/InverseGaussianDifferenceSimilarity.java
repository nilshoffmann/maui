/* 
 * Maltcms, modular application toolkit for chromatography-mass spectrometry. 
 * Copyright (C) 2008-2012, The authors of Maltcms. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maltcms may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maltcms, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maltcms is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
package net.sf.maltcms.rt2DVis;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class InverseGaussianDifferenceSimilarity implements
		IScalarSimilarity {

//    private double tolerance = 5.0d;
	private double threshold = 0.0d;
	private double lambda = 1.0d;
	private double offset = 1.0d;

	/**
	 * Calculates the scalar
	 *
	 * @param time1
	 * @param time2
	 * @return
	 */
	@Override
	public double apply(double time1, double time2) {
		// if no time is supplied, use 1 as default -> cosine/dot product
		// similarity
		if((time1 == -1) || (time2 == -1)) {
			return 1.0d;
		}
//		if(time1<time2) {
//			return apply(time2,time1);
//		}
		final double a = Math.pow(time1-offset-time2, 2);
//		System.out.println("a="+a);
		final double b = (-lambda)*a;
//		System.out.println("b="+b);
		double c = 2.0d*Math.pow(time2,2)*(time1-offset);
//		System.out.println("c="+c);
		double d = Math.exp(b/c);
//		System.out.println("d="+d);
//		final double weight = Math.exp(
//				(-(lambda * (time1 - offset - time2) * (time1 - offset - time2)) / (2.0d * time2 * time2 * (time1 - offset))));
		// 1 for perfect time correspondence, 0 for really bad time
		// correspondence (towards infinity)
		
//		if (normalize) {
			double e = lambda/2.0d * Math.PI * Math.pow(time1 - offset, 3.0d);
//			System.out.println("e="+e);
			double value = d * Math.sqrt(e);
//			System.out.println("value at " + time1 + " " + time2 + "=" + value);
			d = value;
//		}
		if (d - this.threshold < 0) {
			return 0;//Double.NEGATIVE_INFINITY;
		}
		return d;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}
	
	public double getOffset() {
		return offset;
	}

	public void setOffset(double offset) {
		this.offset = offset;
	}
	
}
