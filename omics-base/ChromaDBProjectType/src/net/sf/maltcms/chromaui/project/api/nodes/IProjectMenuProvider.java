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
package net.sf.maltcms.chromaui.project.api.nodes;

/**
 * Register implementations of this class by adding a ServiceProvider annotation
 * before the class declaration:
 * <code>@ServiceProvider(service=IProjectMenuProvider)</code>.
 *
 * The name returned will be used when the project menu is displayed. The
 * position attribute defines the order in which menus are displayed. User menus
 * should use position numbers >0.
 *
 * The action path is the system file system path under which your actions are
 * organized. Maui Project actions should be registered under
 * <code>Actions/ChromAUIProjectLogicalView/YourCustomCategory/YourCustomAction</code>
 *
 * You can register multiple actions below <code>.../YourCustomCategory/</code>.
 * These actions can in turn be context sensitive actions or provide a popup
 * menu.
 *
 * For an example for a dynamic sub menu implementation, see Maui Core Pipeline
 * Execution's RunMaltcmsPipelinesAction.
 *
 * @author Nils Hoffmann
 */
public interface IProjectMenuProvider extends IObjectNodeMenuProvider {

}
