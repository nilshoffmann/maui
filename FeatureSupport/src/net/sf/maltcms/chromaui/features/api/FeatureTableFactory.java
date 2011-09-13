/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.features.api;

import java.util.List;
import net.sf.maltcms.chromaui.features.spi.FeatureTable;

/**
 *
 * @author nilshoffmann
 */
public class FeatureTableFactory {
    
    public static IFeatureTable createFeatureTable(List<String> factorNames, List<String> variableNames) {
        FeatureTable ft = new FeatureTable(factorNames.size(),variableNames.size());
        for(int i = 0;i<factorNames.size();i++) {
            ft.setFactorName(i, factorNames.get(i));
        }
        for(int j = 0;j<variableNames.size();j++) {
            ft.setVariableName(j, variableNames.get(j));
        }
        return ft;
        
    }
    
}
