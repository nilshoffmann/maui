/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api;

import java.util.Collection;
import net.sf.maltcms.chromaui.db.api.ICrudProvider;

/**
 *
 * @author hoffmann
 */
public interface IChromAUIProject {

    void addContainer(IContainer... ic);

    void removeContainer(IContainer... ic);

    <T extends IContainer> Collection<T> getContainer(Class<T> c);

    ICrudProvider getCrudProvider();

}
