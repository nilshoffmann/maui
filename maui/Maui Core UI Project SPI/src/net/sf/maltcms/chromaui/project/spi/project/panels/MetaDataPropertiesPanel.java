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
package net.sf.maltcms.chromaui.project.spi.project.panels;

import java.awt.BorderLayout;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import net.sf.maltcms.chromaui.project.api.container.MetaDataContainer;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

public class MetaDataPropertiesPanel extends javax.swing.JPanel implements ExplorerManager.Provider, Lookup.Provider, LookupListener {

    private final Lookup lookup;
    private final Category category;
    private final ExplorerManager manager;
    private JPanel selectionContentPanel;
    private Lookup.Result<MetaDataContainer> containerResult;
    private BeanTreeView view;

    public MetaDataPropertiesPanel(Category category, Lookup lkp) {
        initComponents();
        this.category = category;
        this.manager = new ExplorerManager();
        selectionContentPanel = new JPanel();
        view = new BeanTreeView();
        JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(view), selectionContentPanel);
        add(jsp, BorderLayout.CENTER);
//        add(new JPanel(), BorderLayout.CENTER);
        this.manager.setRootContext(new AbstractNode(Children.create(new MetaDataContainerFactory(lkp), true)) {

            @Override
            public String getName() {
                return "metaDataContainers";
            }

            @Override
            public String getDisplayName() {
                return "Meta Data Containers"; //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getShortDescription() {
                return "Shows a list of all meta data containers registered in the project database."; //To change body of generated methods, choose Tools | Templates.
            }
        });
        ActionMap map = getActionMap();
//        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(manager));
//        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(manager));
//        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(manager));
//        map.put("delete", ExplorerUtils.actionDelete(manager, true)); // or false
        InputMap keys = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
//        keys.put(KeyStroke.getKeyStroke("control C"), DefaultEditorKit.copyAction);
//        keys.put(KeyStroke.getKeyStroke("control X"), DefaultEditorKit.cutAction);
//        keys.put(KeyStroke.getKeyStroke("control V"), DefaultEditorKit.pasteAction);
//        keys.put(KeyStroke.getKeyStroke("DELETE"), "delete");
        lookup = ExplorerUtils.createLookup(manager, map);
        containerResult = lookup.lookupResult(MetaDataContainer.class);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public ExplorerManager getExplorerManager() {
        return this.manager;
    }

    @Override
    public Lookup getLookup() {
        return this.lookup;
    }

    // ...methods as before, but replace componentActivated and
    // componentDeactivated with e.g.:
    @Override
    public void addNotify() {
        super.addNotify();
        ExplorerUtils.activateActions(manager, true);
        containerResult.addLookupListener(this);
    }

    @Override
    public void removeNotify() {
        containerResult.removeLookupListener(this);
        ExplorerUtils.activateActions(manager, false);
        super.removeNotify();
    }

    @Override
    public void resultChanged(LookupEvent le) {
        selectionContentPanel.removeAll();
//        for (MetaDataContainer container : containerResult.allInstances()) {
//            selectionContentPanel.add(container.createEditor(), BorderLayout.CENTER);
//            break;
//        }
    }
}
