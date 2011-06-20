/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api;

/**
 *
 * @author hoffmann
 */
public interface IContainer<T extends IDescriptor> extends Comparable, IGenericContainer<T> {

    String getName();

    void setName(String s);

    int getPrecedence();

    void setPrecedence(int i);
    
    String getDisplayName();
    
    void setDisplayName(String s);

}
