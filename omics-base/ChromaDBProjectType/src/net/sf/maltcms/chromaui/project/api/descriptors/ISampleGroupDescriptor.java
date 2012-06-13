/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

/**
 *
 * @author hoffmann
 */
public interface ISampleGroupDescriptor extends IBasicDescriptor, IColorizableDescriptor {

    String getComment();

    void setComment(String comment);
}
