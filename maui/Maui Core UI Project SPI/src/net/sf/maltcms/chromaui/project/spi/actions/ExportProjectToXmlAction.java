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
package net.sf.maltcms.chromaui.project.spi.actions;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.converters.javabean.JavaBeanConverter;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.IMauiProject;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IBasicDescriptor;
import net.sf.maltcms.chromaui.project.spi.project.ChromAUIProject;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.netbeans.api.project.ProjectUtils;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Maui",
        id = "net.sf.maltcms.chromaui.project.spi.actions.ExportProjectToXmlAction")
@ActionRegistration(displayName = "#CTL_ExportProjectToXmlAction")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1415)})
@Messages("CTL_ExportProjectToXmlAction=Export to XML")
public final class ExportProjectToXmlAction implements ActionListener {
    
    private final IChromAUIProject context;
    
    public ExportProjectToXmlAction(IChromAUIProject context) {
        this.context = context;
    }
    
    @RequiredArgsConstructor
    private class XMLExportTask extends AProgressAwareRunnable {
        
        private final IChromAUIProject context;
        
        @Override
        public void run() {
            try {
                getProgressHandle().start();
                getProgressHandle().setDisplayName("Exporting project to XML");
                ClassLoaderReference clr = new ClassLoaderReference(Lookup.getDefault().lookup(ClassLoader.class));
                final XStream xstream = new XStream(new PureJavaReflectionProvider(), new StaxDriver(), clr);
//                xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
//                Converter c = new Converter() {
//                    @Override
//                    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mc) {
//                        IChromAUIProject project = (IChromAUIProject) o;
//                        writer.startNode("project");
//                        writer.addAttribute("location", project.getLocation().toURI().toString());
//                        writer.addAttribute("displayName", ProjectUtils.getInformation(project).getDisplayName());
//                        writer.addAttribute("name", ProjectUtils.getInformation(project).getName());
//                        writer.addAttribute("importDirectory", Utilities.toURI(project.getImportDirectory()).toString());
//                        writer.addAttribute("outputDirectory", Utilities.toURI(project.getOutputDirectory()).toString());
//                        for (IContainer item : project.getContainer(IContainer.class)) {
//                            if (item == null) {
//                                String name = xstream.getMapper().serializedClass(null);
//                                ExtendedHierarchicalStreamWriterHelper.startNode(writer, name, Mapper.Null.class);
//                                writer.endNode();
//                            } else {
//                                String name = xstream.getMapper().serializedClass(item.getClass());
//                                ExtendedHierarchicalStreamWriterHelper.startNode(writer, name, item.getClass());
//                                mc.convertAnother(item);
//                                writer.endNode();
//                            }
//                        }
//                        writer.endNode();
//                    }
//
//                    @Override
//                    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
//                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                    }
//
//                    @Override
//                    public boolean canConvert(Class type) {
//                        return (type.equals(IChromAUIProject.class));
//                    }
//
//                };
//                xstream.registerConverter(c, XStream.PRIORITY_VERY_HIGH);
                JavaBeanConverter descriptorConverter = new JavaBeanConverter(xstream.getMapper(), IBasicDescriptor.class);
                xstream.registerConverter(descriptorConverter, XStream.PRIORITY_VERY_HIGH);
                CollectionConverter cc = new CollectionConverter(xstream.getMapper(), com.db4o.collections.ActivatableCollection.class);
                xstream.registerConverter(cc, XStream.PRIORITY_VERY_HIGH);
                xstream.allowTypesByWildcard(new String[]{"net.sf.maltcms.chromaui.project.api.container.*", "net.sf.maltcms.chromaui.project.api.descriptors.*", "net.sf.maltcms.chromaui.project.api.types.*", "maltcms.datastructures.*", "de.unibielefeld.*"});
                xstream.denyTypesByWildcard(new String[]{"org.openide.**", "org.netbeans.**", "javax.**", "java.awt.**", "com.db4o.**"});
                xstream.denyTypes(new Class[]{ChromAUIProject.class, IChromAUIProject.class, IMauiProject.class});
                xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
                File outputFile = new File(FileUtil.toFile(context.getProjectDirectory()), "project-export.xml");
                OutputStream fos = null;
                try {
                    fos = new BufferedOutputStream(new FileOutputStream(outputFile));
                    xstream.marshal(new ChromAUIProjectAdapter(context), new PrettyPrintWriter(new OutputStreamWriter(fos)));
                    fos.close();
                } catch (IOException ex) {
//                    Exceptions.printStackTrace(ex);
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException ex) {
//                            Exceptions.printStackTrace(ex);
                        }
                    }
                }
                getProgressHandle().progress("Done!");
            } catch (Exception e) {
            } finally {
                getProgressHandle().finish();
            }
        }
    }
    
    @Getter
    @Setter
    public class ChromAUIProjectAdapter {
        
        private Collection<IContainer> containers;
        private File outputDirectory;
        private File importDirectory;
        private String name;
        private String displayName;
        
        public ChromAUIProjectAdapter(IChromAUIProject project) {
            setContainers(project.getContainer(IContainer.class));
            setOutputDirectory(project.getOutputDirectory());
            setImportDirectory(project.getImportDirectory());
            setName(ProjectUtils.getInformation(project).getName());
            setDisplayName(ProjectUtils.getInformation(project).getDisplayName());
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent ev) {
        if (context != null) {
            XMLExportTask task = new XMLExportTask(context);
            XMLExportTask.createAndRun("Exporting project to xml", task);
        }
    }
}
