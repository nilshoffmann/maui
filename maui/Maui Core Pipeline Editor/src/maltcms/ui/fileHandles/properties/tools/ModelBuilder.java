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

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import maltcms.ui.fileHandles.properties.wizards.WidgetTableModel;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Nils Hoffmann
 */
public class ModelBuilder {

    public static WidgetTableModel getModel(Configuration properties, Class<?> c) {

        Vector<String> header = new Vector<>();
        header.add("Key");
        header.add("Value");
        Logger.getLogger(ModelBuilder.class.getName()).log(Level.INFO, "properties: {0}", properties);

        return new WidgetTableModel(header, PropertyLoader.asHash(properties), c);
    }

}
