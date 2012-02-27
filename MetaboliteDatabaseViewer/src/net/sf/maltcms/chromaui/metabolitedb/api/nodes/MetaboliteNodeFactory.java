/*
 * $license$
 *
 * $Id$
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
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class MetaboliteNodeFactory extends ChildFactory<IMetabolite> implements PropertyChangeListener {

    private ICrudSession session;
    private Collection<IMetabolite> metabolites;
    private BufferedImage image;
    private URL databaseLocation;
    public final static String ACTION_PATH = "Actions/Maui/Metabolite";

    public MetaboliteNodeFactory(URL databaseLocation, ICrudSession session) {
        this.session = session;
        this.databaseLocation = databaseLocation;
        metabolites = this.session.retrieve(IMetabolite.class);
        image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(new Color(255, 0, 0, 0));
        g2.fillRect(0, 0, 16, 16);
        g2.dispose();
    }

    @Override
    protected boolean createKeys(List<IMetabolite> toPopulate) {
        for (IMetabolite metabolite : metabolites) {
            if (Thread.interrupted()) {
                return false;
            } else {
                toPopulate.add(metabolite);
            }
        }
        return true;
    }

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
                    List<Action> actionList = new LinkedList<Action>();
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
                node.setDisplayName(proxy.getName().substring(proxy.getName().lastIndexOf("_") + 1));
                proxy.setShortName(node.getDisplayName());
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
        System.out.println("Retrieved property change from " + pce.getSource().getClass());
        System.out.println("Updating metabolite " + ((MetaboliteProxy) pce.getSource()).getMetabolite());
        session.update((IMetabolite) ((MetaboliteProxy) pce.getSource()).getMetabolite());
    }
}
