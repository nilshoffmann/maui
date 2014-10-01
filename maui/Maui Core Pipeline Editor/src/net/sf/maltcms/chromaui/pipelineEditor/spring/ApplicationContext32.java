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
package net.sf.maltcms.chromaui.pipelineEditor.spring;

import cross.commands.fragments.IFragmentCommand;
import cross.tools.StringTools;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import maltcms.io.xml.bindings.spring.beans.XMLBeanContextFactory32;
import maltcms.io.xml.bindings.spring.beans.beans32.Bean;
import maltcms.io.xml.bindings.spring.beans.beans32.Beans;
import maltcms.io.xml.bindings.spring.beans.beans32.ConstructorArg;
import maltcms.io.xml.bindings.spring.beans.beans32.DefaultableBoolean;
import maltcms.io.xml.bindings.spring.beans.beans32.Description;
import maltcms.io.xml.bindings.spring.beans.beans32.MetaType;
import maltcms.io.xml.bindings.spring.beans.beans32.Property;
import org.openide.util.Lookup;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.instrument.classloading.SimpleThrowawayClassLoader;

/**
 *
 * @author Nils Hoffmann
 */
public class ApplicationContext32 {

    private Beans beans;

    public ApplicationContext32() {
        beans = new Beans();
    }

    public Beans getBeans() {
        return beans;
    }

    public void load(File inputFile) {
        XMLBeanContextFactory32 xf = new XMLBeanContextFactory32();
        beans = xf.load(inputFile);
    }

    public void save(File outputFile) {
        XMLBeanContextFactory32 xf = new XMLBeanContextFactory32();
        File outputDir = outputFile.getParentFile();
        outputDir.mkdirs();
        xf.save(beans, outputFile);
    }

    public ClassPathScanningCandidateComponentProvider getComponentProvider(String packageName, URL...urls) {
        ClassPathScanningCandidateComponentProvider scanner
                = new ClassPathScanningCandidateComponentProvider(true);
        URLClassLoader ucl = new URLClassLoader(urls, Lookup.getDefault().lookup(ClassLoader.class));
        SimpleThrowawayClassLoader stcl = new SimpleThrowawayClassLoader(ucl);
        DefaultResourceLoader drl = new DefaultResourceLoader(stcl);
        SimpleMetadataReaderFactory smrf = new SimpleMetadataReaderFactory(drl);
        scanner.setResourceLoader(drl);
        scanner.setMetadataReaderFactory(smrf);
        return scanner;
    }
    
    public Set<BeanDefinition> getBeanDefinitionsFromPackageScan(String packageName, URL...urls) {
        return getComponentProvider(packageName, urls).findCandidateComponents(packageName);
    }

    public Set<BeanDefinition> getFragmentCommandBeans(String packageName, URL...urls) {
        ClassPathScanningCandidateComponentProvider scanner
                = getComponentProvider(packageName, urls);
        scanner.addIncludeFilter(new AssignableTypeFilter(IFragmentCommand.class));
        return scanner.findCandidateComponents(packageName);
    }

    public Bean getBean(String beanId) {
        for (Object impAliasOrBean : beans.getImportsAndAliasAndBeen()) {
            if (impAliasOrBean instanceof Bean) {
                Bean bean = (Bean) impAliasOrBean;
                if (bean.getId().equals(beanId)) {
                    return bean;
                }
            }
        }
        return null;
    }

    public Set<String> getReferencedBeanIds(String beanId) {
        Set<String> referencedBeanIds = new LinkedHashSet<>();
        Bean bean = getBean(beanId);
        if (bean != null) {
            for (Property prop : getProperties(bean)) {
                if (prop.getRef() != null && !prop.getRef().isEmpty()) {
                    referencedBeanIds.add(prop.getRef());
                }
            }
        }
        return referencedBeanIds;
    }

    public List<Property> getProperties(Bean bean) {
        List<Property> properties = new ArrayList<>();
        List<Object> metasConstArgsProps = bean.getMetasAndConstructorArgsAndProperties();
        for (Object obj : metasConstArgsProps) {
            if (obj instanceof Property) {
                properties.add((Property) obj);
            }
        }
        return properties;
    }

    public List<ConstructorArg> getConstructorArgs(Bean bean) {
        List<ConstructorArg> constructorArgs = new ArrayList<>();
        List<Object> metasConstArgsProps = bean.getMetasAndConstructorArgsAndProperties();
        for (Object obj : metasConstArgsProps) {
            if (obj instanceof ConstructorArg) {
                constructorArgs.add((ConstructorArg) obj);
            }
        }
        return constructorArgs;
    }

