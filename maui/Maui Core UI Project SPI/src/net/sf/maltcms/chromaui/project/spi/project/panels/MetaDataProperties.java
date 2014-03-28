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
package net.sf.maltcms.chromaui.project.spi.project.panels;

import javax.swing.JComponent;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author hoffmann
 */
public class MetaDataProperties implements ProjectCustomizer.CompositeCategoryProvider {

    private static final String METADATA = "MetaData";

    @ProjectCustomizer.CompositeCategoryProvider.Registration(
        projectType = "net-sf-maltcms-chromaui-project-api-ichromauiproject", position = 10)
    public static MetaDataProperties createGeneralProperties() {
        return new MetaDataProperties();
    }

    @NbBundle.Messages("LBL_Config_MetaData=Meta Data")
    @Override
    public Category createCategory(Lookup lkp) {
        return ProjectCustomizer.Category.create(
            METADATA,
            Bundle.LBL_Config_MetaData(),
            null);
    }

    @Override
    public JComponent createComponent(Category category, Lookup lkp) {
        return new MetaDataPropertiesPanel(category, lkp);
    }
}
