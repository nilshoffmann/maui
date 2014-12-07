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
public class TestFindMultipleAlignment {

    private final static String exampleWorkflow = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<?xml-stylesheet type=\"text/xsl\" href=\"http://maltcms.sourceforge.net/res/maltcmsHTMLResult.xsl\"?>\n"
            + "<workflow class=\"cross.datastructures.workflow.DefaultWorkflow\" name=\"workflow\">\n"
            + "  <workflowInputs>\n"
            + "    <workflowInput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/data/mut_t1_a.cdf\" />\n"
            + "    <workflowInput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/data/mut_t1_b.cdf\" />\n"
            + "    <workflowInput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/data/mut_t1_c.cdf\" />\n"
            + "    <workflowInput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/data/mut_t2_a.cdf\" />\n"
            + "    <workflowInput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/data/mut_t2_b.cdf\" />\n"
            + "    <workflowInput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/data/mut_t2_c.cdf\" />\n"
            + "    <workflowInput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/data/wt_t1_a.cdf\" />\n"
            + "    <workflowInput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/data/wt_t1_b.cdf\" />\n"
            + "    <workflowInput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/data/wt_t1_c.cdf\" />\n"
            + "    <workflowInput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/data/wt_t2_a.cdf\" />\n"
            + "    <workflowInput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/data/wt_t2_b.cdf\" />\n"
            + "    <workflowInput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/data/wt_t2_c.cdf\" />\n"
            + "  </workflowInputs>\n"
            + "  <workflowOutputs>\n"
            + "    <workflowOutput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t1_a.cdf\" />\n"
            + "    <workflowOutput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t1_b.cdf\" />\n"
            + "    <workflowOutput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t1_c.cdf\" />\n"
            + "    <workflowOutput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t2_a.cdf\" />\n"
            + "    <workflowOutput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t2_b.cdf\" />\n"
            + "    <workflowOutput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t2_c.cdf\" />\n"
            + "    <workflowOutput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t1_a.cdf\" />\n"
            + "    <workflowOutput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t1_b.cdf\" />\n"
            + "    <workflowOutput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t1_c.cdf\" />\n"
            + "    <workflowOutput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t2_a.cdf\" />\n"
            + "    <workflowOutput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t2_b.cdf\" />\n"
            + "    <workflowOutput uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t2_c.cdf\" />\n"
            + "  </workflowOutputs>\n"
            + "  <workflowCommands>\n"
            + "    <workflowCommand class=\"maltcms.commands.fragments.io.CSVAnchorReader\" />\n"
            + "    <workflowCommand class=\"maltcms.commands.fragments.preprocessing.DefaultVarLoader\" />\n"
            + "    <workflowCommand class=\"maltcms.commands.fragments.preprocessing.MassFilter\" />\n"
            + "    <workflowCommand class=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" />\n"
            + "    <workflowCommand class=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" />\n"
            + "  </workflowCommands>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowStatisticsResult\" slot=\"STATISTICS\" generator=\"maltcms.commands.fragments.io.CSVAnchorReader\">\n"
            + "    <statistics>\n"
            + "      <item name=\"RUNTIME_MILLISECONDS\" value=\"0.45849698781967163\" />\n"
            + "    </statistics>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowStatisticsResult\" slot=\"STATISTICS\" generator=\"maltcms.commands.fragments.preprocessing.DefaultVarLoader\">\n"
            + "    <statistics>\n"
            + "      <item name=\"RUNTIME_MILLISECONDS\" value=\"0.2792240083217621\" />\n"
            + "    </statistics>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowStatisticsResult\" slot=\"STATISTICS\" generator=\"maltcms.commands.fragments.preprocessing.MassFilter\">\n"
            + "    <statistics>\n"
            + "      <item name=\"RUNTIME_MILLISECONDS\" value=\"116.66273498535156\" />\n"
            + "    </statistics>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"GENERAL_PREPROCESSING\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t1_a.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t1_a.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t1_a.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"GENERAL_PREPROCESSING\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t1_b.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t1_b.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t1_b.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"GENERAL_PREPROCESSING\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t1_c.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t1_c.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t1_c.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"GENERAL_PREPROCESSING\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t2_a.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t2_a.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t2_a.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"GENERAL_PREPROCESSING\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t2_b.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t2_b.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t2_b.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"GENERAL_PREPROCESSING\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t2_c.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t2_c.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/mut_t2_c.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"GENERAL_PREPROCESSING\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t1_a.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t1_a.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t1_a.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"GENERAL_PREPROCESSING\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t1_b.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t1_b.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t1_b.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"GENERAL_PREPROCESSING\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t1_c.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t1_c.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t1_c.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"GENERAL_PREPROCESSING\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t2_a.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t2_a.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t2_a.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"GENERAL_PREPROCESSING\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t2_b.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t2_b.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t2_b.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"GENERAL_PREPROCESSING\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t2_c.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t2_c.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/03_DenseArrayProducer/wt_t2_c.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowStatisticsResult\" slot=\"STATISTICS\" generator=\"maltcms.commands.fragments.preprocessing.DenseArrayProducer\">\n"
            + "    <statistics>\n"
            + "      <item name=\"RUNTIME_MILLISECONDS\" value=\"3057.882080078125\" />\n"
            + "    </statistics>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"ALIGNMENT\" generator=\"maltcms.io.csv.CSVWriter\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/multiple-alignment.csv\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/multiple-alignment.csv\">\n"
            + "    <resources />\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"ALIGNMENT\" generator=\"maltcms.io.csv.CSVWriter\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/multiple-alignmentRT.csv\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/multiple-alignmentRT.csv\">\n"
            + "    <resources />\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"ALIGNMENT\" generator=\"maltcms.io.csv.CSVWriter\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/multiple-alignmentArea.csv\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/multiple-alignmentArea.csv\">\n"
            + "    <resources />\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"ALIGNMENT\" generator=\"maltcms.io.csv.CSVWriter\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/multiple-alignmentAreaPercent.csv\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/multiple-alignmentAreaPercent.csv\">\n"
            + "    <resources />\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"ALIGNMENT\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/peakCliqueAssignment.maltcmsAlignment.xml\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/peakCliqueAssignment.maltcmsAlignment.xml\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t1_a.cdf\" />\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t1_b.cdf\" />\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t1_c.cdf\" />\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t2_a.cdf\" />\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t2_b.cdf\" />\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t2_c.cdf\" />\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t1_a.cdf\" />\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t1_b.cdf\" />\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t1_c.cdf\" />\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t2_a.cdf\" />\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t2_b.cdf\" />\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t2_c.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"STATISTICS\" generator=\"maltcms.io.csv.CSVWriter\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/pairwise_distances.csv\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/pairwise_distances.csv\">\n"
            + "    <resources />\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"STATISTICS\" generator=\"maltcms.io.csv.CSVWriter\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/pairwise_distances.csv\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/pairwise_distances.csv\">\n"
            + "    <resources />\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"CLUSTERING\" generator=\"maltcms.io.csv.CSVWriter\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/center-star.csv\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/center-star.csv\">\n"
            + "    <resources />\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKMATCHING\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t1_a.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t1_a.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t1_a.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKMATCHING\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t1_b.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t1_b.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t1_b.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKMATCHING\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t1_c.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t1_c.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t1_c.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKMATCHING\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t2_a.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t2_a.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t2_a.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKMATCHING\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t2_b.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t2_b.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t2_b.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKMATCHING\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t2_c.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t2_c.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/mut_t2_c.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKMATCHING\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t1_a.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t1_a.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t1_a.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKMATCHING\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t1_b.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t1_b.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t1_b.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKMATCHING\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t1_c.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t1_c.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t1_c.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKMATCHING\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t2_a.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t2_a.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t2_a.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKMATCHING\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t2_b.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t2_b.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t2_b.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowResult\" slot=\"PEAKMATCHING\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\" file=\"/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t2_c.cdf\" file-uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t2_c.cdf\">\n"
            + "    <resources>\n"
            + "      <resource uri=\"file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/wt_t2_c.cdf\" />\n"
            + "    </resources>\n"
            + "  </workflowElementResult>\n"
            + "  <workflowElementResult class=\"cross.datastructures.workflow.DefaultWorkflowStatisticsResult\" slot=\"STATISTICS\" generator=\"maltcms.commands.fragments.alignment.PeakCliqueAlignment\">\n"
            + "    <statistics>\n"
            + "      <item name=\"RUNTIME_MILLISECONDS\" value=\"10524.1171875\" />\n"
            + "    </statistics>\n"
            + "  </workflowElementResult>\n"
            + "</workflow>";

    @Test
    public void testGetAlignmentFile() throws Exception {
        //          <workflowElementResult class="cross.datastructures.workflow.DefaultWorkflowResult" slot="ALIGNMENT" generator="maltcms.io.csv.CSVWriter" file="/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/multiple-alignment.csv" file-uri="file:/home/hoffmann/MauiProjects/chlamytest/output/maltcms-1.3.0/2013-11-10_20-11-52/04_PeakCliqueAlignment/multiple-alignment.csv">
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);
        XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = "/workflow/workflowElementResult[@slot='ALIGNMENT'][contains(@file-uri,'/multiple-alignment.csv')]/@file-uri";
        StringReader sr = new StringReader(exampleWorkflow);
        InputSource inputSource = new InputSource(sr);
        NodeList nodes = (NodeList) xpath.compile(expression).evaluate(inputSource, XPathConstants.NODESET);
        Assert.assertEquals(1, nodes.getLength());
        for (int i = 0; i < nodes.getLength(); i++) {
            Node currentItem = nodes.item(i);
            String key = currentItem.getTextContent();
            Assert.assertTrue(key.endsWith("multiple-alignment.csv"));
        }
    }
}
