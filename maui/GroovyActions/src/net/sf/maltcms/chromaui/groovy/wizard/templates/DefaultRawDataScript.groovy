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
<#if package?? && package != "">
package ${package};

</#if>

import java.text.DateFormat
import java.text.SimpleDateFormat

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.project.Project;
import net.sf.maltcms.chromaui.groovy.api.RawDataGroovyScript;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import maltcms.ui.fileHandles.cdf.CDFDataObject;
import org.openide.loaders.DataObject;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Cancellable;
import net.sf.maltcms.*;
import cross.*;
import maltcms.*;
import cross.datastructures.fragments.IFileFragment

/**
*
* @author ${user}
*/
class ${name} implements RawDataGroovyScript {
    
    final String name = "${name}"
    final String category = "Raw data processing"
    private IChromAUIProject project
    private Collection<CDFDataObject> dataObjects
    private ProgressHandle progressHandle
    
    public void create(IChromAUIProject project, ProgressHandle progressHandle, Collection<CDFDataObject> dobjects) {
        this.project = project
        this.progressHandle = progressHandle
        this.dataObjects = dobjects
    }
    
    @Override
    public boolean cancel() {
        //implement cancellation logic
    }
    
    @Override
    public void run() {
        File outdir = project.getOutputLocation(this);
        try {
            progressHandle.setDisplayName(name)
            progressHandle.start(dataObjects.size())
            def i = 1;
            dataObjects.each{ it ->
                progressHandle.progress("Processing ${r"${it.fragment.name}"}",i)
                //do something

                i++
            }
        } finally {
            progressHandle.finish()
        }
        FileObject fo = FileUtil.toFileObject(outdir)
        fo.refresh()
    }
}
