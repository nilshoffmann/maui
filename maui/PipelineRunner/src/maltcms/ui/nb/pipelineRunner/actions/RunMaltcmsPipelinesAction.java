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
package maltcms.ui.nb.pipelineRunner.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SMALL_ICON;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import maltcms.ui.fileHandles.properties.pipeline.MaltcmsPipelineFormatDataObject;
import maltcms.ui.nb.pipelineRunner.ui.PipelineRunOpenSupport;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import net.sf.maltcms.chromaui.ui.support.api.LookupUtils;
import org.apache.commons.io.FileUtils;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.awt.DynamicMenuContent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.ContextAwareAction;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.ActionPresenterProvider;
import org.openide.util.actions.Presenter;

/**
 *
 * @author Nils Hoffmann
 */
@ActionID(category = "Maui",
        id = "maltcms.ui.nb.pipelineRunner.actions.RunMaltcmsPipelinesAction")
@ActionRegistration(displayName = "#CTL_RunMaltcmsPipelinesAction", lazy = false)
@ActionReferences({
    @ActionReference(path = "Actions/ChromAUIProjectLogicalView/Pipelines")})
@NbBundle.Messages("CTL_RunMaltcmsPipelinesAction=Run Maltcms Pipeline")
public final class RunMaltcmsPipelinesAction extends AbstractAction implements ContextAwareAction, ActionListener {

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        MaltcmsPipelinesContextPopupMenuAction cpma = new MaltcmsPipelinesContextPopupMenuAction(NbBundle.getMessage(RunMaltcmsPipelinesAction.class, "CTL_RunMaltcmsPipelinesAction"), (Icon) getValue(Action.SMALL_ICON), lkp);
        cpma.setActions(buildActions(lkp));
        return cpma;
    }

    private Action[] buildActions(Lookup lookup) {
        final IChromAUIProject project = LookupUtils.ensureSingle(lookup, IChromAUIProject.class);
        Collection<Action> topLevelActions = new ArrayList<Action>();
        File projectPipelinesPath = new File(FileUtil.toFile(project.getLocation()), "pipelines");
        File[] maltcmsVersions = projectPipelinesPath.listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }
        });
        if (maltcmsVersions == null) {
            return new Action[0];
        }
        System.out.println("Found maltcms versions: " + Arrays.deepToString(maltcmsVersions));
        for (File maltcmsVersion : maltcmsVersions) {
            System.out.println("Checking pipelines below " + maltcmsVersion);
            List<File> c = new ArrayList<File>(FileUtils.listFiles(maltcmsVersion, new String[]{"mpl"}, true));
            Collections.sort(c, new Comparator<File>() {

                @Override
                public int compare(File o1, File o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            System.out.println("Found " + c.size() + " pipeline definitions!");
            Collection<Action> actions = new ArrayList<Action>();
            for (File pipelineFile : c) {
                FileObject fo = FileUtil.toFileObject(pipelineFile);
                System.out.println("Adding pipeline " + pipelineFile.getName());
                DataObject dobj;
                try {
                    dobj = DataObject.find(fo);
                    if (dobj instanceof MaltcmsPipelineFormatDataObject) {
                        final MaltcmsPipelineFormatDataObject mpfdo = (MaltcmsPipelineFormatDataObject) dobj;
                        AbstractAction pipelineRunAction = new AbstractAction(fo.getName()) {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                SwingUtilities.invokeLater(new Runnable() {

                                    @Override
                                    public void run() {
                                        System.out.println("Creating PipelineRunOpenSupport");
                                        PipelineRunOpenSupport pos = new PipelineRunOpenSupport(mpfdo.getPrimaryEntry());
                                        System.out.println("Calling pos.open()!");
                                        pos.open();
                                        System.out.println("Done!");
                                    }
                                });
                            }
                        };
                        System.out.println("Adding dataobject action");
                        actions.add(pipelineRunAction);
//						subMenu.add(new JMenuItem(pipelineRunAction));
                    }
                } catch (DataObjectNotFoundException ex) {
                    Exceptions.printStackTrace(ex);
                }

            }
            System.out.println("Adding " + actions.size() + " Pipeline specific actions!");
            topLevelActions.add(Lookup.getDefault().lookup(INodeFactory.class).createMenuItem(maltcmsVersion.getName(), actions.toArray(new Action[actions.size()])));
        }
        return topLevelActions.toArray(new Action[topLevelActions.size()]);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    private static final class MaltcmsPipelinesContextPopupMenuAction extends AbstractAction implements Presenter.Popup {

        private final Project p;
        private final String name;
        private Action[] actions = new Action[0];

        public MaltcmsPipelinesContextPopupMenuAction(String name, Icon icon, Lookup context) {
            p = context.lookup(Project.class);
            this.name = name;
//			String name = ProjectUtils.getInformation(p).getDisplayName();
//			// TODO state for which projects action should be enabled
//			char c = name.charAt(0);
//			setEnabled(c >= 'A' && c <= 'M');
//			putValue(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
            // TODO menu item label with optional mnemonics
            if (icon != null) {
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
//					System.out.println("Context aware action");
                    Action contextAwareAction = ((ContextAwareAction) action).createContextAwareInstance(context);
                    if (contextAwareAction == null) {
                        Logger.getLogger(Utilities.class.getName()).log(Level.WARNING, "ContextAwareAction.createContextAwareInstance(context) returns null. That is illegal!" + " action={0}, context={1}", new Object[]{action, context});
                    } else {
                        action = contextAwareAction;
                    }
                }

                JMenuItem item;
                if (action instanceof JMenuItem || action instanceof JMenu) {
                    item = (JMenuItem) action;
                } else if (action instanceof Presenter.Popup) {
//					System.out.println("Popup menu");
                    item = ((Presenter.Popup) action).getPopupPresenter();
                    if (item == null) {
                        Logger.getLogger(Utilities.class.getName()).log(Level.WARNING, "findContextMenuImpl, getPopupPresenter returning null for {0}", action);
                        continue;
                    }
                } else if (action instanceof DynamicMenuContent) {
//					System.out.println("Dynamic content menu");
                    DynamicMenuContent dmc = (DynamicMenuContent) action;
                    JComponent[] presenters = dmc.getMenuPresenters();
                    String name = action.getValue("name").toString();
                    item = new JMenuItem(name);
                    for (JComponent jc : presenters) {
                        item.add(jc);
                    }
                } else {
//					System.out.println("Plain menu action");
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
