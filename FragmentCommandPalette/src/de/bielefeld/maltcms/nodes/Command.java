/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.nodes;

import cross.datastructures.tuple.Tuple2D;
import de.bielefeld.maltcms.tools.MaltcmsServiceTools;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.openide.util.Exceptions;

/**
 *
 * @author mwilhelm
 */
public class Command {

    public static final String PROPERTIES = "properties";
    public static final String VARIABLES = "variables";
    private String fqdn;
    private Map<String, Map<String, String>> allProperties;

    public Command(String fqdn) {
        this.fqdn = fqdn;
        this.allProperties = new HashMap<String, Map<String, String>>();

        init();
    }

    private void init() {
        Tuple2D<Map<String, String>, Map<String, String>> t = MaltcmsServiceTools.handleShowProperties(this.fqdn, this.getClass());
        this.allProperties.put(PROPERTIES, t.getFirst());
        this.allProperties.put(VARIABLES, t.getSecond());

        String v;
        for (String k : this.allProperties.get(PROPERTIES).keySet()) {
            v = this.allProperties.get(PROPERTIES).get(k);
            if (MaltcmsServiceTools.isConfigurable(v)) {
                checkCategory(v);
            }
        }
    }

    public String getName() {
        String[] s = this.fqdn.split("\\.");
        return s[s.length - 1];
    }

    public String getFqdn() {
        return fqdn;
    }

    public void setFqdn(String fqdn) {
        this.fqdn = fqdn;
    }

    public Set<String> getCategories() {
        return this.allProperties.keySet();
    }

    public Set<String> getKeySetForCategory(String category) {
        checkCategory(category);
        return this.allProperties.get(category).keySet();
    }

    public String getValueForCategory(String category, String key) {
        checkCategory(category);
        if (this.allProperties.get(category).containsKey(key)) {
            return this.allProperties.get(category).get(key);
        } else {
            return "Value not defined";
        }
    }

    public void clearPropertiesForCategory(String category) {
        if (this.allProperties.containsKey(category)) {
            this.allProperties.remove(category);
        }
    }

    public void addPropertiesForCategory(String category) {
        checkCategory(category);
    }

    public void setValueForCategory(String category, String key, String value) {
        checkCategory(category);
        if (value != null) {
            this.allProperties.get(category).put(key, value);
        } else {
            if (this.allProperties.get(category).containsKey(key)) {
                this.allProperties.get(category).remove(key);
            }
        }
    }

    public String getProvidedVariables() {
        return this.allProperties.get(VARIABLES).get(MaltcmsServiceTools.PROVIDED_VARS);
    }

    public String getRequiredVariables() {
        return this.allProperties.get(VARIABLES).get(MaltcmsServiceTools.REQUIRED_VARS);
    }

    public String getOptionalVairables() {
        return this.allProperties.get(VARIABLES).get(MaltcmsServiceTools.OPTIONAL_VARS);
    }

    @Override
    public Command clone() {
        return new Command(this.fqdn);
    }

    private void checkCategory(String category) {
        if (!this.allProperties.containsKey(category)) {
            loadCategory(category);
        }
    }

    private void loadCategory(String category) {
        try {
            Map<String, String> ret = MaltcmsServiceTools.get(category);
            this.allProperties.remove(category);
            this.allProperties.put(category, new HashMap<String, String>());
            for (String k : ret.keySet()) {
                this.allProperties.get(category).put(k, ret.get(k));
            }
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public boolean isCategory(String category) {
        return this.allProperties.containsKey(category);
    }
}
