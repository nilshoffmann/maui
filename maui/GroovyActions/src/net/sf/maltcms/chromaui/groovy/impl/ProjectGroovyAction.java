package net.sf.maltcms.chromaui.groovy.impl;

import groovy.lang.GroovyClassLoader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.sf.maltcms.chromaui.groovy.api.GroovyProjectScript;
import net.sf.maltcms.chromaui.groovy.ui.GroovyScriptSelectionForm;
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
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;

/**
 *
 * @author hoffmann
 */
@ActionID(category = "Groovy",
        id = "net.sf.maltcms.chromaui.groovy.ProjectGroovyAction")
@ActionRegistration(displayName = "#CTL_ProjectGroovyAction")
@ActionReferences({
    @ActionReference(path = "Actions/ChromAUIProjectLogicalView/Scripts", position = 500)
})
@NbBundle.Messages("CTL_ProjectGroovyAction=Run Project Groovy Action")
public final class ProjectGroovyAction implements ActionListener {

    private final IChromAUIProject context;

    public ProjectGroovyAction(IChromAUIProject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (context != null) {
            GroovyClassLoader gcl = new GroovyClassLoader();
            List<FileObject> scriptFiles = Utils.getScriptLocations(context);
            List<GroovyProjectScript> groovyScripts = new ArrayList<GroovyProjectScript>();
            for (FileObject child : scriptFiles) {
                Class clazz;
                try {
                    clazz = gcl.parseClass(FileUtil.toFile(child));
                    Class<GroovyProjectScript> irdgs = clazz.asSubclass(
                            GroovyProjectScript.class);
                    GroovyProjectScript script = irdgs.newInstance();
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
                GroovyProjectScript selectedScript = gssf.getSelectedScript(
                        GroovyProjectScript.class);
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
                    selectedScript.create(context, handle);
                    RequestProcessor rp = new RequestProcessor(selectedScript.getName(), 1, true);
                    rp.post(selectedScript);
                }
            } else {
            }

        }
    }
}
