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
import net.sf.maltcms.chromaui.groovy.api.GroovyProjectScript;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Cancellable;
import net.sf.maltcms.*;
import cross.*;
import maltcms.*;

/**
*
* @author ${user}
*/
class ${name} implements GroovyProjectScript<IChromAUIProject> {
    
    final String name = "${name}"
    private IChromAUIProject project
    private ProgressHandle progressHandle
    
    public String getName() {
        return name
    }

    public String getCategory() {
        return "Project modification"
    }

    public void create(IChromAUIProject project, ProgressHandle progressHandle) {
        this.project = project;
        this.progressHandle = progressHandle;
    }
    
    @Override
    public boolean cancel() {
        if(process!=null) {
            process.destroy()
        }
    }
    
    @Override
    public void run() {
        //uncomment if you need a tool specific output location
        File outdir = project.getOutputLocation(this);
        try {
            progressHandle.start()
            progressHandle.progress("Processing project "+project.getLocation())
            //do something with the project
        } finally {
            progressHandle.finish()
        }
        //uncomment, if you used a tool specific output location
        FileObject fo = FileUtil.toFileObject(outdir)
        fo.refresh()
    }
}
