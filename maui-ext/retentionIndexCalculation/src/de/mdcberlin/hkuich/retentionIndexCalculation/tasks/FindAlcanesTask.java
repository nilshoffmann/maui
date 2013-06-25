package de.mdcberlin.hkuich.retentionIndexCalculation.tasks;

import cross.datastructures.tuple.Tuple2D;
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
    
    @Override
    public void run() {
        getProgressHandle().start(context.getChromatograms().size());
	int cnt = 1;
        try {
            for (IChromatogramDescriptor chrom : context.getChromatograms()) {
                    getProgressHandle().progress("Annotating peaks on " + chrom.getDisplayName(), cnt);

                    for (Peak1DContainer container : context.getPeaks(chrom)) {
                        if(isCancel()) {
                                return;
                        }
                        getProgressHandle().progress("Annotating peaks on container " + container.getDisplayName());

                        System.out.println("Resetting peak match information!");
                        for (IPeakAnnotationDescriptor ipad : container.getMembers()) {
                                if(isCancel()) {
                                        return;
                                }
                                ipad.setSimilarity(Double.NaN);
                                ipad.setNativeDatabaseId("NA");
                                ipad.setName("Unknown Compound");
                                ipad.setFormula("NA");
                                ipad.setDisplayName("Unknown Compound");
                                ipad.setLibrary("custom");
                                ipad.setRetentionIndex(Double.NaN);
                        }

                        //container.getMembers() contains all the peaks. 
                        ArrayList<IPeakAnnotationDescriptor> peaklist = new ArrayList<IPeakAnnotationDescriptor>(container.getMembers());
                        System.out.println("Finding Alcanes in container: " + container.getDisplayName());
                        int peakIndex = 0;

                        for (IPeakAnnotationDescriptor peak : peaklist) { 
                            IScan scan = new Scan1D(Array.factory(peak.getMassValues()), Array.factory(peak.getIntensityValues()), peak.getIndex(), peak.getApexTime());

//                              AMetabolitePredicate amp = predicate.copy();
//				amp.setMaxHits(5);
//                              amp.setScoreThreshold(0.9);         
//				amp.setScan(scan);
//                              AMetabolitePredicate queryPredicate = amp;                                

                            double similarity = cosineSimilarity(Array.factory(peak.getIntensityValues()),Array.factory(peak.getIntensityValues()));
                            /*System.out.println("Array.factory(peak.getMassValues()): " + Array.factory(peak.getMassValues()));
                            System.out.println("Array.factory(peak.getIntensityValues()): " + Array.factory(peak.getIntensityValues()));
                            System.out.println("CosineSimilarity of identical peaks: " + similarity);
                            System.out.println("peak.getIndex(): " + peak.getIndex()); 
                            System.out.println("peakIndex: " + peakIndex); */
                            peakIndex++;
                        }
                            
                        double[] alcaneMasses = {71.0,85.0,99.0};
                        int numberOfMaxima = 9;

                        ArrayList<Integer[][]> globalMaximaList = findNGlobalMaxima(peaklist, alcaneMasses, numberOfMaxima);
                        extractMostFrequentPeaks(globalMaximaList, numberOfMaxima);    
                        
                    }
                    cnt++;
            }
        } finally {
                getProgressHandle().finish();
        }
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
        
        System.out.println();
        System.out.println();
        for(Candidate candidate : candidates) {
            System.out.println(candidate.peakNumber + " " + candidate.counter);
        }
        System.out.println();
        System.out.println();
        
        Collections.sort(candidates);
        
        System.out.println();
        System.out.println();
        for(Candidate candidate : candidates) {
            System.out.println(candidate.peakNumber + " " + candidate.counter);
        }
        System.out.println();
        System.out.println();
        
        for(int i=0; i<numberOfMaxima;i++){
            mostFrequentPeaks[i] = candidates.get(i).getPeakNumber();
        }
        
        System.out.println("mostFrequentPeaks unsorted:");
        for(int i=0; i<numberOfMaxima;i++){
            System.out.println(mostFrequentPeaks[i]);
        } 
        
        java.util.Arrays.sort(mostFrequentPeaks);
                
        System.out.println("mostFrequentPeaks sorted:");
        for(int i=0; i<numberOfMaxima;i++){
            System.out.println(mostFrequentPeaks[i]);
        } 
        
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
