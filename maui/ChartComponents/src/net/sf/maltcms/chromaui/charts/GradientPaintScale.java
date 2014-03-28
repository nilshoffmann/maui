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
package net.sf.maltcms.chromaui.charts;

import java.awt.Color;
import java.awt.Component;
import java.awt.Paint;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import org.apache.commons.configuration.Configuration;
import org.jfree.chart.renderer.PaintScale;

import cross.IConfigurable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import maltcms.io.csv.ColorRampReader;
import maltcms.tools.ImageTools;
import org.openide.util.Exceptions;

/**
 * @author Nils Hoffmann
 *
 *
 */
public class GradientPaintScale implements PaintScale, IConfigurable,
		Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1121734275349109897L;
	private int[][] ramp = null;
	private double[] sampleTable = null;
	private double[] breakPoints = null;
	private double[] st = null;
	private double min = 0.0d;
	private double max = 1.0d;
	private double alpha = 0.0d;
	private double beta = 1.0d;
	private BufferedImage lookupImage;
	private Color[] colors;
	private Color[] lookupColors;
	private double thresholdLow = Double.NaN;
	private double thresholdHigh = Double.NaN;
	private int thresholdLowIndex = 0;
	private int thresholdHighIndex = 0;
	private String label = "<NA>";

	@Deprecated
	public GradientPaintScale(double[] sampleTable, double[] breakPoints,
			String colorRampLocation, double min, double max) {
		this(sampleTable, breakPoints, new ColorRampReader().readColorRamp(colorRampLocation), min, max);
	}

	@Deprecated
	public GradientPaintScale(double[] sampleTable, double[] breakPoints,
			int[][] colorRamp, double min, double max) {
		this.sampleTable = sampleTable;
		this.st = this.sampleTable;
		this.breakPoints = breakPoints;
//        this.st = this.breakPoints;
		this.ramp = colorRamp;
		this.min = min;
		this.max = max;
		this.thresholdLow = min;
		this.thresholdHigh = max;
		this.colors = ImageTools.rampToColorArray(colorRamp);
		this.lookupImage = createLookupImage(this.st, this.colors);
		this.lookupColors = createLookupColors();
	}

	@Deprecated
	public GradientPaintScale(double[] sampleTable, double[] breakPoints,
			double min, double max) {
		this(sampleTable, breakPoints, new ColorRampReader().getDefaultRamp(),
				min, max);
	}

	public GradientPaintScale(double[] sampleTable, double min, double max,
			Color[] colors) {
		this.sampleTable = sampleTable;
		this.st = this.sampleTable;
		this.min = min;
		this.max = max;
		this.thresholdLow = min;
		this.thresholdHigh = max;
		this.colors = colors;
		this.lookupImage = createLookupImage(this.st, this.colors);
		this.lookupColors = createLookupColors();
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}

	private BufferedImage createLookupImage(double[] sampleTable,
			Color... colors) {
		// System.out.println("" + sampleTable.length + " samples");
		// System.out.println("" + colors.length + " colors");
		return ImageTools.createColorRampImage(sampleTable,
				BufferedImage.TRANSLUCENT, colors);
	}

	private void update() {
		this.lookupImage = ImageTools.createModifiedLookupImage(this.colors,
				this.st, this.alpha, this.beta, Transparency.TRANSLUCENT, 1.0f);
		this.lookupColors = createLookupColors();
	}

	public void setRamp(int[][] r) {
		this.ramp = r;
		this.colors = ImageTools.rampToColorArray(this.ramp);
		update();
	}

	public int[][] getRamp() {
		return this.ramp;
	}

	public double getAlpha() {
		return this.alpha;
	}

	public double getBeta() {
		return this.beta;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
		update();
	}

	public void setBeta(double beta) {
		this.beta = beta;
		update();
	}

	public void setAlphaBeta(double alpha, double beta) {
		this.alpha = alpha;
		this.beta = beta;
		update();
	}

	private Color[] createLookupColors() {
		Color[] c = new Color[this.st.length];
		for (int i = 0; i < c.length; i++) {
			c[i] = new Color(this.lookupImage.getRGB(i, 0));
		}
		return c;
	}

	public void setUpperBound(double ub) {
		this.max = ub;
	}

	public void setLowerBound(double lb) {
		this.min = lb;
	}

	public void setLowerBoundThreshold(double value) {
		this.thresholdLow = value;
	}

	public void setUpperBoundThreshold(double value) {
		this.thresholdHigh = value;
	}

	@Override
	public double getUpperBound() {
		return this.max;
	}

	public double getValueForIndex(int i) {
		if (i >= 0 && i < st.length) {
			double relativeIndex = (double) i / (double) st.length;
			return (relativeIndex * (max - min)) + min;
		}
		return Double.NaN;
	}

	@Override
	public Paint getPaint(double arg0) {
		double relativeIndex = 0;
		int sample = 0;
//        if(thresholdLow!=Double.NaN) {
		relativeIndex = ((arg0 - thresholdLow) / (thresholdHigh - thresholdLow));
		sample = Math.max(0, thresholdLowIndex + Math.min(
				(int) ((this.st.length - 1 - thresholdLowIndex) * relativeIndex),
				this.st.length - 1));
//        }else{
//            relativeIndex = ((arg0 - min) / (max - min));
//            sample = Math.max(0, Math.min(
//                (int) ((this.st.length - 1) * relativeIndex),
//                this.st.length - 1));
//        }
		int idx = Math.max(0, Math.min(this.lookupImage.getWidth() - 1,
				(int) ((this.st.length - 1) * this.st[sample])));
		return this.lookupColors[idx];
	}

	@Override
	public double getLowerBound() {
		return this.min;
	}

	public BufferedImage getLookupImage() {
		return this.lookupImage;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * cross.IConfigurable#configure(org.apache.commons.configuration.Configuration
	 * )
	 */
	@Override
	public void configure(Configuration cfg) {
		// TODO Auto-generated method stub
	}

	public void saveGradientPaintScale(File f) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
			double lowerBound = getLowerBound();
			double upperBound = getUpperBound();
			double incr = (upperBound - lowerBound) / 256.0d;
			double value = lowerBound;
			for (int i = 0; i < 256; i++) {
				Paint p = getPaint(value);
				if (p instanceof Color) {
					Color c = (Color) p;
					bw.write(c.getRed() + "\t" + c.getGreen() + "\t" + c.getBlue());
					bw.newLine();
				}
				lowerBound += incr;
			}
		} catch (IOException ex) {
			Exceptions.printStackTrace(ex);
		}
	}
	
	@Override
	public String toString() {
		return label;
	}

	public static void main(String[] args) {
		double[] st = ImageTools.createSampleTable(256);
		System.out.println(Arrays.toString(st));
		double min = 564.648;
		double max = 24334.234;
		GradientPaintScale gps = new GradientPaintScale(st, min, max,
				new Color[]{Color.BLACK, Color.RED, Color.orange,
			Color.yellow, Color.white});
		double val = min;
		double incr = (max - min) / (st.length - 1);
		System.out.println("Increment: " + incr);
		for (int i = 0; i < st.length; i++) {
			System.out.println("Value: " + val);
			gps.getPaint(val);
			val += incr;
		}
		System.out.println("Printing min and max values");
		System.out.println("Min: " + min + " gps min: " + gps.getPaint(min));
		System.out.println("Max: " + max + " gps max: " + gps.getPaint(max));
		JList jl = new JList();
		DefaultListModel dlm = new DefaultListModel();
		jl.setModel(dlm);
		jl.setCellRenderer(new ListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				if (value instanceof JLabel) {
					// Border b =
					// BorderFactory.createCompoundBorder(BorderFactory
					// .createEmptyBorder(0, 0, 5, 0), BorderFactory
					// .createLineBorder(Color.BLACK, 1));
					// ((JLabel) value).setBorder(b);
					return (Component) value;
				}
				return new JLabel(value.toString());
			}
		});
		JFrame jf = new JFrame();
		jf.add(new JScrollPane(jl));
		jf.setVisible(true);
		jf.setSize(200, 400);
		for (int alpha = -10; alpha <= 10; alpha++) {
			for (int beta = 1; beta <= 20; beta++) {
				gps.setAlphaBeta(alpha, beta);
				// System.out.println(Arrays.toString(gps.st));
				// System.out.println(Arrays.toString(gps.sampleTable));
				BufferedImage bi = gps.getLookupImage();
				ImageIcon ii = new ImageIcon(bi);
				dlm.addElement(new JLabel(ii));
			}
		}

	}

	public static Color[] getDefaultColorRamp() {
		Color[] c = new Color[]{Color.BLUE, Color.MAGENTA, Color.RED, Color.ORANGE, Color.YELLOW, Color.WHITE};
		return c;
	}
}
