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

import com.db4o.ta.Activatable;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.openide.util.HelpCtx;

/**
 *
 * @author Nils Hoffmann
 */
public interface IBasicDescriptor extends Activatable, Comparable<IBasicDescriptor>, HelpCtx.Provider, IPropertyChangeSupport, Serializable {

    /**
     *
     */
    final String PROP_DISPLAYNAME = "displayName";

    /**
     *
     */
    final String PROP_NAME = "name";

    /**
     *
     */
    final String PROP_DATE = "date";

    /**
     *
     */
    final String PROP_ID = "id";

    /**
     *
     */
    final String PROP_SHORTDESCRIPTION = "shortDescription";

    /**
     *
     */
    final String PROP_PROJECT = "project";

    /**
     *
     * @return
     */
    public IChromAUIProject getProject();

    /**
     *
     * @param project
     */
    public void setProject(IChromAUIProject project);

    /**
     *
     * @return
     */
    public String getDisplayName();

    /**
     *
     * @param displayName
     */
    public void setDisplayName(String displayName);

    /**
     *
     * @return
     */
    public String getName();

    /**
     *
     * @param name
     */
    public void setName(String name);

    /**
     *
     * @return
     */
    public Date getDate();

    /**
     *
     * @param date
     */
    public void setDate(Date date);

    /**
     *
     * @return
     */
    public UUID getId();

    /**
     *
     * @param id
     * @deprecated
     */
    @Deprecated
    public void setId(UUID id);

    /**
     *
     * @return
     */
    public String getShortDescription();

    /**
     *
     * @param shortDescription
     */
    public void setShortDescription(String shortDescription);

}
