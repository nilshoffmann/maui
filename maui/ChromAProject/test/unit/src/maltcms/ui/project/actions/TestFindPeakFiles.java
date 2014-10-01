package maltcms.ui.project.actions;

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
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author Nils Hoffmann
 */
public class TestFindPeakFiles {

    private final static String exampleWorkflow = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
"<?xml-stylesheet type=\"text/xsl\" href=\"http://maltcms.sourceforge.net/res/maltcmsHTMLResult.xsl\"?>\n" +
"<workflow class=\"cross.datastructures.workflow.DefaultWorkflow\" name=\"workflow\">\n" +
"  <workflowInputs>\n" +
"    <workflowInput uri=\"file:/home/hoffmann/Uni/data/ap/CDF/AP%20591.CDF\" />\n" +
"    <workflowInput uri=\"file:/home/hoffmann/Uni/data/ap/CDF/AP%20592.CDF\" />\n" +
"    <workflowInput uri=\"file:/home/hoffmann/Uni/data/ap/CDF/AP%20593.CDF\" />\n" +
"    <workflowInput uri=\"file:/home/hoffmann/Uni/data/ap/CDF/AP%20594.CDF\" />\n" +
"    <workflowInput uri=\"file:/home/hoffmann/Uni/data/ap/CDF/AP%20595.CDF\" />\n" +
"  </workflowInputs>\n" +
"  <workflowOutputs>\n" +
"    <workflowOutput uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20591.CDF\" />\n" +
"    <workflowOutput uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20592.CDF\" />\n" +
"    <workflowOutput uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20593.CDF\" />\n" +
"    <workflowOutput uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20594.CDF\" />\n" +
"    <workflowOutput uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20595.CDF\" />\n" +
"  </workflowOutputs>\n" +
"  <workflowCommands>\n" +
"    <workflowCommand class=\"maltcms.commands.fragments.io.ANDIChromImporter\" />\n" +
"    <workflowCommand class=\"maltcms.commands.fragments.preprocessing.ScanExtractor\" />\n" +
"    <workflowCommand class=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" />\n" +
"    <workflowCommand class=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" />\n" +
"    <workflowCommand class=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" />\n" +
"  </workflowCommands>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowStatisticsResult\" slot=\"STATISTICS\" generator=\"maltcms.commands.fragments.io.ANDIChromImporter\">\n" +
"    <statistics>\n" +
"      <item name=\"RUNTIME_SECONDS\" value=\"1.204813838005066\" />\n" +
"      <item name=\"RUNTIME_MILLISECONDS\" value=\"1.3585365454393E13\" />\n" +
"    </statistics>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowStatisticsResult\" slot=\"STATISTICS\" generator=\"maltcms.commands.fragments.preprocessing.ScanExtractor\">\n" +
"    <statistics>\n" +
"      <item name=\"RUNTIME_SECONDS\" value=\"0.03774873539805412\" />\n" +
"      <item name=\"RUNTIME_MILLISECONDS\" value=\"1.358564547811E13\" />\n" +
"    </statistics>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"GENERAL_PREPROCESSING\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP 591.CDF\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20591.CDF\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20591.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"GENERAL_PREPROCESSING\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP 592.CDF\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20592.CDF\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20592.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"GENERAL_PREPROCESSING\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP 593.CDF\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20593.CDF\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20593.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"GENERAL_PREPROCESSING\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP 594.CDF\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20594.CDF\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20594.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"GENERAL_PREPROCESSING\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP 595.CDF\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20595.CDF\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20595.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowStatisticsResult\" slot=\"STATISTICS\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\">\n" +
"    <statistics>\n" +
"      <item name=\"RUNTIME_SECONDS\" value=\"22.185771942138672\" />\n" +
"      <item name=\"RUNTIME_MILLISECONDS\" value=\"1.3608023406501E13\" />\n" +
"    </statistics>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"VISUALIZATION\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/combinedTICandPeakChart-AP-594.png\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/combinedTICandPeakChart-AP-594.png\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20594.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKFINDING\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP 594_peakAreas.csv\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20594_peakAreas.csv\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20594.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKFINDING\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP 594.maltcmsAnnotation.xml\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20594.maltcmsAnnotation.xml\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20594.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"VISUALIZATION\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/combinedTICandPeakChart-AP-593.png\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/combinedTICandPeakChart-AP-593.png\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20593.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKFINDING\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP 593_peakAreas.csv\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20593_peakAreas.csv\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20593.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKFINDING\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP 593.maltcmsAnnotation.xml\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20593.maltcmsAnnotation.xml\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20593.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"VISUALIZATION\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/combinedTICandPeakChart-AP-591.png\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/combinedTICandPeakChart-AP-591.png\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20591.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKFINDING\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP 591_peakAreas.csv\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20591_peakAreas.csv\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20591.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKFINDING\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP 591.maltcmsAnnotation.xml\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20591.maltcmsAnnotation.xml\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20591.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"VISUALIZATION\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/combinedTICandPeakChart-AP-592.png\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/combinedTICandPeakChart-AP-592.png\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20592.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKFINDING\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP 592_peakAreas.csv\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20592_peakAreas.csv\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20592.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKFINDING\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP 592.maltcmsAnnotation.xml\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20592.maltcmsAnnotation.xml\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20592.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"VISUALIZATION\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/combinedTICandPeakChart-AP-595.png\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/combinedTICandPeakChart-AP-595.png\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20595.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKFINDING\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP 595_peakAreas.csv\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20595_peakAreas.csv\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20595.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKFINDING\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP 595.maltcmsAnnotation.xml\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20595.maltcmsAnnotation.xml\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/02_DenseArrayProducer/AP%20595.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKFINDING\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP 591.CDF\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20591.CDF\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20591.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKFINDING\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP 592.CDF\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20592.CDF\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20592.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKFINDING\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP 593.CDF\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20593.CDF\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20593.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKFINDING\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP 594.CDF\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20594.CDF\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20594.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKFINDING\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP 595.CDF\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20595.CDF\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/03_TICPeakFinder/AP%20595.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowStatisticsResult\" slot=\"STATISTICS\" generator=\"maltcms.commands.fragments.peakfinding.TICPeakFinder\">\n" +
"    <statistics>\n" +
"      <item name=\"RUNTIME_SECONDS\" value=\"19.071500778198242\" />\n" +
"      <item name=\"RUNTIME_MILLISECONDS\" value=\"1.3627304999486E13\" />\n" +
"    </statistics>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"ALIGNMENT\" generator=\"maltcms.io.csv.CSVWriter\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/multiple-alignment.csv\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/multiple-alignment.csv\">\n" +
"    <resources />\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"ALIGNMENT\" generator=\"maltcms.io.csv.CSVWriter\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/multiple-alignmentRT.csv\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/multiple-alignmentRT.csv\">\n" +
"    <resources />\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"ALIGNMENT\" generator=\"maltcms.io.csv.CSVWriter\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/multiple-alignmentArea.csv\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/multiple-alignmentArea.csv\">\n" +
"    <resources />\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"ALIGNMENT\" generator=\"maltcms.io.csv.CSVWriter\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/multiple-alignmentAreaPercent.csv\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/multiple-alignmentAreaPercent.csv\">\n" +
"    <resources />\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"ALIGNMENT\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/peakCliqueAssignment.maltcmsAlignment.xml\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/peakCliqueAssignment.maltcmsAlignment.xml\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20591.CDF\" />\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20592.CDF\" />\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20593.CDF\" />\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20594.CDF\" />\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20595.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"STATISTICS\" generator=\"maltcms.io.csv.CSVWriter\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/pairwise_distances.csv\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/pairwise_distances.csv\">\n" +
"    <resources />\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"STATISTICS\" generator=\"maltcms.io.csv.CSVWriter\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/pairwise_distances.csv\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/pairwise_distances.csv\">\n" +
"    <resources />\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"CLUSTERING\" generator=\"maltcms.io.csv.CSVWriter\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/center-star.csv\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/center-star.csv\">\n" +
"    <resources />\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKMATCHING\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP 591.CDF\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20591.CDF\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20591.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKMATCHING\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP 592.CDF\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20592.CDF\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20592.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKMATCHING\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP 593.CDF\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20593.CDF\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20593.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKMATCHING\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP 594.CDF\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20594.CDF\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20594.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKMATCHING\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP 595.CDF\" file-uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20595.CDF\">\n" +
"    <resources>\n" +
"      <resource uri=\"file:/opt/projects/sf/maltcms/maltcms-ap-distribution/target/maltcms-ap-1.3.1.RC1-bin/maltcms-ap-1.3.1.RC1/bin/ap-output/ap/hoffmann/09-25-2014_19-44-48/04_PeakCliqueAlignment/AP%20595.CDF\" />\n" +
"    </resources>\n" +
"  </workflowElementResult>\n" +
"  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowStatisticsResult\" slot=\"STATISTICS\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\">\n" +
"    <statistics>\n" +
"      <item name=\"RUNTIME_SECONDS\" value=\"1.9860029220581055\" />\n" +
"      <item name=\"RUNTIME_MILLISECONDS\" value=\"1.3629527178192E13\" />\n" +
"    </statistics>\n" +
"  </workflowElementResult>\n" +
"</workflow>";

    @Test
    public void testGetAlignmentFile() throws Exception {
        //          <workflowElementResult class="cross.datastructures.workflow.DefaultWorkflowResult" slot="ALIGNMENT" generator="maltcms.io.csv.CSVWriter" file="/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/multiple-alignment.csv" file-uri="file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/multiple-alignment.csv">
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);
        XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = "/workflow/workflowElementResult[@slot='PEAKFINDING'][contains(@file-uri,'.cdf') or contains(@file-uri,'.CDF')]/@file-uri";
        StringReader sr = new StringReader(exampleWorkflow);
        InputSource inputSource = new InputSource(sr);
        NodeList nodes = (NodeList) xpath.compile(expression).evaluate(inputSource, XPathConstants.NODESET);
        Assert.assertEquals(5, nodes.getLength());
        for (int i = 0; i < nodes.getLength(); i++) {
            Node currentItem = nodes.item(i);
            String key = currentItem.getTextContent();
            Assert.assertTrue(key.toLowerCase().endsWith(".cdf"));
        }
    }
}
