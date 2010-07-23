/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.nb;

import java.util.logging.Logger;
import net.sourceforge.maltcms.chromauiproject.AttributeType;
import net.sourceforge.maltcms.chromauiproject.BooleanAttributeType;
import net.sourceforge.maltcms.chromauiproject.ClassAttributeType;
import net.sourceforge.maltcms.chromauiproject.DoubleAttributeType;
import net.sourceforge.maltcms.chromauiproject.FloatAttributeType;
import net.sourceforge.maltcms.chromauiproject.IntAttributeType;
import net.sourceforge.maltcms.chromauiproject.StringAttributeType;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;

/**
 *
 * @author nilshoffmann
 */
public class PropertyBuilder {

    public Property buildProperty(AttributeType at){
        Property p = null;
        try{
        if (at instanceof StringAttributeType) {
            StringAttributeType sat = (StringAttributeType) at;
            p = new PropertySupport.Reflection<String>(sat, String.class, "stringAttribute");
            p.setName(sat.getName());
        } else if (at instanceof BooleanAttributeType) {
            BooleanAttributeType sat = (BooleanAttributeType) at;
            p = new PropertySupport.Reflection<Boolean>(sat, Boolean.class, "booleanAttribute");
            p.setName(sat.getName());
        } else if (at instanceof ClassAttributeType) {
            ClassAttributeType sat = (ClassAttributeType) at;
            p = new PropertySupport.Reflection<Class>(sat, Class.class, "classAttribute");
            p.setName(sat.getName());
        } else if (at instanceof DoubleAttributeType) {
            DoubleAttributeType sat = (DoubleAttributeType) at;
            p = new PropertySupport.Reflection<Double>(sat, Double.class, "doubleAttribute");
            p.setName(sat.getName());
        } else if (at instanceof FloatAttributeType) {
            FloatAttributeType sat = (FloatAttributeType) at;
            p = new PropertySupport.Reflection<Float>(sat, Float.class, "floatAttribute");
            p.setName(sat.getName());
        } else if (at instanceof IntAttributeType) {
            IntAttributeType sat = (IntAttributeType) at;
            p = new PropertySupport.Reflection<Integer>(sat, Integer.class, "intAttribute");
            p.setName(sat.getName());
        } else {
            Logger.getLogger(this.getClass().getName()).warning("Do not know how to generate Property for attribute of type: " + at.getClass().getName());
        }
        }catch(NoSuchMethodException nsme) {
            Logger.getLogger(this.getClass().getName()).warning("Could not find a method for attributeType: "+at.getName());
        }
        return p;
    }
}
