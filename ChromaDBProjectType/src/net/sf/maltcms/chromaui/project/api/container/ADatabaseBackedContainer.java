/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.container;

import com.db4o.activation.ActivationPurpose;
import com.db4o.collections.ActivatableArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.descriptors.ADescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;

/**
 *
 * @author hoffmann
 */
public abstract class ADatabaseBackedContainer<T extends IBasicDescriptor> extends ADescriptor
        implements IContainer<T> {

    private int precedence = 0;
    private List<T> members = new ActivatableArrayList<T>();

    public ADatabaseBackedContainer() {
        setName(getClass().getSimpleName());
    }

    @Override
    public int getPrecedence() {
        activate(ActivationPurpose.READ);
        return this.precedence;
    }

    @Override
    public void setPrecedence(int i) {
        activate(ActivationPurpose.WRITE);
        this.precedence = i;
        int oldPrecedence = this.precedence;
        firePropertyChange(PROP_PRECEDENCE,
                oldPrecedence, i);
    }

    @Override
    public int compareTo(IBasicDescriptor ic) {
        if (ic instanceof IContainer) {
            if (getPrecedence() < ((IContainer)ic).getPrecedence()) {
                return -1;
            } else if (getPrecedence() > ((IContainer)ic).getPrecedence()) {
                return 1;
            }
        }
        return getDisplayName().compareTo(ic.getDisplayName());
    }

    @Override
    public Collection<T> getMembers() {
        activate(ActivationPurpose.READ);
        return members;
    }

    @Override
    public void setMembers(T... f) {
        activate(ActivationPurpose.WRITE);
        this.members = new ActivatableArrayList<T>(Arrays.asList(f));
        firePropertyChange(PROP_MEMBERS, null,
                this.members);
    }

    @Override
    public void addMembers(T... f) {
        activate(ActivationPurpose.WRITE);
        this.members.addAll(Arrays.asList(f));
        firePropertyChange(PROP_MEMBERS, null,
                this.members);
    }

    @Override
    public void removeMembers(T... f) {
        activate(ActivationPurpose.WRITE);
        this.members.removeAll(Arrays.asList(f));
        firePropertyChange(PROP_MEMBERS, null,
                this.members);
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

}
