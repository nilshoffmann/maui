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
