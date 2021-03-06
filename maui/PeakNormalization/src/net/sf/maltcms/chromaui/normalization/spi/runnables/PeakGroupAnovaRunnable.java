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
package net.sf.maltcms.chromaui.normalization.spi.runnables;

import com.db4o.collections.ActivatableArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Data;
import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import net.sf.maltcms.chromaui.normalization.spi.PvalueAdjustment;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.container.StatisticsContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IAnovaDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IStatisticsDescriptor;
import net.sf.maltcms.chromaui.rserve.api.RserveConnectionFactory;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.util.Exceptions;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPFactor;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RFactor;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Nils Hoffmann
 */
@Data
public class PeakGroupAnovaRunnable extends AProgressAwareRunnable {

    private final IPeakNormalizer normalizer;
    private final PeakGroupContainer container;
    private final IChromAUIProject project;
    private final PvalueAdjustment pvalueAdjustmentMethod;

    @Override
    public void run() {
//        try {
        progressHandle.start(container.getMembers().size());
        try {
            RConnection c = RserveConnectionFactory.getDefaultConnection();
            try {
                StatisticsContainer anovaDescriptors = new StatisticsContainer();
                anovaDescriptors.setName("anova");
                anovaDescriptors.setMethod("Anova");
                anovaDescriptors.setDisplayName("Analysis of Variance");
                int maxFactors = 0;
                int unit = 0;
                List<double[]> pvalues = new ArrayList<>();
                for (IPeakGroupDescriptor descr : container.getMembers()) {
                    int i = 0;
                    double[] peakArea = new double[descr.getPeakAnnotationDescriptors().size()];
                    String[] treatmentGroup = new String[peakArea.length];
//                        String[] sampleGroup = new String[peakArea.length];
                    HashMap<String, Set<IPeakAnnotationDescriptor>> groupToPeak = new HashMap<>();
                    for (IPeakAnnotationDescriptor pad : descr.getPeakAnnotationDescriptors()) {
                        peakArea[i] = normalizer.getNormalizationFactor(pad) * pad.getArea();
//                        sampleGroup[i] = pad.getChromatogramDescriptor().
//                                getSampleGroup().getName();
                        treatmentGroup[i] = pad.getChromatogramDescriptor().
                                getTreatmentGroup().getName();
                        if (groupToPeak.containsKey(treatmentGroup[i])) {
                            Set<IPeakAnnotationDescriptor> set = groupToPeak.get(
                                    treatmentGroup[i]);
                            set.add(pad);
                        } else {
                            Set<IPeakAnnotationDescriptor> set = new LinkedHashSet<>();
                            set.add(pad);
                            groupToPeak.put(treatmentGroup[i], set);
                        }
                        i++;
                    }
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Peak set contains group factor with {0} levels: {1}", new Object[]{groupToPeak.keySet().size(), groupToPeak.keySet()});
                    if (groupToPeak.keySet().size() < 2) {
                        Logger.getLogger(getClass().getName()).log(Level.WARNING, "Warning: peak group {0} only has one level for factor group! Omitting from ANOVA!", descr.getDisplayName());
                    } else {
                        for (String group1 : groupToPeak.keySet()) {
                            Set<IPeakAnnotationDescriptor> set = groupToPeak.get(
                                    group1);
                            double[] values1 = new double[set.size()];
                            int k = 0;
                            for (IPeakAnnotationDescriptor pad : set) {
                                values1[k] = normalizer.getNormalizationFactor(pad) * pad.getArea();
                            }
                            for (String group2 : groupToPeak.keySet()) {
                                if (!group1.equals(
                                        group2)) {
                                }
                            }
                        }
                        RList l = new RList();
                        l.put("peakAreas", new REXPDouble(peakArea));
//                    System.out.println("  assign x=pairlist");
                        c.assign("groups", new REXPFactor(
                                new RFactor(treatmentGroup)));
//                    System.out.println("  assign y=vector");
//                    c.assign("y", new REXPGenericVector(l));
//                    System.out.println("  assign z=data.frame");
                        c.assign("peaks", REXP.createDataFrame(l));
                        String rcall = "anovaResult <- aov(peakAreas ~ groups,data=peaks)";
                        REXP anova = c.parseAndEval(rcall);
                        REXP pvalueResult = c.parseAndEval(
                                "summary(anovaResult)[[1]]$\"Pr(>F)\"");
                        REXP fvalueResult = c.parseAndEval(
                                "summary(anovaResult)[[1]]$\"F value\"");
                        REXP degrFreedom = c.parseAndEval(
                                "summary(anovaResult)[[1]]$\"Df\"");
                        REXP factors = c.parseAndEval(
                                "row.names(summary(anovaResult)[[1]])");
                        IAnovaDescriptor ad = DescriptorFactory.newAnovaDescriptor();
                        if (pvalueResult.isVector()) {
                            if (pvalueResult.isNumeric()) {
                                double[] pvs = pvalueResult.asDoubles();
                                double[] targetpvs = new double[pvs.length - 1];
                                maxFactors = Math.max(maxFactors,
                                        targetpvs.length);
                                System.arraycopy(pvs, 0, targetpvs, 0,
                                        pvs.length - 1);
                                pvalues.add(targetpvs);
                                ad.setPvalues(targetpvs);

                                Logger.getLogger(getClass().getName()).log(Level.INFO, "p-value: {0}", Arrays.toString(
                                        targetpvs));
                            }
                        }
                        if (fvalueResult.isVector()) {
                            if (fvalueResult.isNumeric()) {
                                double[] fvs = fvalueResult.asDoubles();
                                double[] targetfvs = new double[fvs.length - 1];
                                System.arraycopy(fvs, 0, targetfvs, 0,
                                        fvs.length - 1);
                                ad.setFvalues(targetfvs);

                                Logger.getLogger(getClass().getName()).log(Level.INFO, "F-values: {0}", Arrays.toString(
                                        targetfvs));
                            }
                        }
                        if (degrFreedom.isVector()) {
                            if (degrFreedom.isNumeric()) {
                                int[] dvs = degrFreedom.asIntegers();
                                int[] targetdvs = new int[dvs.length - 1];
                                System.arraycopy(dvs, 0, targetdvs, 0,
                                        dvs.length - 1);
                                ad.setDegreesOfFreedom(targetdvs);

                                Logger.getLogger(getClass().getName()).log(Level.INFO, "Degrees of freedom: {0}", Arrays.toString(
                                        targetdvs));
                            }
                        }
                        if (factors.isVector()) {
                            if (factors.isString()) {
                                String[] svs = factors.asStrings();
                                String[] targetsvs = new String[svs.length - 1];
                                System.arraycopy(svs, 0, targetsvs, 0,
                                        svs.length - 1);
                                ad.setFactors(targetsvs);

                                Logger.getLogger(getClass().getName()).log(Level.INFO, "Factors: {0}", Arrays.toString(
                                        targetsvs));
                            }
                        }

                        //cleanup
                        c.eval("rm(groups,peakAreas,peaks,anovaResult)");

                        ad.setPeakGroupDescriptor(descr);
                        ad.setName("Anova " + descr.getName());
                        Logger.getLogger(getClass().getName()).log(Level.INFO, "GroupDescriptor:{0}", ad.getPeakGroupDescriptor().getDisplayName());
                        ad.setDisplayName(ad.getPeakGroupDescriptor().
                                getDisplayName());
                        anovaDescriptors.addMembers(ad);
                        ad.setPvalueAdjustmentMethod(pvalueAdjustmentMethod.getDisplayName());
                    }
                    progressHandle.progress(unit++);
                }
                adjustPvalues(pvalues, maxFactors, c, anovaDescriptors);

                List<StatisticsContainer> statContainers = container.getStatisticsContainers();
                if (statContainers == null) {
                    statContainers = new ActivatableArrayList<>();
                }
                statContainers.add(anovaDescriptors);
                container.setStatisticsContainers(statContainers);
                progressHandle.progress("Adding Anova results");
                project.updateContainer(container);
                // close RConnection, we're done
                //c.close();
            } catch (RserveException | REXPMismatchException rse) { // RserveException (transport layer - e.g. Rserve is not running)
                System.out.println(rse);
                Exceptions.printStackTrace(rse);
            } catch (REngineException e) { // something else
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Something went wrong, but it''s not the Rserve: {0}", e.getMessage());
                Exceptions.printStackTrace(e);
            }
//            project.refresh();
//            } catch (RserveException ex) {
//                Exceptions.printStackTrace(ex);
//            }
            progressHandle.finish();
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
            progressHandle.finish();
        }
    }

