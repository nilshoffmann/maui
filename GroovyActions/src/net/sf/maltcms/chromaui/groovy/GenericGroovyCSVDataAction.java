/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.groovy;

import net.sf.maltcms.chromaui.ui.support.api.ContextAction;
import groovy.lang.GroovyClassLoader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Action;
import maltcms.ui.fileHandles.csv.CSVDataObject;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.codehaus.groovy.control.CompilationFailedException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.util.Utilities;

/**
 *
 * @author nilshoffmann
 */
@ActionID(category = "Groovy",
id = "net.sf.maltcms.chromaui.groovy.GenericGroovyCSVDataAction")
@ActionRegistration(displayName = "#CTL_GenericGroovyCSVDataAction")
@ActionReferences({
    @ActionReference(path = "Loaders/text/csv/Actions", position = -500)
})
@Messages("CTL_GenericGroovyCSVDataAction=Run Groovy Action")
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

    @Override
    public void doAction(Collection<? extends CSVDataObject> instances) {
        IChromAUIProject icap = Utilities.actionsGlobalContext().lookup(
                IChromAUIProject.class);
        if (icap != null) {
            GroovyClassLoader gcl = new GroovyClassLoader();
            List<FileObject> scriptFiles = Utils.getScriptLocations(icap);
            List<CSVDataGroovyScript> groovyScripts = new LinkedList<CSVDataGroovyScript>();
            for (FileObject child : scriptFiles) {
                Class clazz;
                try {
                    clazz = gcl.parseClass(FileUtil.toFile(child));
                    Class<CSVDataGroovyScript> irdgs = clazz.asSubclass(
                            RawDataGroovyScript.class);
                    CSVDataGroovyScript script = irdgs.newInstance();
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
                CSVDataGroovyScript selectedScript = gssf.getSelectedScript(
                        CSVDataGroovyScript.class);
                ProgressHandle handle = ProgressHandleFactory.createHandle(selectedScript.getName());
                selectedScript.create(icap, handle, instances);
                RequestProcessor rp = new RequestProcessor(selectedScript.
                        getName(), 1, true);
                rp.post(selectedScript);
            } else {
            }
        }
    }

    

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new GenericGroovyCSVDataAction(actionContext);
    }
}
