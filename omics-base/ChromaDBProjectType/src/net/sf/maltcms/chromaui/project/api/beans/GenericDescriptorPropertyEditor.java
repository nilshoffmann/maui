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

import java.beans.PropertyEditorSupport;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IDescriptor;

/**
 *
 * @author Nils Hoffmann
 */
public class GenericDescriptorPropertyEditor extends PropertyEditorSupport {
    
    public GenericDescriptorPropertyEditor() {
    }

    @Override
    public String getAsText() {
		Object value = getValue();
		if(value instanceof IDescriptor) {
			IDescriptor id = (IDescriptor)value;
			return id.getDisplayName();
		}else if(value instanceof IBasicDescriptor) {
			IBasicDescriptor id = (IBasicDescriptor)value;
			return id.getDisplayName();
		}
		return "<NA>";
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException {
        throw new IllegalArgumentException("Editing of descriptor name is not supported!");
    }
}
