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
package net.sf.maltcms.chromaui.groovy.wizard;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.api.templates.TemplateRegistration;
import org.netbeans.api.templates.TemplateRegistrations;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.ContextAwareAction;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

// TODO define position attribute
@TemplateRegistrations({
    @TemplateRegistration(scriptEngine = "freemarker", displayName = "Groovy Project Script", folder = "Maui/Groovy", content = "templates/DefaultProjectScript.groovy", position = 0, targetName = "ProjectScript", description = "projectScript.html"),
    @TemplateRegistration(scriptEngine = "freemarker", displayName = "Groovy CDF Script", folder = "Maui/Groovy", content = "templates/DefaultRawDataScript.groovy", position = 1, targetName = "RawDataScript", description = "rawDataScript.html"),
    @TemplateRegistration(scriptEngine = "freemarker", displayName = "Groovy CSV Script", folder = "Maui/Groovy", content = "templates/DefaultCSVScript.groovy", position = 2, targetName = "CSVScript", description = "csvScript.html"),
    @TemplateRegistration(scriptEngine = "freemarker", displayName = "Groovy/R XCMS Matched Filter Peak Finder Script", folder = "Maui/Groovy", content = "templates/XCMSMatchedFilterPeakFinder.groovy", position = 3, targetName = "groovy", description = "xcmsMatchedFilterScript.html")
})
@Messages("GroovyScriptWizardIterator_displayName=New Groovy Script")
public final class GroovyScriptWizardIterator implements WizardDescriptor.InstantiatingIterator<WizardDescriptor> {

    private int index;

    private WizardDescriptor wizard;
    private List<WizardDescriptor.Panel<WizardDescriptor>> panels;

//    @Override
    protected WizardDescriptor.Panel<WizardDescriptor> createTargetChooser(WizardDescriptor wizard) {
        Project project = (Project) wizard.getProperty("project");
        SourceGroup[] sourceGroups;
        if (project != null) {
            Sources c = ProjectUtils.getSources(project);
            sourceGroups = c.getSourceGroups(Sources.TYPE_GENERIC);
            try {
                FileObject scriptsFolder = FileUtil.createFolder(project.getProjectDirectory(), "scripts");
                Templates.setTargetFolder(wizard, FileUtil.createFolder(scriptsFolder, "groovy"));
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            sourceGroups = new SourceGroup[0];
        }

        return Templates.buildSimpleTargetChooser(project, sourceGroups).create();
    }

    private List<WizardDescriptor.Panel<WizardDescriptor>> getPanels() {
        if (panels == null) {
            panels = new ArrayList<>();
            panels.add(createTargetChooser(wizard));
            String[] steps = createSteps();
            for (int i = 0; i < panels.size(); i++) {
                Component c = panels.get(i).getComponent();
                if (steps[i] == null) {
                    // Default step name to component name of panel. Mainly
                    // useful for getting the name of the target chooser to
                    // appear in the list of steps.
                    steps[i] = c.getName();
                }
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                    jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
                }
            }
        }
        return panels;
    }

    @Override
    public Set<?> instantiate() throws IOException {
        String n = Templates.getTargetName(wizard);
        DataFolder folder = DataFolder.findFolder(Templates.getTargetFolder(wizard));
        DataObject template = DataObject.find(Templates.getTemplate(wizard));
        Map<String, Object> wizardProps = new HashMap<>();
        for (Map.Entry<String, ? extends Object> entry : wizard.getProperties().entrySet()) {
            wizardProps.put("wizard." + entry.getKey(), entry.getValue()); // NOI18N
        }

        DataObject obj = template.createFromTemplate(folder, n, wizardProps);

        // run default action (hopefully should be here)
        final Node node = obj.getNodeDelegate();
        Action _a = node.getPreferredAction();
        if (_a instanceof ContextAwareAction) {
            _a = ((ContextAwareAction) _a).createContextAwareInstance(node.getLookup());
        }
        final Action a = _a;
        if (a != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    a.actionPerformed(new ActionEvent(node, ActionEvent.ACTION_PERFORMED, "")); // NOI18N
                }
            });
        }

        return Collections.singleton(obj);
    }

    @Override
    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
    }

    @Override
    public void uninitialize(WizardDescriptor wizard) {
        panels = null;
    }

    @Override
    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return getPanels().get(index);
    }

    @Override
    public String name() {
        return index + 1 + ". from " + getPanels().size();
    }

    @Override
    public boolean hasNext() {
        return index < getPanels().size() - 1;
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    @Override
    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    // If nothing unusual changes in the middle of the wizard, simply:
    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }
    // If something changes dynamically (besides moving between panels), e.g.
    // the number of panels changes in response to user input, then use
    // ChangeSupport to implement add/removeChangeListener and call fireChange
    // when needed

    // You could safely ignore this method. Is is here to keep steps which were
    // there before this wizard was instantiated. It should be better handled
    // by NetBeans Wizard API itself rather than needed to be implemented by a
    // client code.
    private String[] createSteps() {
        String[] beforeSteps = (String[]) wizard.getProperty("WizardPanel_contentData");
        assert beforeSteps != null : "This wizard may only be used embedded in the template wizard";
        String[] res = new String[(beforeSteps.length - 1) + panels.size()];
        for (int i = 0; i < res.length; i++) {
            if (i < (beforeSteps.length - 1)) {
                res[i] = beforeSteps[i];
            } else {
                res[i] = panels.get(i - beforeSteps.length + 1).getComponent().getName();
            }
        }
        return res;
    }

}
