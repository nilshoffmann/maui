/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.normalization.spi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.sf.maltcms.chromaui.normalization.api.IStatisticalTest;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class PeakGroupStatistics implements Map<String,IStatisticalTest>{
    
    private HashMap<String,IStatisticalTest> map = new HashMap<String,IStatisticalTest>();

    @Override
    public String toString() {
        return map.toString();
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return map.equals(o);
    }

    @Override
    public Collection<IStatisticalTest> values() {
        return map.values();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public IStatisticalTest remove(Object o) {
        return map.remove(o);
    }

    @Override
    public void putAll(Map<? extends String, ? extends IStatisticalTest> map) {
        this.map.putAll(map);
    }

    @Override
    public IStatisticalTest put(String k, IStatisticalTest v) {
        return map.put(k, v);
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public IStatisticalTest get(Object o) {
        return map.get(o);
    }

    @Override
    public Set<Entry<String, IStatisticalTest>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean containsValue(Object o) {
        return map.containsValue(o);
    }

    @Override
    public boolean containsKey(Object o) {
        return map.containsKey(o);
    }

    @Override
    public Object clone() {
        return map.clone();
    }

    @Override
    public void clear() {
        map.clear();
    }
}
