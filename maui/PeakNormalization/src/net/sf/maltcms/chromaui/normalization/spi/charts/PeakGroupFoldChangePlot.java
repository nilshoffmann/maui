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
package net.sf.maltcms.chromaui.normalization.spi.charts;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import lombok.Data;
import net.sf.maltcms.chromaui.charts.ChartCustomizer;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import net.sf.maltcms.common.charts.api.dataset.INamedElementProvider;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

/**
 *
 * @author Nils Hoffmann
 */
@Data
public class PeakGroupFoldChangePlot {

	private final IChromAUIProject project;
	private final Collection<IPeakGroupDescriptor> pgdl;
	private final IPeakNormalizer normalizer;
	private boolean showAreas = true;
	private boolean ignoreNullAndNanValues;

	public ADataset1D<? extends IPeakGroupDescriptor, ? extends IPeakAnnotationDescriptor> createDataset() {
		List<INamedElementProvider<? extends IPeakGroupDescriptor, ? extends IPeakAnnotationDescriptor>> elementProviders = new ArrayList<INamedElementProvider<? extends IPeakGroupDescriptor, ? extends IPeakAnnotationDescriptor>>();
		for (IPeakGroupDescriptor pgd : pgdl) {
			elementProviders.add(new PeakGroupDescriptorDatasetElementProvider(pgd));
		}
		PeakGroupDescriptorDataset dataset = new PeakGroupDescriptorDataset(elementProviders);
		return dataset;
	}

	public List<JFreeChart> createChart(ADataset1D<? extends IPeakGroupDescriptor, ? extends IPeakAnnotationDescriptor> dataset) {
		List<JFreeChart> charts = new ArrayList<JFreeChart>();
		XYPlot plot = new XYPlot();

//            DefaultBoxAndWhiskerCategoryDataset baw = new DefaultBoxAndWhiskerCategoryDataset();
//            for (IPeakAnnotationDescriptor ipad : descriptors) {
//                ITreatmentGroupDescriptor treatmentGroup = ipad.
//                        getChromatogramDescriptor().getTreatmentGroup();
//                HashSet<IPeakAnnotationDescriptor> descr = map.get(
//                        treatmentGroup);
//                if (descr == null) {
//                    descr = new HashSet<IPeakAnnotationDescriptor>();
//                    map.put(treatmentGroup, descr);
//                }
//                descr.add(ipad);
//            }
//            List<Color> colors = new LinkedList<Color>();
//            for (ITreatmentGroupDescriptor tgd : map.keySet()) {
//                String name = getPeakName(pgd);
//                baw.add(createBoxAndWhiskerItem(map.get(tgd)), tgd.getName()+" ("+map.get(tgd).size()+")",
//                        name);
//                colors.add(tgd.getColor());
//            }
//            BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
//            renderer.setFillBox(true);
//            renderer.setMeanVisible(false);
//            renderer.setMedianVisible(true);
//            renderer.setArtifactPaint(new Color(0,0,0,128));
//            renderer.setMaximumBarWidth(0.1);
//			renderer.setUseOutlinePaintForWhiskers(false);
////            renderer.setAutoPopulateSeriesFillPaint(true);
////            renderer.setAutoPopulateSeriesPaint(true);
////            renderer.setAutoPopulateSeriesOutlinePaint(true);
//            CategoryPlot cp = new CategoryPlot(baw, new CategoryAxis(
//                    "Treatment Groups"), new NumberAxis("Normalized Peak Area"),
//                    renderer);
//            System.out.println("Setting "+colors.size()+" colors!");
//            ChartCustomizer.setSeriesColors(cp, 0.6f, colors);
////            ChartCustomizer.setSeriesColors(cp, 0.9f,colors);
//            plots.add(cp);
//            JFreeChart chart = new JFreeChart(cp);
//            chart.setTitle("Peak group "+pgd.getDisplayName()+" size: "+pgd.getPeakAnnotationDescriptors().size());
//            charts.add(chart);
//        }
//        CategoryAxis ca = new CategoryAxis("Treatment Groups");
//        NumberAxis va = new NumberAxis("Normalized Peak Area");
//        CombinedDomainCategoryPlot cdcp = new CombinedDomainCategoryPlot(ca);
//        for (CategoryPlot cp : plots) {
//            cp.setRangeAxis(va);
//            cdcp.add(cp);
//            break;
//        }
//        return new JFreeChart(cdcp);
		return charts;
	}

	protected String getPeakName(IPeakGroupDescriptor pgd) {
		String rt = "mean area: " + String.format("%.2f", pgd.getMeanArea(normalizer)) + "+/-" + String.format("%.2f", pgd.getAreaStdDev(normalizer)) + "; median area: " + String.format("%.2f", pgd.getMedianArea(normalizer)) + ": ";
		LinkedHashMap<String, Integer> names = new LinkedHashMap<String, Integer>();
		if (!pgd.getDisplayName().equals(pgd.getName())) {
			return rt + pgd.getDisplayName();
		}
		for (IPeakAnnotationDescriptor ipad : pgd.getPeakAnnotationDescriptors()) {
			if (names.containsKey(ipad.getName())) {
				names.put(ipad.getName(), names.get(ipad.getName()) + 1);
			} else {
				names.put(ipad.getName(), 1);
			}
		}
		if (names.isEmpty()) {
			return rt + "<NA>";
		}
		if (names.size() > 1) {
			StringBuilder sb = new StringBuilder();
			for (String key : names.keySet()) {
				sb.append(key);
				sb.append(" (" + names.get(key) + ")");
				sb.append(" | ");
			}
			return rt + sb.replace(sb.length() - 1, sb.length() - 1, "").toString();
		} else {
			return rt + names.keySet().toArray(new String[0])[0];
		}
	}

	protected BoxAndWhiskerItem createBoxAndWhiskerItem(
			Collection<IPeakAnnotationDescriptor> descriptors) {
		List<Double> values = new LinkedList<Double>();
		for (IPeakAnnotationDescriptor ipad : descriptors) {
			double factor = normalizer.getNormalizationFactor(ipad);
			if (showAreas) {
				values.add(factor * ipad.getArea());
			} else {
				values.add(factor * ipad.getApexIntensity());
			}
		}

//        for (int i = 0; i < 100; i++) {
//            values.add(1000000 * Math.random());
//        }
		return BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(values,
				ignoreNullAndNanValues);
	}
}
