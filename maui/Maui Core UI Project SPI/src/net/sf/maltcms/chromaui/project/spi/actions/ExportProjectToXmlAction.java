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
package net.sf.maltcms.chromaui.project.spi.actions;

import com.db4o.collections.ActivatableArrayList;
import com.db4o.collections.ActivatableMap;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.converters.javabean.BeanProvider;
import com.thoughtworks.xstream.converters.javabean.JavaBeanConverter;
import com.thoughtworks.xstream.converters.javabean.JavaBeanConverter.DuplicatePropertyException;
import com.thoughtworks.xstream.converters.javabean.JavaBeanProvider;
import com.thoughtworks.xstream.converters.reflection.MissingFieldException;
import com.thoughtworks.xstream.converters.reflection.SunLimitedUnsafeReflectionProvider;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.core.util.FastField;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.mapper.Mapper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.IContainer;
import net.sf.maltcms.chromaui.project.spi.descriptors.AnovaDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.ChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.NormalizationDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.PcaDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.Peak2DAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.PeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.PeakGroupDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.SampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.ToolDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.TreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.UserDatabaseDescriptor;
import net.sf.maltcms.chromaui.project.spi.project.ChromAUIProject;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.netbeans.api.project.ProjectUtils;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(category = "Maui",
        id = "net.sf.maltcms.chromaui.project.spi.actions.ExportProjectToXmlAction")
