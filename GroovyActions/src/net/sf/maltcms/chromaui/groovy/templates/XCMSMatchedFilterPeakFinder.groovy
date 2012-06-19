/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.groovy.templates

import java.text.DateFormat
import java.text.SimpleDateFormat

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.project.Project;
import net.sf.maltcms.chromaui.groovy.RawDataGroovyScript;
import net.sf.maltcms.chromaui.groovy.CSVFile;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import maltcms.ui.fileHandles.cdf.CDFDataObject;
import org.apache.commons.configuration.*;
import org.openide.loaders.DataObject;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputWriter;
import org.openide.util.Cancellable;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import net.sf.maltcms.*;
import cross.*;
import cross.datastructures.fragments.*;
import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.VariableFragment;
import cross.datastructures.tools.ArrayTools;
import cross.tools.MathTools;
import cross.tools.StringTools;
import maltcms.*;
import maltcms.datastructures.ms.*;
import ucar.ma2.*;

/**
 *
 * @author nilshoffmann
 */
class XCMSMatchedFilterPeakFinder implements RawDataGroovyScript {

    final String name = "XCMSMatchedFilterPeakFinder"
    private IChromAUIProject project
    private Collection<CDFDataObject> dataObjects
    private ProgressHandle progressHandle

    double signalToNoise = 5.0
    double fullWidthAtHalfMaximum = 5.0
    int max = 10
    boolean useQsub = false
    String xcmsRScriptLocation = "/home/hoffmann/maui/extensions/R/xcmsMatchedFilterPeakFinder.R"
    
    private boolean cancel = false

    private Process process

    public void create(IChromAUIProject project, ProgressHandle progressHandle, Collection<CDFDataObject> dobjects) {
        this.project = project
        this.progressHandle = progressHandle
        this.dataObjects = dobjects
    }
    
    public String getCategory() {
        return "PeakFinding,PeakAlignment"
    }

    @Override
    public boolean cancel() {
        cancel = true
        if(process!=null) {
            process.waitForOrKill(1000)
        }
    }
    
    private int runQSubCall(File outdir, File rscriptLocation, Collection<CDFDataObject> dataObjects, OutputWriter writer) {
        def qsubArrayJobFile = new File(outdir,"arrayJob.txt")
        def arrayJobScript =
"""#!/bin/bash
ARGUMENTS=\$(sed -n -e "\${SGE_TASK_ID}p" ${qsubArrayJobFile.absolutePath})
/vol/r-2.13/bin/Rscript ${rscriptLocation} \$ARGUMENTS
"""
        def arrayJobFile = new File(outdir,"arrayJob.sh")
        arrayJobFile.setText(arrayJobScript)

        def submissionScript =
"""#!/bin/bash
qsub -V -cwd -sync y -j y -o "submit.out" -N "MAUI-QSUB" -t 1-${dataObjects.size()} -q "*@@qics,*@@fujitsu1,*@@supermicro1,*@@supermicro2" arrayJob.sh
"""
        def submissionScriptFile = new File(outdir,"submit.sh")
        submissionScriptFile.setText(submissionScript)

        writer.print "Creating job configuration at ${qsubArrayJobFile.absolutePath}\n"
        writer.print "Creating job script at ${arrayJobFile.absolutePath}\n"

        def chmodAJF = "chmod ug+x ${arrayJobFile.absolutePath}".execute()
        chmodAJF.consumeProcessOutput(writer,writer)
        chmodAJF.waitFor()
        if(chmodAJF.exitValue()!=0) {
            throw new RuntimeException("Could not set rights to executable for group and user on ${arrayJobFile.absolutePath}. Please check permissions in ${outdir.absolutePath}")
        }

        def chmodScript = "chmod ug+x ${submissionScriptFile.absolutePath}".execute()
        chmodScript.consumeProcessOutput(writer,writer)
        chmodScript.waitFor()
        if(chmodScript.exitValue()!=0) {
            throw new RuntimeException("Could not set rights to executable for group and user on ${submissionScriptFile.absolutePath}. Please check permissions in ${outdir.absolutePath}")
        }

        progressHandle.progress("Creating call script",2)
        def dobjects = dataObjects as Set
        for(CDFDataObject it:dobjects) {
            if(cancel) {
                progressHandle.finish()
                return
            }
            IFileFragment rootFragment = cross.datastructures.tools.FragmentTools.getDeepestAncestor(it.getFragment())[0]
            //
            qsubArrayJobFile.append("--filepath ${rootFragment.absolutePath} --out ${outdir.absolutePath} --snr ${signalToNoise} --fwhm ${fullWidthAtHalfMaximum} --max ${max}\n")
        }

        writer.print "Processing ${dataObjects.size()} lines of parameters\n"
        progressHandle.progress("Running XCMS",3);
        process = new ProcessBuilder(processCall).directory(outdir).start()
        process.consumeProcessOutput(writer,writer)
        process.waitFor()
        return process.exitValue()
    }
    
