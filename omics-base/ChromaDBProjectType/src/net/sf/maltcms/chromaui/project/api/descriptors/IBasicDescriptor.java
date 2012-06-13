/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

import com.db4o.ta.Activatable;
import java.util.Date;
import java.util.UUID;
import org.openide.util.HelpCtx;

/**
 *
 * @author nilshoffmann
 */
public interface IBasicDescriptor extends Activatable, Comparable<IBasicDescriptor>, HelpCtx.Provider, IPropertyChangeSupport {

    final String PROP_DISPLAYNAME = "displayName";
    final String PROP_NAME = "name";
    final String PROP_DATE = "date";
    final String PROP_ID = "id";
    final String PROP_SHORTDESCRIPTION = "shortDescription";

    public String getDisplayName();

    public void setDisplayName(String displayName);

    public String getName();

    public void setName(String name);

    public Date getDate();

    public void setDate(Date date);

    public UUID getId();

    public void setId(UUID id);

    public String getShortDescription();

    public void setShortDescription(String shortDescription);
    
}
