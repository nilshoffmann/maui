/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.nodes.propertySupport;

import de.bielefeld.maltcms.nodes.Command;
import de.bielefeld.maltcms.tools.MaltcmsServiceTools;
import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;

/**
 *
 * @author mwilhelm
 */
public class VariablesSupport extends PropertySupport.ReadOnly<String> {

    private Command c;
    private String var;

    public VariablesSupport(Command c, String var) {
        super(var, String.class, getDisplayName(var), var);
        this.c = c;
        this.var = var;
    }

    private static String getDisplayName(String var) {
        if (var.equals(MaltcmsServiceTools.OPTIONAL_VARS)) {
            return "Optional variables";
        }
        if (var.equals(MaltcmsServiceTools.PROVIDED_VARS)) {
            return "Provided variables";
        }
        if (var.equals(MaltcmsServiceTools.REQUIRED_VARS)) {
            return "Required variables";
        }
        return "Unkown";
    }

    @Override
    public String getValue() throws IllegalAccessException, InvocationTargetException {
        return this.c.getValueForCategory(Command.VARIABLES, this.var).replaceAll("\\,", "\n");
    }
}
