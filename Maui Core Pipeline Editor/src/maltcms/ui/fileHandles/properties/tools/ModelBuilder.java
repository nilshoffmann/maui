/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.tools;

import java.util.Vector;
import maltcms.ui.fileHandles.properties.wizards.WidgetTableModel;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author hoffmann
 */
public class ModelBuilder {
     
    
    public static WidgetTableModel getModel(Configuration properties, Class<?> c) {

        Vector<String> header = new Vector<String>();
        header.add("Key");
        header.add("Value");
        System.out.println("properties: " + properties);

        return new WidgetTableModel(header, PropertyLoader.asHash(properties), c);
    }

}
