/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api;

import java.net.URI;

/**
 *
 * @author nilshoffmann
 */
public interface IResourceDescriptor extends IDescriptor{

    String getResourceLocation();

    void setResourceLocation(String u);
    
}
