/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.tools;

import de.bielefeld.maltcms.nodes.Package;
import de.bielefeld.maltcms.nodes.Command;
import net.sf.maltcms.apps.Maltcms;
import cross.annotations.AnnotationInspector;
import cross.datastructures.tuple.Tuple2D;
import de.bielefeld.maltcms.nodes.propertySupport.editor.PropertyEditorServiceAArrayFilter;
import de.bielefeld.maltcms.nodes.propertySupport.editor.PropertyEditorServiceIArrayDoubleComp;
import java.beans.PropertyEditorManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import maltcms.commands.distances.IArrayDoubleComp;
import maltcms.commands.filters.array.AArrayFilter;

/**
 *
 * @author mwilhelm
 */
public class MaltcmsServiceTools {

    static {
        PropertyEditorManager.registerEditor(IArrayDoubleComp.class, PropertyEditorServiceIArrayDoubleComp.class);
        PropertyEditorManager.registerEditor(AArrayFilter.class, PropertyEditorServiceAArrayFilter.class);
    }

    public static final String REQUIRED_VARS = "Required Variables";
    public static final String OPTIONAL_VARS = "Optional Variables";
    public static final String PROVIDED_VARS = "Provided Variables";
    private static List<Package> roots;

    public static String[] getListServiceProviders(String optionValue) {

        Maltcms.getInstance();

        final List<String> ret = new ArrayList<String>();
        final Class<?> c;
        try {
            c = Class.forName(optionValue);
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

        if (ret.isEmpty()) {
            return new String[]{"Can not find any Servies for " + optionValue};
        } else {
            return ret.toArray(new String[]{});
        }
    }

    public static List<Package> getRootPackages() {
        if (roots == null) {
            List<Package> list = new ArrayList<Package>();

            Map<String, List<String>> maltcmsCommandTree = new HashMap<String, List<String>>();
            String[] stree = getListServiceProviders("cross.commands.fragments.AFragmentCommand");

            for (String s : stree) {
                String[] splitted = s.split("\\.");
                String ss = splitted[0];
                String sss = "";
                for (int i = 0; i < splitted.length - 1; i++) {
                    sss = ss + "." + splitted[i + 1];
                    if (maltcmsCommandTree.containsKey(ss)) {
                        if (!maltcmsCommandTree.get(ss).contains(sss)) {
                            maltcmsCommandTree.get(ss).add(sss);
                        }
                    } else {
                        List<String> l = new ArrayList<String>();
                        l.add(sss);
                        maltcmsCommandTree.put(ss, l);
                    }
                    ss = sss;
                }
            }

            for (String rootStrings : getRootString()) {
                Package p = new Package(rootStrings);
                add(p, maltcmsCommandTree);
                list.add(p);
            }

            roots = list;
        }
        return roots;
    }

    public static List<Package> getPackagesWithCommands() {
        if (roots == null) {
            getRootPackages();
        }
        List<Package> list = new ArrayList<Package>();

        for (Package p : roots) {
            addPackagesWithCommands(list, p);
        }

        return list;
    }

    private static void addPackagesWithCommands(List<Package> list, Package p) {
        if (p.getCommands().size() > 0) {
            list.add(p);
        }
        for (Package p1 : p.getSubPackages()) {
            addPackagesWithCommands(list, p1);
        }
    }

    public static void printPackage(Package p) {
        printPackage(p, "");
    }

    private static void printPackage(Package p, String t) {
        System.out.println(t + p.getFqdn());
        for (Package p1 : p.getSubPackages()) {
            printPackage(p1, t + "\t");
        }
        for (Command c : p.getCommands()) {
            System.out.println(t + c.getName());
        }
    }

    private static void add(Package p, Map<String, List<String>> tree) {
        for (String s : tree.get(p.getFqdn())) {
            if (tree.containsKey(s)) {
                Package p1 = new Package(s);
                p.addPackage(p1);
                add(p1, tree);
            } else {
                p.addCommand(new Command(s));
            }
        }
    }

    private static List<String> getRootString() {
        List<String> root = new ArrayList<String>();
        String[] stree = getListServiceProviders("cross.commands.fragments.AFragmentCommand");

        for (String s : stree) {
            String[] splitted = s.split("\\.");
            if (!root.contains(splitted[0])) {
                root.add(splitted[0]);
            }
        }

        return root;
    }

    /**
     * @param optionValues
     */
    public static Tuple2D<Map<String, String>, Map<String, String>> handleShowProperties(String optionValues, Class loader) {
        Map<String, String> ret = new HashMap<String, String>();
        Map<String, String> var = new HashMap<String, String>();
        Class<?> c;
        String requiredVariables = "";
        String optionalVariables = "";
        String providedVariables = "";
        try {
            c = loader.getClassLoader().loadClass(optionValues);
            Collection<String> reqVars = AnnotationInspector.getRequiredVariables(c);
            for (String rv : reqVars) {
                requiredVariables += rv + ",";
            }
            if (requiredVariables.length() > 0) {
                requiredVariables = requiredVariables.substring(0, requiredVariables.length() - 1);
            }
            var.put(REQUIRED_VARS, requiredVariables);

            Collection<String> optVars = AnnotationInspector.getOptionalRequiredVariables(c);
            for (String rv : optVars) {
                optionalVariables += rv + ",";
            }
            if (optionalVariables.length() > 0) {
                optionalVariables = optionalVariables.substring(0, optionalVariables.length() - 1);
            }
            var.put(OPTIONAL_VARS, optionalVariables);

            Collection<String> provVars = AnnotationInspector.getProvidedVariables(c);
            for (String rv : provVars) {
                providedVariables += rv + ",";
            }
            if (providedVariables.length() > 0) {
                providedVariables = providedVariables.substring(0, providedVariables.length() - 1);
            }
            var.put(PROVIDED_VARS, providedVariables);

            Collection<String> keys = AnnotationInspector.getRequiredConfigKeys(c);
            if (!keys.isEmpty()) {
                for (String key : keys) {
                    ret.put(key, AnnotationInspector.getDefaultValueFor(c, key));
                }
            }
        } catch (ClassNotFoundException ex) {
            //Exceptions.printStackTrace(ex);
            return null;
        }
        return new Tuple2D<Map<String, String>, Map<String, String>>(ret, var);
    }

    public static Class getType(String value) {
        try {
            final Class c = Class.forName(value);
            final Class[] ics = c.getInterfaces();

            for (Class ic : ics) {
                if (PropertyEditorManager.findEditor(ic) != null) {
                    return ic;
                }
            }
            final Class sc = c.getSuperclass();
            if (PropertyEditorManager.findEditor(sc) != null) {
                return sc;
            }
            return getType(sc.getName());
        } catch (ClassNotFoundException ex) {
//            ex.printStackTrace();
        }
        try {
            Integer.parseInt(value);
            return Integer.class;
        } catch (NumberFormatException e) {
//            e.printStackTrace();
        }

        try {
            Double.parseDouble(value);
            return Double.class;
        } catch (NumberFormatException e) {
//            e.printStackTrace();
        }


        Boolean b = Boolean.parseBoolean(value);
        if (b.toString().equalsIgnoreCase(value)) {
            return Boolean.class;
        }

        return String.class;
    }

    public static boolean isConfigurable(String sc) {
        Class c = null;
        try {
            c = Class.forName(sc);
            return AnnotationInspector.getRequiredConfigKeys(c).size() > 0;
        } catch (ClassNotFoundException ex) {
//            Exceptions.printStackTrace(ex);
        }
        return false;
    }

    public static Map<String, String> get(String sc) throws ClassNotFoundException {
        Class c = Class.forName(sc);
        Map<String, String> ret = new HashMap<String, String>();
        Collection<String> keys = AnnotationInspector.getRequiredConfigKeys(c);
        if (!keys.isEmpty()) {
            for (String key : keys) {
                ret.put(key, AnnotationInspector.getDefaultValueFor(c, key));
            }
        }

        return ret;
    }

    public static String getLast(String p) {
        String[] pp = p.split("\\.");
        return pp[pp.length - 1];
    }
}
