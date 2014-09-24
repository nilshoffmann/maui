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
package maltcms.ui.fileHandles.properties;

import java.util.logging.Level;
import java.util.logging.Logger;
import maltcms.ui.fileHandles.properties.pipeline.MaltcmsPipelineFormatDataObject;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author Mathias Wilhelm
 */
public class PipelineEditorOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public PipelineEditorOpenSupport(MaltcmsPipelineFormatDataObject.Entry entry) {
        super(entry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        final MaltcmsPipelineFormatDataObject dobj = (MaltcmsPipelineFormatDataObject) entry.getDataObject();

        final PipelineEditorTopComponent tc = new PipelineEditorTopComponent();
        tc.setDisplayName(dobj.getName());
        tc.setBaseFile(entry.getFile());
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Start parsing file: {0}", entry.getFile().getPath());
//        PipelineGraphScene scene = tc.getPipelineGraphScene();
//        PropertyLoader.parseIntoScene(entry.getFile().getPath(), scene);
        //tc.setPipelineGraphScene(PropertyLoader.getScene(entry.getFile().getPath()));
//        System.out.println("End");

        return tc;
    }
}
