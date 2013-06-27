package de.mdcberlin.hkuich.retentionIndexCalculation.tasks;

import com.db4o.foundation.ArrayFactory;
import cross.datastructures.tuple.Tuple2D;
import cross.tools.MathTools;
import de.mdcberlin.hkuich.retentionIndexCalculation.ri.RICalculator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import lombok.Data;
import maltcms.datastructures.ms.IMetabolite;
import maltcms.datastructures.ms.IScan;
import maltcms.datastructures.ms.Scan1D;
import maltcms.tools.ArrayTools;
import maltcms.tools.MaltcmsTools;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import net.sf.maltcms.db.search.api.ri.RetentionIndexCalculator;
import net.sf.maltcms.db.search.api.similarities.AMetabolitePredicate;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.MAMath;
import ucar.ma2.MAVector;

/**
 *
 * @author hkuich
 */

@Data
public class FindAlcanesTask extends AProgressAwareRunnable implements Serializable {
    private final IChromAUIProject context;
    private final AMetabolitePredicate predicate;
    private final String alcaneMix;
    
    @Override
    public void run() {
        
        double[] alcaneRetentionTimes = new double[9];
        int alcaneRetentionTimeWindow = 13;
        List<int[]> alcaneSpectra = new ArrayList<int[]>();
        
        getProgressHandle().start(context.getChromatograms().size());
	int cnt = 1;
        try {
            
            //FIND A WASH FIRST TO ANNOTATE IT BEFORE ANNOTATING ALL THE OTHERS!
            for (IChromatogramDescriptor chrom : context.getChromatograms()) {
                if(chrom.getDisplayName().contains("wash") || chrom.getDisplayName().contains("Wash")){
                    System.out.println("Finding Alcanes in container: " + chrom.getDisplayName());
                    double[] alcaneMasses = {71.0,85.0,99.0};
                    int numberOfMaxima = 9;  
                    
                    for(Peak1DContainer container : context.getPeaks(chrom))
                    {    
                        ArrayList<IPeakAnnotationDescriptor> peaklist = new ArrayList<IPeakAnnotationDescriptor>(container.getMembers());
                        
                        Collections.sort(peaklist, new Comparator<IPeakAnnotationDescriptor>(){

                            @Override
                            public int compare(IPeakAnnotationDescriptor o1, IPeakAnnotationDescriptor o2) {
                                if(o1.getApexTime()>o2.getApexTime()) return 1;
                                if(o1.getApexTime()<o2.getApexTime()) return -1;
                                return 0;
                            }
                            
                        });
                        
                        //Completely clear previous annotation
                        for (IPeakAnnotationDescriptor ipad : peaklist) {
                            ipad.setSimilarity(Double.NaN);
                            ipad.setNativeDatabaseId("NA");
                            ipad.setName("Unknown Compound");
                            ipad.setFormula("NA");
                            ipad.setDisplayName("Unknown Compound");
                            ipad.setLibrary("custom");
                            ipad.setRetentionIndex(Double.NaN);
                        }                     
                        
                        ArrayList<Integer[][]> globalMaximaList = findNGlobalMaxima(peaklist, alcaneMasses, numberOfMaxima);
                        int alcanePeaks[] = extractMostFrequentPeaks(globalMaximaList, numberOfMaxima); 
                        
                        alcaneSpectra = annotateAlcanesWash(peaklist, alcanePeaks, numberOfMaxima);                    
                        System.out.println("Done with Annotation of Wash!!");
                        
                        for(int i = 0; i<alcanePeaks.length;i++) {
                            alcaneRetentionTimes[i] = peaklist.get(alcanePeaks[i]).getApexTime();
                        }

                        System.out.println("the name of the peak/first alcane is: " + peaklist.get(alcanePeaks[0]).getDisplayName());
                        System.out.println("the retentionTime of the peak/first alcane is: " + peaklist.get(alcanePeaks[0]).getApexTime());
                        System.out.println();
                        
                        System.out.println("start RI writing...");
                        //populate alcaneAtoms
                        
                        int[] alcaneAtoms = {10,12,15,18,19,22,28,32,36};
                        if(alcaneMix.startsWith("C17 Mix")){
                            alcaneAtoms[3] = 17;
                        } 
                        
                        double[] alcaneRTs = getAlcaneRTs(peaklist, alcanePeaks, numberOfMaxima);
                        
                        /*for(int b =0; b < alcaneAtoms.length; b++){
                            System.out.println(alcaneAtoms[b]);
                        }
                        for(int b =0; b < alcaneRTs.length; b++){
                            System.out.println(alcaneRTs[b]);
                        }*/
                        
                        RICalculator riCalc = new RICalculator(alcaneAtoms, alcaneRTs);
                        
                        //Set RI
//                        int count = 0;

                        for (IPeakAnnotationDescriptor peak : peaklist) {
                            double currentRT = peak.getApexTime();
                            
                            double linear = riCalc.getLinearIndex(currentRT);
//                            double isothermal = riCalc.getIsothermalKovatsIndex(currentRT);
//                            double tempProgr = riCalc.getTemperatureProgrammedKovatsIndex(currentRT);
                            
//                            System.out.println("current RT = " + currentRT + "   ---   linear = " + linear + "   ---   isothermal = " + isothermal + "   ---   tempProg: " + tempProgr);

//                            if(alcanePeaks[alcanePeaks.length-1] == count && Double.isNaN(currentRI)) {
//                                System.out.println("Caught! " +currentRT+ " "+currentRI + " "+ 3600 + " " + count);
//                                currentRI = 3600;
//                            }
                            peak.setRetentionIndex(linear);
                            //System.out.println("current RT = " + currentRT + "   ---   current RI = " + currentRI + "   ---   old RI: " + oldRI);
//                            count++;
//                            currentPeak++;
                        }
                        
//                        for(int i = 0; i<4; i++){
//                            for(int j =0; j<peaklist.size(); j++){
//                                if(j != peaklist.size()-1) {
//                                    System.out.print( riOutputs[i][j]+ ",");
//                                } else {
//                                    System.out.print( riOutputs[i][j]);
//                                }
//                            }
//                            System.out.println();
//                        }
                        
                        System.out.println("ended RI writing...");
                    }
                    
                    
                }
                else{
                    System.out.println("This is not a wash: " + chrom.getDisplayName());
                }
            }
            
            //NOW THAT THE WASH IS DONE, ANNOTATE THE OTHERS USING THE HELP FROM THE WASH
            for (IChromatogramDescriptor chrom : context.getChromatograms()) {
                if(!chrom.getDisplayName().contains("wash") && !chrom.getDisplayName().contains("Wash")){
                    getProgressHandle().progress("Annotating peaks on " + chrom.getDisplayName(), cnt);
                    System.out.println("Clearing Peaks on: " + chrom.getDisplayName());
                    int containerCounter=0;
                    for (Peak1DContainer container : context.getPeaks(chrom)) {
                        System.out.println("counter of container: " + ++containerCounter);
                        if(isCancel()) {
                                return;
                        }
                        getProgressHandle().progress("Annotating peaks on container " + container.getDisplayName());

                        ArrayList<IPeakAnnotationDescriptor> peaklist = new ArrayList<IPeakAnnotationDescriptor>(container.getMembers());                       
                        
                        Collections.sort(peaklist, new Comparator<IPeakAnnotationDescriptor>(){

                            @Override
                            public int compare(IPeakAnnotationDescriptor o1, IPeakAnnotationDescriptor o2) {
                                if(o1.getApexTime()>o2.getApexTime()) return 1;
                                if(o1.getApexTime()<o2.getApexTime()) return -1;
                                return 0;
                            }
                            
                        });
                        
                        //for compounds, reset everything
                        for (IPeakAnnotationDescriptor ipad : peaklist) {
                            ipad.setSimilarity(Double.NaN);
                            ipad.setNativeDatabaseId("NA");
                            ipad.setName("Unknown Compound");
                            ipad.setFormula("Na");
                            ipad.setDisplayName("Unknown Compound");
                            ipad.setLibrary("custom");
                            ipad.setRetentionIndex(Double.NaN);
             
                            //System.out.println(ipad.getApexTime());
                        }                        
                        /*for(int[] spectra : alcaneSpectra){
                            for(int g = 0; g<spectra.length;g++){
                                System.out.print(spectra[g] + " ");
                            }
                            System.out.print(spectra.length);
                            System.out.println("");
                        }
                        
                        System.out.println("AlcaneRetentionTimesArray");
                        for(int g = 0; g<alcaneRetentionTimes.length;g++){
                                System.out.print(alcaneRetentionTimes[g] + " ");
                            }
                        System.out.print(alcaneRetentionTimes.length);
                            System.out.println("");*/
                        
                        
                        annotateAlcanesSamples(peaklist, alcaneRetentionTimeWindow, alcaneRetentionTimes, alcaneSpectra);
                     
                    }
                    cnt++;
                }
            }
        } finally {
                getProgressHandle().finish();
        }
    }
    
