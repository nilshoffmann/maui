/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api;

/**
 *
 * @author hoffmann
 */
public interface IChromAUIProject {

    void addContainer(IContainer ic);

    void removeContainer(IContainer ic);

    IContainer getContainer(Class<IContainer> c);

}
