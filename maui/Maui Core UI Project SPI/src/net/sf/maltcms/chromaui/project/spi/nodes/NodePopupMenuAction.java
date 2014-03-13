package net.sf.maltcms.chromaui.project.spi.nodes;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import org.openide.awt.Actions;
import org.openide.awt.DynamicMenuContent;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.util.actions.Presenter;

/**
 *
 * @author Nils Hoffmann
 */
public class NodePopupMenuAction extends AbstractAction implements Presenter.Menu, Presenter.Popup {

    private static final Logger LOG = Logger.getLogger(NodePopupMenuAction.class.getName());
    private final boolean showIcons;
    private final String mnemonic;
    private final String actionPath;

    public static NodePopupMenuAction create(String label, String mnemonic,  String actionPath) {
        return new NodePopupMenuAction(true, label, mnemonic, actionPath);
    }

    public static JMenu createMenu(String label, String mnemonic,  String actionPath) {
        NodePopupMenuAction action = new NodePopupMenuAction(true, label, mnemonic, actionPath);
        return (JMenu) action.getMenuPresenter();
    }

    /**
     * Creates a new instance of TestMenu
     */
    NodePopupMenuAction(boolean showIcons, String label, String mnemonic, String actionPath) {
        super(label);
        this.showIcons = showIcons;
        this.mnemonic = mnemonic;
        this.actionPath = actionPath;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
    }

    @Override
    public javax.swing.JMenuItem getMenuPresenter() {
        return new SubMenu();
    }

    @Override
    public javax.swing.JMenuItem getPopupPresenter() {
        return getMenuPresenter();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof NodePopupMenuAction);
    }

    @Override
    public int hashCode() {
        return 1;
    }

    private final class SubMenu extends JMenu {

        private boolean createMenuLazily = true;
        private boolean wasSeparator;
        private boolean shouldAddSeparator;

        public SubMenu() {
            super((String) NodePopupMenuAction.this.getValue(Action.NAME));
            if (showIcons) {
                setMnemonic(mnemonic.charAt(0));
            }
        }

        /**
         * Gets popup menu. Overrides superclass. Adds lazy menu items creation.
         */
        @Override
        public JPopupMenu getPopupMenu() {
            if (createMenuLazily) {
                createMenuItems();
                createMenuLazily = false;
            }
            return super.getPopupMenu();
        }

        /**
         * Creates items when actually needed.
         */
        private void createMenuItems() {
            removeAll();
            FileObject fo = FileUtil.getConfigFile(actionPath); // NOI18N
            DataFolder df = fo == null ? null : DataFolder.findFolder(fo);

            if (df != null) {
                wasSeparator = true;
                shouldAddSeparator = false;
                DataObject actionObjects[] = df.getChildren();
                for (DataObject actionObject : actionObjects) {
                    InstanceCookie ic = actionObject.getLookup().lookup(InstanceCookie.class);
                    if (ic == null) {
                        continue;
                    }
                    Object instance;
                    try {
                        instance = ic.instanceCreate();
                    } catch (IOException | ClassNotFoundException e) {
                        // ignore
                        LOG.log(Level.WARNING, actionObject.toString(), e);
                        continue;
                    }
                    if (instance instanceof Presenter.Popup) {
                        JMenuItem temp = ((Presenter.Popup) instance).getPopupPresenter();
                        if (temp instanceof DynamicMenuContent) {
                            for (JComponent presenter : ((DynamicMenuContent) temp).getMenuPresenters()) {
                                addPresenter(presenter);
                            }
                        } else {
                            addPresenter(temp);
                        }
                    } else if (instance instanceof Action) {
                        // if the action is the refactoring action, pass it information
                        // whether it is in editor, popup or main menu
                        JMenuItem mi = new JMenuItem();
                        Actions.connect(mi, (Action) instance, true);
                        addPresenter(mi);
                    } else if (instance instanceof JSeparator) {
                        addPresenter((JSeparator) instance);
                    }
                }
            }
        }

        private void addPresenter(JComponent presenter) {
            if (!showIcons && presenter instanceof AbstractButton) {
                ((AbstractButton) presenter).setIcon(null);
            }

            boolean isSeparator = presenter == null || presenter instanceof JSeparator;

            if (isSeparator) {
                if (!wasSeparator) {
                    shouldAddSeparator = true;
                    wasSeparator = true;
                }
            } else {
                if (shouldAddSeparator) {
                    addSeparator();
                    shouldAddSeparator = false;
                }
                add(presenter);
                wasSeparator = false;
            }

        }

    }
}