@ActionRegistration(displayName = "#CTL_ExportProjectToXmlAction")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1415)})
@Messages("CTL_ExportProjectToXmlAction=Export to XML")
@Log
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
                final XStream xstream = new XStream(new SunLimitedUnsafeReflectionProvider(), new StaxDriver(), clr);
                xstream.setMode(XStream.ID_REFERENCES);
                Converter c = new JavaBeanConverter(xstream.getMapper(), IChromAUIProject.class) {
                    @Override
                    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mc) {
                        IChromAUIProject project = (IChromAUIProject) o;
                        log.log(Level.INFO, "Marshalling project");
                        writer.startNode("project");
                        writer.addAttribute("location", project.getLocation().toURI().toString());
                        writer.addAttribute("displayName", ProjectUtils.getInformation(project).getDisplayName());
                        writer.addAttribute("name", ProjectUtils.getInformation(project).getName());
                        writer.addAttribute("importDirectory", Utilities.toURI(project.getImportDirectory()).toString());
                        writer.addAttribute("outputDirectory", Utilities.toURI(project.getOutputDirectory()).toString());
                        for (IContainer item : project.getContainer(IContainer.class)) {
                            if (item == null) {
                                String name = xstream.getMapper().serializedClass(null);
                                ExtendedHierarchicalStreamWriterHelper.startNode(writer, name, Mapper.Null.class);
                                writer.endNode();
                            } else {
                                String name = xstream.getMapper().serializedClass(item.getClass());
                                ExtendedHierarchicalStreamWriterHelper.startNode(writer, name, item.getClass());
                                mc.convertAnother(item);
                                writer.endNode();
                            }
                        }
                        writer.endNode();
                    }

                    @Override
                    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public boolean canConvert(Class type) {
                        boolean canConvert = (type.equals(IChromAUIProject.class) || type.equals(ChromAUIProject.class));
                        log.log(Level.INFO, "Can convert class {0}? {1}", new Object[]{type, canConvert});
                        return canConvert;
                    }

                };
                xstream.registerConverter(c);
                xstream.registerConverter(new JavaBeanConverter(xstream.getMapper(), TreatmentGroupDescriptor.class));
                xstream.registerConverter(new JavaBeanConverter(xstream.getMapper(), SampleGroupDescriptor.class));
                xstream.registerConverter(new ContainerConverter(xstream.getMapper(), ChromatogramDescriptor.class));
                xstream.registerConverter(new JavaBeanConverter(xstream.getMapper(), PeakAnnotationDescriptor.class));
                xstream.registerConverter(new JavaBeanConverter(xstream.getMapper(), Peak2DAnnotationDescriptor.class));
                xstream.registerConverter(new JavaBeanConverter(xstream.getMapper(), PeakGroupDescriptor.class));
                xstream.registerConverter(new JavaBeanConverter(xstream.getMapper(), AnovaDescriptor.class));
                xstream.registerConverter(new JavaBeanConverter(xstream.getMapper(), PcaDescriptor.class));
                xstream.registerConverter(new JavaBeanConverter(xstream.getMapper(), UserDatabaseDescriptor.class));
                xstream.registerConverter(new JavaBeanConverter(xstream.getMapper(), NormalizationDescriptor.class));
                xstream.registerConverter(new JavaBeanConverter(xstream.getMapper(), ToolDescriptor.class));
                xstream.registerConverter(new ContainerConverter(xstream.getMapper(), IContainer.class), -20);

                CollectionConverter cc = new CollectionConverter(xstream.getMapper(), com.db4o.collections.ActivatableCollection.class) {
                    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
                        Collection collection = (Collection) source;
                        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                            Object item = iterator.next();
                            writeItem(item, context, writer);
                        }
                    }
                };
                xstream.registerConverter(cc, -20);
                CollectionConverter lc = new CollectionConverter(xstream.getMapper(), com.db4o.collections.ActivatableArrayList.class) {
                    @Override
                    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
                        log.info("Marshalling activatable array list");
                        ActivatableArrayList collection = (ActivatableArrayList) source;
                        for (int i = 0; i < collection.size(); i++) {
                            Object item = collection.get(i);
                            writeItem(item, context, writer);
                        }
                    }
                };
                xstream.registerConverter(lc, -20);
                MapConverter mc = new MapConverter(xstream.getMapper(), com.db4o.collections.ActivatableMap.class) {
                    @Override
                    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
                        log.info("Marshalling activatable map");
                        ActivatableMap map = (ActivatableMap) source;
                        String entryName = mapper().serializedClass(ActivatableMap.Entry.class);
                        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
                            ActivatableMap.Entry entry = (ActivatableMap.Entry) iterator.next();
                            ExtendedHierarchicalStreamWriterHelper.startNode(writer, entryName, entry.getClass());

                            writeItem(entry.getKey(), context, writer);
                            writeItem(entry.getValue(), context, writer);

                            writer.endNode();
                        }
                    }
                };
                xstream.registerConverter(mc, -20);
                File outputFile = new File(FileUtil.toFile(context.getProjectDirectory()), "project-export.xml.gz");
                OutputStream fos = null;
                try {
                    fos = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(outputFile)));
                    xstream.marshal(context, new PrettyPrintWriter(new OutputStreamWriter(fos)));
                    fos.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException ex) {
                            Exceptions.printStackTrace(ex);
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

    private class ContainerConverter implements Converter {

        /*
         * TODO:
         *  - support indexed properties
         *  - support attributes (XSTR-620)
         *  - support local converters (XSTR-601)
         *  Problem: Mappers take definitions based on reflection, they don't know about bean info
         */
        protected final Mapper mapper;
        protected final JavaBeanProvider beanProvider;
        private final Class type;

        /**
         * @deprecated As of 1.3, no necessity for field anymore.
         */
        private String classAttributeIdentifier;

        public ContainerConverter(Mapper mapper) {
            this(mapper, (Class) null);
        }

        public ContainerConverter(Mapper mapper, Class type) {
            this(mapper, new BeanProvider(), type);
        }

        public ContainerConverter(Mapper mapper, JavaBeanProvider beanProvider) {
            this(mapper, beanProvider, null);
        }

        public ContainerConverter(Mapper mapper, JavaBeanProvider beanProvider, Class type) {
            this.mapper = mapper;
            this.beanProvider = beanProvider;
            this.type = type;
        }

        /**
         * @deprecated As of 1.3, use {@link #JavaBeanConverter(Mapper)} and
         * {@link com.thoughtworks.xstream.XStream#aliasAttribute(String, String)}
         */
        public ContainerConverter(Mapper mapper, String classAttributeIdentifier) {
            this(mapper, new BeanProvider());
            this.classAttributeIdentifier = classAttributeIdentifier;
        }

        /**
         * Checks if the bean provider can instantiate this type. If you need
         * less strict checks, subclass JavaBeanConverter
         */
        public boolean canConvert(Class type) {
            return (this.type == null || this.type == type);// && beanProvider.canInstantiate(type);
        }

        public void marshal(final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context) {
            final String classAttributeName = classAttributeIdentifier != null ? classAttributeIdentifier : mapper.aliasForSystemAttribute("class");
            beanProvider.visitSerializableProperties(source, new JavaBeanProvider.Visitor() {
                public boolean shouldVisit(String name, Class definedIn) {
                    return mapper.shouldSerializeMember(definedIn, name) || name.equals("members");
                }

                public void visit(String propertyName, Class fieldType, Class definedIn, Object newObj) {
                    if (newObj != null) {
                        writeField(propertyName, fieldType, newObj, definedIn);
                    }
                }

                private void writeField(String propertyName, Class fieldType, Object newObj, Class definedIn) {
                    Class actualType = newObj.getClass();
                    Class defaultType = mapper.defaultImplementationOf(fieldType);
                    String serializedMember = mapper.serializedMember(source.getClass(), propertyName);
                    ExtendedHierarchicalStreamWriterHelper.startNode(writer, serializedMember, actualType);
                    if (!actualType.equals(defaultType) && classAttributeName != null) {
                        writer.addAttribute(classAttributeName, mapper.serializedClass(actualType));
                    }
                    context.convertAnother(newObj);

                    writer.endNode();
                }
            });
        }

        public Object unmarshal(final HierarchicalStreamReader reader, final UnmarshallingContext context) {
            final Object result = instantiateNewInstance(context);
            final Set seenProperties = new HashSet() {
                public boolean add(Object e) {
                    if (!super.add(e)) {
                        throw new DuplicatePropertyException(((FastField) e).getName());
                    }
                    return true;
                }
            };

            Class resultType = result.getClass();
            while (reader.hasMoreChildren()) {
                reader.moveDown();

                String propertyName = mapper.realMember(resultType, reader.getNodeName());

                if (mapper.shouldSerializeMember(resultType, propertyName)) {
                    boolean propertyExistsInClass = beanProvider.propertyDefinedInClass(propertyName, resultType);

                    if (propertyExistsInClass) {
                        Class type = determineType(reader, result, propertyName);
                        Object value = context.convertAnother(result, type);
                        beanProvider.writeProperty(result, propertyName, value);
                        seenProperties.add(new FastField(resultType, propertyName));
                    } else {
                        throw new MissingFieldException(resultType.getName(), propertyName);
                    }
                }
                reader.moveUp();
            }

            return result;
        }

        private Object instantiateNewInstance(UnmarshallingContext context) {
            Object result = context.currentObject();
            if (result == null) {
                result = beanProvider.newInstance(context.getRequiredType());
            }
            return result;
        }

        private Class determineType(HierarchicalStreamReader reader, Object result, String fieldName) {
            final String classAttributeName = classAttributeIdentifier != null ? classAttributeIdentifier : mapper.aliasForSystemAttribute("class");
            String classAttribute = classAttributeName == null ? null : reader.getAttribute(classAttributeName);
            if (classAttribute != null) {
                return mapper.realClass(classAttribute);
            } else {
                return mapper.defaultImplementationOf(beanProvider.getPropertyType(result, fieldName));
            }

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
