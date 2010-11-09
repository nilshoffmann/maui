/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.ui.execution.api;

import java.io.File;
import java.util.concurrent.Callable;
import org.netbeans.api.extexecution.ExecutionDescriptor;
import org.netbeans.api.extexecution.ExecutionService;

/**
 *
 * @author nilshoffmann
 */
public interface IExecutionSupport {

    public Callable<Process> getProcessCallable();

    public ExecutionDescriptor getExecutionDescriptor();

    public String getDisplayName();

    public ExecutionService createExecutionService();

    public void initialize(String displayName, String commandLine, File workingDirectory);

}
