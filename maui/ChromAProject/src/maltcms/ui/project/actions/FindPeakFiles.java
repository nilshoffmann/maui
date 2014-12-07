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
package maltcms.ui.project.actions;

import net.sf.maltcms.chromaui.project.api.extimport.IFileResultFinder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import maltcms.ui.project.tasks.MaltcmsPeakFinderImporter;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.extimport.FileBasedToolResultDescriptor;
import net.sf.maltcms.chromaui.project.api.extimport.IFileBasedToolResultDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;
import org.openide.util.lookup.ServiceProvider;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author Nils Hoffmann
 */
@ServiceProvider(service=IFileResultFinder.class)
public class FindPeakFiles implements IFileResultFinder {
    public File[] getResults(File maltcmsProjectDirectory) {
        File workflowXml = new File(maltcmsProjectDirectory, "workflow.xml");
        if (workflowXml.exists() && workflowXml.canRead()) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(false);
            XPath xpath = XPathFactory.newInstance().newXPath();
            String expression = "/workflow/workflowElementResult[@slot='PEAKFINDING'][contains(@file-uri,'.cdf') or contains(@file-uri,'.CDF')]/@file-uri";
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(workflowXml))) {
                InputSource inputSource = new InputSource(bis);
                NodeList nodes = (NodeList) xpath.compile(expression).evaluate(inputSource, XPathConstants.NODESET);
                File[] files = new File[nodes.getLength()];
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node currentItem = nodes.item(i);
                    String key = currentItem.getTextContent();
                    files[i] = Utilities.toFile(URI.create(key));
                }
                return files;
            } catch (IOException | XPathExpressionException ex) {
                Exceptions.printStackTrace(ex);
                return new File[0];
            }
        } else {
            Logger.getLogger(FindPeakFiles.class.getName()).log(Level.WARNING, "Could not locate 'workflow.xml' file in directory {0}", maltcmsProjectDirectory);
            return new File[0];
        }
    }
    
    @Override
    public IFileBasedToolResultDescriptor createDescriptor(IChromAUIProject project, File outputDirectory) {
        IFileBasedToolResultDescriptor descr = new FileBasedToolResultDescriptor();
        descr.setOutputDirectory(outputDirectory);
        descr.setProject(project);
        descr.setName("findPeakFiles");
        descr.setDisplayName("Import Peak Reports");
        descr.setProgressAwareRunnable(
            new MaltcmsPeakFinderImporter(
                project,
                getResults(outputDirectory)
            )
        );
        return descr;
    }
}
