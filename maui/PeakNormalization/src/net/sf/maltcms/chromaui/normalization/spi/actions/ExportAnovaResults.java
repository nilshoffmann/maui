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
package net.sf.maltcms.chromaui.normalization.spi.actions;

import cross.datastructures.StatsMap;
import cross.datastructures.Vars;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.normalization.api.ui.NormalizationDialog;
import net.sf.maltcms.chromaui.normalization.spi.PeakGroupUtilities;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.StatisticsContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IAnovaDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IStatisticsDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IPeakNormalizer;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(category = "Maui",
        id = "net.sf.maltcms.chromaui.project.spi.actions.ExportAnovaResults")
@ActionRegistration(displayName = "#CTL_ExportAnovaResults")
@ActionReferences({
    @ActionReference(path = "Actions/ContainerNodeActions/StatisticsContainer/anova")
//@ActionReference(path = "Menu/File", position = 1520)
})
@Messages("CTL_ExportAnovaResults=Export Anova Results")
public final class ExportAnovaResults implements ActionListener {

    private final StatisticsContainer context;

    public ExportAnovaResults(StatisticsContainer context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (context.getMethod().equalsIgnoreCase("anova")) {
            Logger.getLogger(getClass().getName()).info("Exporting peak tables!");
            IChromAUIProject project = Utilities.actionsGlobalContext().lookup(IChromAUIProject.class);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
            File exportDir = new File(FileUtil.toFile(project.getLocation()), "export/anova/" + sdf.format(new Date()));
            exportDir.mkdirs();
            File exportFile = new File(exportDir, context.getDisplayName()+"-anovaExport.csv");
            List<IAnovaDescriptor> anovas = new ArrayList<>();
            for (IStatisticsDescriptor statd : context.getMembers()) {
                if (statd instanceof IAnovaDescriptor) {
                    anovas.add((IAnovaDescriptor) statd);
                }
            }
            ExportRunnable er = new ExportRunnable(project, anovas, exportFile);
            ExportRunnable.createAndRun("Anova Peak Group Export", er);
        } else {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Not applicable for method {0}", context.getMethod());
        }
    }

    class ExportRunnable extends AProgressAwareRunnable {

        private final IChromAUIProject project;
        private final List<IAnovaDescriptor> context;
        private final File output;

        public ExportRunnable(IChromAUIProject project, List<IAnovaDescriptor> context, File output) {
            this.project = project;
            this.context = context;
            this.output = output;
        }

