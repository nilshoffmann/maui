/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.features.api;

import org.apache.commons.math.linear.RealMatrix;

/**
 *
 * @author nilshoffmann
 */
public interface IFeatureTable {

    String getFactorName(int factor);

    RealMatrix getMatrix();

    void setColumn(int variable, double[] values);

    void setValue(int factor, int variable, double value);

    void setFactorName(int factor, String name);

    void setVariableName(int variable, String name);

    String getVariableName(int variable);

    void setRow(int factor, double[] values);
}
