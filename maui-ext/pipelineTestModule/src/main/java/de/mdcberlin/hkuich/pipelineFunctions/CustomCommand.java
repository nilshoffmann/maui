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
                    
                    int index1 = 1; //c.getIndexFor(2.0);
                    int index2 = 2; //c.getIndexFor(100.0);
                    List<Array> intensities = c.getBinnedIntensities().subList(index1, index2);
                    
                    System.out.println("Here: " + index1 + " " + index2 + " " + intensities);
                    log.info("Chromatogram {} has {} scans!",c.getParent().getName(),c.getNumberOfScans());
		}
		return in;
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