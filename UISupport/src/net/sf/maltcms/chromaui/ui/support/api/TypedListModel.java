/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.ui.support.api;

import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractListModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author nilshoffmann
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class TypedListModel<T> extends AbstractListModel {
    
    private List<T> model = new LinkedList<T>();

    @Override
    public int getSize() {
        return model.size();
    }

    @Override
    public Object getElementAt(int i) {
        return model.get(i);
    }
    
}
