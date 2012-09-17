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
package net.sf.maltcms.chromaui.project.api.descriptors;

import com.db4o.activation.ActivationPurpose;
import com.db4o.config.annotations.Indexed;

/**
 * Base class for bits of information in the domain model.
 * Containers extend this base class, as well as all other Descriptor implementations.
 * Provides basic functionality for name, displayName, date, id, shortDescription
 * and tool. A tool is the thing that created the descriptor in the first place.
 * @author Nils Hoffmann
 */
public class ADescriptor extends ABasicDescriptor implements IDescriptor {

    @Indexed IToolDescriptor tool = DescriptorFactory.newToolResultDescriptor();
    
    @Override
    public IToolDescriptor getTool() {
        activate(ActivationPurpose.READ);
        return tool;
    }
    
    @Override
    public void setTool(IToolDescriptor tool) {
        activate(ActivationPurpose.WRITE);
        IToolDescriptor old = this.tool;
        this.tool = tool;
        firePropertyChange(PROP_TOOL, old, tool);
    }
    
}