        @Override
        public void run() {
            ProgressHandle ph = getProgressHandle();
            ph.start(context.size());
            int i = 1;
            try {
                ph.progress("Exporting Anova Results...");
                BufferedWriter bw = null;
                LinkedHashMap<UUID, Integer> chromToIndex = new LinkedHashMap<>();
                List<String> chromatogramNames = new ArrayList<>();
                int idx = 0;
                int separationDimensions = -1;
                for (IChromatogramDescriptor chrom : project.getChromatograms()) {
                    chromatogramNames.add(chrom.getDisplayName());
                    chromToIndex.put(chrom.getId(), idx++);
                    if (separationDimensions == -1) {
                        separationDimensions = chrom.getSeparationType().getFeatureDimensions();
                    } else {
                        if (separationDimensions != chrom.getSeparationType().getFeatureDimensions()) {
                            throw new IllegalArgumentException("Separation dimensions of peaks in peak group must be equal!");
                        }
                    }
                }
                try {
                    bw = new BufferedWriter(new FileWriter(output));
                    StringBuilder header = new StringBuilder();
                    List<String> headerStrings = new ArrayList<>();
                    headerStrings.add("PutativeIdentification");
                    headerStrings.add("DatabaseId");
                    headerStrings.add("RetentionTimeAvg");
                    headerStrings.add("RetentionTimeStdev");
                    if (separationDimensions == 2) {
                        headerStrings.add("RetentionTime1Avg");
                        headerStrings.add("RetentionTime1Stdev");
                        headerStrings.add("RetentionTime2Avg");
                        headerStrings.add("RetentionTime2Stdev");
                    }
                    headerStrings.addAll(chromatogramNames);
                    headerStrings.addAll(Arrays.asList(new String[]{"Name", "PeakGroupId", "Factors", "DegreesOfFreedom", "Fvalues", "PvalueAdjustment", "Pvalues"}));
                    for (String head : headerStrings) {
                        header.append(head).append("\t");
                    }
                    bw.write(header.toString());
                    bw.newLine();
                    IPeakNormalizer normalizer = null;
                    PeakGroupUtilities utils = new PeakGroupUtilities();
                    for (IAnovaDescriptor group : context) {
                        ph.progress(i++);
                        StringBuilder sb = new StringBuilder();
                        IPeakGroupDescriptor peakGroup = group.getPeakGroupDescriptor();
                        if (normalizer == null) {
                            normalizer = NormalizationDialog.getPeakNormalizer(peakGroup.getPeakGroupContainer());
                            if (normalizer == null) {
                                Logger.getLogger(getClass().getName()).info("Normalization cancelled by user!");
                                bw.close();
                                output.delete();
                                output.getParentFile().delete();
                                ph.finish();
                                return;
                            }
                        }
                        sb.append(group.getPeakGroupDescriptor().getMajorityDisplayName()).append("\t");
                        sb.append(group.getPeakGroupDescriptor().getMajorityName()).append("\t");
                        sb.append(group.getPeakGroupDescriptor().getMeanApexTime()).append("\t");
                        sb.append(group.getPeakGroupDescriptor().getApexTimeStdDev()).append("\t");
                        if (separationDimensions == 2) {
                            StatsMap rt1Stats = utils.getStatsForArray(utils.getArrayForRt1(peakGroup));
                            StatsMap rt2Stats = utils.getStatsForArray(utils.getArrayForRt2(peakGroup));
                            sb.append(rt1Stats.get(Vars.Mean.name())).append("\t");
                            sb.append(Math.sqrt(rt1Stats.get(Vars.Variance.name()))).append("\t");
                            sb.append(rt2Stats.get(Vars.Mean.name())).append("\t");
                            sb.append(Math.sqrt(rt2Stats.get(Vars.Variance.name()))).append("\t");
                        }
                        String[] peaks = new String[chromatogramNames.size()];
                        for (IPeakAnnotationDescriptor peak : peakGroup.getPeakAnnotationDescriptors()) {
                            IChromatogramDescriptor peakChrom = peak.getChromatogramDescriptor();
                            double factor = normalizer.getNormalizationFactor(peak);
                            double value = peak.getArea();
                            if (Double.isNaN(factor)) {
                                value = 0;
                            } else {
                                value = value * factor;
                            }
                            peaks[chromToIndex.get(peakChrom.getId())] = value + "";
                        }
                        for (String peak : peaks) {
                            if (peak == null) {
                                sb.append("0");
                            } else {
                                sb.append(peak);
                            }
                            sb.append("\t");
                        }
                        Logger.getLogger(getClass().getName()).log(Level.INFO, "Row: {0}", Arrays.toString(peaks));
                        sb.append(group.getName()).append("\t");
                        sb.append(group.getPeakGroupDescriptor().getId()).append("\t");
                        if (group.getFactors().length == 1) {
                            sb.append(group.getFactors()[0]).append("\t");
                            sb.append(group.getDegreesOfFreedom()[0]).append("\t");
                            sb.append(group.getFvalues()[0]).append("\t");
                            sb.append(group.getPvalueAdjustmentMethod()).append("\t");
                            sb.append(group.getPvalues()[0]);
                        } else {
                            sb.append(Arrays.toString(group.getFactors())).append("\t");
                            sb.append(Arrays.toString(group.getDegreesOfFreedom())).append("\t");
                            sb.append(Arrays.toString(group.getFvalues())).append("\t");
                            sb.append(group.getPvalueAdjustmentMethod()).append("\t");
                            sb.append(Arrays.toString(group.getPvalues()));
                        }
                        bw.write(sb.toString());
                        bw.newLine();
                    }
                    bw.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                    if (bw != null) {
                        try {
                            bw.close();
                        } catch (IOException ioe) {
                            Exceptions.printStackTrace(ioe);
                        }
                    }
                }
            } finally {
                ph.finish();
            }
        }
    }
}
