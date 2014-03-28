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
