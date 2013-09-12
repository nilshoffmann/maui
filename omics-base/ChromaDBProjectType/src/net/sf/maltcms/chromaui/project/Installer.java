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
package net.sf.maltcms.chromaui.project;

import java.beans.PropertyEditorManager;
import java.util.UUID;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.beans.*;
import net.sf.maltcms.chromaui.project.api.descriptors.IDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

	@Override
	public void restored() {
		PropertyEditorManager.registerEditor(UUID.class, UUIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(IDetectorType.class, DetectorTypePropertyEditor.class);
		PropertyEditorManager.registerEditor(ISeparationType.class, SeparationTypePropertyEditor.class);
		PropertyEditorManager.registerEditor(ITreatmentGroupDescriptor.class, TreatmentGroupDescriptorPropertyEditor.class);
		PropertyEditorManager.registerEditor(ISampleGroupDescriptor.class, SampleGroupDescriptorPropertyEditor.class);
		PropertyEditorManager.registerEditor(IChromAUIProject.class, ChromAUIProjectPropertyEditor.class);
		PropertyEditorManager.registerEditor(IDescriptor.class, GenericDescriptorPropertyEditor.class);
	}
}
