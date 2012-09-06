/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.spi;

import de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api.IRegistry;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.openide.windows.TopComponent;
import org.openide.util.Utilities;
/**
 * A registry for open
 *
 * @see TopComponent objects, linked to arbitrary domain objects associated to
 * their originating project.
 * @author hoffmann
 */
public class Registry implements IRegistry {

    private Map<Project, Map<Object, TopComponent>> typeToTopComponent = new ConcurrentHashMap<Project, Map<Object, TopComponent>>();

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
            } catch (InstantiationException ex) {
                Logger.getLogger(Registry.class.getName()).log(Level.SEVERE,
                        null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Registry.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        } else {
            tc.requestActive();
        }
        return tc;
    }

    @Override
    public List<TopComponent> closeTopComponentsFor(Object object) {
        List<TopComponent> closedComponents = new LinkedList<TopComponent>();
        for (Map<Object, TopComponent> map : typeToTopComponent.values()) {
            TopComponent tc = map.remove(object);
            if (tc != null) {
                Logger.getLogger(getClass().getName()).info("Closing TopComponent for Object: " + object.getClass().getName());
                tc.close();
                closedComponents.add(tc);
            }
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
            map = new ConcurrentHashMap<Object, TopComponent>();
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
        List<TopComponent> closedComponents = new LinkedList<TopComponent>();
        Map<Object, TopComponent> map = getTopComponentsForProject(project);
        for (Object obj : map.keySet()) {
            TopComponent tc = map.get(obj);
            tc.close();
            closedComponents.add(tc);
        }
        return closedComponents;
    }
}
