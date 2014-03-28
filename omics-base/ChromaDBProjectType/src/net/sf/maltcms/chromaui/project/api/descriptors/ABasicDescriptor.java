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
package net.sf.maltcms.chromaui.project.api.descriptors;

import com.db4o.activation.ActivationPurpose;
import com.db4o.activation.Activator;
import com.db4o.config.annotations.Indexed;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.openide.util.HelpCtx;

/**
 *
 * @author Nils Hoffmann
 */
public class ABasicDescriptor implements IBasicDescriptor {

    private transient PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    protected PropertyChangeSupport getPropertyChangeSupport() {
        if (this.pcs == null) {
            this.pcs = new PropertyChangeSupport(this);
        }
        return this.pcs;
    }

    @Override
    public synchronized void removePropertyChangeListener(String string, PropertyChangeListener pl) {
        getPropertyChangeSupport().removePropertyChangeListener(string, pl);
    }

    @Override
    public synchronized void removePropertyChangeListener(PropertyChangeListener pl) {
        getPropertyChangeSupport().removePropertyChangeListener(pl);
    }

    @Override
    public synchronized boolean hasListeners(String string) {
        return getPropertyChangeSupport().hasListeners(string);
    }

    @Override
    public synchronized PropertyChangeListener[] getPropertyChangeListeners(String string) {
        return getPropertyChangeSupport().getPropertyChangeListeners(string);
    }

    @Override
    public synchronized PropertyChangeListener[] getPropertyChangeListeners() {
        return getPropertyChangeSupport().getPropertyChangeListeners();
    }

    @Override
    public void firePropertyChange(PropertyChangeEvent pce) {
        getPropertyChangeSupport().firePropertyChange(pce);
    }

    @Override
    public void firePropertyChange(String string, boolean bln, boolean bln1) {
        getPropertyChangeSupport().firePropertyChange(string, bln, bln1);
    }

    @Override
    public void firePropertyChange(String string, int i, int i1) {
        getPropertyChangeSupport().firePropertyChange(string, i, i1);
    }

    @Override
    public void firePropertyChange(String string, Object o, Object o1) {
        getPropertyChangeSupport().firePropertyChange(string, o, o1);
    }

    @Override
    public void fireIndexedPropertyChange(String string, int i, boolean bln, boolean bln1) {
        getPropertyChangeSupport().fireIndexedPropertyChange(string, i, bln, bln1);
    }

    @Override
    public void fireIndexedPropertyChange(String string, int i, int i1, int i2) {
        getPropertyChangeSupport().fireIndexedPropertyChange(string, i, i1, i2);
    }

    @Override
    public void fireIndexedPropertyChange(String string, int i, Object o, Object o1) {
        getPropertyChangeSupport().fireIndexedPropertyChange(string, i, o, o1);
    }

    @Override
    public synchronized void addPropertyChangeListener(String string, PropertyChangeListener pl) {
        getPropertyChangeSupport().addPropertyChangeListener(string, pl);
    }

    @Override
    public synchronized void addPropertyChangeListener(PropertyChangeListener pl) {
        getPropertyChangeSupport().addPropertyChangeListener(pl);
    }

    private transient IChromAUIProject project;

    @Override
    public IChromAUIProject getProject() {
        return project;
    }

    @Override
    public void setProject(IChromAUIProject project) {
        if (project != null) {
            IChromAUIProject old = this.project;
            this.project = project;
        }
//		if(old!=project) {
//			getPropertyChangeSupport().firePropertyChange(IDescriptor.PROP_PROJECT, old, this.project);
//		}
    }

    private transient Activator activator;

    @Override
    public void bind(Activator activator) {
        if (this.activator == activator) {
            return;
        }
        if (activator != null && null != this.activator) {
            throw new IllegalStateException(
                    "Object can only be bound to one activator");
        }
        this.activator = activator;
    }

    @Override
    public void activate(ActivationPurpose activationPurpose) {
        if (null != activator && null != activationPurpose) {
            activator.activate(activationPurpose);
        }
    }

    @Indexed
    private String name = "<NA>";

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    @Override
    public String getName() {
        activate(ActivationPurpose.READ);
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    @Override
    public void setName(String name) {
        activate(ActivationPurpose.WRITE);
        String oldName = this.name;
        this.name = name;
        getPropertyChangeSupport().firePropertyChange(PROP_NAME, oldName, name);
    }

    private String displayName = "<NA>";

    /**
     * Get the value of displayName
     *
     * @return the value of displayName
     */
    @Override
    public String getDisplayName() {
        activate(ActivationPurpose.READ);
//        if (displayName == null) {
//            return getName();
//        }
        return displayName;
    }

    /**
     * Set the value of displayName
     *
     * @param displayName new value of displayName
     */
    @Override
    public void setDisplayName(String displayName) {
        activate(ActivationPurpose.WRITE);
        String oldDisplayName = this.displayName;
        this.displayName = displayName;
        getPropertyChangeSupport().firePropertyChange(PROP_DISPLAYNAME, oldDisplayName, displayName);
    }

    @Indexed
    private Date date = new Date();

    @Override
    public Date getDate() {
        activate(ActivationPurpose.READ);
        return this.date;
    }

    @Override
    public void setDate(Date date) {
        activate(ActivationPurpose.WRITE);
        Date old = this.date;
        this.date = date;
        firePropertyChange(PROP_DATE, old, date);
    }

    @Indexed
    private final UUID id = UUID.randomUUID();

    @Override
    public UUID getId() {
        activate(ActivationPurpose.READ);
        return this.id;
    }

    @Override
    public void setId(UUID id) {
//        activate(ActivationPurpose.WRITE);
//        UUID old = this.id;
//        this.id = id;
//        firePropertyChange(PROP_ID, old, id);
        Logger.getLogger(ABasicDescriptor.class.getName()).warning("Setting of UUIDs is no longer supported! UUIDs are created automatically when objects are first instantiated!");
    }

    private String shortDescription = "";

    @Override
    public String getShortDescription() {
        activate(ActivationPurpose.READ);
        return this.name;
    }

    @Override
    public void setShortDescription(String shortDescription) {
        activate(ActivationPurpose.WRITE);
        String old = this.shortDescription;
        this.shortDescription = shortDescription;
        firePropertyChange(PROP_SHORTDESCRIPTION, old, shortDescription);
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    public int compareTo(IBasicDescriptor t) {
        return getId().compareTo(t.getId());
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(getClass());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ABasicDescriptor other = (ABasicDescriptor) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

}
