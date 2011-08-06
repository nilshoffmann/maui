/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.nodes;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author mwilhelm
 */
public class PackageNode extends AbstractNode {

    public PackageNode(Package pack, Children.Keys ch) {
        super(ch, Lookups.singleton(pack));
        if (ch instanceof MaltcmsCommandsTree) {
            setDisplayName(pack.getName());
        }
        if (ch instanceof MaltcmsCommandsPaletteTree) {
            setDisplayName(pack.getFqdn());
        }
    }
}
