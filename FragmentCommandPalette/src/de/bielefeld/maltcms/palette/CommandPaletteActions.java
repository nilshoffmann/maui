/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.palette;

import javax.swing.Action;
import org.netbeans.spi.palette.PaletteActions;
import org.openide.util.Lookup;

/**
 *
 * @author mwilhelm
 */
public class CommandPaletteActions extends PaletteActions {

    @Override
    public Action[] getImportActions() {
        return null;
    }

    @Override
    public Action[] getCustomPaletteActions() {
        return null;
    }

    @Override
    public Action[] getCustomCategoryActions(Lookup lookup) {
        return null;
    }

    @Override
    public Action[] getCustomItemActions(Lookup lookup) {
        return null;
    }

    @Override
    public Action getPreferredAction(Lookup lookup) {
        return null;
    }
}