    public void adjustPvalues(List<double[]> pvalues, int maxFactors,
            RConnection c, StatisticsContainer anovaDescriptors) throws REXPMismatchException, REngineException {
        List<double[]> transposedPvalues = new ArrayList<>(pvalues.size());
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Adding {0} pvalue arrays with length {1}", new Object[]{maxFactors, pvalues.size()});
        for (int i = 0; i < maxFactors; i++) {
            transposedPvalues.add(new double[pvalues.size()]);
        }
        int row = 0;
        for (double[] d : pvalues) {
            for (int i = 0; i < d.length; i++) {
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Accessing index {0} of transposedPvalues at row {1}", new Object[]{i, row});
                transposedPvalues.get(i)[row] = d[i];
            }
            row++;
        }

        for (int i = 0; i < transposedPvalues.size(); i++) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Adjusting pvalues for factor {0}", i);
            REXP r = new REXPDouble(transposedPvalues.get(i));
            c.assign("pvaluesArray", r);
            REXP returnVal = c.parseAndEval("p.adjust(pvaluesArray,method=\"" + pvalueAdjustmentMethod + "\")");
            double[] adjPvalues = returnVal.asDoubles();
            int j = 0;
            for (IStatisticsDescriptor descr : anovaDescriptors.getMembers()) {
                IAnovaDescriptor anovaDescr = (IAnovaDescriptor) descr;
                double[] pvals = anovaDescr.getPvalues();
                if (pvals != null) {
                    pvals[i] = adjPvalues[j++];
                    anovaDescr.setPvalues(pvals);
                } else {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Skipping adjustment of empty p-values for {0}", anovaDescr.getDisplayName());
                }
            }
        }
    }
}
