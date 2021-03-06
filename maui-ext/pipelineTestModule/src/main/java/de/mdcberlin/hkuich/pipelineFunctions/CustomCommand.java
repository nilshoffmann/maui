/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mdcberlin.hkuich.pipelineFunctions;

import cross.annotations.AnnotationInspector;
import cross.annotations.Configurable;
import cross.commands.fragments.AFragmentCommand;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.tuple.TupleND;
import cross.datastructures.workflow.WorkflowSlot;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import maltcms.datastructures.ms.ProfileChromatogram1D;
import org.openide.util.lookup.ServiceProvider;
import ucar.ma2.Array;
import ucar.ma2.MAMath;
import ucar.ma2.MAMath.MinMax;
import ucar.ma2.MAVector;
/**
 * Custom Command to show how to create a command that is available in cross/maltcms.
 * @author Nils Hoffmann
 */
@Slf4j
@Data
@ServiceProvider(service = AFragmentCommand.class)
public class CustomCommand extends AFragmentCommand {

	@Configurable(description = "This is a custom parameter. Set the value between ... and ...", value="")
	private String myCustomParameter;
	@Configurable(description = "This is another parameter.", value = "1")
	private int myCustomNumericParameter = 1;
	private final WorkflowSlot workflowSlot = WorkflowSlot.GENERAL_PREPROCESSING;

	@Override
	public String getDescription() {
            System.out.println("Hello constructor");
		return buildDescription();                
	}

	public TupleND<IFileFragment> apply(TupleND<IFileFragment> in) {
		//print some info about the class
                System.out.println("Hello TupleSCSC");
                
                initProgress(in.size());
                
		log.info("{}",getDescription());
		for (IFileFragment f : in) {
                    getProgress().nextStep();
                    //wrap the FileFragment in a chromatogram
                    ProfileChromatogram1D c = new ProfileChromatogram1D(f);
                    
                    IFileFragment parent = c.getParent();
                    MinMax mmTime = MAMath.getMinMax(parent.getChild("scan_acquisition_time").getArray());
                    double startTime = mmTime.min;
                    double stopTime = mmTime.max;
                    
                    MinMax mmMasses = MAMath.getMinMax(parent.getChild("mass_values").getArray());
                    double minMass = mmMasses.min;
                    double maxMass = mmMasses.max;
                    
                    System.out.println("startTime: " + startTime);
                    System.out.println("stopTime: " + stopTime);
                    
                    System.out.println("minMass: " + minMass);
                    System.out.println("minMass: " + maxMass);
                    
                    
                    int index1 = c.getIndexFor(startTime);
                    int index2 = c.getIndexFor(stopTime);
                    List<Array> intensities = c.getBinnedIntensities().subList(index1, index2/2);
                    
                    double cosineSimilarity; 
                    double cosineSimilarity2; 
                                        
                    final Array t1 = intensities.get(0);
                    final Array t2 = intensities.get(1);
                    
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
                
                
		return in;
	}
        
        
        
        private double cosineSimilarity(final Array t1, final Array t2) {
            if ((t1.getRank() == 1) && (t2.getRank() == 1)) {
                final MAVector ma1 = new MAVector(t1);
                final MAVector ma2 = new MAVector(t2);
                return ma1.cos(ma2);
            }
            throw new IllegalArgumentException("Arrays shapes are incompatible! " + t1.getShape()[0] + " != " + t2.getShape()[0]);
        }
        
        
        

	protected String buildDescription() {
		//build a concise description of the class
		StringBuilder sb = new StringBuilder();
		sb.append("Description of class ").append(CustomCommand.class.getSimpleName()).append("\n");
		sb.append("Package: ").append(CustomCommand.class.getPackage().toString()).append("\n");
		for (String key : AnnotationInspector.getRequiredConfigKeys(CustomCommand.class)) {
			String description = AnnotationInspector.getDescriptionFor(CustomCommand.class, key);
			String fieldName = key.substring(key.lastIndexOf(".")+1);
			sb.append(fieldName).append("= ").append(description).append("\n");
			sb.append("Default value: ").append(AnnotationInspector.getDefaultValueFor(CustomCommand.class, key)).append("\n");
		}
                System.out.println("Hello buildDescription");
		return sb.toString();
	}
}
