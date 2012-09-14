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
package maltcms.ui.viewer;

import cross.Factory;
import maltcms.ui.charts.JFreeChartViewer;
import maltcms.ui.fileHandles.cdf.CDFDataObject;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author mw
 */
public class GCViewerOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public GCViewerOpenSupport(CDFDataObject.Entry entry) {
        super(entry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {

        CompositeConfiguration cfg = new CompositeConfiguration();
        cfg.addConfiguration(new SystemConfiguration());
        try {
            PropertiesConfiguration pcfg = new PropertiesConfiguration(GCViewerOpenSupport.class.getClassLoader().getResource("cfg/default.properties"));
            cfg.addConfiguration(pcfg);
            Factory.getInstance().configure(cfg);
        } catch (ConfigurationException e) {
            PropertiesConfiguration pcfg;
            try {
                pcfg = new PropertiesConfiguration("cfg/default.properties");
                cfg.addConfiguration(pcfg);
                Factory.getInstance().configure(cfg);
            } catch (ConfigurationException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }


        CDFDataObject dobj = (CDFDataObject) entry.getDataObject();
//        System.out.println("filename: " + dobj.getPrimaryFile().getPath());
        GCViewerTopComponent jtc = new GCViewerTopComponent(dobj);
        return jtc;
    }
}
