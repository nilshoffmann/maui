/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

/**
 *
 * @author nilshoffmann
 */
public interface IDescriptor extends IBasicDescriptor {
    final String PROP_TOOL = "tool";
    public IToolDescriptor getTool();
    public void setTool(IToolDescriptor toolDescriptor);
    
}
