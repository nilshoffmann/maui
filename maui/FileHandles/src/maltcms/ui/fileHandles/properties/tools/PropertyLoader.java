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
package maltcms.ui.fileHandles.properties.tools;

import cross.annotations.AnnotationInspector;
import cross.datastructures.tuple.Tuple2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Vector;
import javax.swing.table.TableModel;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openide.util.Exceptions;

/**
 *
 * @author mw
 */
public class PropertyLoader {

    public static final String REQUIRED_VARS = "Required Variables";
    public static final String OPTIONAL_VARS = "Optional Variables";
    public static final String PROVIDED_VARS = "Provided Variables";

    /**
     * @param optionValues
     */
    public static String[] getListServiceProviders(String optionValue) {
        List<String> ret = new ArrayList<String>();
        Class<?> c;
        try {
            c = Class.forName(optionValue);
            System.out.println("Loading service: " + c.getName());
            ServiceLoader<?> sl = ServiceLoader.load(c);
            for (Object o : sl) {
                if (o != null) {
                    ret.add(o.getClass().getName());
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error err) {
            err.printStackTrace();
        }

        if (ret.size() == 0) {
            return new String[]{"Can not find any Services for " + optionValue};
        } else {
            return ret.toArray(new String[]{});
        }
    }

    /**
     * Keeps only service providers, whose package name
     * starts with any of the Strings contained in names.
     * @param sps
     * @return
     */
    public static String[] filterByPackageName(String[] sps, String... names) {
        int[] matches = new int[sps.length];
        int matchcnt = 0;
        for (int i = 0; i < sps.length; i++) {
            for (int j = 0; j < names.length; j++) {
                if (sps[i].startsWith(names[j])) {
                    matches[i] = j + 1;
                    matchcnt++;
                    break;
                }
            }
        }
//        if(matchcnt==0) {
//            return sps;
//        }
        String[] ret = new String[matchcnt];
        int retIdx = 0;
        for (int i = 0; i < matches.length; i++) {
            if (matches[i] > 0) {
                String name = names[matches[i] - 1];
                ret[retIdx++] = sps[i].substring(name.length(), sps[i].length());
            }
        }
        return ret;
    }

    /**
     * @param optionValues 
     */
    public static Tuple2D<Configuration, Configuration> handleShowProperties(Object optionValues, Class loader) {
        PropertiesConfiguration ret = new PropertiesConfiguration();
        PropertiesConfiguration var = new PropertiesConfiguration();
        Class<?> c;
        String requiredVariables = "";
        String optionalVariables = "";
        String providedVariables = "";
        try {
            c = loader.getClassLoader().loadClass((String) optionValues);
            Collection<String> reqVars = AnnotationInspector.getRequiredVariables(c);
            for (String rv : reqVars) {
                requiredVariables += rv + ",";
            }
            if (requiredVariables.length() > 0) {
                requiredVariables = requiredVariables.substring(0, requiredVariables.length() - 1);
            }
            var.setProperty(REQUIRED_VARS, requiredVariables);

            Collection<String> optVars = AnnotationInspector.getOptionalRequiredVariables(c);
            for (String rv : optVars) {
                optionalVariables += rv + ",";
            }
            if (optionalVariables.length() > 0) {
                optionalVariables = optionalVariables.substring(0, optionalVariables.length() - 1);
            }
            var.setProperty(OPTIONAL_VARS, optionalVariables);

            Collection<String> provVars = AnnotationInspector.getProvidedVariables(c);
            for (String rv : provVars) {
                providedVariables += rv + ",";
            }
            if (providedVariables.length() > 0) {
                providedVariables = providedVariables.substring(0, providedVariables.length() - 1);
            }
            var.setProperty(PROVIDED_VARS, providedVariables);

            Collection<String> keys = AnnotationInspector.getRequiredConfigKeys(c);
            if (!keys.isEmpty()) {
                for (String key : keys) {
                    ret.setProperty(key, AnnotationInspector.getDefaultValueFor(c, key));
                }
            }
        } catch (ClassNotFoundException ex) {
            //Exceptions.printStackTrace(ex);
            return null;
        }
        return new Tuple2D<Configuration, Configuration>(ret, var);
    }

    public static Configuration getHash(String filename) {
        try {
//            Maltcms m = Maltcms.getInstance();
            System.out.println("Loading PIPELINE from: " + filename);
            return new PropertiesConfiguration(filename);
        } catch (ConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        }
        System.out.println("Returning empty configuration!");
        return new PropertiesConfiguration();
    }
    
    public static Map<String, Object> asHash(Configuration cfg) {
        Map<String, Object> hash = new HashMap<String, Object>();
        Iterator i = cfg.getKeys();
        String key;
        while (i.hasNext()) {
            key = (String) i.next();
            Object o = cfg.getProperty(key);
            hash.put(key, o);
        }
        return hash;
    }
    
    public static TableModel getModel(String filename, Class<?> c) {
        System.out.println("Getting model for: " + filename);
        Vector<String> header = new Vector<String>();
        header.add("Key");
        header.add("Value");
        return new HashTableModel(header, PropertyLoader.asHash(PropertyLoader.getHash(filename)), c);
    }

    public static TableModel getModel(String filename) {
        System.out.println("Getting model for: " + filename);
        Vector<String> header = new Vector<String>();
        header.add("Key");
        header.add("Value");
        return new HashTableModel(header, PropertyLoader.asHash(PropertyLoader.getHash(filename)), null);
    }

}
