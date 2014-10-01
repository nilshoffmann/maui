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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import maltcms.ui.fileHandles.cdf.CDFDataObject;
import net.sf.maltcms.chromaui.groovy.api.RawDataGroovyScript;
import net.sf.maltcms.chromaui.groovy.api.ScriptLoader;
import net.sf.maltcms.chromaui.groovy.ui.GroovyScriptSelectionForm;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.Projects;
import org.codehaus.groovy.control.CompilationFailedException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.util.Utilities;

@ActionID(category = "Groovy",
        id = "net.sf.maltcms.chromaui.groovy.GenericGroovyChromatogramDescriptorDataAction")
@ActionRegistration(displayName = "#CTL_GenericGroovyChromatogramDescriptorDataAction", lazy = true)
@ActionReferences({
    @ActionReference(path = "Actions/DescriptorNodeActions/IChromatogramDescriptor/Actions", position = 500)
})
@Messages("CTL_GenericGroovyChromatogramDescriptorDataAction=Run CDF Groovy Action")
public final class GenericGroovyChromatogramDescriptorDataAction implements ActionListener {

    private final List<IChromatogramDescriptor> context;

    public GenericGroovyChromatogramDescriptorDataAction(List<IChromatogramDescriptor> context) {
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
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Found IChromAUIProject at location {0} as parent of {1}", new Object[]{project.getProjectDirectory(), fobj});
                    return (IChromAUIProject) project;
                } else {
                    Collection<? extends IChromAUIProject> projects = Projects.getSelectedOpenProject(IChromAUIProject.class, "Please select a project for this action", "Project");
                    if (!projects.isEmpty()) {
                        return projects.iterator().next();
                    }
                }
            } catch (IOException | IllegalArgumentException ex) {
                Exceptions.printStackTrace(ex);
            }
            currentFileObject = currentFileObject.getParent();
        }
        return null;
    }

    private class RawDataScriptLoader implements ScriptLoader<RawDataGroovyScript> {

        @Override
        public RawDataGroovyScript loadScript(FileObject fileObject, GroovyClassLoader classLoader) {
            Class clazz;
            try {
                clazz = classLoader.parseClass(FileUtil.toFile(fileObject));
                Class<RawDataGroovyScript> irdgs = clazz.asSubclass(
                        RawDataGroovyScript.class);
                RawDataGroovyScript script = irdgs.newInstance();
                return script;
            } catch (InstantiationException | IllegalAccessException | CompilationFailedException | IOException ex) {
                Exceptions.printStackTrace(ex);
            } catch (ClassCastException ex) {
                Logger.getLogger(GenericGroovyChromatogramDescriptorDataAction.class.getName()).warning("Ignoring groovy script with wrong class!");
            }
            return null;
        }

    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        IChromAUIProject icap = Utilities.actionsGlobalContext().lookup(
                IChromAUIProject.class);
        if (icap != null) {
            GroovyScriptSelectionForm<RawDataGroovyScript> gssf = new GroovyScriptSelectionForm<>(Utils.getScriptDirectories(icap), new RawDataScriptLoader());
            gssf.updateModel();
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
                    List<CDFDataObject> list = new LinkedList<>();
                    for (IChromatogramDescriptor descr : context) {
                        DataObject dobj;
                        try {
                            dobj = DataObject.find(FileUtil.toFileObject(new File(descr.getResourceLocation())));
                            if (dobj instanceof CDFDataObject) {
                                list.add((CDFDataObject) dobj);
                            }
                        } catch (DataObjectNotFoundException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                    selectedScript.create(icap, handle, list);
                    RequestProcessor rp = new RequestProcessor(selectedScript.getName(), 1, true);
                    rp.post(selectedScript);
                }
            } else {
            }

        }
    }
}
