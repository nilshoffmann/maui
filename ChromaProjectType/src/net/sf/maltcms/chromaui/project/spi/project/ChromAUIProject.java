/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.spi.project;

import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.IContainer;

/**
 *
 * @author hoffmann
 */
public class ChromAUIProject implements IChromAUIProject {

    @Override
    public void addContainer(IContainer ic) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeContainer(IContainer ic) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IContainer getContainer(Class<IContainer> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
