<#if package?? && package != "">
package ${package};

</#if>

import java.text.DateFormat
import java.text.SimpleDateFormat

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.project.Project;
import net.sf.maltcms.chromaui.groovy.RawDataGroovyScript;
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
    
    String name = "${name}"
    String category = "Raw data processing"
    IChromAUIProject project
    Collection<CDFDataObject> dataObjects
    ProgressHandle progressHandle
    
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

