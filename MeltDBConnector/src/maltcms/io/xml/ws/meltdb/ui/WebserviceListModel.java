/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maltcms.io.xml.ws.meltdb.ui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import maltcms.io.xml.ws.meltdb.MeltDBSession;

/**
 *
 * @author nilshoffmann
 */
public abstract class WebserviceListModel<T> implements ListModel {

    private List<ListDataListener> listeners = new LinkedList<ListDataListener>();
    private List<T> values = Collections.emptyList();

    public abstract void initModel(MeltDBSession ms);

    @Override
    public int getSize() {
        return values.size();
    }

    @Override
    public Object getElementAt(int i) {
        return getTypedElementAt(i);
    }

    public T getTypedElementAt(int i) {
        return values.get(i);
    }

    @Override
    public void addListDataListener(ListDataListener ll) {
        listeners.add(ll);
    }

    @Override
    public void removeListDataListener(ListDataListener ll) {
        listeners.remove(ll);
    }

}
