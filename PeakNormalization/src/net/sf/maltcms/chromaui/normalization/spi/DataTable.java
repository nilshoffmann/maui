/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.normalization.spi;

import cross.tools.MathTools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import org.openide.util.Exceptions;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPGenericVector;
import org.rosuda.REngine.REXPList;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REXPVector;
import org.rosuda.REngine.RList;

/**
 *
 * @author nilshoffmann
 */
@Data
public class DataTable {

    private final PeakGroupContainer peakGroupContainer;
    private final IPeakNormalizer normalizer;
    private final String name;
    private final LinkedHashMap<IPeakGroupDescriptor, double[]> groupToValues;
    private final List<IChromatogramDescriptor> rowNames;
    private final ImputationMode imputationMode;

    public enum ImputationMode {

        STRICT, GROUP_MEAN, GROUP_MEDIAN, ZERO
    };

    public double getGroupMeanArea(Set<IPeakAnnotationDescriptor> peaks, IPeakNormalizer normalizer) {
        double[] values = new double[peaks.size()];
        int i = 0;
        for (IPeakAnnotationDescriptor pad : peaks) {
            values[i++] = normalizer.getNormalizationFactor(pad)*pad.getArea();
        }
        return MathTools.average(values, 0, values.length - 1);
    }

    public double getGroupMedianArea(Set<IPeakAnnotationDescriptor> peaks, IPeakNormalizer normalizer) {
        double[] values = new double[peaks.size()];
        int i = 0;
        for (IPeakAnnotationDescriptor pad : peaks) {
            values[i++] = normalizer.getNormalizationFactor(pad)*pad.getArea();
        }
        return MathTools.median(values, 0, values.length - 1);
    }

    public DataTable(PeakGroupContainer peakGroupContainer, IPeakNormalizer normalizer, String name, ImputationMode imputationMode) {
        this.peakGroupContainer = peakGroupContainer;
        this.normalizer = normalizer;
        this.name = name;
//        int peaks = 0;
        int lastPosition = 0;
        //create a map from each chromatogram descriptor to a unique index
        LinkedHashMap<IChromatogramDescriptor, Integer> chromToRowIndex = new LinkedHashMap<IChromatogramDescriptor, Integer>();
        //loop over all peak groups
        for (IPeakGroupDescriptor ipgd : peakGroupContainer.getMembers()) {
//            peaks = Math.max(ipgd.getPeakAnnotationDescriptors().size(), peaks);
            //loop over all peak annotations within the current group
            for (IPeakAnnotationDescriptor ipad : ipgd.getPeakAnnotationDescriptors()) {
                //if the current chromatogram is new, add new index for it
                if (!chromToRowIndex.containsKey(ipad.getChromatogramDescriptor())) {
                    chromToRowIndex.put(ipad.getChromatogramDescriptor(), Integer.valueOf(lastPosition));
                    lastPosition++;
                }
            }
        }
        rowNames = new LinkedList<IChromatogramDescriptor>();
        for (IChromatogramDescriptor descr : chromToRowIndex.keySet()) {
            System.out.println("Chromatogram " + descr.getDisplayName() + " at location " + chromToRowIndex.get(descr));
            rowNames.add(descr);
        }
        int chromatograms = rowNames.size();
        groupToValues = new LinkedHashMap<IPeakGroupDescriptor, double[]>();
        for (IPeakGroupDescriptor ipgd : peakGroupContainer.getMembers()) {
            double[] variableValues = new double[chromatograms];
            for (int i = 0; i < variableValues.length; i++) {
                variableValues[i] = Double.NaN;
            }
            for (IPeakAnnotationDescriptor ipad : ipgd.getPeakAnnotationDescriptors()) {
                if (chromToRowIndex.containsKey(ipad.getChromatogramDescriptor())) {
                    Integer index = chromToRowIndex.get(ipad.getChromatogramDescriptor());
                    variableValues[index.intValue()] = normalizer.getNormalizationFactor(ipad)*ipad.getArea();
                }
            }
            groupToValues.put(ipgd, variableValues);
        }

        this.imputationMode = imputationMode;

    }

    public REXP toDataFrame() {
        RList list = new RList();
        for (IPeakGroupDescriptor peakGroup : groupToValues.keySet()) {

            Map<ITreatmentGroupDescriptor, Set<IPeakAnnotationDescriptor>> groupMap = peakGroup.getPeaksByTreatmentGroup();
            double[] values = groupToValues.get(peakGroup);
            for (int i = 0; i < values.length; i++) {
                if (Double.isNaN(
                        values[i])) {
                    IChromatogramDescriptor chrom = rowNames.get(i);
                    ITreatmentGroupDescriptor treatmentGroup = chrom.getTreatmentGroup();
                    Set<IPeakAnnotationDescriptor> s = groupMap.get(treatmentGroup);
                    if (s != null) {
                        switch (imputationMode) {
                            case GROUP_MEAN:
                                values[i] = getGroupMeanArea(s, normalizer);
                                break;
                            case GROUP_MEDIAN:
                                values[i] = getGroupMedianArea(s, normalizer);
                                break;
                            case ZERO:
                                values[i] = 0;
                                break;

                        }
                    } else {
                        //FIXME add fallback to complete dataset imputation?
                        System.err.println("Warning: group had no values, setting to 0!");
                        values[i] = 0;
                    }
                }
            }
            list.put(peakGroup.getMedianApexTime() + "", new REXPDouble(values));
            System.out.println("group " + peakGroup.getDisplayName() + "=" + Arrays.toString(values));
        }

        try {
            REXP dataFrame = REXP.createDataFrame(list);//DataTable.createDataFrameWithRowNames(list,rexpRowNames);
            return dataFrame;

        } catch (REXPMismatchException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    public REXP getRowNamesREXP() {
        String[] rows = new String[rowNames.size()];
        int i = 0;
        for (IChromatogramDescriptor descr : rowNames) {
            rows[i++] = descr.getDisplayName();
        }
        REXPString rexpRowNames = new REXPString(rows);
        return rexpRowNames;
    }

    public List<IChromatogramDescriptor> getRowNames() {
        return this.rowNames;
    }

    public List<IPeakGroupDescriptor> getVariables() {
        return new ArrayList<IPeakGroupDescriptor>(groupToValues.keySet());
    }

    public static REXP createDataFrameWithRowNames(RList l, REXPString rowNames) throws REXPMismatchException {
        if (l == null || l.size() < 1) {
            throw new REXPMismatchException(new REXPList(l), "data frame (must have dim>0)");
        }
        if (!(l.at(0) instanceof REXPVector)) {
            throw new REXPMismatchException(new REXPList(l), "data frame (contents must be vectors)");
        }
        REXPVector fe = (REXPVector) l.at(0);
        if (fe.length() != rowNames.length()) {
            throw new REXPMismatchException(rowNames, "row names (must have dim=" + fe.length() + ")");
        }
        return new REXPGenericVector(l,
                new REXPList(
                new RList(
                new REXP[]{
                    new REXPString("data.frame"),
                    new REXPString(l.keys()),
                    rowNames
                },
                new String[]{
                    "class",
                    "names",
                    "row.names"
                })));
    }
}
