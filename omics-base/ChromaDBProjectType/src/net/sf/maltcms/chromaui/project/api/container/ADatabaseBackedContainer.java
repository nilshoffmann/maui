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
package net.sf.maltcms.chromaui.project.api.container;

import com.db4o.activation.ActivationPurpose;
import com.db4o.collections.ActivatableArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.ADescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;

/**
 *
 * @author Nils Hoffmann
 * @param <T>
 */
public abstract class ADatabaseBackedContainer<T extends IBasicDescriptor> extends ADescriptor
        implements IContainer<T> {

    private int level = 0;

    public static final String PROP_LEVEL = "level";

    /**
     * Get the value of level
     *
     * @return the value of level
     */
    @Override
    public int getLevel() {
        activate(ActivationPurpose.READ);
        return level;
    }

    /**
     * Set the value of level
     *
     * @param level new value of level
     */
    @Override
    public void setLevel(int level) {
        activate(ActivationPurpose.WRITE);
        if (level < 0) {
            throw new IllegalArgumentException("Level must be >= 0!");
        }
        int oldLevel = this.level;
        this.level = level;
        getPropertyChangeSupport().firePropertyChange(PROP_LEVEL, oldLevel, level);
    }

    private int precedence = 0;
    private List<T> members = new ActivatableArrayList<>();

    public ADatabaseBackedContainer() {
        setName(getClass().getSimpleName());
    }

    @Override
    public void setProject(IChromAUIProject project) {
        super.setProject(project);
        if (project != null) {
            for (T t : getMembers()) {
                if (t != null) {
                    t.setProject(project);
                }
            }
        }
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
        if (ic == null) {
            return 1;
        }
        if (ic instanceof IContainer) {
            if (getPrecedence() < ((IContainer) ic).getPrecedence()) {
                return -1;
            } else if (getPrecedence() > ((IContainer) ic).getPrecedence()) {
                return 1;
            }
        }
        if (getDisplayName() == null && ic.getDisplayName() == null) {
            return 0;
        } else if (getDisplayName() == null && ic.getDisplayName() != null) {
            return -1;
        } else if (getDisplayName() != null && ic.getDisplayName() == null) {
            return 1;
        } else {
            return getDisplayName().compareTo(ic.getDisplayName());
        }
    }

    @Override
    public Collection<T> getMembers() {
        activate(ActivationPurpose.READ);
        return members;
    }

    @Override
    public void setMembers(Collection<T> members) {
        activate(ActivationPurpose.WRITE);
        if (members instanceof ActivatableArrayList) {
            this.members = (ActivatableArrayList<T>) members;
        } else {
            this.members = new ActivatableArrayList<>(members);
        }
        for (T t : this.members) {
            t.setProject(getProject());
        }
        firePropertyChange(PROP_MEMBERS, null,
                this.members);
    }

    @Override
    public void setMembers(T... f) {
        activate(ActivationPurpose.WRITE);
        for (T t : f) {
            t.setProject(getProject());
        }
        this.members = new ActivatableArrayList<>(Arrays.asList(f));
        firePropertyChange(PROP_MEMBERS, null,
                this.members);
    }

    @Override
    public void addMembers(T... f) {
        activate(ActivationPurpose.WRITE);
        for (T t : f) {
            t.setProject(getProject());
        }
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
