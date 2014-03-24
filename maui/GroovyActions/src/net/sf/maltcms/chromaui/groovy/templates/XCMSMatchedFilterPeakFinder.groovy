<#if package?? && package != "">
package ${package};

</#if>
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
* @author ${user}
*/
class ${name} implements RawDataGroovyScript {

    final String name = "${name}"
    private IChromAUIProject project
    private Collection<CDFDataObject> dataObjects
    private ProgressHandle progressHandle

    double signalToNoise = 5.0
    double fullWidthAtHalfMaximum = 5.0
    int max = 10
    String xcmsRScriptLocation = "scripts/R/xcmsMatchedFilterPeakFinder.R"
    String rscriptLocation = "/vol/r-2.13/bin/Rscript"

    String rscript = """
suppressPackageStartupMessages(library("optparse"))
option_list <- list(
make_option("--filepath",default=getwd(),help="Path to chromatogram file [default %default]"),
make_option("--out",default=getwd(),help="Path to output directory [default %default]"),
make_option("--snr", default=10.0,
help="Signal-to-noise threshold for peaks [default %default]"),
make_option("--max", default=5,
help="Maximum number of peaks per EIC [default %default]"),
make_option("--fwhm", default=30.0,
help="Full width at half maximum for peaks [default %default]"))
opt <- parse_args(OptionParser(option_list=option_list))
print(opt)

SNR <- opt\$snr
FWHM <- opt\$fwhm
MAX <- opt\$max
FILEPATH <- sub("%20"," ",opt\$filepath,fixed=TRUE)
OUTDIR <- sub("%20"," ", opt\$out,fixed=TRUE)

cat("Using ",FILEPATH," as input","\n")
suppressPackageStartupMessages(library("xcms"))

paramString <- paste("snr",SNR,"_","fwhm",FWHM,"_","max",MAX,sep="")
cat("Processing file ",basename(FILEPATH),"\n")
xraw <- xcmsRaw(filename=FILEPATH,profmethod="bin",profstep=1.0);
peaks <- findPeaks(xraw,max=MAX,snthresh=SNR,fwhm=FWHM);
write.table(peaks, file=paste(OUTDIR,"/",basename(FILEPATH),".xpt",sep=""), col.names=TRUE,sep="\t");
"""
    
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
            	"Running ${r"${name}"} on ${r"${dataObjects.size()}"} chromatograms", true);
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
            if(!rscriptLocation.isAbsolute()) {
                File rscriptFile = new File(FileUtil.toFile(project.getProjectDirectory()),xcmsRScriptLocation)
                rscriptFile.getParentFile().mkdirs()
                rscriptLocation = rscriptFile
            }
            if(!rscriptLocation.exists()) {
                rscriptLocation.setText(rscript)
            }

            int exitValue = createCall(outdir, rscriptLocation, dataObjects, writer)
            
            if(exitValue!=0) {
                throw new RuntimeException("Script finished execution with code ${r"${exitValue}"}. Please check output in ${r"${outdir.absolutePath}"}")
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

            writer.println("Using " + chromatogram.getResourceLocation() + " as chromatogram!");
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
                        IPeakAnnotationDescriptor descriptor = null
                        String[] lineContent = line.split("\t")
                        writer.println "Parsing content for peak ${r"${index}"}: ${r"${lineContent}"}"
                        //lineContent[0] = lineNumber
                        double mz = Double.parseDouble(lineContent[1])
                        //double mzmin = Double.parseDouble(lineContent[2])
                        //double mzmax = Double.parseDouble(lineContent[3])
                        double rt = Double.parseDouble(lineContent[4])
                        double rtmin = Double.parseDouble(lineContent[5])
                        double rtmax = Double.parseDouble(lineContent[6])
                        double rawArea = Double.parseDouble(lineContent[7])
                        double filteredArea = Double.parseDouble(lineContent[8])
                        //double rawIntensity = Double.parseDouble(lineContent[9])
                        double filteredIntensity = Double.parseDouble(lineContent[10])
                        //double rankInEic = Double.parseDouble(lineContent[11])
                        double snr = Double.parseDouble(lineContent[12])
                        double[] mzs= new double[1]
                        mzs[0]=mz
                        if(peakSet.containsKey(Double.valueOf(rt))) {
                            descriptor = peakSet.get(Double.valueOf(rt))
                            double[] mzsOld = descriptor.getQuantMasses()
                            double[] mzsNew = new double[mzsOld.length+1]
                            mzsNew[mzsOld.length] = mzs[0]
                            System.arraycopy(mzsOld,0,mzsNew,0,mzsOld.length)
                            Arrays.sort(mzsNew)
                            descriptor.setQuantMasses(mzsNew)
                            descriptor.setArea(descriptor.getArea()+filteredArea)
                            descriptor.setRawArea(descriptor.getRawArea()+rawArea)
                            descriptor.setApexIntensity(descriptor.getApexIntensity()+filteredIntensity)
                            double[] massValuesOld = descriptor.getMassValues()
                            double[] massValuesNew = new double[massValuesOld.length+1]
                            System.arraycopy(massValuesOld,0,massValuesNew,0,massValuesOld.length)
                            massValuesNew[massValuesOld.length] = mz
                            descriptor.setMassValues(massValuesNew)
                            int[] intensityValuesOld = descriptor.getIntensityValues()
                            int[] intensityValuesNew = new int[intensityValuesOld.length+1]
                            System.arraycopy(intensityValuesOld,0,intensityValuesNew,0,intensityValuesOld.length)
                            intensityValuesNew[intensityValuesOld.length] = (int)Math.rint(filteredIntensity)
                            descriptor.setIntensityValues(intensityValuesNew)
                        }else{
                            descriptor = DescriptorFactory.newPeakAnnotationDescriptor(
                                chromatogram,
                                "@"+String.format("%.2f",rt),
                                mzs[0],
                                mzs,
                                Double.NaN,
                                snr,
                                fullWidthAtHalfMaximum,
                                Double.NaN,
                                null,
                                null,
                                null,
                                "XCMS Matched Filter",
                                rtmin,
                                rt,
                                rtmax,
                                filteredArea,
                                filteredIntensity
                            )
                            double[] mza = new double[1]
                            mza[0] = mz
                            descriptor.setMassValues(mza)
                            int[] inta = new int[1]
                            inta[0] = (int)Math.rint(filteredIntensity)
                            descriptor.setIntensityValues(inta)
                            descriptor.setRawArea(rawArea)
                            //TODO descriptor massValues und intensityValues ergaenzen
                            descriptor.setIndex(index-1)
                            descriptor.setDisplayName(descriptor.getName()) 
                            peakSet.put(Double.valueOf(rt),descriptor)
                        }
                    }
                    index++;
                }
                
                for(Double d:peakSet.keySet()) {
                    writer.println "Adding peak for rt ${r"${d}"}"
                    IPeakAnnotationDescriptor descriptor = peakSet.get(d)
                    double[] massValues = descriptor.getMassValues()
                    int[] intensityValues = descriptor.getIntensityValues()
                    def mvals = new TreeMap()
                    for(int i = 0; i<massValues.length; i++) {
                        mvals[massValues[i]] = intensityValues[i]
                    }
                    int cnt = 0
                    mvals.each{ 
                        key,value -> 
                        massValues[cnt] = key
                        intensityValues[cnt] = value
                        cnt++
                    }
                    descriptor.setMassValues(massValues)
                    descriptor.setIntensityValues(intensityValues)
                    peaks.add(descriptor)
                }

                DescriptorFactory.addPeakAnnotations(project,
                    chromatogram,
                    peaks, trd);
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
                chromName = chromName.substring(0, chromName.lastIndexOf("."));
            }
            if (chromatograms.containsKey(chromName)) {
                reports.put(chromName, file);
                writer.println("Adding report: " + chromName+" with file "+file.absolutePath);
            } else {
                writer.println("Could not find matching chromatogram for report: " + chromName);
            }
        }
        if (reports.size() != chromatograms.size()) {
            writer.println("Not all chromatograms could be matched!");
        }
        return reports;
    }

    private LinkedHashMap<String, IChromatogramDescriptor> createChromatogramMap(IChromAUIProject project,writer) {
        LinkedHashMap<String, IChromatogramDescriptor> chromatograms = new LinkedHashMap<String, IChromatogramDescriptor>();
        for (IChromatogramDescriptor descriptor : project.getChromatograms()) {
            String chromName = new File(descriptor.getResourceLocation()).getName();
            while(chromName.contains(".")) {
                chromName = chromName.substring(0, chromName.lastIndexOf("."));
            }
            chromatograms.put(chromName, descriptor);
            writer.println("Added chromatogram " + chromName + ": " + descriptor);
        }
        return chromatograms;
    }

    }
