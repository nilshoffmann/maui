/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.runnable;

import cross.Factory;
import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.VariableFragment;
import cross.datastructures.tools.ArrayTools;
import cross.datastructures.tuple.Tuple2D;
import cross.tools.MathTools;
import cross.tools.StringTools;
import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.Data;
import net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.parser.ChromaTOFParser;
import net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.parser.TableRow;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayInt;
import ucar.ma2.MAMath;

/**
 *
 * @author nilshoffmann
 */
@Data
public class ChromaTofPeakList1DImporter extends AProgressAwareRunnable {

    private final IChromAUIProject project;
    private final File[] files;
    private Locale locale = Locale.US;

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
                if (file.getName().toLowerCase().endsWith("csv")) {
                    ChromaTOFParser.FIELD_SEPARATOR = ",";
                } else if (file.getName().toLowerCase().endsWith("tsv")) {
                    ChromaTOFParser.FIELD_SEPARATOR = "\t";
                }
                Tuple2D<LinkedHashSet<String>, List<TableRow>> report = ChromaTOFParser.parseReport(reports.get(chromName));
                List<IPeakAnnotationDescriptor> peaks = new ArrayList<IPeakAnnotationDescriptor>();
                LinkedHashSet<String> header = report.getFirst();
                System.out.println("Available fields: " + header);
                int index = 0;
                for (TableRow tr : report.getSecond()) {
//                Peak1D p = new Peak1D();
//                p.setStartTime(
//                        );
//                p.setApexTime());
//                p.setStopTime(
//                        );
//                p.setArea();
//                p.setName(tr.get("NAME"));
                    String rt = tr.get("R.T._(S)");
                    if (rt.contains(",")) {//2D mode
                        String[] rts = rt.split(",");
                        double rt1 = parseDouble(rts[0].trim());
                        double rt2 = parseDouble(rts[1].trim());
                        IPeakAnnotationDescriptor descriptor = DescriptorFactory.newPeak2DAnnotationDescriptor(
                                chromatogram,
                                tr.get("NAME"),
                                parseDouble((tr.get("UNIQUEMASS"))),
                                parseDoubleArray("QUANT_MASSES", tr, ","),
                                parseDouble((tr.get("RETENTION_INDEX"))),
                                parseDouble((tr.get("S/N"))),
                                Double.parseDouble(tr.get(
                                "FULL_WIDTH_AT_HALF_HEIGHT")),
                                parseDouble((tr.get("SIMILARITY"))),
                                tr.get("LIBRARY"),
                                tr.get("CAS"),
                                tr.get("FORMULA"),
                                "ChromaTOF", parseDouble(tr.get("INTEGRATIONBEGIN")),
                                rt1+rt2, parseDouble((tr.get(
                                "INTEGRATIONEND"))), parseDouble((tr.get("AREA"))),
                                Double.NaN,rt1,rt2);
                        descriptor.setIndex(index++);
//                descriptor.setPeak(p);
                        Tuple2D<double[], int[]> massSpectrum = ChromaTOFParser.convertMassSpectrum(tr.get("SPECTRA"));
                        descriptor.setMassValues(massSpectrum.getFirst());
                        descriptor.setIntensityValues(massSpectrum.getSecond());
                        peaks.add(descriptor);
                    } else {
                        IPeakAnnotationDescriptor descriptor = DescriptorFactory.newPeakAnnotationDescriptor(
                                chromatogram,
                                tr.get("NAME"),
                                parseDouble((tr.get("UNIQUEMASS"))),
                                parseDoubleArray("QUANT_MASSES", tr, ","),
                                parseDouble((tr.get("RETENTION_INDEX"))),
                                parseDouble((tr.get("S/N"))),
                                Double.parseDouble(tr.get(
                                "FULL_WIDTH_AT_HALF_HEIGHT")),
                                parseDouble((tr.get("SIMILARITY"))),
                                tr.get("LIBRARY"),
                                tr.get("CAS"),
                                tr.get("FORMULA"),
                                "ChromaTOF", parseDouble(tr.get("INTEGRATIONBEGIN")),
                                parseDouble((tr.get("R.T._(S)"))), parseDouble((tr.get(
                                "INTEGRATIONEND"))), parseDouble((tr.get("AREA"))),
                                Double.NaN);
                        descriptor.setIndex(index++);
//                descriptor.setPeak(p);
                        Tuple2D<double[], int[]> massSpectrum = ChromaTOFParser.convertMassSpectrum(tr.get("SPECTRA"));
                        descriptor.setMassValues(massSpectrum.getFirst());
                        descriptor.setIntensityValues(massSpectrum.getSecond());
                        peaks.add(descriptor);
                    }

//                    System.out.println("Adding peak: " + descriptor.getName() + " " + descriptor.
//                            getApexTime());

                }
                createArtificialChromatogram(project,
                        new File(chromatogram.getResourceLocation()).getName(),
                        peaks);
                //System.out.println("Adding peak annotations: " + peaks);
                DescriptorFactory.addPeakAnnotations(project,
                        chromatogram,
                        peaks, trd);
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
            String peakListName, List<IPeakAnnotationDescriptor> peaks) {
        try {
            //        try {
            PropertiesConfiguration pc = new PropertiesConfiguration(
                    "/cfg/default.properties");
            Factory.getInstance().configure(pc);
        } catch (ConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        }

        File importDir = new File(FileUtil.toFile(project.getProjectDirectory()),
                "import");
        importDir.mkdirs();
        File fragment = new File(importDir, StringTools.removeFileExt(
                peakListName));
        FileFragment f = new FileFragment(fragment);
        List<Array> masses = new ArrayList<Array>();
        List<Array> intensities = new ArrayList<Array>();
        Array sat = new ArrayDouble.D1(peaks.size());
        ArrayInt.D1 scanIndex = new ArrayInt.D1(peaks.size());
        ArrayInt.D1 tic = new ArrayInt.D1(peaks.size());
        ArrayDouble.D1 massMin = new ArrayDouble.D1(peaks.size());
        ArrayDouble.D1 massMax = new ArrayDouble.D1(peaks.size());
        int i = 0;
        int scanOffset = 0;
        double minMass = Double.POSITIVE_INFINITY;
        double maxMass = Double.NEGATIVE_INFINITY;
        for (IPeakAnnotationDescriptor descr : peaks) {
            minMass = Math.min(minMass, MathTools.min(descr.getMassValues()));
            maxMass = Math.max(maxMass, MathTools.max(descr.getMassValues()));
            massMin.set(i, minMass);
            massMax.set(i, maxMass);
            masses.add(Array.factory(descr.getMassValues()));
            Array intensA = Array.factory(descr.getIntensityValues());
            intensities.add(intensA);
            sat.setDouble(i, descr.getApexTime());
            scanIndex.set(i, scanOffset);
            tic.setDouble(i, MAMath.sumDouble(intensA));
            scanOffset += descr.getMassValues().length;
            i++;
        }
        IVariableFragment scanIndexVar = new VariableFragment(f,
                "scan_index");
        scanIndexVar.setArray(scanIndex);
        IVariableFragment massValuesVar = new VariableFragment(f,
                "mass_values");
        massValuesVar.setArray(ArrayTools.glue(masses));
        IVariableFragment intensityValuesVar = new VariableFragment(f,
                "intensity_values");
        intensityValuesVar.setArray(ArrayTools.glue(intensities));
        IVariableFragment satVar = new VariableFragment(f,
                "scan_acquisition_time");
        satVar.setArray(sat);
        IVariableFragment ticVar = new VariableFragment(f,
                "total_intensity");
        ticVar.setArray(tic);
        IVariableFragment minMassVar = new VariableFragment(f, "mass_range_min");
        minMassVar.setArray(massMin);
        IVariableFragment maxMassVar = new VariableFragment(f, "mass_range_max");
        maxMassVar.setArray(massMax);
        f.save();
//            return f;
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        return null;

    }

    public double[] parseDoubleArray(String fieldName, TableRow row,
            String elementSeparator) {
        String[] values = row.get(fieldName).split(elementSeparator);
        double[] v = new double[values.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = parseDouble(values[i]);
        }
        return v;
    }

    public double parseDouble(String fieldName, TableRow tr) {
        System.out.println("Retrieving " + fieldName);
        String value = tr.get(fieldName);
        System.out.println("Value: " + value);
        if (value.isEmpty()) {
            return Double.NaN;
        }
        return parseDouble(value);
    }

    public double parseDouble(String s) {
        return parseDouble(s, locale);
    }

    public double parseDouble(String s, Locale locale) {
        try {
            return NumberFormat.getNumberInstance(locale).parse(s).doubleValue();
        } catch (ParseException ex) {
            try {
                return NumberFormat.getNumberInstance(Locale.US).parse(s).
                        doubleValue();
            } catch (ParseException ex1) {
                return Double.NaN;
            }
        }
    }
}
