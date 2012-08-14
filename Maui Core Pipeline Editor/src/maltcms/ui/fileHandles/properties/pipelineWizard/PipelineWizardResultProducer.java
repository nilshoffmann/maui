/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.properties.pipelineWizard;

import java.util.HashMap;
import java.util.Map;
import org.netbeans.spi.wizard.WizardException;
import org.netbeans.spi.wizard.WizardPage;

/**
 *
 * @author mw
 */
public class PipelineWizardResultProducer implements WizardPage.WizardResultProducer {

    @Override
    public Object finish(Map map) throws WizardException {
        Map<String, String> properties = new HashMap<String, String>();
        for (Object key : map.keySet()) {
            if (map.get(key) != null) {
                System.out.println(key + " = " + map.get(key).toString());
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
