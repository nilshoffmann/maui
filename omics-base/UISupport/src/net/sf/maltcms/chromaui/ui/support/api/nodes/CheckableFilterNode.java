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
package net.sf.maltcms.chromaui.ui.support.api.nodes;

import javax.swing.Action;
import org.openide.explorer.view.CheckableNode;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Nils Hoffmann
 */
public class CheckableFilterNode extends FilterNode {
    
    private boolean ignoreActions = false;
    
    public CheckableFilterNode(Node orig) {
        super(orig, FilterNode.Children.LEAF, new ProxyLookup(
                Lookups.fixed(new CheckableNodeCapability()),
                orig.getLookup()));
        CheckableNode cn = getLookup().lookup(CheckableNode.class);
        if (cn != null) {
            cn.setSelected(false);
        }
    }

    public CheckableFilterNode(Node orig, Children children) {
        super(orig, children, new ProxyLookup(
                Lookups.fixed(new CheckableNodeCapability()),
                orig.getLookup()));
        CheckableNode cn = getLookup().lookup(CheckableNode.class);
        if (cn != null) {
            cn.setSelected(false);
        }
    }

    public CheckableFilterNode(Node orig, Children children, Lookup lookup) {
        super(orig, children, new ProxyLookup(lookup,
                Lookups.fixed(new CheckableNodeCapability()),
                orig.getLookup()));
        CheckableNode cn = getLookup().lookup(CheckableNode.class);
        if (cn != null) {
            cn.setSelected(false);
        }
    }
    
    public boolean isIgnoreActions() {
        return ignoreActions;
    }

    public void setIgnoreActions(boolean ignoreActions) {
        this.ignoreActions = ignoreActions;
    }

    @Override
    public Action[] getActions(boolean context) {
        if(ignoreActions) {
            return new Action[0];
        }
        return super.getActions(context);
    }
}
