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
package net.sf.maltcms.chromaui.lookupResultListener.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;

/**
 *
 * @author Nils Hoffmann
 */
public class LookupResultListeners implements List<AbstractLookupResultListener>,
        ILookupResultListener {

    private ArrayList<AbstractLookupResultListener> lookupResultListeners = new ArrayList<AbstractLookupResultListener>(
            2);

    @Override
    public String toString() {
        return lookupResultListeners.toString();
    }

    @Override
    public boolean retainAll(Collection<?> clctn) {
        return lookupResultListeners.retainAll(clctn);
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
        return lookupResultListeners.removeAll(clctn);
    }

    @Override
    public boolean containsAll(Collection<?> clctn) {
        return lookupResultListeners.containsAll(clctn);
    }

    @Override
    public List<AbstractLookupResultListener> subList(int i, int i1) {
        return lookupResultListeners.subList(i, i1);
    }

    @Override
    public ListIterator<AbstractLookupResultListener> listIterator(int i) {
        return lookupResultListeners.listIterator(i);
    }

    @Override
    public ListIterator<AbstractLookupResultListener> listIterator() {
        return lookupResultListeners.listIterator();
    }

    @Override
    public Iterator<AbstractLookupResultListener> iterator() {
        return lookupResultListeners.iterator();
    }

    @Override
    public int hashCode() {
        return lookupResultListeners.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return lookupResultListeners.equals(o);
    }

    public void trimToSize() {
        lookupResultListeners.trimToSize();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return lookupResultListeners.toArray(ts);
    }

    @Override
    public Object[] toArray() {
        return lookupResultListeners.toArray();
    }

    @Override
    public int size() {
        return lookupResultListeners.size();
    }

    @Override
    public AbstractLookupResultListener set(int i,
            AbstractLookupResultListener e) {
        return lookupResultListeners.set(i, e);
    }

    @Override
    public boolean remove(Object o) {
        return lookupResultListeners.remove(o);
    }

    @Override
    public AbstractLookupResultListener remove(int i) {
        return lookupResultListeners.remove(i);
    }

    @Override
    public int lastIndexOf(Object o) {
        return lookupResultListeners.lastIndexOf(o);
    }

    @Override
    public boolean isEmpty() {
        return lookupResultListeners.isEmpty();
    }

    @Override
    public int indexOf(Object o) {
        return lookupResultListeners.indexOf(o);
    }

    @Override
    public AbstractLookupResultListener get(int i) {
        return lookupResultListeners.get(i);
    }

    public void ensureCapacity(int i) {
        lookupResultListeners.ensureCapacity(i);
    }

    @Override
    public boolean contains(Object o) {
        return lookupResultListeners.contains(o);
    }

    @Override
    public Object clone() {
        return lookupResultListeners.clone();
    }

    @Override
    public void clear() {
        lookupResultListeners.clear();
    }

    @Override
    public boolean addAll(int i,
            Collection<? extends AbstractLookupResultListener> clctn) {
        return lookupResultListeners.addAll(i, clctn);
    }

    @Override
    public boolean addAll(
            Collection<? extends AbstractLookupResultListener> clctn) {
        return lookupResultListeners.addAll(clctn);
    }

    @Override
    public void add(int i, AbstractLookupResultListener e) {
        lookupResultListeners.add(i, e);
    }

    @Override
    public boolean add(AbstractLookupResultListener e) {
        return lookupResultListeners.add(e);
    }

    @Override
    public void deregister() {
        for(AbstractLookupResultListener listener:this) {
            listener.deregister();
        }
    }

    @Override
    public void register(Lookup targetLookup) {
        for(AbstractLookupResultListener listener:this) {
            listener.register(targetLookup);
        }
    }

    @Override
    public void resultChanged(LookupEvent result) {
        for(AbstractLookupResultListener listener:this) {
            listener.resultChanged(result);
        }
    }
}
