<#if package?? && package != "">
package ${package};

</#if>

import java.text.DateFormat
import java.text.SimpleDateFormat

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.project.Project;
import net.sf.maltcms.chromaui.groovy.api.CSVDataGroovyScript;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import maltcms.ui.fileHandles.csv.CSVDataObject;
import org.openide.loaders.DataObject;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputWriter;
import org.openide.util.Cancellable;
import net.sf.maltcms.*;
import cross.*;
import maltcms.*;
import cross.datastructures.fragments.IFileFragment

/**
*
* @author ${user}
*/
class ${name} implements CSVDataGroovyScript {
    
    String name = "${name}"
    String category = "Project modification"
    IChromAUIProject project
    Collection<CSVDataObject> dataObjects
    ProgressHandle progressHandle
    
    public void create(IChromAUIProject project, ProgressHandle progressHandle, Collection<CSVDataObject> dobjs) {
        this.project = project;
        this.progressHandle = progressHandle;
        this.dataObjects = dobjs;
    }
    
    @Override
    public boolean cancel() {
        //implement custom cancellation logic
    }
    
    @Override
    public void run() {
        File outdir = project.getOutputLocation(this);
        try {
            progressHandle.setDisplayName(name)
            progressHandle.start(dataObjects.length)
            def i = 1;
            dataObjects.each{ it ->
                progressHandle.progress("Processing ${r"${rootFragment.getName()}"}",i)
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