    private double[] getAlcaneRTs(final ArrayList<IPeakAnnotationDescriptor> peaklist, int[] alcanePeaks, int numberOfMaxima) {
        double[] alcaneRTs = new double[numberOfMaxima];
        int counter = 0;
        int i = 0;
        for(IPeakAnnotationDescriptor peak : peaklist){            
            if(i<numberOfMaxima && alcanePeaks[i] == counter){
                alcaneRTs[i] = peak.getApexTime();
                i++;
            }
            counter ++;
        }        
        return alcaneRTs;
    }

    private void annotateAlcanesSamples(final ArrayList<IPeakAnnotationDescriptor> peaklist, int rtWindow, double[] alcaneRTs, List<int[]> alcaneSpectra) {
        
        int maxIndex = 0; 
        
        for(int i = 0; i<alcaneRTs.length;i++){
            System.out.println("alcane number: " + i);
            maxIndex = cosineInWindow(i,findIndexByRetentionTime(peaklist, alcaneRTs[i]-rtWindow), findIndexByRetentionTime(peaklist, alcaneRTs[i] + rtWindow), peaklist, alcaneSpectra);
            System.out.println("alcane number: " + i + "end");
            System.out.println("\t\tcurrentMaxIndex would be: " + maxIndex);
            
            peaklist.get(maxIndex).setSimilarity(Double.NaN);
            peaklist.get(maxIndex).setNativeDatabaseId("NA");
            peaklist.get(maxIndex).setFormula("found");
            peaklist.get(maxIndex).setLibrary("custom");
            peaklist.get(maxIndex).setRetentionIndex(Double.NaN); 
            //actually annotate the peaks
            switch(i){
                        case 0:                             
                            peaklist.get(maxIndex).setName("c10");                            
                            peaklist.get(maxIndex).setDisplayName("c10");                            
                            break;
                        case 1: 
                            peaklist.get(maxIndex).setName("c12");                            
                            peaklist.get(maxIndex).setDisplayName("c12"); 
                            break;
                        case 2: 
                            peaklist.get(maxIndex).setName("c15");                            
                            peaklist.get(maxIndex).setDisplayName("c15"); 
                            break;
                        case 3: 
                            if(alcaneMix.startsWith("C17 Mix")){
                                peaklist.get(maxIndex).setName("c17");                            
                                peaklist.get(maxIndex).setDisplayName("c17"); 
                            }
                            if(alcaneMix.startsWith("C18 Mix")){
                                peaklist.get(maxIndex).setName("c18");                            
                                peaklist.get(maxIndex).setDisplayName("c18"); 
                            }
                            break;
                        case 4: 
                            peaklist.get(maxIndex).setName("c19");                            
                            peaklist.get(maxIndex).setDisplayName("c19"); 
                            break;
                        case 5: 
                            peaklist.get(maxIndex).setName("c22");                            
                            peaklist.get(maxIndex).setDisplayName("c22"); 
                            break;
                        case 6: 
                            peaklist.get(maxIndex).setName("c28");                            
                            peaklist.get(maxIndex).setDisplayName("c28"); 
                            break;
                        case 7: 
                            peaklist.get(maxIndex).setName("c32");                            
                            peaklist.get(maxIndex).setDisplayName("c32"); 
                            break;
                        case 8: 
                            peaklist.get(maxIndex).setName("c36");                            
                            peaklist.get(maxIndex).setDisplayName("c36"); 
                            break;
                        };
                    
        }   


    }
    
