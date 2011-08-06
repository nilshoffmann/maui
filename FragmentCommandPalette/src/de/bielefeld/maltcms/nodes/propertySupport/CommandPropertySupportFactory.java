/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.nodes.propertySupport;

import de.bielefeld.maltcms.nodes.Command;
import de.bielefeld.maltcms.tools.MaltcmsServiceTools;
import java.beans.PropertyEditorManager;

/**
 *
 * @author mw
 */
public class CommandPropertySupportFactory {

    public static CommandPropertySupport getSupportFor(Command c, String category, String key) {
        Class ct = MaltcmsServiceTools.getType(c.getValueForCategory(category, key));
        CommandPropertySupport t = null;
        if (PropertyEditorManager.findEditor(ct) == null) {
            t = new CommandPropertySupport(c, category, key, String.class);
        } else {
            t = new CommandPropertySupport(c, category, key, ct);
        }

        return t;
    }
}
