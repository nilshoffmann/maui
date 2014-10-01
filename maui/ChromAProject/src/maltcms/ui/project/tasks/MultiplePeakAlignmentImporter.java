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
package maltcms.ui.project.tasks;

import cross.datastructures.tools.EvalTools;
import cross.datastructures.tuple.Tuple2D;
import cross.tools.StringTools;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Data;
import maltcms.io.csv.CSVReader;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import ucar.ma2.Array;

/**
 *
 * @author Nils Hoffmann
 */
@Data
public class MultiplePeakAlignmentImporter extends AProgressAwareRunnable {

    private final IChromAUIProject project;
    private final File[] alignmentFiles;

    @Override
    public void run() {
        try {
            progressHandle.start();
            for (File alignmentFile : alignmentFiles) {
                progressHandle.progress("Importing alignment "+alignmentFile.getPath());
                CSVReader csvr = new CSVReader();
                try {
                    LinkedHashSet<IChromatogramDescriptor> chromatograms = new LinkedHashSet<>(project.getChromatograms());
                    Tuple2D<Vector<Vector<String>>, Vector<String>> table = csvr.read(alignmentFile.toURI().toURL().openStream());
                    if (table.getFirst().isEmpty()) {
                        DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message("Could not import multiple alignment! File is empty! ", NotifyDescriptor.WARNING_MESSAGE));
                    }
                    //map chromatograms in output directory of alignmentFile to retrieve scan_acquisition_time for each peak
                    HashMap<String, IChromatogramDescriptor> nameToPeakFileFragments = new HashMap<>();

                    HashMap<Integer, IChromatogramDescriptor> indexToChrom = new HashMap<>();
                    Vector<String> header = table.getSecond();
                    int index = 0;
                    final HashMap<String, IChromatogramDescriptor> nameToChrom = new HashMap<>();
                    HashMap<IChromatogramDescriptor, Collection<Peak1DContainer>> chromToPeaks = new HashMap<>();
                    for (IChromatogramDescriptor descr : project.getChromatograms()) {
                        nameToChrom.put(StringTools.removeFileExt(new File(descr.getResourceLocation()).getName()), descr);
                    }
                    for (final String str : header) {
                        if (nameToChrom.containsKey(str)) {
                            IChromatogramDescriptor cd = nameToChrom.get(str);
                            Logger.getLogger(MultiplePeakAlignmentImporter.class.getName()).log(Level.FINE, "Found match for column {0} in chromatogram {1}", new Object[]{str, cd.getResourceLocation()});
                            indexToChrom.put(index, cd);
                            chromToPeaks.put(cd, project.getPeaks(cd));
                        } else {
                            Logger.getLogger(MultiplePeakAlignmentImporter.class.getName()).log(Level.WARNING, "Warning: could not match chromatogram in alignment file header {0}", str);
                        }
                        index++;
                    }
                    IDescriptorFactory descriptorFactory = Lookup.getDefault().lookup(IDescriptorFactory.class);
                    for (File f : alignmentFile.getParentFile().listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return nameToChrom.containsKey(StringTools.removeFileExt(name));
                        }
                    })) {
                        IChromatogramDescriptor descriptor = descriptorFactory.newChromatogramDescriptor();
                        descriptor.setResourceLocation(f.toURI().toString());
                        nameToPeakFileFragments.put(StringTools.removeFileExt(f.getName()), descriptor);
                    }

                    int rowIdx = 0;

