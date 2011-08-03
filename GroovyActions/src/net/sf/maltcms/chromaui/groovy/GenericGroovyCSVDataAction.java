/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.groovy;

import groovy.lang.GroovyClassLoader;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Action;
import maltcms.ui.fileHandles.csv.CSVDataObject;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import org.codehaus.groovy.control.CompilationFailedException;
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
    @ActionReference(path = "Loaders/text/csv/Actions", position = -400)
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
            File fo = FileUtil.toFile(icap.getLocation());
            FileObject groovyDir = null;
            if (!new File(fo, "groovy").isDirectory()) {
                try {
                    groovyDir = icap.getLocation().createFolder("groovy");
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else {
                groovyDir = icap.getLocation().getFileObject("groovy/");
            }


            List<CSVDataGroovyScript> groovyScripts = new LinkedList<CSVDataGroovyScript>();
            if (groovyDir.isValid()) {
                Enumeration<? extends FileObject> enumeration = groovyDir.
                        getChildren(true);
                while (enumeration.hasMoreElements()) {
                    FileObject child = enumeration.nextElement();
                    if (child.hasExt("groovy")) {
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
                }
            } else {
            }

            GroovyScriptSelectionForm gssf = new GroovyScriptSelectionForm();
            gssf.setModel(groovyScripts);
            DialogDescriptor dd = new DialogDescriptor(gssf,
                    "Please select a script for execution");
            Object ret = DialogDisplayer.getDefault().notify(dd);
            if (DialogDescriptor.OK_OPTION.equals(ret)) {
                CSVDataGroovyScript selectedScript = gssf.getSelectedScript(
                        CSVDataGroovyScript.class);

                selectedScript.setProject(icap);
                selectedScript.setDataObjects(instances.toArray(new CSVDataObject[instances.
                        size()]));
                RequestProcessor.getDefault().post(selectedScript);
            } else {
            }




        }
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new GenericGroovyCSVDataAction(actionContext);
    }
}
