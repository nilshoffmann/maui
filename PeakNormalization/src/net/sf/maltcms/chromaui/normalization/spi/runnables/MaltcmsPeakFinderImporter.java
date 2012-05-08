/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.normalization.spi.runnables;

import cross.Factory;
import cross.datastructures.fragments.FileFragment;
import java.io.File;
import java.util.*;
import lombok.Data;
import maltcms.datastructures.peak.Peak1D;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.*;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import ucar.ma2.Array;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@Data
public class MaltcmsPeakFinderImporter extends AProgressAwareRunnable {

    private final IChromAUIProject project;
    private final File[] files;

    @Override
    public void run() {
        try {
            progressHandle.start(files.length);
            progressHandle.progress("Retrieving Chromatograms");
            LinkedHashMap<String, IChromatogramDescriptor> chromatograms = new LinkedHashMap<String, IChromatogramDescriptor>();
            for (IChromatogramDescriptor descriptor : project.getChromatograms()) {
                String chromName = new File(descriptor.getResourceLocation()).getName();
                chromName = chromName.substring(0, chromName.lastIndexOf(
                        "."));
                chromatograms.put(chromName, descriptor);
                System.out.println(
                        "Added chromatogram " + chromName + ": " + descriptor);
            }
            progressHandle.progress("Matching Chromatograms");
            LinkedHashMap<String, File> reports = new LinkedHashMap<String, File>();
            for (File file : files) {
                String chromName = file.getName();
                chromName = chromName.substring(0, chromName.lastIndexOf(
                        "."));
                if (chromatograms.containsKey(chromName)) {
                    reports.put(chromName, file);
                    System.out.println("Adding report: " + chromName);
                } else {
                    System.out.println(
                            "Could not find matching chromatogram for report: " + chromName);
                }
            }

            if (reports.size() != chromatograms.size()) {
                System.err.println(
                        "Not all chromatograms could be matched!");
            }
            if (reports.isEmpty()) {
                NotifyDescriptor.Message message = new NotifyDescriptor.Message(
                        "Could not match reports to existing chromatograms!",
                        NotifyDescriptor.WARNING_MESSAGE);
                DialogDisplayer.getDefault().notify(message);
            }
            int peaksReportsImported = 0;
            progressHandle.progress("Importing Peak Lists");
            IToolDescriptor trd = DescriptorFactory.newToolResultDescriptor();
            trd.setName(getClass().getSimpleName());
            trd.setDisplayName(getClass().getSimpleName());
            for (String chromName : reports.keySet()) {
                progressHandle.progress(
                        "Importing " + (peaksReportsImported + 1) + "/" + files.length,
                        peaksReportsImported);
                System.out.println("Importing report " + chromName + ".");

                IChromatogramDescriptor chromatogram = chromatograms.get(
                        chromName);

                System.out.println(
                        "Using " + chromatogram.getResourceLocation() + " as chromatogram!");
                File file = reports.get(chromName);
                DescriptorFactory.addPeakAnnotations(project,
                        chromatogram,
                        createPeaks(file, chromatogram), trd);
                createArtificialChromatogram(project, file);
                peaksReportsImported++;

                progressHandle.progress(
                        "Imported " + (peaksReportsImported + 1) + "/" + files.length);
            }
            progressHandle.finish();
        } catch (Exception e) {
            progressHandle.finish();
        }
    }

    public void createArtificialChromatogram(IChromAUIProject project,
            File importFile) {

        File importDir = project.getImportLocation(this);
        File fragment = new File(importDir, importFile.getName());
        FileFragment f = new FileFragment(fragment);
        f.addSourceFile(new FileFragment(importFile));
        f.save();
    }

    private List<IPeakAnnotationDescriptor> createPeaks(File file, IChromatogramDescriptor chromatogram) {
        List<IPeakAnnotationDescriptor> pads = new ArrayList<IPeakAnnotationDescriptor>();
        List<Peak1D> peaks = Peak1D.fromFragment(new FileFragment(file));
        int index = 0;
        for (Peak1D peak : peaks) {
            System.out.println("Peak "+peak.getName());
            IPeakAnnotationDescriptor descriptor = DescriptorFactory.newPeakAnnotationDescriptor(
                    chromatogram,
                    peak.getName(),
                    peak.getMw(),
                    new double[]{peak.getMw()},
                    Double.NaN,
                    peak.getSnr(),
                    Double.NaN,
                    Double.NaN,
                    null,
                    null,
                    null,
                    "TICPeakFinder", peak.getStartTime(),
                    peak.getApexTime(), peak.getStopTime(), peak.getArea(),
                    peak.getApexIntensity());
            descriptor.setIndex(index++);
//            int scanIndex = chromatogram.getChromatogram().getIndexFor(peak.getApexTime());
//            Array masses = chromatogram.getChromatogram().getMasses().get(scanIndex);
//            Array intensities = chromatogram.getChromatogram().getIntensities().get(scanIndex);
//            descriptor.setMassValues((double[])masses.get1DJavaArray(double.class));
//            descriptor.setIntensityValues((int[])intensities.get1DJavaArray(int.class));
            pads.add(descriptor);
        }
        return pads;
    }
}
