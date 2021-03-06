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
package net.sf.maltcms.chromaui.chromatogram1Dviewer.actions;

import cross.exception.ResourceNotAvailableException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IScan;
import maltcms.datastructures.ms.IScan2D;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeak2DAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.LookupUtils;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import net.sf.maltcms.common.charts.api.dataset.ADataset2D;
import net.sf.maltcms.common.charts.api.overlay.SelectionOverlay;
import net.sf.maltcms.common.charts.api.selection.ISelection;
import net.sf.maltcms.common.charts.api.selection.xy.XYSelection;
import org.jfree.data.general.Dataset;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.InstanceCookie;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.NotImplementedException;
import ucar.ma2.Array;

@ActionID(
        category = "OverlayNodeActions/SelectionOverlay",
        id = "net.sf.maltcms.chromaui.chromatogram1Dviewer.actions.AddSelectionAsPeakDescriptors")
@ActionRegistration(
        displayName = "#CTL_AddSelectionAsPeakDescriptors")
@NbBundle.Messages("CTL_AddSelectionAsPeakDescriptors=Add Selection as Peaks")
public final class AddSelectionAsPeakDescriptors implements ActionListener {

    private final Node context;

    public AddSelectionAsPeakDescriptors(Node context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        Object obj;
        try {
            obj = context.getLookup().lookup(InstanceCookie.class).instanceCreate();
            if (obj instanceof SelectionOverlay) {
                invokeAction(AddSelectionAsPeakDescriptors.class, (SelectionOverlay) obj);
            }
        } catch (IOException | ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static IPeakAnnotationDescriptor createPeakAnnotationFor(IChromAUIProject project, IChromatogramDescriptor chromatogram, XYSelection selection, int index) {
        int seperationDimensions = chromatogram.getSeparationType().getFeatureDimensions();
        if (seperationDimensions == 1) {
            IScan target = (IScan) selection.getTarget();
            double rt = target.getScanAcquisitionTime();
            IPeakAnnotationDescriptor descriptor = DescriptorFactory.newPeakAnnotationDescriptor(
                    chromatogram,
                    target.getScanIndex() + " @" + rt,
                    Double.NaN,
                    new double[]{Double.NaN},
                    Double.NaN,
                    Double.NaN,
                    Double.NaN,
                    Double.NaN,
                    null,
                    null,
                    null,
                    "Manual Selection", rt,
                    rt, rt, Double.NaN,
                    target.getTotalIntensity());
            descriptor.setIndex(index);
            descriptor.setApexIntensity(target.getTotalIntensity());
            descriptor.setApexTime(rt);
            descriptor.setArea(Double.NaN);
            descriptor.setStartTime(rt);
            descriptor.setStopTime(rt);
            descriptor.setNormalizedArea(Double.NaN);
            descriptor.setNormalizationMethods(new String[0]);
            try {
                Array masses = target.getMasses();
                Array intensities = target.getIntensities();
                descriptor
                        .setMassValues((double[]) masses.get1DJavaArray(double.class));
                descriptor.setIntensityValues(
                        (int[]) intensities.get1DJavaArray(int.class));
            } catch (ResourceNotAvailableException rnae) {
                Exceptions.printStackTrace(rnae);
            }
            return descriptor;
        } else if (seperationDimensions == 2) {
            IScan target = (IScan) selection.getTarget();
            double rt = target.getScanAcquisitionTime();
            if (chromatogram.getChromatogram() instanceof IChromatogram2D) {
                IChromatogram2D chrom = (IChromatogram2D) chromatogram.getChromatogram();
                IScan2D scan = chrom.getScan(target.getScanIndex());
                if (scan == null) {
                    throw new ResourceNotAvailableException("Could not find an actual scan for scan index " + target.getScanIndex());
                }
                IPeak2DAnnotationDescriptor descriptor = DescriptorFactory.newPeak2DAnnotationDescriptor(
                        chromatogram,
                        target.getScanIndex() + " @" + rt,
                        Double.NaN,
                        new double[]{Double.NaN},
                        Double.NaN,
                        Double.NaN,
                        Double.NaN,
                        Double.NaN,
                        null,
                        null,
                        null,
                        "Manual Selection", rt,
                        rt, rt, Double.NaN,
                        target.getTotalIntensity(),
                        scan.getFirstColumnScanAcquisitionTime(),
                        scan.getSecondColumnScanAcquisitionTime());

                descriptor.setIndex(index);
                descriptor.setApexIntensity(target.getTotalIntensity());
                descriptor.setApexTime(rt);
                descriptor.setArea(Double.NaN);
                descriptor.setStartTime(rt);
                descriptor.setStopTime(rt);
                descriptor.setNormalizedArea(Double.NaN);
                descriptor.setNormalizationMethods(new String[0]);
                try {
                    Array masses = target.getMasses();
                    Array intensities = target.getIntensities();
                    descriptor
                            .setMassValues((double[]) masses.get1DJavaArray(double.class));
                    descriptor.setIntensityValues(
                            (int[]) intensities.get1DJavaArray(int.class));
                } catch (ResourceNotAvailableException rnae) {
                    Exceptions.printStackTrace(rnae);
                }
                return descriptor;
            } else {
                throw new IllegalArgumentException("Unsupported chromatogram type with separation dimension=2: " + chromatogram.getChromatogram().getClass().getName());
            }
        } else {
            throw new IllegalArgumentException("Unsupported number of separation dimensions for selection: " + chromatogram.getSeparationType().getFeatureDimensions());
        }
    }

    public static void invokeAction(Class<?> creatorClazz, SelectionOverlay so) throws NotImplementedException, IllegalArgumentException {
        Map<IChromatogram, Set<IPeakAnnotationDescriptor>> sourceToSelection = new LinkedHashMap<>();
        Map<IChromatogram, IChromatogramDescriptor> chromToDescriptor = new LinkedHashMap<>();

        IChromAUIProject project = null;
        for (ISelection selection : so.getMouseClickSelection()) {
            project = createPeakAnnotationDescriptor(selection, project, chromToDescriptor, sourceToSelection);
        }
        addPeakAnnotations(creatorClazz, sourceToSelection, project, chromToDescriptor);
    }

    public static void invokeHoverAction(Class<?> creatorClazz, ISelection selection) throws NotImplementedException, IllegalArgumentException {
        Map<IChromatogram, Set<IPeakAnnotationDescriptor>> sourceToSelection = new LinkedHashMap<>();
        Map<IChromatogram, IChromatogramDescriptor> chromToDescriptor = new LinkedHashMap<>();

        IChromAUIProject project = null;
        if (selection != null) {
            project = createPeakAnnotationDescriptor(selection, project, chromToDescriptor, sourceToSelection);
            addPeakAnnotations(creatorClazz, sourceToSelection, project, chromToDescriptor);
        }
    }

    private static void addPeakAnnotations(Class<?> creatorClazz, Map<IChromatogram, Set<IPeakAnnotationDescriptor>> sourceToSelection, IChromAUIProject project, Map<IChromatogram, IChromatogramDescriptor> chromToDescriptor) {
        IToolDescriptor trd = DescriptorFactory.newToolResultDescriptor();
        trd.setName(creatorClazz.getSimpleName());
        trd.setDisplayName(creatorClazz.getSimpleName());
        for (IChromatogram chrom : sourceToSelection.keySet()) {
            DescriptorFactory.addPeakAnnotations(project,
                    chromToDescriptor.get(chrom), new ArrayList<>(sourceToSelection.get(chrom)), trd);
        }
    }

    private static IChromAUIProject createPeakAnnotationDescriptor(ISelection selection, IChromAUIProject project, Map<IChromatogram, IChromatogramDescriptor> chromToDescriptor, Map<IChromatogram, Set<IPeakAnnotationDescriptor>> sourceToSelection) throws NotImplementedException, IllegalArgumentException {
        Dataset dataset = selection.getDataset();
        if (dataset instanceof ADataset2D) {
            throw new NotImplementedException("Manual peak annotation import for 2D chromatograms is not available yet!");
        } else if (dataset instanceof ADataset1D && !(dataset instanceof ADataset2D)) {
            IChromatogram1D source = (IChromatogram1D) selection.getSource();
            IScan target = (IScan) selection.getTarget();
            ADataset1D ds = (ADataset1D) dataset;
            IChromAUIProject activeProject = LookupUtils.ensureSingle(ds.getLookup(), IChromAUIProject.class);
            if (project == null) {
                project = activeProject;
            } else {
                if (!activeProject.getLocation().getPath().equals(project.getLocation().getPath())) {
                    throw new IllegalArgumentException("Can not work with selection from multiple projects!");
                }
            }
            for (IChromatogramDescriptor descr : project.getChromatograms()) {
                if (descr.getChromatogram().getParent().getName().equals(source.getParent().getName())) {
                    chromToDescriptor.put(source, descr);
                    if (sourceToSelection.containsKey(source)) {
                        IPeakAnnotationDescriptor pad = createPeakAnnotationFor(project, descr, (XYSelection) selection, sourceToSelection.get(source).size());
                        sourceToSelection.get(source).add(pad);
                    } else {
                        IPeakAnnotationDescriptor pad = createPeakAnnotationFor(project, descr, (XYSelection) selection, 0);
                        LinkedHashSet<IPeakAnnotationDescriptor> lhs = new LinkedHashSet<>();
                        lhs.add(pad);
                        sourceToSelection.put(source, lhs);
                    }
                }
            }
        }
        return project;
    }
}
