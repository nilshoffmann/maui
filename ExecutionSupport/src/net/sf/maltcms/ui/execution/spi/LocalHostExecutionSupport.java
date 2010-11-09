/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.ui.execution.spi;

import java.io.File;
import java.util.concurrent.Callable;
import net.sf.maltcms.ui.execution.api.IExecutionSupport;
import org.netbeans.api.extexecution.ExecutionDescriptor;
import org.netbeans.api.extexecution.ExecutionService;
import org.netbeans.api.extexecution.ExternalProcessBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author nilshoffmann
 */
@ServiceProvider(service=IExecutionSupport.class)
public class LocalHostExecutionSupport implements IExecutionSupport{

    private File workingDirectory;
    private String commandLine;
    private ExternalProcessBuilder pb;

    private ExecutionDescriptor ed;
    private String displayName;
    private ExecutionService service;

    @Override
    public void initialize(String displayName, String commandLine, File workingDirectory) {
        System.out.println("Using working dir "+workingDirectory.getAbsolutePath());
        this.workingDirectory = workingDirectory;
        this.displayName = displayName;
        this.ed = new ExecutionDescriptor();
        this.commandLine = commandLine;
    }

    @Override
    public Callable<Process> getProcessCallable() {
        if(this.pb == null) {
            this.pb = new ExternalProcessBuilder(this.commandLine);
            this.pb.workingDirectory(this.workingDirectory);
        }
        return this.pb;
    }

    @Override
    public ExecutionDescriptor getExecutionDescriptor() {
        return this.ed;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public ExecutionService createExecutionService() {
        if(service==null) {
            service = ExecutionService.newService(getProcessCallable(), getExecutionDescriptor(), getDisplayName());
        }
        return service;
    }

}
