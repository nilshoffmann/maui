/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
package de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.spi;

import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistry;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.netbeans.api.project.Project;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * A registry for open @link{TopComponent} objects, linked to arbitrary domain
 * objects associated to their originating project.
 *
 * @author Nils Hoffmann
 */
public class Registry implements IRegistry {

    private Map<Project, Map<Object, TopComponent>> typeToTopComponent = new ConcurrentHashMap<>();

    @Override
    public TopComponent openTopComponentFor(Object object,
            Class<? extends TopComponent> topComponentClass) {
        Project project = getSelectedProject();
        Map<Object, TopComponent> map = getTopComponentsForProject(project);
        TopComponent tc = map.get(object);
        if (tc == null) {
            System.out.println("Creating new TopComponent instance for class: " + topComponentClass.
                    getName() + "; active project: " + project.
                    getProjectDirectory().getName());
            try {
                tc = topComponentClass.newInstance();
                map.put(object, tc);
                tc.open();
                tc.requestActive();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(Registry.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        } else {
            tc.requestActive();
        }
        return tc;
    }

    @Override
    public List<TopComponent> closeTopComponentsFor(final Object object) {
        final List<TopComponent> closedComponents = new LinkedList<>();
        for (final Map<Object, TopComponent> map : typeToTopComponent.values()) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    TopComponent tc = map.remove(object);
                    if (tc != null) {
                        Logger.getLogger(Registry.class.getName()).info("Closing TopComponent for Object: " + object.getClass().getName());
                        tc.close();
                        closedComponents.add(tc);
                    }
                }
            };
            SwingUtilities.invokeLater(r);
        }
        return closedComponents;
    }

    /**
     * Queries
     *
     * @see Utilities.actionsGlobalContext() to retrieve selected project.
     * Returns the selected project if present in the lookup, otherwise, throws
     * an illegal state exception. This method is not intended for external use!
     *
     * @return the active project in global selection scope
     * @throws IllegalStateException if no project is selected
     */
    protected Project getSelectedProject() throws IllegalStateException {
        Project project = Utilities.actionsGlobalContext().lookup(Project.class);
        if (project == null) {
            throw new IllegalStateException(
                    "Can not open TopComponent with no project in selection!");
        }
        return project;
    }

    @Override
    public Map<Object, TopComponent> getTopComponentsForProject(Project project) {
        Map<Object, TopComponent> map = null;
        if (typeToTopComponent.containsKey(project)) {
            map = typeToTopComponent.get(project);
        } else {
            map = new ConcurrentHashMap<>();
            typeToTopComponent.put(project, map);
        }
        return map;
    }

    @Override
    public TopComponent getTopComponentFor(Object object) {
        Project project = getSelectedProject();
        Map<Object, TopComponent> map = getTopComponentsForProject(project);
        if (map.containsKey(object)) {
            Logger.getLogger(getClass().getName()).fine("Found TopComponent instance");
            return map.get(object);
        }
        return null;
    }

    @Override
    public List<TopComponent> closeTopComponentsForProject(final Project project) {
        final List<TopComponent> closedComponents = new LinkedList<>();
        final Map<Object, TopComponent> map = getTopComponentsForProject(project);
        for (final Object obj : map.keySet()) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    Logger.getLogger(Registry.class.getName()).info("Closing TopComponent for Object: " + obj.getClass().getName());
                    TopComponent tc = map.get(obj);
                    tc.close();
                    closedComponents.add(tc);
                    map.remove(obj);
                }
            };
            SwingUtilities.invokeLater(r);
        }
        return closedComponents;
    }

    @Override
    public void registerTopComponentFor(Object object, TopComponent topComponent) {
        Project project = getSelectedProject();
        Map<Object, TopComponent> map = getTopComponentsForProject(project);
        TopComponent tc = map.get(object);
        if (tc == null) {
            System.out.println("Adding TopComponent instance for class: " + topComponent.getClass().
                    getName() + "; active project: " + project.
                    getProjectDirectory().getName());
            map.put(object, topComponent);
        } else {
            System.err.println("TopComponent " + topComponent + " already registered for object: " + object);
        }
    }

    @Override
    public void unregisterTopComponentFor(Object object, TopComponent topComponent) {
        try {
            Project project = getSelectedProject();
            unregisterTopComponentFor(project, object, topComponent);
        } catch (IllegalStateException ise) {
            //no project in lookup
            for (final Map<Object, TopComponent> map : typeToTopComponent.values()) {
                map.remove(object);
            }
        }
    }

    @Override
    public void unregisterTopComponentFor(Project project, Object object, TopComponent topComponent) {
        Map<Object, TopComponent> map = getTopComponentsForProject(project);
        System.out.println("Removing TopComponent instance for class: " + topComponent.getClass().
                getName() + "; active project: " + project.
                getProjectDirectory().getName());
        map.remove(object);
    }
}
