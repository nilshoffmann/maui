/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.features.api;

import cross.datastructures.fragments.IFileFragment;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import maltcms.datastructures.array.IFeatureVector;

/**
 *
 * @author nilshoffmann
 */
public class FeatureVectorCollection implements Iterable<IFeatureVector>{
    
    private IFileFragment fragment;
    
    private List<String> groups;
    
    private List<IFeatureVector> featureVectors;

    public FeatureVectorCollection(IFileFragment fragment,
            List<String> groups,
            List<IFeatureVector> featureVectors) {
        this.fragment = fragment;
        this.groups = groups;
        this.featureVectors = featureVectors;
    }

    /**
     * Returns an unmodifiable iterator.
     * @return 
     */
    @Override
    public Iterator<IFeatureVector> iterator() {
        return Collections.unmodifiableList(featureVectors).iterator();
    }
    
    public int size() {
        return this.featureVectors.size();
    }
    
}