                    progressHandle.progress("Mapping peaks to groups");
                    PeakGroupContainer pgc = new PeakGroupContainer();
                    pgc.setDisplayName("PeakGroups");
                    progressHandle.switchToDeterminate(table.getFirst().size());
                    for (Vector<String> row : table.getFirst()) {
                        IPeakGroupDescriptor pgd = DescriptorFactory.newPeakGroupDescriptor("group-" + (rowIdx + 1));
                        pgd.setIndex(rowIdx);
                        pgd.setDisplayName("group-" + (rowIdx + 1));
                        progressHandle.progress("Adding group " + (rowIdx + 1), rowIdx + 1);
                        Logger.getLogger(MultiplePeakAlignmentImporter.class.getName()).log(Level.FINE, "Adding group {0}", pgd.getDisplayName());
                        List<IPeakAnnotationDescriptor> descriptors = new ArrayList<>();
                        int colIdx = 0;
                        for (String element : row) {
                            if (!element.trim().equals("-")) {
                                int peakIndex = Integer.parseInt(element.trim());
                                IChromatogramDescriptor chrom = indexToChrom.get(Integer.valueOf(colIdx));
                                if (chrom != null) {
                                    IChromatogramDescriptor sourceChrom = nameToPeakFileFragments.get(StringTools.removeFileExt(new File(chrom.getResourceLocation()).getName()));
                                    if (sourceChrom != null) {
                                        IPeakAnnotationDescriptor ipad = getDescriptor(
                                                peakIndex, project,
                                                chrom, sourceChrom);
                                        if (ipad != null) {
                                            descriptors.add(ipad);
                                        } else {
                                            Logger.getLogger(MultiplePeakAlignmentImporter.class.getName()).log(Level.WARNING, "Warning: Peak at index {0} in file {1} could not be matched!", new Object[]{peakIndex, indexToChrom.get(colIdx).getDisplayName()});
                                        }
                                    } else {
                                        Logger.getLogger(MultiplePeakAlignmentImporter.class.getName()).log(Level.WARNING, "Warning: Source Chromatogram at column index {0} was not found in project!", colIdx);
                                    }
                                } else {
                                    Logger.getLogger(MultiplePeakAlignmentImporter.class.getName()).log(Level.WARNING, "Warning: Chromatogram at column index {0} was not found in project!", colIdx);
                                }
                            }
                            colIdx++;
                        }
                        if (!descriptors.isEmpty()) {
                            pgd.setPeakGroupContainer(pgc);
                            pgd.setPeakAnnotationDescriptors(descriptors);

                            pgc.addMembers(pgd);
                        }
                        rowIdx++;
                    }
                    if (pgc.getMembers().isEmpty()) {
                        DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message("Could not import multiple alignment, mapping failed!", NotifyDescriptor.WARNING_MESSAGE));
                    } else {
                        project.addContainer(pgc);
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        } catch (NumberFormatException e) {
            Exceptions.printStackTrace(e);
        } finally {
            progressHandle.finish();
        }
    }

    protected IPeakAnnotationDescriptor getDescriptor(int index,
            IChromAUIProject descr, IChromatogramDescriptor chrom, IChromatogramDescriptor sourceChromatogram) {
        Collection<Peak1DContainer> c = descr.getPeaks(chrom);
        Array sat = sourceChromatogram.getChromatogram().getScanAcquisitionTime();
        EvalTools.notNull(sourceChromatogram, this);
        EvalTools.notNull(sourceChromatogram.getChromatogram(), this);
        double alignedPeakRt = sat.getDouble(index);
        for (Peak1DContainer p : c) {
            for (IPeakAnnotationDescriptor ipad : p.getMembers()) {
                double apexTime = ipad.getApexTime();
                try {
                    int queryIndex = sourceChromatogram.getChromatogram().getIndexFor(apexTime);
                    double queryRt = sat.getDouble(queryIndex);
                    if (alignedPeakRt == queryRt) {
                        Logger.getLogger(MultiplePeakAlignmentImporter.class.getName()).log(Level.FINE, "matched peak to index {0}", index);
                        return ipad;
                    }
                } catch (ArrayIndexOutOfBoundsException aio) {
                    Logger.getLogger(MultiplePeakAlignmentImporter.class.getName()).log(Level.WARNING, "Match index for peak out of bounds!");
                }
            }
        }
        Logger.getLogger(MultiplePeakAlignmentImporter.class.getName()).log(Level.WARNING, "No match found!");
        return null;
    }
}
