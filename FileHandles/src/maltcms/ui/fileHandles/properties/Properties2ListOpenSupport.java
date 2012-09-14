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
package maltcms.ui.fileHandles.properties;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import maltcms.ui.fileHandles.csv.CSVTableViewDescription;
import maltcms.ui.fileHandles.properties.tools.PropertyLoader;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author Mathias Wilhelm
 */
public class Properties2ListOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public Properties2ListOpenSupport(PropertiesDataObject.Entry entry) {
        super(entry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        final PropertiesDataObject dobj = (PropertiesDataObject) entry.getDataObject();
        final CSVTableViewDescription tc = new CSVTableViewDescription();
        MultiViewDescription[] dsc = {tc};
//        final CSVTableView tc = new CSVTableView();
//        tc.setDisplayName(dobj.getName());

        tc.setTableModel(PropertyLoader.getModel(entry.getFile().getPath()));

        CloneableTopComponent ctc = MultiViewFactory.createCloneableMultiView(dsc, dsc[0]);
        ctc.setDisplayName(dobj.getName());
        return ctc;
    }
}
