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