    private int createCall(File outdir, File rscriptLocation, Collection<CDFDataObject> dataObjects, OutputWriter writer) {
        def dobjects = dataObjects as Set
        progressHandle.progress("Running XCMS",3);
        for(CDFDataObject it:dobjects) {
            if(cancel) {
                progressHandle.finish()
                return
            }
            IFileFragment rootFragment = cross.datastructures.tools.FragmentTools.getDeepestAncestor(it.getFragment())[0]
            List<String> processCall = ["Rscript",rscriptLocation.absolutePath, "--filepath" ,rootFragment.absolutePath,"--out",outdir.absolutePath ,"--snr",""+signalToNoise,"--fwhm",""+fullWidthAtHalfMaximum,"--max",""+max]
            process = new ProcessBuilder(processCall).directory(outdir).start()
            process.consumeProcessOutput(writer,writer)
            process.waitFor()
            if(process.exitValue()!=0) {
                return process.exitValue()
            }
        }
        return 0
    }

    @Override
    public void run() {
        InputOutput io = IOProvider.getDefault().getIO(
            	"Running ${name} on ${dataObjects.size()} chromatograms", true);
        io.select();
        final OutputWriter writer = io.getOut();
        try{
            progressHandle.setDisplayName(name)
            progressHandle.start(5)
            def Date date = new Date()
            def DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
            def outdir = new File(FileUtil.toFile(project.getOutputDir()),getName())
            outdir = new File(outdir,formatter.format(date))
            outdir.mkdirs()
            
            progressHandle.progress("Retrieving Chromatograms",1);
            LinkedHashMap<String, IChromatogramDescriptor> chromatograms = createChromatogramMap(project, writer);

            
            def jobLines = dataObjects.size()

            def rscriptLocation = new File(xcmsRScriptLocation)

            int exitValue = 1
            
            if(useQsub) {
                exitValue = createQSubCall(outdir, rscriptLocation, dataObjects, writer)
            } else {
                exitValue = createCall(outdir, rscriptLocation, dataObjects, writer)
            }
            
            if(process.exitValue()!=0) {
                throw new RuntimeException("Script finished execution with code ${process.exitValue()}. Please check output in ${outdir.absolutePath}")
            }

            progressHandle.progress("Matching Chromatograms",4);
            LinkedHashMap<String, File> reports = mapReports(chromatograms, outdir, writer);
            if (reports.isEmpty()) {
                NotifyDescriptor.Message message = new NotifyDescriptor.Message(
                        "Could not match reports to existing chromatograms!",
                    NotifyDescriptor.WARNING_MESSAGE);
                DialogDisplayer.getDefault().notify(message);
            }

            progressHandle.progress("Creating Peak Reports",5);
            IToolDescriptor trd = DescriptorFactory.newToolResultDescriptor();
            trd.setName(getClass().getSimpleName());
            trd.setDisplayName(getClass().getSimpleName());
            File importDir = null;
            if(!reports.keySet().isEmpty()) {
                importDir = project.getImportLocation(this);
            }

            writer.print "Importing Peaks\n"
            importPeaks(progressHandle, reports, chromatograms, trd, importDir, writer)
            writer.print "Done\n"
            FileObject fo = FileUtil.toFileObject(outdir)
            fo.refresh()
            project.refresh()
            //io.closeInputOutput()
            progressHandle.finish()
        } catch(Exception e) {
            writer.println "Caught Exception:"
            writer.println e.getLocalizedMessage()
            e.printStackTrace(writer)
            progressHandle.finish()
        } finally {
            //progressHandle.finish()
        }

    }

