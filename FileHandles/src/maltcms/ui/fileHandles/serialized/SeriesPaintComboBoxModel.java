/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maltcms.ui.fileHandles.serialized;

import cross.datastructures.tuple.Tuple2D;
import java.awt.Paint;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author nilshoffmann
 */
public class SeriesPaintComboBoxModel extends DefaultComboBoxModel{
    
    public SeriesPaintComboBoxModel(List<Comparable> keys, List<Paint> colors) {
        for(int i = 0;i<keys.size();i++) {
            addElement(new Tuple2D<Comparable,Paint>(keys.get(i),colors.get(i)));
        }
    }

}
