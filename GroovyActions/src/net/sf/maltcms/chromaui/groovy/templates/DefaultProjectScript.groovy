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
    
    String name = "${name}"
    IChromAUIProject project
    ProgressHandle progressHandle
    
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
