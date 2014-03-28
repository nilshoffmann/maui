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
package de.unibielefeld.gi.kotte.laborprogramm.topComponentRegistry.api;

import java.util.List;
import java.util.Map;
import org.netbeans.api.project.Project;
import org.openide.windows.TopComponent;

/**
 *
 * @author Nils Hoffmann
 */
public interface IRegistry {

    public TopComponent getTopComponentFor(Object object);

    public Map<Object, TopComponent> getTopComponentsForProject(Project project);

    public TopComponent openTopComponentFor(Object object, Class<? extends TopComponent> topComponentClass);

    public void registerTopComponentFor(Object object, TopComponent topComponent);

    public void unregisterTopComponentFor(Object object, TopComponent topComponent);

    public void unregisterTopComponentFor(Project project, Object object, TopComponent topComponent);

    public List<TopComponent> closeTopComponentsFor(Object object);

    public List<TopComponent> closeTopComponentsForProject(Project project);
}
