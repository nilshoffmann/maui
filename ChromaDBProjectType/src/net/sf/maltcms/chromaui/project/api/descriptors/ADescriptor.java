/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

import com.db4o.activation.ActivationPurpose;
import com.db4o.config.annotations.Indexed;
import net.sf.maltcms.chromaui.project.spi.descriptors.ToolDescriptor;

/**
 * Base class for bits of information in the domain model.
 * Containers extend this base class, as well as all other Descriptor implementations.
 * Provides basic functionality for name, displayName, date, id, shortDescription
 * and tool. A tool is the thing that created the descriptor in the first place.
 * @author nilshoffmann
 */
public class ADescriptor extends ABasicDescriptor implements IDescriptor {

    @Indexed IToolDescriptor tool = new ToolDescriptor();
    
    @Override
    public IToolDescriptor getTool() {
        activate(ActivationPurpose.READ);
        return tool;
    }
    
    @Override
    public void setTool(IToolDescriptor tool) {
        activate(ActivationPurpose.WRITE);
        IToolDescriptor old = this.tool;
        this.tool = tool;
        firePropertyChange(PROP_TOOL, old, tool);
    }
    
}
