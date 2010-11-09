/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.tools;

import cross.annotations.AnnotationInspector;
import cross.datastructures.tuple.Tuple2D;
import cross.tools.StringTools;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Vector;
import javax.swing.table.TableModel;
import maltcms.ui.fileHandles.properties.graph.PipelineElementWidget;
import maltcms.ui.fileHandles.properties.graph.PipelineGraphScene;
import maltcms.ui.fileHandles.properties.wizards.HashTableModel;
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

    public static HashTableModel getModel(Configuration properties, Class<?> c) {

        Vector<String> header = new Vector<String>();
        header.add("Key");
        header.add("Value");
        System.out.println("properties: " + properties);

        return new HashTableModel(header, asHash(properties), c);
    }

    public static TableModel getModel(String filename, Class<?> c) {
        System.out.println("Getting model for: " + filename);
        return PropertyLoader.getModel(PropertyLoader.getHash(filename), c);
    }

    public static TableModel getModel(String filename) {
        System.out.println("Getting model for: " + filename);
        return PropertyLoader.getModel(PropertyLoader.getHash(filename), null);
    }

    public static void parseIntoScene(String filename, PipelineGraphScene scene) {
        parseIntoScene(filename, getHash(filename), scene);
    }

    /**
     * Supports both absolute and relative paths and also arbitrary combinations of the two.
     * In case of relative paths, the location of the file containing the pipeline
     * configuration is used as basedir to resolve the relative path.
     *
     * Example for relative path:
     * <pre>pipelines.properties = fragmentCommands/myClassName.properties</pre>
     * Example for absolute path:
     * <pre>pipelines.properties = /home/juser/myFunkyDir/myClassName.properties</pre>
     *
     * <pre>pipeline.properties</pre> accepts multiple entries, separated by a ',' (comma) character.
     * Example:
     * <pre>pipeline.properties = fragmentCommands/myClassName.properties,/home/juser/myFunkyDir/myClassName.properties</pre>
     *
     * @param filename the filename of the base configuration, which contains the pipeline= and pipeline.properties keys.
     * @param cfg the configuration object resembling the content of filename.
     * @param scene the graph scene into which to load the configuration.
     */
    public static void parseIntoScene(String filename, Configuration cfg, PipelineGraphScene scene) {
        System.out.println("###################################################################");
        System.out.println("Creating graph scene from file");
        File f = new File(filename);
        //Get pipeline from configuration
        List<String> pipeline = StringTools.toStringList(cfg.getList("pipeline", Collections.emptyList()));
        String[] pipes = pipeline.toArray(new String[]{});
        System.out.println("Pipeline elements: "+Arrays.toString(pipes));
        if (pipes.length == 0 || pipes[0] == null || pipes[0].isEmpty()) {
            return;
        }
        System.out.println("Loading " + pipes.length + " pipeline elements!");
        //pipeline properties
        String[] pipesProps = new String[pipes.length];
        if (f.exists()) {
//            for (int i = 0; i < pipes.length; i++) {
//                pipesProps[i] = getPropertyFileForClass(f, pipes[i], i);
//            }
//        } else {
            List<String> pipelineProperty = StringTools.toStringList(cfg.getList("pipeline.properties", Collections.emptyList()));
            pipesProps = pipelineProperty.toArray(new String[]{});
        }

        String lastNode = null;
        String edge;
        int edgeCounter = 0;
        Configuration pipeHash = new PropertiesConfiguration();
        for (int i = 0; i < pipes.length; i++) {
            File pipeCfg = new File(pipesProps[i]);
            if (!pipeCfg.isAbsolute()) {
                System.out.println("PipeCfg path is not absolute!");
                pipeCfg = new File(f.getParentFile(), pipesProps[i]);
            }
            System.out.println("Loading element config from: "+pipeCfg.getAbsolutePath());
            System.out.println("Adding element "+pipes[i]);
//        for (String pipe : pipes) {
            PipelineElementWidget node = (PipelineElementWidget) scene.addNode(pipes[i]);
            node.setPropertyFile(pipeCfg.getAbsolutePath());
            scene.validate();
//                scene.getSceneAnimator().animatePreferredLocation(node, new Point(x, y));
//                scene.validate();

            System.out.println("Parsing pipeline element " + pipeCfg.getAbsolutePath());
            node.setClassName(pipes[i]);
            node.setCurrentClassProperties();
            Configuration prop = node.getProperties();
            pipeHash = PropertyLoader.getHash(pipeCfg.getAbsolutePath());
            node.setPropertyFile(pipeCfg.getAbsolutePath());
            Iterator iter = pipeHash.getKeys();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                prop.setProperty(key, pipeHash.getProperty(key));
            }
            //node.setProperties(prop);

            if (lastNode != null) {
                edge = "Ledge" + edgeCounter++;
                scene.addEdge(edge);
                scene.setEdgeSource(edge, lastNode);
                scene.setEdgeTarget(edge, pipes[i]);
                scene.validate();
            }
//                x += dx;
//                y += dy;
            lastNode = pipes[i];
        }

//        PipelineGeneralConfigWidget gnode = (PipelineGeneralConfigWidget) scene.getGeneral();
//        Iterator pipeIter = cfg.getKeys();
//        while (pipeIter.hasNext()) {
//            String key = (String) pipeIter.next();
//            if (!key.equalsIgnoreCase("pipeline") && !key.equalsIgnoreCase("pipeline.properties")) {
//                gnode.setProperty(key, cfg.getProperty(key));
//            }
//        }

        scene.validate();
        SceneLayouter.layoutDiagonal(scene);
    }
//    private static String getPropertyFileForClass(File baseConfig, String className, int count) {
//        String[] b = filename.split("/");
//        String s = "";
//        for (int i = 0; i < b.length - 2; i++) {
//            s += b[i] + "/";
//
//        }
//        String[] c = className.split("\\.");
//
//        // FIXME
//        return s + "0" + count + "_" + c[c.length - 1] + "/" + c[c.length - 1] + ".properties";
//    }
}
