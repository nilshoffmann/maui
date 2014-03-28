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

import net.sf.maltcms.chromaui.groovy.api.RawDataGroovyScript;
import groovy.lang.GroovyClassLoader;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import maltcms.ui.fileHandles.cdf.CDFDataObject;
import net.sf.maltcms.chromaui.groovy.ui.GroovyScriptSelectionForm;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.codehaus.groovy.control.CompilationFailedException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.util.Utilities;

@ActionID(category = "Groovy",
        id = "net.sf.maltcms.chromaui.groovy.GenericGroovyRawDataAction")
@ActionRegistration(displayName = "#CTL_GenericGroovyRawDataAction")
@ActionReferences({
    @ActionReference(path = "Loaders/application/x-cdf/Actions", position = 500)
})
@Messages("CTL_GenericGroovyRawDataAction=Run CDF Groovy Action")
public final class GenericGroovyRawDataAction implements ActionListener {

    private final List<CDFDataObject> context;

    public GenericGroovyRawDataAction(List<CDFDataObject> context) {
        this.context = context;
    }

    public IChromAUIProject locateProject(CDFDataObject dobj) {
        FileObject fobj = dobj.getPrimaryFile();
        FileObject currentFileObject = fobj.getParent();
        while (!currentFileObject.isRoot()) {
            Project project;
            try {
                project = ProjectManager.getDefault().findProject(currentFileObject);
                if (project != null && project instanceof IChromAUIProject) {
                    System.out.println("Found IChromAUIProject at location " + project.getProjectDirectory() + " as parent of " + fobj);
                    return (IChromAUIProject) project;
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalArgumentException ex) {
                Exceptions.printStackTrace(ex);
            }
            currentFileObject = currentFileObject.getParent();
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        IChromAUIProject icap = Utilities.actionsGlobalContext().lookup(
                IChromAUIProject.class);
        if (icap == null) {
            if (!context.isEmpty()) {
                icap = locateProject(context.get(0));
            }
        }
        if (icap
                != null) {
            GroovyClassLoader gcl = new GroovyClassLoader();
            List<FileObject> scriptFiles = Utils.getScriptLocations(icap);
            List<RawDataGroovyScript> groovyScripts = new ArrayList<RawDataGroovyScript>();
            for (FileObject child : scriptFiles) {
                Class clazz;
                try {
                    clazz = gcl.parseClass(FileUtil.toFile(child));
                    Class<RawDataGroovyScript> irdgs = clazz.asSubclass(
                            RawDataGroovyScript.class);
                    RawDataGroovyScript script = irdgs.newInstance();
                    groovyScripts.add(script);
                } catch (InstantiationException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (IllegalAccessException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (CompilationFailedException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (ClassCastException ex) {
                    System.out.println("Ignoring groovy script with wrong class!");
                }
            }

            GroovyScriptSelectionForm gssf = new GroovyScriptSelectionForm();
            gssf.setModel(groovyScripts);
            DialogDescriptor dd = new DialogDescriptor(gssf,
                    "Please select a script for execution");
            Object ret = DialogDisplayer.getDefault().notify(dd);
            if (DialogDescriptor.OK_OPTION.equals(ret)) {
                RawDataGroovyScript selectedScript = gssf.getSelectedScript(
                        RawDataGroovyScript.class);
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
                    ProgressHandle handle = ProgressHandleFactory.createHandle(selectedScript.getName(), selectedScript);
                    selectedScript.create(icap, handle, this.context);
                    RequestProcessor rp = new RequestProcessor(selectedScript.getName(), 1, true);
                    rp.post(selectedScript);
                }
            } else {
            }

        }
    }
}
