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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import org.netbeans.api.project.Project;
import org.openide.util.ContextAwareAction;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.actions.ActionPresenterProvider;
import org.openide.util.actions.Presenter;

/**
 *
 * @author hoffmann
 */
public class NodePopupAction extends AbstractAction implements ContextAwareAction, ActionListener {

	private String name;
	private Icon icon;
	private Action[] actions;

	public NodePopupAction() {
		this.name = "Undefined";
	}

	public NodePopupAction(String name) {
		this.name = name;
	}

	public NodePopupAction(String name, Icon icon) {
		this.name = name;
		this.icon = icon;
	}
	
	public void setActions(Action[] actions) {
		this.actions = actions;
	}

	@Override
	public Action createContextAwareInstance(Lookup lkp) {
		ContextPopupMenuAction cpma = new ContextPopupMenuAction(name, icon, lkp);
		cpma.setActions(actions);
		return cpma;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	private static final class ContextPopupMenuAction extends AbstractAction implements Presenter.Popup {

		private final Project p;
		private final String name;
		private Action[] actions = new Action[0];

		public ContextPopupMenuAction(String name, Icon icon, Lookup context) {
			p = context.lookup(Project.class);
			this.name = name;
//			String name = ProjectUtils.getInformation(p).getDisplayName();
//			// TODO state for which projects action should be enabled
//			char c = name.charAt(0);
//			setEnabled(c >= 'A' && c <= 'M');
//			putValue(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
			// TODO menu item label with optional mnemonics
			if(icon!=null) {
				putValue(SMALL_ICON, icon);
			}
			putValue(NAME, name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
//			// TODO what to do when run
//			String msg = "Project location: "
//					+ FileUtil.getFileDisplayName(p.getProjectDirectory());
//			DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(msg));
		}

		public Action[] getActions() {
			return actions;
		}

		public void setActions(Action[] actions) {
			this.actions = actions;
		}

//		@Override
//		public boolean isEnabled() {
//			return null != context.lookup(Project.class);
//		}

		@Override
		public JMenuItem getPopupPresenter() {
			return actionsToMenu(name, actions, Utilities.actionsGlobalContext());
		}
	}

	protected static JMenu actionsToMenu(String menuName, Action[] actions, Lookup context) {
		//code from Utilities.actionsToPopup
		// keeps actions for which was menu item created already (do not add them twice)
		Set<Action> counted = new HashSet<Action>();
		// components to be added (separators are null)
		List<Component> components = new ArrayList<Component>();

		for (Action action : actions) {
			if (action != null && counted.add(action)) {
				// switch to replacement action if there is some
				if (action instanceof ContextAwareAction) {
					Action contextAwareAction = ((ContextAwareAction) action).createContextAwareInstance(context);
					if (contextAwareAction == null) {
						Logger.getLogger(Utilities.class.getName()).log(Level.WARNING, "ContextAwareAction.createContextAwareInstance(context) returns null. That is illegal!" + " action={0}, context={1}", new Object[]{action, context});
					} else {
						action = contextAwareAction;
					}
				}

				JMenuItem item;
				if (action instanceof Presenter.Popup) {
					item = ((Presenter.Popup) action).getPopupPresenter();
					if (item == null) {
						Logger.getLogger(Utilities.class.getName()).log(Level.WARNING, "findContextMenuImpl, getPopupPresenter returning null for {0}", action);
						continue;
					}
				} else {
					// We need to correctly handle mnemonics with '&' etc.
					item = ActionPresenterProvider.getDefault().createPopupPresenter(action);
				}

				for (Component c : ActionPresenterProvider.getDefault().convertComponents(item)) {
					if (c instanceof JSeparator) {
						components.add(null);
					} else {
						components.add(c);
					}
				}
			} else {
				components.add(null);
			}
		}

		// Now create actual menu. Strip adjacent, leading, and trailing separators.
		JMenu menu = new JMenu(menuName);
		boolean nonempty = false; // has anything been added yet?
		boolean pendingSep = false; // should there be a separator before any following item?
		for (Component c : components) {
			try {
				if (c == null) {
					pendingSep = nonempty;
				} else {
					nonempty = true;
					if (pendingSep) {
						pendingSep = false;
						menu.addSeparator();
					}
					menu.add(c);
				}
			} catch (RuntimeException ex) {
				Exceptions.attachMessage(ex, "Current component: " + c); // NOI18N
				Exceptions.attachMessage(ex, "List of components: " + components); // NOI18N
				Exceptions.attachMessage(ex, "List of actions: " + Arrays.asList(actions)); // NOI18N
				Exceptions.printStackTrace(ex);
			}
		}
		return menu;
	}
}