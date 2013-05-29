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
package net.sf.maltcms.chromaui.normalization.spi.runnables;

import com.db4o.query.Predicate;
import com.db4o.query.Query;
import cross.datastructures.tools.EvalTools;
import cross.datastructures.tuple.Tuple2D;
import cross.tools.StringTools;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Vector;
import lombok.Data;
import maltcms.io.csv.CSVReader;
import net.sf.maltcms.chromaui.db.api.exceptions.AuthenticationException;
import net.sf.maltcms.chromaui.db.api.query.IQuery;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import ucar.ma2.Array;

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
				//map chromatograms in output directory of alignmentFile to retrieve scan_acquisition_time for each peak
				HashMap<String, IChromatogramDescriptor> nameToPeakFileFragments = new HashMap<String, IChromatogramDescriptor>();

				HashMap<Integer, IChromatogramDescriptor> indexToChrom = new HashMap<Integer, IChromatogramDescriptor>();
				Vector<String> header = table.getSecond();
				int index = 0;
				final HashMap<String, IChromatogramDescriptor> nameToChrom = new HashMap<String, IChromatogramDescriptor>();
				HashMap<IChromatogramDescriptor, Collection<Peak1DContainer>> chromToPeaks = new HashMap<IChromatogramDescriptor, Collection<Peak1DContainer>>();
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
				IDescriptorFactory descriptorFactory = Lookup.getDefault().lookup(IDescriptorFactory.class);
				for (File f : alignmentFile.getParentFile().listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						if (nameToChrom.containsKey(StringTools.removeFileExt(name))) {
							return true;
						}
						return false;
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
				for (Vector<String> row : table.getFirst()) {
					IPeakGroupDescriptor pgd = DescriptorFactory.newPeakGroupDescriptor("group-" + (rowIdx + 1));
					pgd.setIndex(rowIdx);
					pgd.setDisplayName("group-" + (rowIdx + 1));
					System.out.println("Adding group " + pgd.getDisplayName());
					List<IPeakAnnotationDescriptor> descriptors = new ArrayList<IPeakAnnotationDescriptor>();
					int colIdx = 0;
					for (String element : row) {
						if (!element.trim().equals("-")) {
							int peakIndex = Integer.parseInt(element.trim());
							IChromatogramDescriptor chrom = indexToChrom.get(Integer.valueOf(colIdx));
							IChromatogramDescriptor sourceChrom = nameToPeakFileFragments.get(StringTools.removeFileExt(new File(chrom.getResourceLocation()).getName()));
							if (chrom != null) {
								if (sourceChrom != null) {
									IPeakAnnotationDescriptor ipad = getDescriptor(
											peakIndex, project,
											chrom, sourceChrom);
									if (ipad != null) {
										descriptors.add(ipad);
									} else {
										System.err.println("Warning: Peak at index " + peakIndex + " in file " + indexToChrom.get(Integer.valueOf(colIdx)).getDisplayName() + " could not be matched!");
									}
								} else {
									System.err.println("Warning: Source Chromatogram at column index " + colIdx + " was not found in project!");
								}
							} else {
								System.err.println("Warning: Chromatogram at column index " + colIdx + " was not found in project!");
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
				project.addContainer(pgc);
			} catch (IOException ex) {
				Exceptions.printStackTrace(ex);
			}
			progressHandle.finish();
		} catch (Exception e) {
			Exceptions.printStackTrace(e);
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
						System.out.println("matched peak to index " + index);
						return ipad;
					}
				} catch (ArrayIndexOutOfBoundsException aio) {
					System.err.println("Match index for peak out of bounds!");
				}
			}
		}
		System.out.println("No match found!");
		return null;
	}
}
