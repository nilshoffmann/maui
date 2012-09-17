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
package net.sf.maltcms.chromaui.db.spi.db4o.options;

import java.awt.Component;

/**
 *
 * @author Nils Hoffmann
 */
public class ValidationMessage {
    
    public static enum Type{
        CLEAR,WARNING,ERROR,INFO;
    };
    
    private final Component source;
    private final Type type;
    private final String message;

    public ValidationMessage(Component source, Type type, String message) {
        this.source = source;
        this.type = type;
        this.message = message;
    }

    public Component getSource() {
        return source;
    }

    public Type getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
    
}