    private int cosineInWindow(int alcaneNumber, int windowStart, int windowEnd, final ArrayList<IPeakAnnotationDescriptor> peaklist, List<int[]> alcaneSpectra){
        
        if(windowStart < 0){
            windowStart = 0;
        }        
        if(windowEnd > peaklist.size()){
            windowEnd = peaklist.size();
        }        
        if(windowEnd < windowStart)
        {
            windowEnd = peaklist.size();
        }

        List<IPeakAnnotationDescriptor> peakSubList = new ArrayList<IPeakAnnotationDescriptor>(peaklist.subList(windowStart, windowEnd));
        System.out.println("cosineSimilarity: start at " + windowStart);
        double maxSim = 0.0d;
        int maxIndex = 0;
        for(IPeakAnnotationDescriptor peak : peakSubList){    
            double currentSim = cosineSimilarity(Array.factory(alcaneSpectra.get(alcaneNumber)), Array.factory(peak.getIntensityValues())); 
            currentSim = currentSim * peak.getArea();
            if(currentSim > maxSim)
            {
                maxSim = currentSim;
                maxIndex = findExactIndexByRetentionTime(peaklist,peak.getApexTime());
            }
        }
        System.out.println("maxSim: " + maxSim);
        System.out.println("cosineSimilarity: end at " + windowEnd);
        return maxIndex;
    }
    
