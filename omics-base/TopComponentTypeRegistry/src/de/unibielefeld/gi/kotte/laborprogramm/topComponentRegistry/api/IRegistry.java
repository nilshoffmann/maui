/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api;

import java.util.List;
import java.util.Map;
import org.netbeans.api.project.Project;
import org.openide.windows.TopComponent;

/**
 *
 * @author hoffmann
 */
public interface IRegistry {
    public TopComponent getTopComponentFor(Object object);
    public Map<Object,TopComponent> getTopComponentsForProject(Project project);
    public TopComponent openTopComponentFor(Object object, Class<? extends TopComponent> topComponentClass);
    public List<TopComponent> closeTopComponentsFor(Object object);
    public List<TopComponent> closeTopComponentsForProject(Project project);
}
