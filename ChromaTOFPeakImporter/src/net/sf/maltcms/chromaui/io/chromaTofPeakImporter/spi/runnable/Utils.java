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
import cross.tools.MathTools;
import cross.tools.StringTools;
import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.parser.TableRow;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openide.util.Exceptions;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayInt;
import ucar.ma2.MAMath;

/**
 *
 * @author hoffmann
 */
public class Utils {
    
    public static void createArtificialChromatogram(File importDir, IChromAUIProject project,
            String peakListName, List<IPeakAnnotationDescriptor> peaks) {
        try {
            //        try {
            PropertiesConfiguration pc = new PropertiesConfiguration(
                    "/cfg/default.properties");
            Factory.getInstance().configure(pc);
        } catch (ConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        }

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
    
    public static double[] parseDoubleArray(String fieldName, TableRow row,
            String elementSeparator) {
        String[] values = row.get(fieldName).split(elementSeparator);
        double[] v = new double[values.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = parseDouble(values[i]);
        }
        return v;
    }

    public static double parseDouble(String fieldName, TableRow tr) {
        System.out.println("Retrieving " + fieldName);
        String value = tr.get(fieldName);
        System.out.println("Value: " + value);
        if (value.isEmpty()) {
            return Double.NaN;
        }
        return parseDouble(value);
    }

    public static double parseDouble(String s) {
        return parseDouble(s, Locale.getDefault());
    }

    public static double parseDouble(String s, Locale locale) {
        if(s==null) {
            return Double.NaN;
        }
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
