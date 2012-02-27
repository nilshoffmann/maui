/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.normalization.spi.runnables;

import com.db4o.ObjectSet;
import cross.datastructures.tuple.Tuple2D;
import cross.tools.StringTools;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Vector;
import lombok.Data;
import maltcms.io.csv.CSVReader;
import net.sf.maltcms.chromaui.db.api.IMatchPredicate;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.util.Exceptions;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@Data
public class PeakAlignmentImporter extends AProgressAwareRunnable {

    private final IChromAUIProject project;
    private final File alignmentFile;

    @Override
    public void run() {
        try {
            progressHandle.start();
            progressHandle.progress("Importing alignment file");
            CSVReader csvr = new CSVReader();
            try {
                LinkedHashSet<IChromatogramDescriptor> chromatograms = new LinkedHashSet<IChromatogramDescriptor>(project.getChromatograms());
                Tuple2D<Vector<Vector<String>>, Vector<String>> table = csvr.read(alignmentFile.toURI().toURL().openStream());
                HashMap<Integer, IChromatogramDescriptor> indexToChrom = new HashMap<Integer, IChromatogramDescriptor>();
                Vector<String> header = table.getSecond();
                int index = 0;
                HashMap<String, IChromatogramDescriptor> nameToChrom = new HashMap<String, IChromatogramDescriptor>();
                HashMap<IChromatogramDescriptor,Collection<Peak1DContainer>> chromToPeaks =  new HashMap<IChromatogramDescriptor,Collection<Peak1DContainer>>();
                for (IChromatogramDescriptor descr : project.getChromatograms()) {
                    nameToChrom.put(StringTools.removeFileExt(new File(descr.getResourceLocation()).getName()), descr);
                }
                for (final String str : header) {
                    if (nameToChrom.containsKey(str)) {
                        IChromatogramDescriptor cd = nameToChrom.get(str);
                        System.out.println("Found match for column " + str + " in chromatogram " + cd.getResourceLocation());
                        indexToChrom.put(Integer.valueOf(index), cd);
                        chromToPeaks.put(cd, project.getPeaks(cd));
                    } else {
                        System.err.println("Warning: could not match chromatogram in alignment file header " + str);
                    }
                    index++;
                }

                int rowIdx = 0;

                progressHandle.progress("Mapping peaks to groups");
                PeakGroupContainer pgc = new PeakGroupContainer();
                pgc.setDisplayName("PeakGroups");
                for (Vector<String> row : table.getFirst()) {
                    IPeakGroupDescriptor pgd = DescriptorFactory.newPeakGroupDescriptor("group-" + (rowIdx + 1));
                    pgd.setIndex(rowIdx);
                    pgd.setDisplayName("group-" + (rowIdx + 1));
                    List<IPeakAnnotationDescriptor> descriptors = new ArrayList<IPeakAnnotationDescriptor>();
                    int colIdx = 0;
                    for (String element : row) {
                        if (!element.trim().equals("-")) {
                            int peakIndex = Integer.parseInt(element.trim());
                            IPeakAnnotationDescriptor ipad = getDescriptor(
                                    peakIndex, project,
                                    indexToChrom.get(Integer.valueOf(colIdx)));
                            descriptors.add(ipad);
                        }
                        colIdx++;
                    }
                    pgd.setPeakGroupContainer(pgc);
                    pgd.setPeakAnnotationDescriptors(descriptors);

                    pgc.addMembers(pgd);
                    rowIdx++;
                }
                project.addContainer(pgc);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            progressHandle.finish();
        } catch (Exception e) {
            progressHandle.finish();
        }
    }

    protected IPeakAnnotationDescriptor getDescriptor(int index,
            IChromAUIProject descr, IChromatogramDescriptor chrom) {
        Collection<Peak1DContainer> c = descr.getPeaks(chrom);
        for (Peak1DContainer p : c) {
            for (IPeakAnnotationDescriptor ipad : p.getMembers()) {
                if (ipad.getIndex() == index) {
                    return ipad;
                }
            }
        }
        return null;
    }
}
