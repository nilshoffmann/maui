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
package net.sf.maltcms.chromaui.ui.support.api.outline;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import org.openide.explorer.view.OutlineView;
import org.openide.util.Exceptions;

/**
 *
 * @author Nils Hoffmann
 */
public class ColumnUtilities {

    public void addPropertyColumns(OutlineView view, Collection<ColumnDescriptor> columns, HashSet<String> whiteList) {
        for (ColumnDescriptor cd : columns) {
            if (whiteList.contains(cd.name)) {
                view.addPropertyColumn(cd.name, cd.displayName,
                        cd.shortDescription);
            }
        }
    }

    public void addPropertyColumns(OutlineView view, Collection<ColumnDescriptor> columns) {
        for (ColumnDescriptor cd : columns) {
            view.addPropertyColumn(cd.name, cd.displayName,
                    cd.shortDescription);
        }
    }

    public Collection<ColumnDescriptor> getColumnDescriptorsForClasses(Collection<? extends Class> container) {
        HashMap<String, ColumnDescriptor> columns = new LinkedHashMap<>();
        for (Class sdesc : container) {
            try {
                BeanInfo info = Introspector.getBeanInfo(
                        sdesc);
                PropertyDescriptor[] pds = info.getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {
                    if (!columns.containsKey(pd.getName())) {
                        columns.put(pd.getName(), new ColumnDescriptor(
                                pd.getName(),
                                pd.getDisplayName(),
                                pd.getShortDescription()));
                    }
                }
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return columns.values();
    }

//    public HashSet<ColumnDescriptor> getColumnDescriptors(Collection<? extends Object> container) {
//        ArrayList<Class> l = new ArrayList<>();
//        for(Object o:container) {
//            l.add(o.getClass());
//        }
//        return getColumnDescriptorsForClasses(l);
//    }
}
