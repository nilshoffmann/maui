/*
 * $license$
 *
 * $Id$
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
import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@Data
public class PeakGroupBoxPlot {

    private final IChromAUIProject project;
    private final Collection<IPeakGroupDescriptor> pgdl;
    private final IPeakNormalizer normalizer;
    private boolean showAreas = true;
    private boolean ignoreNullAndNanValues;

    public List<JFreeChart> createChart() {
        List<JFreeChart> charts = new ArrayList<JFreeChart>();
        LinkedHashSet<ITreatmentGroupDescriptor> treatmentGroups = new LinkedHashSet<ITreatmentGroupDescriptor>(project.
                getTreatmentGroups());
        List<CategoryPlot> plots = new LinkedList<CategoryPlot>();
        for (IPeakGroupDescriptor pgd : pgdl) {
            LinkedHashMap<ITreatmentGroupDescriptor, HashSet<IPeakAnnotationDescriptor>> map = new LinkedHashMap<ITreatmentGroupDescriptor, HashSet<IPeakAnnotationDescriptor>>();
            for (ITreatmentGroupDescriptor itgd : treatmentGroups) {
                map.put(itgd, new LinkedHashSet<IPeakAnnotationDescriptor>());
            }
            List<IPeakAnnotationDescriptor> descriptors = pgd.
                    getPeakAnnotationDescriptors();

            DefaultBoxAndWhiskerCategoryDataset baw = new DefaultBoxAndWhiskerCategoryDataset();
            for (IPeakAnnotationDescriptor ipad : descriptors) {
                ITreatmentGroupDescriptor treatmentGroup = ipad.
                        getChromatogramDescriptor().getTreatmentGroup();
                HashSet<IPeakAnnotationDescriptor> descr = map.get(
                        treatmentGroup);
                if (descr == null) {
                    descr = new HashSet<IPeakAnnotationDescriptor>();
                    map.put(treatmentGroup, descr);
                }
                descr.add(ipad);
            }
            List<Color> colors = new LinkedList<Color>();
            for (ITreatmentGroupDescriptor tgd : map.keySet()) {
                String name = getPeakName(pgd);
                baw.add(createBoxAndWhiskerItem(map.get(tgd)), tgd.getName()+" ("+map.get(tgd).size()+")",
                        name);
                colors.add(tgd.getColor());
            }
            BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
            renderer.setFillBox(true);
            renderer.setMeanVisible(true);
            renderer.setMedianVisible(true);
            renderer.setArtifactPaint(new Color(0,0,0,128));
            renderer.setMaximumBarWidth(0.2);
//            renderer.setAutoPopulateSeriesFillPaint(true);
//            renderer.setAutoPopulateSeriesPaint(true);
//            renderer.setAutoPopulateSeriesOutlinePaint(true);
            CategoryPlot cp = new CategoryPlot(baw, new CategoryAxis(
                    "Treatment Groups"), new NumberAxis("Normalized Peak Area"),
                    renderer);
            System.out.println("Setting "+colors.size()+" colors!");
            ChartCustomizer.setSeriesColors(cp, 0.6f, colors);
//            ChartCustomizer.setSeriesColors(cp, 0.9f,colors);
            plots.add(cp);
            JFreeChart chart = new JFreeChart(cp);
            chart.setTitle("Peak group "+pgd.getDisplayName()+" size: "+pgd.getPeakAnnotationDescriptors().size());
            charts.add(chart);
        }
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
        String rt = "mean area: "+String.format("%.2f",pgd.getMeanArea(normalizer))+"+/-"+String.format("%.2f",pgd.getAreaStdDev(normalizer))+"; median area: "+String.format("%.2f",pgd.getMedianArea(normalizer))+": ";
        LinkedHashMap<String,Integer> names = new LinkedHashMap<String,Integer>();
        if(!pgd.getDisplayName().equals(pgd.getName())) {
            return rt+pgd.getDisplayName();
        }
        for (IPeakAnnotationDescriptor ipad : pgd.getPeakAnnotationDescriptors()) {
            if(names.containsKey(ipad.getName())) {
                names.put(ipad.getName(), names.get(ipad.getName())+1);
            }else{
                names.put(ipad.getName(),1);
            }
        }
        if (names.isEmpty()) {
            return rt+"<NA>";
        }
        if (names.size() > 1) {
            StringBuilder sb = new StringBuilder();
            for(String key:names.keySet()) {
                sb.append(key);
                sb.append(" ("+names.get(key)+")");
                sb.append(" | ");
            }
            return rt+sb.replace(sb.length()-1, sb.length()-1, "").toString();
        } else {
            return rt+names.keySet().toArray(new String[0])[0];
        }
    }

    protected BoxAndWhiskerItem createBoxAndWhiskerItem(
            Collection<IPeakAnnotationDescriptor> descriptors) {
        List<Double> values = new LinkedList<Double>();
        for (IPeakAnnotationDescriptor ipad : descriptors) {
            if (showAreas) {
                values.add(normalizer.getNormalizedArea(ipad));
            } else {
                values.add(normalizer.getNormalizedIntensity(ipad));
            }
        }

//        for (int i = 0; i < 100; i++) {
//            values.add(1000000 * Math.random());
//        }
        return BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(values,
                ignoreNullAndNanValues);
    }
}
