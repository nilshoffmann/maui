/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.project.spi.nodes;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.openide.awt.Actions;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.util.actions.Presenter;

/**
 *
 * @author hoffmann
 */


public class ProjectNodePopupAction extends AbstractAction implements Presenter.Popup {

	private Action[] actions = new Action[0];

	private String name;
	
	public ProjectNodePopupAction() {
		this.name = "Undefined";
	}

	public ProjectNodePopupAction(String name) {
		super(name);
		this.name = name;
	}

	public ProjectNodePopupAction(String name, Icon icon) {
		super(name, icon);
		this.name = name;
	}

	public Action[] getActions() {
		return actions;
	}

	public void setActions(Action[] actions) {
		this.actions = actions;
	}
	
	@Override
	public JMenuItem getPopupPresenter() {
		JMenu menu = new JMenu(name);
		for(Action a:actions) {
			menu.add(new JMenuItem(a));
		}
		return menu;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}
	
}
