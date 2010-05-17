/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.tools;

import apps.Maltcms;
import cross.annotations.AnnotationInspector;
import cross.datastructures.tuple.Tuple2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Vector;
import javax.swing.table.TableModel;
import maltcms.ui.fileHandles.properties.graph.PipelineElementWidget;
import maltcms.ui.fileHandles.properties.graph.PipelineGeneralConfigWidget;
import maltcms.ui.fileHandles.properties.graph.PipelineGraphScene;
import maltcms.ui.fileHandles.properties.wizards.HashTableModel;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.netbeans.api.visual.widget.Widget;
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
            ServiceLoader<?> sl = ServiceLoader.load(c);
            for (Object o : sl) {
                if (o != null) {
                    ret.add(o.getClass().getName());
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (ret.size() == 0) {
            return new String[]{"Can not find any Servies for " + optionValue};
        } else {
            return ret.toArray(new String[]{});
        }
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

    public static Map<String, String> getHash(String filename) {
        try {
            Maltcms m = Maltcms.getInstance();
            // set up Maltcms configuration
            final CompositeConfiguration cfg = new CompositeConfiguration();
            cfg.addConfiguration(new PropertiesConfiguration(filename));

            //CompositeConfiguration cfg = m.parseCommandLine(new String[]{"-c", filename});

            Map<String, String> hash = new HashMap<String, String>();
            Iterator<String> i = cfg.getKeys();
            String key;
            String[] values;
            String value;
            while (i.hasNext()) {
                key = i.next();
                values = cfg.getStringArray(key);
                value = "";
                for (int j = 0; j < values.length; j++) {
                    value += values[j];
                    if (j < values.length - 1) {
                        value += ",";
                    }
                }
                hash.put(key, value);
            }
            //System.out.println("PIPELINE: " + cfg.getString("pipeline"));
            return hash;
        } catch (ConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    public static HashTableModel getModel(Map<String, String> properties) {
        Vector<String> header = new Vector<String>();
        header.add("Key");
        header.add("Value");
        System.out.println("properties: " + properties);
        return new HashTableModel(header, properties);
    }

    public static TableModel getModel(String filename) {
        System.out.println("Getting model for: " + filename);
        return PropertyLoader.getModel(PropertyLoader.getHash(filename));
    }

    public static PipelineGraphScene parseIntoScene(String filename, PipelineGraphScene scene) {
//        int x = 10;
//        int dx = 100;
//        int y = 10;
//        int dy = 100;

        Map<String, String> hash = getHash(filename);
        if (hash != null && hash.get("pipeline") != null) {
            String pipeline = hash.get("pipeline");
            pipeline = pipeline.substring(1, pipeline.length() - 1);
            pipeline.replaceAll(" ", "");
            String[] pipes = pipeline.split(",");

            String[] pipesProps = new String[pipes.length];
            if (filename.endsWith("runtime.properties")) {
                for (int i = 0; i < pipes.length; i++) {
                    pipesProps[i] = getPropertyFileForClass(filename, pipes[i], i);
                }
            } else {
                String pipelineProperty = hash.get("pipeline.properties");
                pipelineProperty = pipelineProperty.substring(1, pipelineProperty.length() - 1);
                pipelineProperty.replaceAll(" ", "");
                pipesProps = pipelineProperty.split(",");
            }

            String lastNode = null;
            String edge;
            int edgeCounter = 0;
            Map<String, String> pipeHash = new HashMap<String, String>();
            for (int i = 0; i < pipes.length; i++) {
//        for (String pipe : pipes) {
                PipelineElementWidget node = (PipelineElementWidget) scene.addNode(pipes[i]);
                node.setPropertyFile(pipesProps[i]);
                scene.validate();
//                scene.getSceneAnimator().animatePreferredLocation(node, new Point(x, y));
//                scene.validate();

                node.setClassName(pipes[i]);
                node.setCurrentClassProperties();
                Map<String, String> prop = node.getProperties();
                pipeHash = PropertyLoader.getHash(pipesProps[i]);
                node.setPropertyFile(pipesProps[i]);
                for (String key : pipeHash.keySet()) {
                    prop.put(key, pipeHash.get(key));
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

            PipelineGeneralConfigWidget gnode = (PipelineGeneralConfigWidget) scene.getGeneral();
            for (String key : hash.keySet()) {
                if (!key.equalsIgnoreCase("pipeline") && !key.equalsIgnoreCase("pipeline.properties")) {
                    gnode.setPorperty(key, hash.get(key));
                }
            }

            scene.validate();
            SceneLayouter.layoutDiagonal(scene);
            return scene;
        } else {
            return null;
        }
    }

    private static String getPropertyFileForClass(String filename, String className, int count) {
        String[] b = filename.split("/");
        String s = "";
        for (int i = 0; i < b.length - 2; i++) {
            s += b[i] + "/";

        }
        String[] c = className.split("\\.");

        // FIXME
        return s + "0" + count + "_" + c[c.length - 1] + "/" + c[c.length - 1] + ".properties";
    }
}
