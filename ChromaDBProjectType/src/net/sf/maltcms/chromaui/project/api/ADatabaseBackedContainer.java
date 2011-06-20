/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 *
 * @author hoffmann
 */
public class ADatabaseBackedContainer<T extends IDescriptor> implements IContainer<T> {

    private String name = getClass().getSimpleName(), displayName;
    private int precedence = 0;
    private LinkedHashSet<T> members = new LinkedHashSet<T>();
    private IChromAUIProject parentProject;

    public IChromAUIProject getParentProject() {
        return parentProject;
    }

    public void setParentProject(IChromAUIProject parentProject) {
        this.parentProject = parentProject;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String s) {
        this.name = s;
    }

    @Override
    public int getPrecedence() {
        return this.precedence;
    }

    @Override
    public void setPrecedence(int i) {
        this.precedence = i;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof IContainer) {
            IContainer ic = ((IContainer) o);
            if (getPrecedence() < ic.getPrecedence()) {
                return -1;
            } else if (getPrecedence() > ic.getPrecedence()) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Collection<T> get() {
        return members;
    }

    @Override
    public void set(T... f) {
        this.members.clear();
        add(f);
    }

    @Override
    public void add(T... f) {
        this.members.addAll(Arrays.asList(f));
    }

    @Override
    public void remove(T... f) {
        this.members.removeAll(Arrays.asList(f));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName() + ": " + getName() + "\n");
        //for (T igd : this.members) {
            //sb.append(igd.toString() + "\n");
//            sb.append(igd.getType()+"\n");
//            sb.append(igd.getLocation()+"\n");
        //}
        return sb.toString();
    }

    @Override
    public String getDisplayName() {
        if (this.displayName == null) {
            return getName();
        }
        return this.displayName;
    }

    @Override
    public void setDisplayName(String s) {
        this.displayName = s;
    }

}
