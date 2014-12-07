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
package maltcms.ui.fileHandles.properties.pipelineWizard;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.spi.wizard.WizardException;
import org.netbeans.spi.wizard.WizardPage;

/**
 *
 * @author Mathias Wilhelm
 */
public class PipelineWizardResultProducer implements WizardPage.WizardResultProducer {

    @Override
    public Object finish(Map map) throws WizardException {
        Map<String, String> properties = new HashMap<>();
        for (Object key : map.keySet()) {
            if (map.get(key) != null) {
                Logger.getLogger(getClass().getName()).log(Level.INFO, "{0} = {1}", new Object[]{key, map.get(key).toString()});
                properties.put((String) key, map.get(key).toString());
            }
        }
        return properties;
    }

    @Override
    public boolean cancel(Map map) {
        return true;
    }
}
