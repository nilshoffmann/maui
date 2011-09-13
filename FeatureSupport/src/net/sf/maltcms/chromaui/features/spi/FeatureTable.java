/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.features.spi;

import net.sf.maltcms.chromaui.features.api.IFeatureTable;
import java.util.HashMap;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.stat.StatUtils;

/**
 *
 * @author nilshoffmann
 */
public class FeatureTable implements IFeatureTable {

    private final RealMatrix featureMatrix;
    private String[] factorNames;
    private String[] variableNames;

    /**
     * factors-> number of rows
     * variables-> number of columns
     * 
     * @param factors 
     * @param variables
     * 
     */
    public FeatureTable(int factors, int variables) {
        this.featureMatrix = MatrixUtils.createRealMatrix(factors,
                variables);
        this.factorNames = new String[factors];
        this.variableNames = new String[variables];
    }

    @Override
    public void setValue(int factor, int variable, double value) {
        this.featureMatrix.setEntry(factor, variable, value);
    }

    @Override
    public void setColumn(int variable, double[] values) {
        this.featureMatrix.setColumn(variable, values);
    }

    @Override
    public void setRow(int factor, double[] values) {
        this.featureMatrix.setRow(factor, values);
    }

    @Override
    public RealMatrix getMatrix() {
        return this.featureMatrix;
    }

    public static RealMatrix normalize(RealMatrix sourceMatrix) {
        return FeatureTable.normalize(sourceMatrix,true, true);
    }

    public static RealMatrix normalize(RealMatrix sourceMatrix, boolean center, boolean normalize) {
        RealMatrix normalized = MatrixUtils.createRealMatrix(sourceMatrix.
                getRowDimension(), sourceMatrix.getColumnDimension());
        for (int col = 0; col < sourceMatrix.getColumnDimension(); col++) {
            double[] columnVector = sourceMatrix.getColumn(col);
            double mean = StatUtils.mean(columnVector);
            double stdev = Math.sqrt(StatUtils.variance(columnVector, mean));
            System.out.println(
                    "column " + col + ", mean=" + mean + " stdev=" + stdev);
            for (int j = 0; j < columnVector.length; j++) {
                normalized.setEntry(j, col,
                        (sourceMatrix.getEntry(j, col) - mean) / stdev);
            }
        }
        return normalized;
    }

    @Override
    public void setFactorName(int factor, String name) {
        this.factorNames[factor]=name;
    }
    
    @Override
    public String getFactorName(int factor) {
        return this.factorNames[factor];
    }

    @Override
    public void setVariableName(int variable, String name) {
        this.variableNames[variable] = name;
    }

    @Override
    public String getVariableName(int variable) {
        return this.variableNames[variable];
    }
}
