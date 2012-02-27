/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.container;

import java.awt.Image;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;

/**
 *
 * @author hoffmann
 */
public interface IContainer<T extends IBasicDescriptor> extends IGenericContainer<T> {

    final String PROP_PRECEDENCE = "precedence";
    
    int getPrecedence();

    void setPrecedence(int i);
    
    Image getIcon(int type);

}
