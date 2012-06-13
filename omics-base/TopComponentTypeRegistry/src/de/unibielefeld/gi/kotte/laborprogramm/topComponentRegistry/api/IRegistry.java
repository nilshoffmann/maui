/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api;

import java.util.Map;
import org.netbeans.api.project.Project;
import org.openide.windows.TopComponent;

/**
 *
 * @author hoffmann
 */
public interface IRegistry {
    public TopComponent getTopComponentFor(Object object);
    public Map<Object,TopComponent> getTopComponentsFor(Project project);
    public void openTopComponent(Object object, Class<? extends TopComponent> topComponentClass);
    public void closeTopComponent(Object object);
    public void closeTopComponentsFor(Project project);
}
