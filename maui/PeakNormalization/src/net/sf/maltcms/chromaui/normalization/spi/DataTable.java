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
package net.sf.maltcms.chromaui.normalization.spi;

import cross.tools.MathTools;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Data;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
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
    private final SampleGroupMode sampleGroupMode;

    public enum ImputationMode {

        STRICT, GROUP_MEAN, GROUP_MEDIAN, ZERO
    };

    public enum SampleGroupMode {

        NONE, MEAN, MEDIAN
    };

    public double getGroupMeanArea(Set<IPeakAnnotationDescriptor> peaks, IPeakNormalizer normalizer) {
        double[] values = new double[peaks.size()];
        int i = 0;
        for (IPeakAnnotationDescriptor pad : peaks) {
            values[i++] = normalizer.getNormalizationFactor(pad) * pad.getArea();
        }
        return MathTools.average(values, 0, values.length - 1);
    }

    public double getGroupMedianArea(Set<IPeakAnnotationDescriptor> peaks, IPeakNormalizer normalizer) {
        double[] values = new double[peaks.size()];
        int i = 0;
        for (IPeakAnnotationDescriptor pad : peaks) {
            values[i++] = normalizer.getNormalizationFactor(pad) * pad.getArea();
        }
        return MathTools.median(values, 0, values.length - 1);
    }

    public DataTable(PeakGroupContainer peakGroupContainer, IPeakNormalizer normalizer, String name, ImputationMode imputationMode) {
        this(peakGroupContainer, normalizer, name, imputationMode, SampleGroupMode.NONE);
    }

    public DataTable(PeakGroupContainer peakGroupContainer, IPeakNormalizer normalizer, String name, ImputationMode imputationMode, SampleGroupMode sampleGroupMode) {
        this.peakGroupContainer = peakGroupContainer;
        this.normalizer = normalizer;
        this.name = name;
//        int peaks = 0;
        int lastPosition = 0;
        //create a map from each chromatogram descriptor to a unique index
        LinkedHashMap<IChromatogramDescriptor, Integer> chromToRowIndex = new LinkedHashMap<>();
        //loop over all peak groups
        for (IPeakGroupDescriptor ipgd : peakGroupContainer.getMembers()) {
//			System.out.println("Checking peak group descriptor "+ipgd.getDisplayName());
//            peaks = Math.max(ipgd.getPeakAnnotationDescriptors().size(), peaks);
            //loop over all peak annotations within the current group
            for (IPeakAnnotationDescriptor ipad : ipgd.getPeakAnnotationDescriptors()) {
//				System.out.println("Checking peak annotation descriptor "+ipad.getDisplayName());
                //if the current chromatogram is new, add new index for it
                IChromatogramDescriptor descriptor = ipad.getChromatogramDescriptor();
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Checking chromatogram: {0}", descriptor);
                if (!chromToRowIndex.containsKey(descriptor)) {
                    chromToRowIndex.put(descriptor, lastPosition);
//					System.out.println("Adding chromatogram "+descriptor.getDisplayName()+" at row index "+lastPosition+" with id "+descriptor.getId().toString());
                    lastPosition++;
                }
            }
        }
        rowNames = new LinkedList<>();
        for (IChromatogramDescriptor descr : chromToRowIndex.keySet()) {
//			System.out.println("Chromatogram " + descr.getDisplayName() + " at row " + chromToRowIndex.get(descr)+" with id "+descr.getId().toString());
            rowNames.add(descr);
        }
        int chromatograms = rowNames.size();
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Added {0} chromatograms to data table!", chromatograms);
        groupToValues = new LinkedHashMap<>();
        for (IPeakGroupDescriptor ipgd : peakGroupContainer.getMembers()) {
            double[] variableValues = new double[chromatograms];
            for (int i = 0; i < variableValues.length; i++) {
                variableValues[i] = Double.NaN;
            }
            for (IPeakAnnotationDescriptor ipad : ipgd.getPeakAnnotationDescriptors()) {
                if (chromToRowIndex.containsKey(ipad.getChromatogramDescriptor())) {
                    Integer index = chromToRowIndex.get(ipad.getChromatogramDescriptor());
                    variableValues[index] = normalizer.getNormalizationFactor(ipad) * ipad.getArea();
                }
            }
            groupToValues.put(ipgd, variableValues);
        }
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Added {0} peak groups to data table!", groupToValues.keySet().size());

        this.imputationMode = imputationMode;
        //TODO implement handling of sampleGroupMode
        this.sampleGroupMode = sampleGroupMode;
//		System.err.println("DataTable contains groups: ");
//		for (IPeakGroupDescriptor ipgd : peakGroupContainer.getMembers()) {
//			System.err.println("Group: "+ipgd.getDisplayName()+" with "+ipgd.getPeakAnnotationDescriptors().size()+" members");
//			Map<ITreatmentGroupDescriptor, Set<IPeakAnnotationDescriptor>> groupMap = ipgd.getPeaksByTreatmentGroup();
//			System.err.println("Group covers "+groupMap.keySet().size()+" treatment groups");
//			System.err.println("Raw values: "+Arrays.toString(groupToValues.get(ipgd)));
//		}
    }

    public REXP toDataFrame() {
        RList list = new RList();
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Iterating over {0} peak groups", groupToValues.size());
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
                        Logger.getLogger(getClass().getName()).log(Level.WARNING, "Warning: group {0} had no values, setting to 0!", treatmentGroup.getDisplayName());
                        values[i] = 0;
                    }

                }
            }
            list.put(peakGroup.getId().toString() + "", new REXPDouble(values));
//				System.out.println("group " + peakGroup.getDisplayName() + "=" + Arrays.toString(values));
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
            rows[i++] = descr.getId().toString();
        }
        REXPString rexpRowNames = new REXPString(rows);
        return rexpRowNames;
    }

    public List<IChromatogramDescriptor> getRowNames() {
        return this.rowNames;
    }

    public List<IPeakGroupDescriptor> getVariables() {
        return new ArrayList<>(groupToValues.keySet());
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
