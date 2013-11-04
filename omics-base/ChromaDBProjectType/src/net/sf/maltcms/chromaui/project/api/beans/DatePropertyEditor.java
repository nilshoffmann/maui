/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.project.api.beans;

import java.beans.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Nils Hoffmann
 */
public class DatePropertyEditor extends PropertyEditorSupport {
    
    public DatePropertyEditor() {
    }

    @Override
    public String getAsText() {
        Date id = (Date)getValue();
        return SimpleDateFormat.getDateTimeInstance().format(id);
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException {
        throw new IllegalArgumentException("Editing of Dates is not supported!");
//        setValue(UUID.fromString(string));
    }
}