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
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.LookupUtils;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import net.sf.maltcms.common.charts.api.dataset.ADataset2D;
import net.sf.maltcms.common.charts.api.overlay.SelectionOverlay;
import net.sf.maltcms.common.charts.api.selection.ISelection;
import net.sf.maltcms.common.charts.api.selection.XYSelection;
import org.jfree.data.general.Dataset;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.InstanceCookie;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.NotImplementedException;
import ucar.ma2.Array;

/**
 *
 * @author Nils Hoffmann
 */


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
                invokeAction((SelectionOverlay) obj);
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public IPeakAnnotationDescriptor createPeakAnnotationFor(IChromAUIProject project, IChromatogramDescriptor chromatogram, XYSelection selection, int index) {
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
    }

    private void invokeAction(SelectionOverlay so) throws NotImplementedException, IllegalArgumentException {
        Map<IChromatogram, Set<IPeakAnnotationDescriptor>> sourceToSelection = new LinkedHashMap<IChromatogram, Set<IPeakAnnotationDescriptor>>();
        Map<IChromatogram, IChromatogramDescriptor> chromToDescriptor = new LinkedHashMap<IChromatogram, IChromatogramDescriptor>();

        IChromAUIProject project = null;
        for (ISelection selection : so.getMouseClickSelection()) {
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
                            LinkedHashSet<IPeakAnnotationDescriptor> lhs = new LinkedHashSet<IPeakAnnotationDescriptor>();
                            lhs.add(pad);
                            sourceToSelection.put(source, lhs);
                        }
                    }
                }
            }
        }
        IToolDescriptor trd = DescriptorFactory.newToolResultDescriptor();
        trd.setName(getClass().getSimpleName());
        trd.setDisplayName(getClass().getSimpleName());
        for (IChromatogram chrom : sourceToSelection.keySet()) {
            DescriptorFactory.addPeakAnnotations(project,
                    chromToDescriptor.get(chrom), new ArrayList<IPeakAnnotationDescriptor>(sourceToSelection.get(chrom)), trd);
        }
    }
}