    public List<MetaType> getMetaTypes(Bean bean) {
        List<MetaType> metaTypes = new ArrayList<>();
        List<Object> metasConstArgsProps = bean.getMetasAndConstructorArgsAndProperties();
        for (Object obj : metasConstArgsProps) {
            if (obj instanceof MetaType) {
                metaTypes.add((MetaType) obj);
            }
        }
        return metaTypes;
    }

    public void addAsBean(BeanDefinition beanDefinition, String beanId) {
        Bean bean = new Bean();
        bean.setAbstract(beanDefinition.isAbstract());
        bean.setAutowireCandidate(DefaultableBoolean.valueOf(Boolean.toString(beanDefinition.isAutowireCandidate()).toUpperCase()));
        bean.setClazz(beanDefinition.getBeanClassName());
        bean.setScope(beanDefinition.getScope());
        if (beanDefinition.getDependsOn() != null) {
            bean.setDependsOn(StringTools.join(beanDefinition.getDependsOn(), ","));
        }
        Description d = new Description();
        bean.setFactoryBean(beanDefinition.getFactoryBeanName());
        bean.setFactoryMethod(beanDefinition.getFactoryMethodName());
        bean.setLazyInit(DefaultableBoolean.valueOf(Boolean.toString(beanDefinition.isLazyInit()).toUpperCase()));
        bean.setId(beanId);//bd.getBeanClassName().substring(bd.getBeanClassName().lastIndexOf(".") + 1));
        bean.setParent(beanDefinition.getParentName());
        bean.setPrimary(beanDefinition.isPrimary());
        bean.setScope(beanDefinition.getScope().isEmpty() ? "singleton" : beanDefinition.getScope());
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        String[] blackListedPropertyNames = {"class", "cvResolver", "description", "workflowSlot"};
        Arrays.sort(blackListedPropertyNames);
        try {
            Object beanObject = Class.forName(beanDefinition.getBeanClassName()).newInstance();
            BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(beanObject);
            for (PropertyDescriptor pv : bw.getPropertyDescriptors()) {
                if (Arrays.binarySearch(blackListedPropertyNames, pv.getName()) < 0) {
                    System.out.println("Adding property: " + pv.getName());
//                    PropertyDescriptor pd = bwi.getPropertyDescriptor(pv.getName());
                    Property p = new Property();
                    p.setName(pv.getName());
                    Object value = bw.getPropertyValue(pv.getName());
                    if (value != null) {
                        p.setValue(value.toString());
                        bean.getMetasAndConstructorArgsAndProperties().add(p);
                    }
//                    } else if (pv.getName().equals("workflowSlot")) {
//                        System.out.println("Adding Workflow Slot!");
//                        d.getContent().add("Slot: "+bw.getPropertyValue("workflowSlot").toString()+"; Description: ");
                } else if (pv.getName().equals("description")) {
                    System.out.println("Adding description!");
                    d.getContent().add(bw.getPropertyValue("description").toString());
                }
            }
            bean.setDescription(d);
            beans.getImportsAndAliasAndBeen().add(bean);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ApplicationContext32.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ApplicationContext32.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ApplicationContext32.class.getName()).log(Level.SEVERE, null, ex);
        } catch (org.springframework.beans.InvalidPropertyException ex) {
            Logger.getLogger(ApplicationContext32.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//                  ClassPathScanningCandidateComponentProvider scanner
//                = new ClassPathScanningCandidateComponentProvider(true);
//        scanner.addIncludeFilter(new AnnotationTypeFilter(Configurable.class));
//        scanner.addIncludeFilter(new AssignableTypeFilter(IFragmentCommand.class));
//        scanner.addIncludeFilter(new AssignableTypeFilter(Callable.class));
//
//        for (BeanDefinition bd
//                : scanner.findCandidateComponents("cross")) {
//            System.out.println(bd.getBeanClassName());
//        }
//        Beans beans = new Beans();
//        for (BeanDefinition bd
//                : scanner.findCandidateComponents("maltcms")) {
//            System.out.println(bd.getBeanClassName());
//            
////                BeanWrapperImpl bwi = new BeanWrapperImpl(Class.forName(bd.getBeanClassName()));
//
//        }
}
