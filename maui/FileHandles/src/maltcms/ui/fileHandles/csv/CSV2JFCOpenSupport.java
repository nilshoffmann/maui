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
package maltcms.ui.fileHandles.csv;

import maltcms.ui.fileHandles.serialized.JFCTopComponent;
import maltcms.ui.fileHandles.serialized.JFCView;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author Mathias Wilhelm
 */
public class CSV2JFCOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    /**
     *
     * @param entry
     */
    public CSV2JFCOpenSupport(CSVDataObject.Entry entry) {
        super(entry);
    }

    /**
     *
     * @return
     */
    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        CSVDataObject dobj = (CSVDataObject) entry.getDataObject();
        JFCTopComponent jtc = new JFCTopComponent();
        CSV2JFCLoader jl = new CSV2JFCLoader();
        jl.load(dobj, jtc);
        return jtc;
    }
}
