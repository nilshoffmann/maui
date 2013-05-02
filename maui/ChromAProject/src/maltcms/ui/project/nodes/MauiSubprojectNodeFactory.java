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
package maltcms.ui.project.nodes;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.NodeChangeEvent;
import java.util.prefs.NodeChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.DeleteOperationImplementation;
import org.netbeans.spi.project.SubprojectProvider;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author hoffmann
 */
@NodeFactory.Registration(projectType = "net-sf-maltcms-chromaui-project", position = 20)
public class MauiSubprojectNodeFactory implements NodeFactory {

	public static final String SUB_ICON = "maltcms/ui/project/resources/MaltcmsWorkflowResult.png";

	@Override
	public NodeList<?> createNodes(Project project) {
		return new MauiSubprojectNodeList((IChromAUIProject) project);
	}

	private class MauiSubprojectNodeList implements NodeList<Project>, NodeListener {

		private Set<Project> subprojects;
		private final IChromAUIProject project;
		private final ChangeSupport cs = new ChangeSupport(this);

		public MauiSubprojectNodeList(IChromAUIProject project) {
			this.project = project;
		}

		@Override
		public List<Project> keys() {
			List<Project> result = new ArrayList<Project>();
			Collection<? extends SubprojectProvider> factories = project.getLookup().lookupAll(SubprojectProvider.class);
			subprojects = new LinkedHashSet<Project>();
			for (SubprojectProvider provider : factories) {
//				addChangeListener(WeakListeners.change(this, provider));
				Set<? extends Project> s = provider.getSubprojects();
				subprojects.addAll(s);
			}
			for (Project oneReportSubProject : subprojects) {
				result.add(oneReportSubProject);
			}
			return result;
		}

		@Override
		public Node node(Project node) {
			FilterNode fn = null;
			try {
				final Node n = DataObject.find(node.
						getProjectDirectory()).getNodeDelegate();
				ProxyLookup lookup = new ProxyLookup(n.getLookup(), node.getLookup(), project.getLookup());
				fn = new NodeProxy(n, new ProxyChildren(n, lookup), lookup) {
					@Override
					public Image getIcon(int type) {
						return ImageUtilities.loadImage(SUB_ICON);
					}

					@Override
					public Image getOpenedIcon(int type) {
						return ImageUtilities.loadImage(SUB_ICON);
					}
				};
				fn.addNodeListener(this);
			} catch (DataObjectNotFoundException ex) {
				Exceptions.printStackTrace(ex);
			}
			return fn;
		}

		@Override
		public void addNotify() {
			cs.fireChange();
		}

		@Override
		public void removeNotify() {
			
		}

		@Override
		public void addChangeListener(ChangeListener cl) {
			System.out.println("Adding change listener "+cl.getClass().getName());
			cs.addChangeListener(cl);
		}

		@Override
		public void removeChangeListener(ChangeListener cl) {
			cs.removeChangeListener(cl);
		}

		@Override
		public void childrenAdded(NodeMemberEvent nme) {
			cs.fireChange();
		}

		@Override
		public void childrenRemoved(NodeMemberEvent nme) {
			cs.fireChange();
		}

		@Override
		public void childrenReordered(NodeReorderEvent nre) {
			cs.fireChange();
		}

		@Override
		public void nodeDestroyed(NodeEvent ne) {
			cs.fireChange();
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			cs.fireChange();
		}
	}

	private class ProxyChildren extends FilterNode.Children implements NodeListener {

		private final Lookup lookup;
		
		public ProxyChildren(Node owner, Lookup lookup) {
			super(owner);
			this.lookup = lookup;
		}

		@Override
		protected Node copyNode(Node original) {
			ProxyLookup pl = new ProxyLookup(original.getLookup(),lookup);
			original.addNodeListener(this);
			return new NodeProxy(original, new ProxyChildren(original, pl), pl);
		}

		@Override
		public void childrenAdded(NodeMemberEvent nme) {
			refresh();
		}

		@Override
		public void childrenRemoved(NodeMemberEvent nme) {
			refresh();
		}

		@Override
		public void childrenReordered(NodeReorderEvent nre) {
			refresh();
		}

		@Override
		public void nodeDestroyed(NodeEvent ne) {
			System.out.println("Node destroyed!");
			refresh();
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			refresh();
		}

	}

	private class NodeProxy extends FilterNode {
		// add your specialized behavior here...

		public NodeProxy(Node original, org.openide.nodes.Children children, Lookup lookup) {
			super(original, children, lookup);
		}
	}
}