    private int findExactIndexByRetentionTime(final ArrayList<IPeakAnnotationDescriptor> peaklist, double retentionTime){
        int counter = 0;
        for(IPeakAnnotationDescriptor peak : peaklist){
            if(peak.getApexTime() == retentionTime)
            {
                return counter;
            }
            counter++;
        }
        return 0;
    }
    
    private int findIndexByRetentionTime(final ArrayList<IPeakAnnotationDescriptor> peaklist, double retentionTime){
        int counter = 0;
        for(IPeakAnnotationDescriptor peak : peaklist){
            if(peak.getApexTime() > retentionTime)
            {
                return counter;
            }
            counter++;
        }
        return 0;
    }
    
    private List<int[]> annotateAlcanesWash(final ArrayList<IPeakAnnotationDescriptor> peaklist, int[] peakNumbers, int numberOfMaxima)
    {
        int counterPeaklist = 0;
        int counterPeaknumbers = 0;
        
        List<int[]> alcaneSpectra = new ArrayList<int[]>();
        
        for(IPeakAnnotationDescriptor peak : peaklist) {
            if(counterPeaknumbers<numberOfMaxima) {
                if(peakNumbers[counterPeaknumbers] == counterPeaklist){
                    System.out.println("counterPeakList: " + counterPeaklist);
                    peak.setLibrary("custom");
                    peak.setRetentionIndex(Double.NaN);
                    peak.setFormula("found");
                    peak.setSimilarity(Double.NaN);
                    peak.setNativeDatabaseId("NA");
                    switch(counterPeaknumbers){
                        case 0:                             
                            peak.setName("c10");                            
                            peak.setDisplayName("c10");                             
                            alcaneSpectra.add(peak.getIntensityValues());
                            break;
                        case 1: 
                            peak.setName("c12");
                            peak.setDisplayName("c12");
                            alcaneSpectra.add(peak.getIntensityValues());
                            break;
                        case 2: 
                            peak.setName("c15");
                            peak.setDisplayName("c15");
                            alcaneSpectra.add(peak.getIntensityValues());
                            break;
                        case 3: 
                            if(alcaneMix.startsWith("C17 Mix")){
                                peak.setName("c17");
                                peak.setDisplayName("c17");
                            }
                            if(alcaneMix.startsWith("C18 Mix")){
                                peak.setName("c18");
                                peak.setDisplayName("c18");
                            }
                            alcaneSpectra.add(peak.getIntensityValues());
                            break;
                        case 4: 
                            peak.setName("c19");
                            peak.setDisplayName("c19");
                            alcaneSpectra.add(peak.getIntensityValues());
                            break;
                        case 5: 
                            peak.setName("c22");
                            peak.setDisplayName("c22");
                            alcaneSpectra.add(peak.getIntensityValues());
                            break;
                        case 6: 
                            peak.setName("c28");
                            peak.setDisplayName("c28");
                            alcaneSpectra.add(peak.getIntensityValues());
                            break;
                        case 7: 
                            peak.setName("c32");
                            peak.setDisplayName("c32");
                            alcaneSpectra.add(peak.getIntensityValues());
                            break;
                        case 8: 
                            peak.setName("c36");
                            peak.setDisplayName("c36");
                            alcaneSpectra.add(peak.getIntensityValues());
                            break;
                        };
                        
                    counterPeaknumbers++;
                }
                counterPeaklist++;
            }            
        }
        
        return alcaneSpectra;
    }
    
    
    private class Candidate implements Comparable<Candidate> {
        int counter;
        int peakNumber;
        
