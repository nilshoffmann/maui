/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.descriptors;

/**
 *
 * @author nilshoffmann
 */
public interface IResourceDescriptor extends IBasicDescriptor{

    String getResourceLocation();

    void setResourceLocation(String u);
    
}
