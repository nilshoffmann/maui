/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.container;

import java.awt.Image;
import net.sf.maltcms.chromaui.project.api.descriptors.IDescriptor;

/**
 *
 * @author hoffmann
 */
public interface IContainer<T extends IDescriptor> extends Comparable, IGenericContainer<T> {

    String getName();

    void setName(String s);

    int getPrecedence();

    void setPrecedence(int i);
    
    @Override
    String getDisplayName();
    
    @Override
    void setDisplayName(String s);
    
    Image getIcon(int type);

}