        Candidate(int peakNumber){
            this.peakNumber = peakNumber;
            this.counter = 1;
        }
        private int getCounter(){
            return this.counter;
        }
        private void setCounter(int counter){
            this.counter = counter;
        }
        private int getPeakNumber(){
            return this.peakNumber;
        }
        private void setPeakNumber(int peakNumber){
            this.peakNumber = peakNumber;
        }
        private void incrementCounter(){
            counter = counter + 1;
        }

        @Override
        public int compareTo(Candidate o) {
            
            int compareCounter = ((Candidate) o).getCounter();
            //return this.counter - compareCounter; //ascending
            return compareCounter - this.counter; //descending
        }
    }
    
    
    
    
    private int[] extractMostFrequentPeaks(ArrayList<Integer[][]> maximaList, int numberOfMaxima) {
        int[] mostFrequentPeaks = new int[numberOfMaxima];
        
        List<Candidate> candidates = new ArrayList<Candidate>(); 
     
        //Go through the maxima list, one massLane at a time
        for(Integer[][] array : maximaList)
        {            
            //Go through the massLane, one peakNumber at a time
            for(int i=0;i<array.length;i++) {
                System.out.println(array[i][0]);
                boolean found = false;
                int newPeakNumber = 0;
                //if the peakNumber is not yet present, add it to a list, if so, update the counter of the peakNumber in the list by one
                if(!candidates.isEmpty())
                {
                    for(Candidate candidate : candidates){     
                        if(!found){
                            if(array[i][0] == candidate.getPeakNumber())
                            {
                                found = true;
                                candidate.incrementCounter();
                            }     
                        }
                    }
                    if(!found){
                        newPeakNumber = array[i][0];
                        candidates.add(new Candidate(newPeakNumber));
                    }
                } else {
                    newPeakNumber = array[i][0];
                    candidates.add(new Candidate(newPeakNumber));
                }
            }       
        }
        /*
        System.out.println();
        System.out.println();
        for(Candidate candidate : candidates) {
            System.out.println(candidate.peakNumber + " " + candidate.counter);
        }
        System.out.println();
        System.out.println();*/
        
        Collections.sort(candidates);
        
        /*System.out.println();
        System.out.println();
        for(Candidate candidate : candidates) {
            System.out.println(candidate.peakNumber + " " + candidate.counter);
        }
        System.out.println();
        System.out.println();*/
        
        for(int i=0; i<numberOfMaxima;i++){
            mostFrequentPeaks[i] = candidates.get(i).getPeakNumber();
        }
        
        /*System.out.println("mostFrequentPeaks unsorted:");
        for(int i=0; i<numberOfMaxima;i++){
            System.out.println(mostFrequentPeaks[i]);
        } */
        
        java.util.Arrays.sort(mostFrequentPeaks);
                
        /*System.out.println("mostFrequentPeaks sorted:");
        for(int i=0; i<numberOfMaxima;i++){
            System.out.println(mostFrequentPeaks[i]);
        } */
        
        return mostFrequentPeaks;
        
    }
    
