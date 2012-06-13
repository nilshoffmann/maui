/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.lookupResultListener.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.openide.util.Lookup;

/**
 *
 * @author nilshoffmann
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
}
