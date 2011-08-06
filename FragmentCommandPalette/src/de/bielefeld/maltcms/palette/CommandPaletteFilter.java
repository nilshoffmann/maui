/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.palette;

import de.bielefeld.maltcms.nodes.Package;
import de.bielefeld.maltcms.nodes.Command;
import org.netbeans.spi.palette.PaletteFilter;
import org.openide.util.Lookup;

/**
 *
 * @author mwilhelm
 */
public class CommandPaletteFilter extends PaletteFilter {

    @Override
    public boolean isValidCategory(Lookup lkp) {
        Package p = lkp.lookup(Package.class);
//        if (p.getFqdn().contains(filter)) {
        return p.getCommands().size() > 0;
//        }
//        return false;
    }

    @Override
    public boolean isValidItem(Lookup lkp) {
        Command p = lkp.lookup(Command.class);
        return p != null;
    }
}
