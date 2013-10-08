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
package net.sf.maltcms.chromaui.project.spi;

import java.beans.PropertyEditorManager;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

	@Override
	public void restored() {
		String[] searchPath = PropertyEditorManager.getEditorSearchPath();
		String[] newPath = new String[searchPath.length+1];
		System.arraycopy(searchPath, 0, newPath, 0, searchPath.length);
		newPath[searchPath.length] = "net.sf.maltcms.chromaui.project.api.beans";
//		PropertyEditorManager.registerEditor(TreatmentGroupDescriptor.class, TreatmentGroupDescriptorPropertyEditor.class);
//		PropertyEditorManager.registerEditor(SampleGroupDescriptor.class, SampleGroupDescriptorPropertyEditor.class);
//		PropertyEditorManager.registerEditor(ChromAUIProject.class, ChromAUIProjectPropertyEditor.class);
//		PropertyEditorManager.registerEditor(ChromatogramDescriptor.class, GenericDescriptorPropertyEditor.class);
//		PropertyEditorManager.registerEditor(AnovaDescriptor.class, GenericDescriptorPropertyEditor.class);
//		PropertyEditorManager.registerEditor(ToolDescriptor.class, GenericDescriptorPropertyEditor.class);
//		PropertyEditorManager.registerEditor(NormalizationDescriptor.class, GenericDescriptorPropertyEditor.class);
//		PropertyEditorManager.registerEditor(PcaDescriptor.class, GenericDescriptorPropertyEditor.class);
//		PropertyEditorManager.registerEditor(Peak2DAnnotationDescriptor.class, GenericDescriptorPropertyEditor.class);
//		PropertyEditorManager.registerEditor(PeakAnnotationDescriptor.class, GenericDescriptorPropertyEditor.class);
//		PropertyEditorManager.registerEditor(PeakGroupDescriptor.class, GenericDescriptorPropertyEditor.class);
//		PropertyEditorManager.registerEditor(UserDatabaseDescriptor.class, GenericDescriptorPropertyEditor.class);
//		PropertyEditorManager.registerEditor(UUID.class, UUIDPropertyEditor.class);
//		PropertyEditorManager.registerEditor(IDetectorType.class, DetectorTypePropertyEditor.class);
//		PropertyEditorManager.registerEditor(ISeparationType.class, SeparationTypePropertyEditor.class);
//		PropertyEditorManager.registerEditor(ITreatmentGroupDescriptor.class, TreatmentGroupDescriptorPropertyEditor.class);
//		PropertyEditorManager.registerEditor(ISampleGroupDescriptor.class, SampleGroupDescriptorPropertyEditor.class);
//		PropertyEditorManager.registerEditor(IChromAUIProject.class, ChromAUIProjectPropertyEditor.class);
//		PropertyEditorManager.registerEditor(IDescriptor.class, GenericDescriptorPropertyEditor.class);
		PropertyEditorManager.setEditorSearchPath(newPath);
	}
}
