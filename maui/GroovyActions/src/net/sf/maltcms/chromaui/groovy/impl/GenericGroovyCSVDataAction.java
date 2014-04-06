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
package net.sf.maltcms.chromaui.groovy.impl;

import groovy.lang.GroovyClassLoader;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;
import javax.swing.Action;
import maltcms.ui.fileHandles.csv.CSVDataObject;
import net.sf.maltcms.chromaui.groovy.api.CSVDataGroovyScript;
import net.sf.maltcms.chromaui.groovy.api.ScriptLoader;
import net.sf.maltcms.chromaui.groovy.ui.GroovyScriptSelectionForm;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.ui.support.api.ContextAction;
import net.sf.maltcms.chromaui.ui.support.api.Projects;
import org.codehaus.groovy.control.CompilationFailedException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Nils Hoffmann
 */
@ActionID(category = "Groovy",
        id = "net.sf.maltcms.chromaui.groovy.GenericGroovyCSVDataAction")
@ActionRegistration(displayName = "#CTL_GenericGroovyCSVDataAction", lazy = true)
@ActionReferences({
    @ActionReference(path = "Loaders/text/csv/Actions", position = -500),
    @ActionReference(path = "Loaders/text/tsv/Actions", position = -500),
    @ActionReference(path = "Loaders/text/txt/Actions", position = -500),})
@Messages("CTL_GenericGroovyCSVDataAction=Run CSV Groovy Action")
public class GenericGroovyCSVDataAction extends ContextAction<CSVDataObject> {

    public GenericGroovyCSVDataAction() {
        super();
    }

    public GenericGroovyCSVDataAction(Lookup lkp) {
        super(lkp);
    }

    public GenericGroovyCSVDataAction(Class<? extends CSVDataObject> contextType) {
        super(contextType);
    }

    private class CsvScriptLoader implements ScriptLoader<CSVDataGroovyScript> {

        @Override
        public CSVDataGroovyScript loadScript(FileObject fileObject, GroovyClassLoader classLoader) {
            Class clazz;
            try {
                clazz = classLoader.parseClass(FileUtil.toFile(fileObject));
                Class<CSVDataGroovyScript> irdgs = clazz.asSubclass(
                        CSVDataGroovyScript.class);
                CSVDataGroovyScript script = irdgs.newInstance();
                return script;
            } catch (InstantiationException | IllegalAccessException | CompilationFailedException | IOException ex) {
                Exceptions.printStackTrace(ex);
            } catch (ClassCastException ex) {
                Logger.getLogger(GenericGroovyRawDataAction.class.getName()).warning("Ignoring groovy script with wrong class!");
            }
            return null;
        }

    }

    @Override
    public void doAction(Collection<? extends CSVDataObject> instances) {
        Collection<? extends IChromAUIProject> projects = Projects.getSelectedOpenProject(IChromAUIProject.class, "Please select a project for this action", "Project");
        if (!projects.isEmpty()) {
            IChromAUIProject icap = projects.iterator().next();
            GroovyScriptSelectionForm<CSVDataGroovyScript> gssf = new GroovyScriptSelectionForm<CSVDataGroovyScript>(Utils.getScriptDirectories(icap), new CsvScriptLoader());
            gssf.updateModel();
            DialogDescriptor dd = new DialogDescriptor(gssf,
                    "Please select a script for execution");
            Object ret = DialogDisplayer.getDefault().notify(dd);
            if (DialogDescriptor.OK_OPTION.equals(ret)) {
                CSVDataGroovyScript selectedScript = gssf.getSelectedScript(
                        CSVDataGroovyScript.class);
                if (selectedScript != null) {
                    try {
                        BeanNode bn = new BeanNode(selectedScript);
                        PropertySheet ps = new PropertySheet();
                        ps.setNodes(new Node[]{bn});
                        DialogDescriptor bnd = new DialogDescriptor(ps,
                                "Set Script Properties");
                        Object bndRet = DialogDisplayer.getDefault().notify(bnd);
                        if (DialogDescriptor.CANCEL_OPTION.equals(bndRet)) {
                            return;
                        }
                    } catch (IntrospectionException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    ProgressHandle handle = ProgressHandleFactory.createHandle(selectedScript.getName());
                    selectedScript.create(icap, handle, instances);
                    RequestProcessor rp = new RequestProcessor(selectedScript.
                            getName(), 1, true);
                    rp.post(selectedScript);
                } else {
                }
            }
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext
    ) {
        return new GenericGroovyCSVDataAction(actionContext);
    }
}