    private ArrayList<Integer[][]> findNGlobalMaxima(final ArrayList<IPeakAnnotationDescriptor> peaklist, double[] NMasses, int numberOfMaxima) {
        
        //put the massLanes into an int array, each row being one mass lane, each column being one peak, the values being ints of intensity
        int[][] NMassMatrix = new int[NMasses.length][peaklist.size()];        
        int peakCounter = 0;
        for (IPeakAnnotationDescriptor peak : peaklist) { 
            IScan scan = new Scan1D(Array.factory(peak.getMassValues()), Array.factory(peak.getIntensityValues()), peak.getIndex(), peak.getApexTime());
            
            for(int massLane = 0; massLane<NMasses.length;massLane++){
                int[] currentPeakIntensities = peak.getIntensityValues();
                double[] currentMasses = peak.getMassValues();
                int massLaneIndex = 0;
                
                //get the correct index for the NMasses
                for(int i = 0; i<currentMasses.length;i++)
                {
                    if(NMasses[massLane] == currentMasses[i])
                    {
                        massLaneIndex = i;
                    }
                }
                int currentMassLaneIntensity = currentPeakIntensities[massLaneIndex];
                NMassMatrix[massLane][peakCounter] = currentMassLaneIntensity;
            }                        
            peakCounter++;
        }     
        
        //now iterate over the three arrays an keep the indeces of the nine highest numbers, then sort them ascendingly, then print them
        
        ArrayList<Integer[][]> resultList = new ArrayList<Integer[][]>();
        
        for(int i = 0; i < NMasses.length; i++)
        {
            Integer maxima[][] = new Integer[numberOfMaxima][2];
            //set array to 0 in all cases
            for(int x = 0; x < numberOfMaxima; x++)
            {
                for(int y = 0; y<2;y++)
                {
                    maxima[x][y] = 0;
                }
            }
            
            //now populate maxima array for current Mass lane (given by i)
            for(int j = 0; j<peaklist.size();j++)
            {
                if(NMassMatrix[i][j] > maxima[numberOfMaxima-1][1])
                {
                    //save index
                    maxima[numberOfMaxima-1][0] = j;
                    //save actual value
                    maxima[numberOfMaxima-1][1] = NMassMatrix[i][j];

                    Arrays.sort(maxima, new Comparator<Integer[]>()
                    {
                        @Override
                        public int compare(Integer[] int1, Integer[] int2)
                        {
                            Integer numOfKeys1 = int1[1];
                            Integer numOfKeys2 = int2[1];
                            return numOfKeys2.compareTo(numOfKeys1);
                        }
                    });
                }
            }
            Arrays.sort(maxima, new Comparator<Integer[]>()
            {
                @Override
                public int compare(Integer[] int1, Integer[] int2)
                {
                    Integer numOfKeys1 = int1[0];
                    Integer numOfKeys2 = int2[0];
                    return numOfKeys1.compareTo(numOfKeys2);
                }
            });  
            resultList.add(maxima);
        }
        
        return resultList;
    }
    
    private double cosineSimilarity(final Array t1, final Array t2) {
            if ((t1.getRank() == 1) && (t2.getRank() == 1)) {
                final MAVector ma1 = new MAVector(t1);
                final MAVector ma2 = new MAVector(t2);
                return ma1.cos(ma2);
            }
            throw new IllegalArgumentException("Arrays shapes are incompatible! " + t1.getShape()[0] + " != " + t2.getShape()[0]);
        }
}
