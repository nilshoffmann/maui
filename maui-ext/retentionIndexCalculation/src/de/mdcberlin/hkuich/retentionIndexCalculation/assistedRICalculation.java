package de.mdcberlin.hkuich.retentionIndexCalculation;

import cross.datastructures.fragments.IFileFragment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ucar.ma2.Array;
import java.util.ArrayList;
import java.util.List;
import maltcms.datastructures.ms.ProfileChromatogram1D;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.Peak1DContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import ucar.ma2.MAMath;
import ucar.ma2.MAVector;
import lombok.extern.slf4j.Slf4j;


@ActionReferences({
    @ActionReference(path = "Actions/ChromAUIProjectLogicalView/DefaultActions")})
@ActionID(
        category = "Maui",
        id = "de.mdcberlin.hkuich.retentionIndexCalculation.assistedRICalculation")
@ActionRegistration(
        displayName = "#CTL_assistedRICalculation")
@Messages("CTL_assistedRICalculation=assistedRICalculation")
@Slf4j
public final class assistedRICalculation implements ActionListener {

    private final IChromAUIProject project;

    public assistedRICalculation(IChromAUIProject project) {
        this.project = project;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        
        List<Peak1DContainer> peakContainers = new ArrayList<Peak1DContainer>();
        
        for(IChromatogramDescriptor chrom : project.getChromatograms()){
            IFileFragment parent = chrom.getChromatogram().getParent();
            ProfileChromatogram1D c = new ProfileChromatogram1D(parent);
            
            MAMath.MinMax mmTime = MAMath.getMinMax(parent.getChild("scan_acquisition_time").getArray());
            double startTime = mmTime.min;
            double stopTime = mmTime.max;
                    
            MAMath.MinMax mmMasses = MAMath.getMinMax(parent.getChild("mass_values").getArray());
            double minMass = mmMasses.min;
            double maxMass = mmMasses.max;
            
            System.out.println("startTime: " + startTime);
            System.out.println("stopTime: " + stopTime);

            System.out.println("minMass: " + minMass);
            System.out.println("minMass: " + maxMass);

            int index1 = c.getIndexFor(startTime);
            int index2 = c.getIndexFor(stopTime);
            //List<Array> intensities = c.getBinnedIntensities().subList(index1, index2/2);
            List<Array> intensities = c.getIntensities().subList(index1, index2/2);

            double cosineSimilarity; 
            double cosineSimilarity2; 

            final Array t1 = intensities.get(0);
            final Array t2 = intensities.get(15);

            cosineSimilarity = cosineSimilarity(t1,t2); 
            cosineSimilarity2 = cosineSimilarity(t1,t1); 

            System.out.println("Similarity between diff spectra: " + cosineSimilarity);
            System.out.println("Similarity between same spectra: " + cosineSimilarity2);
            System.out.println("Number of spectra extracted: " + intensities.size());
            //System.out.println("Here: " + index1 + " " + index2 + " " + "No1: " + intensities.get(0));
            //System.out.println("Here: " + index1 + " " + index2 + " " + "No2: " + intensities.get(1));
            System.out.println("Here: " + index1 + " " + index2 + " " + "No2: " + intensities.get(0).getDouble(73));
            log.info("Chromatogram {} has {} scans!",c.getParent().getName(),c.getNumberOfScans());
    }
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
