/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.groovy;

import groovy.lang.GroovyClassLoader;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import maltcms.ui.fileHandles.cdf.CDFDataObject;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.codehaus.groovy.control.CompilationFailedException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.loaders.DataObject;

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
@Messages("CTL_GenericGroovyRawDataAction=Run Groovy Action")
public final class GenericGroovyRawDataAction implements ActionListener {

    private final List<DataObject> context;

    public GenericGroovyRawDataAction(List<DataObject> context) {
        this.context = context;
    }

    public IChromAUIProject locateProject(DataObject dobj) {
        FileObject fobj = dobj.getPrimaryFile();
        FileObject currentFileObject = fobj.getParent();
        while (!currentFileObject.isRoot()) {
            Project project;
            try {
                project = ProjectManager.getDefault().findProject(currentFileObject);
                if (project != null && project instanceof IChromAUIProject) {
                    System.out.println("Found IChromAUIProject at location "+project.getProjectDirectory()+" as parent of "+fobj);
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
                    selectedScript.setProject(icap);
                    selectedScript.setDataObjects(this.context.toArray(new CDFDataObject[this.context.size()]));
                    try {
                        BeanNode bn = new BeanNode(selectedScript);
                        PropertySheet ps = new PropertySheet();
                        ps.setNodes(new Node[]{bn});
                        DialogDescriptor bnd = new DialogDescriptor(ps,
                                "Set Script Properties");
                        Object bndRet = DialogDisplayer.getDefault().notify(bnd);
                    } catch (IntrospectionException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    ProgressHandle handle = ProgressHandleFactory.createHandle(selectedScript.getName(), selectedScript);
                    selectedScript.setProgressHandle(handle);
                    RequestProcessor rp = new RequestProcessor(selectedScript.getName(), 1, true);
                    rp.post(selectedScript);
                }
            } else {
            }

        }
    }
}