    private void importPeaks(ProgressHandle progressHandle, LinkedHashMap<String,File> reports, LinkedHashMap<String, IChromatogramDescriptor> chromatograms, IToolDescriptor trd, File importDir, writer) {
        for (String chromName : reports.keySet()) {

            IChromatogramDescriptor chromatogram = chromatograms.get(
                chromName);

            writer.println(
                        "Using " + chromatogram.getResourceLocation() + " as chromatogram!");
            File file = reports.get(chromName);
            int index = 0
            //fields
            //"mz"	"mzmin"	"mzmax"	"rt"	"rtmin"	"rtmax"	"into"	"intf"	"maxo"	"maxf"	"i"	"sn"
            List<IPeakAnnotationDescriptor> peaks = new ArrayList<IPeakAnnotationDescriptor>();
            SortedMap<Double,IPeakAnnotationDescriptor> peakSet = new TreeMap<Double,IPeakAnnotationDescriptor>();
            
            try{
                file.eachLine{
                    line ->
                    if(index==0) {
                        writer.println "Skipping first line"
                    }else{
                        String[] lineContent = line.split("\t")
                        //writer.println "Parsing content for peak ${index}: ${lineContent}"
                        //lineContent[0] = lineNumber
                        double rt = Double.parseDouble(lineContent[4])
                        IPeakAnnotationDescriptor descriptor = null
                        double intensity = Double.parseDouble(lineContent[8])
                        double[] mzs= new double[1]
                        mzs[0]=Double.parseDouble(lineContent[1])
                        if(peakSet.containsKey(Double.valueOf(rt))) {
                            descriptor = peakSet.get(Double.valueOf(rt))
                            double[] mzsOld = descriptor.getQuantMasses()
                            double[] mzsNew = new double[mzsOld.length+1]
                            mzsNew[mzsOld.length] = mzs[0]
                            System.arraycopy(mzsOld,0,mzsNew,0,mzsOld.length)
                            Arrays.sort(mzsNew)
                            descriptor.setQuantMasses(mzsNew)
                        }else{
                            descriptor = DescriptorFactory.newPeakAnnotationDescriptor(
                                chromatogram,
				"@"+String.format("%.2f",rt),
                                mzs[0],
                                mzs,
                                Double.NaN,
                                Double.parseDouble(lineContent[12]),
                                fullWidthAtHalfMaximum,
                                Double.NaN,
                                null,
                                null,
                                null,
				"XCMS Matched Filter",
                                Double.parseDouble(lineContent[4]),
                                Double.parseDouble(lineContent[5]),
                                rt,
                                intensity,
                                intensity
                            )
                            //TODO descriptor massValues und intensityValues ergaenzen
                            descriptor.setIndex(index-1)
                            descriptor.setDisplayName(descriptor.getName()) 
                            peakSet.put(Double.valueOf(rt),descriptor)
                        }
                    }
                    index++;
                }
                
                for(Double d:peakSet.keySet()) {
                    writer.println "Adding peak for rt ${d}"
                    IPeakAnnotationDescriptor descriptor = peakSet.get(d)
                    IChromatogram chrom = chromatogram.getChromatogram()
                    Scan1D s = chrom.getScan(chrom.getIndexFor(descriptor.getApexTime()))
                    descriptor.setMassValues((double[])s.getMasses().get1DJavaArray(double.class));
                    descriptor.setIntensityValues((int[])s.getIntensities().get1DJavaArray(int.class));
                    peaks.add(descriptor)
                }

                DescriptorFactory.addPeakAnnotations(project,
                    chromatogram,
                    peaks, trd);


                //createArtificialChromatogram(importDir, project,
                //        new File(chromatogram.getResourceLocation()).getName(),
                //        peaks);
                createArtificialChromatogram(importDir, project,
                    new File(chromatogram.getResourceLocation()).getName(), peaks)
            }catch(Exception e) {
                writer.println(e.getLocalizedMessage())
            }
        }

    }

    private LinkedHashMap<String, File> mapReports(LinkedHashMap<String, IChromatogramDescriptor> chromatograms, File outputDir, writer) {
        LinkedHashMap<String, File> reports = new LinkedHashMap<String, File>();
        //match all xcms peak tables
        def p = ~/.*\.xpt/
        outputDir.eachFileMatch(p) {
            file ->
            String chromName = file.getName();
            while(chromName.contains(".")) {
                chromName = chromName.substring(0, chromName.lastIndexOf(
							"."));
            }
            if (chromatograms.containsKey(chromName)) {
                reports.put(chromName, file);
                writer.println("Adding report: " + chromName+" with file "+file.absolutePath);
            } else {
                writer.println(
							"Could not find matching chromatogram for report: " + chromName);
            }
        }
        if (reports.size() != chromatograms.size()) {
            writer.println(
                    "Not all chromatograms could be matched!");
        }
        return reports;
    }

    private LinkedHashMap<String, IChromatogramDescriptor> createChromatogramMap(IChromAUIProject project,writer) {
        LinkedHashMap<String, IChromatogramDescriptor> chromatograms = new LinkedHashMap<String, IChromatogramDescriptor>();
        for (IChromatogramDescriptor descriptor : project.getChromatograms()) {
            String chromName = new File(descriptor.getResourceLocation()).getName();
            while(chromName.contains(".")) {
                chromName = chromName.substring(0, chromName.lastIndexOf(
						"."));
            }
            chromatograms.put(chromName, descriptor);
            writer.println(
					"Added chromatogram " + chromName + ": " + descriptor);
        }
        return chromatograms;
    }

    private void createArtificialChromatogram(File importDir, IChromAUIProject project,
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

}

