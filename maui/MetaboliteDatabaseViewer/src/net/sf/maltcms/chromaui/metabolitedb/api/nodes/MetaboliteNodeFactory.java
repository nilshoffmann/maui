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
package net.sf.maltcms.chromaui.metabolitedb.api.nodes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import maltcms.datastructures.ms.IMetabolite;
import net.sf.maltcms.chromaui.db.api.ICrudSession;
import net.sf.maltcms.chromaui.metabolitedb.spi.nodes.MetaboliteNode;
import net.sf.maltcms.chromaui.metabolitedb.spi.nodes.MetaboliteProxy;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Nils Hoffmann
 */
public class MetaboliteNodeFactory extends ChildFactory<IMetabolite> implements PropertyChangeListener {

    private ICrudSession session;
    private BufferedImage image;
    private URL databaseLocation;

    /**
     *
     */
    public final static String ACTION_PATH = "Actions/Maui/Metabolite";

    /**
     *
     * @param databaseLocation
     * @param session
     */
    public MetaboliteNodeFactory(URL databaseLocation, ICrudSession session) {
        this.session = session;
        this.databaseLocation = databaseLocation;
        image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(new Color(255, 0, 0, 0));
        g2.fillRect(0, 0, 16, 16);
        g2.dispose();
    }

    /**
     *
     * @param toPopulate
     * @return
     */
    @Override
    protected boolean createKeys(List<IMetabolite> toPopulate) {
        for (IMetabolite metabolite : this.session.retrieve(IMetabolite.class)) {
            if (Thread.interrupted()) {
                return false;
            } else {
                toPopulate.add(metabolite);
            }
        }
        return true;
    }

    /**
     *
     * @param key
     * @return
     */
    @Override
    protected Node createNodeForKey(IMetabolite key) {
        MetaboliteProxy proxy = new MetaboliteProxy(key);
        try {
            MetaboliteNode node = new MetaboliteNode(proxy, Children.LEAF, Lookups.singleton(proxy)) {
                @Override
                public Image getIcon(int type) {
                    return image;
                }

                @Override
                public Action[] getActions(boolean context) {
                    List<Action> actionList = new LinkedList<>();
                    List<? extends Action> l = Utilities.actionsForPath(ACTION_PATH);
                    Action[] actions = super.getActions(context);
                    actionList.addAll(Arrays.asList(actions));
                    actionList.addAll(l);
                    return actionList.toArray(new Action[actionList.size()]);
                }
            };
            proxy.setOrigin(databaseLocation.toString());
            if (proxy.getName().
                    matches("^M\\d{6}.*")) {
                if (proxy.getName().contains("_ALK_")) {
                    node.setDisplayName(proxy.getName().substring(proxy.getName().lastIndexOf("ALK_") + "ALK_".length()));
                    proxy.setShortName(node.getDisplayName());
                } else if (proxy.getName().contains("_FAME_")) {
                    node.setDisplayName(proxy.getName().substring(proxy.getName().lastIndexOf("FAME_") + "FAME_".length()));
                    proxy.setShortName(node.getDisplayName());
                }
            } else {
                node.setDisplayName(proxy.getName());
            }
            WeakListeners.propertyChange(this, proxy.getPropertyChangeSupport());
            return node;
        } catch (IntrospectionException ie) {
            return Node.EMPTY;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Retrieved property change from {0}", pce.getSource().getClass());
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Updating metabolite {0}", ((MetaboliteProxy) pce.getSource()).getMetabolite());
        session.update(((MetaboliteProxy) pce.getSource()).getMetabolite());
    }
}
