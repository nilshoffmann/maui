/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.normalization.spi.runnables;

import com.db4o.collections.ActivatableArrayList;
import java.util.List;
import lombok.Data;
import net.sf.maltcms.chromaui.normalization.spi.DataTable;
import net.sf.maltcms.chromaui.project.api.container.PeakGroupContainer;
import net.sf.maltcms.chromaui.project.api.container.StatisticsContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IPcaDescriptor;
import net.sf.maltcms.chromaui.rserve.api.RserveConnectionFactory;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import org.openide.util.Exceptions;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;

/**
 *
 * data frame format
 * 
 * columns: observations (peak groups)
 * rows: chromatograms
 * 
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@Data
public class PeakGroupPcaRunnable extends AProgressAwareRunnable {

    private final PeakGroupContainer container;
    private final DataTable dataTable;
    private final boolean centerData;
    private final boolean scaleToUnitVariance;
    
    @Override
    public void run() {
//        try {
            progressHandle.start();
            try {
                RConnection c = RserveConnectionFactory.getDefaultConnection();
                try {
                    StatisticsContainer pcaDescriptor = new StatisticsContainer();
                    pcaDescriptor.setName("pca");
                    pcaDescriptor.setMethod("Pca");
                    pcaDescriptor.setDisplayName("Principal Components Analysis");

                    c.assign(dataTable.getName(), dataTable.toDataFrame());
                    c.assign("rowNames",dataTable.getRowNamesREXP());
                    c.eval("row.names("+dataTable.getName()+") <- rowNames");
                    c.eval("write.table(" + dataTable.getName() + ",file=\"/Users/nilshoffmann/rout.csv\")");
                    StringBuilder expression = new StringBuilder();
                    expression.append("result <- prcomp(");
                    expression.append(dataTable.getName());
                    expression.append(",na.action=na.omit");
//                    if (centerData) {
//                        expression.append(",center=T");
//                    }
                    if (scaleToUnitVariance) {
                        expression.append(",scale=T");
                    }
                    expression.append(")");
                    String rcall = expression.toString();
                    System.out.println("Expression: " + rcall);
                    REXP rresult = c.parseAndEval(rcall);
                    String device = "png"; // device we'll call (this would work with pretty much any bitmap device)
                    //projected data
                    REXP x = c.parseAndEval("x<-as.matrix(result$x)");
                    double[][] xm = x.asDoubleMatrix();
                    ArrayDouble.D2 am = new ArrayDouble.D2(xm.length, xm[0].length);
                    for(int i = 0;i<xm.length;i++) {
                        for(int j=0;j<xm[0].length;j++) {
                            am.set(i, j, xm[i][j]);
                        }
                    }
                    //vector for pcs
                    REXP sdev = c.parseAndEval("s<-as.matrix(result$sdev)");
                    double[] sdevm = sdev.asDoubles();
                    ArrayDouble.D1 sm = (ArrayDouble.D1)Array.factory(sdevm);
                    //vector for variables
                    REXP center = c.parseAndEval("c<-as.matrix(result$center)");
                    double[] centerm = center.asDoubles();
                    ArrayDouble.D1 cm = (ArrayDouble.D1)Array.factory(centerm);
                    //boolean
                    REXP scale = c.parseAndEval("sc<-result$scale");
                    boolean scalem = Boolean.valueOf(scale.asString().toLowerCase());
                    //rotation matrix
                    REXP rotation = c.parseAndEval("rot<-as.matrix(result$rotation)");
                    double[][] rotationm = rotation.asDoubleMatrix();
                    ArrayDouble.D2 rm = new ArrayDouble.D2(rotationm.length,rotationm[0].length);
                    for (int i = 0; i < rotationm.length; i++) {
                        for (int j = 0; j < rotationm[0].length; j++) {
                            rm.set(i, j, rotationm[i][j]);
                        }
                        
                    }

                    List<StatisticsContainer> statContainers = container.getStatisticsContainers();
                    if (statContainers == null) {
                        statContainers = new ActivatableArrayList<StatisticsContainer>();
                    }
                    IPcaDescriptor pcadescr = DescriptorFactory.newPcaDescriptor();
                    pcadescr.setName(rcall);
                    pcadescr.setDisplayName(rcall);
                    pcadescr.setRotation(rm);
                    pcadescr.setSdev(sm);
                    pcadescr.setCenter(cm);
                    pcadescr.setScale(scalem);
                    pcadescr.setX(am);
                    pcadescr.setCases(dataTable.getRowNames());
                    pcadescr.setVariables(dataTable.getVariables());
                    pcaDescriptor.addMembers(pcadescr);
                    statContainers.add(pcaDescriptor);
                    container.setStatisticsContainers(statContainers);

                    // close RConnection, we're done
                    c.close();
                } catch (RserveException rse) { // RserveException (transport layer - e.g. Rserve is not running)
                    System.out.println(rse);
                    Exceptions.printStackTrace(rse);
                } catch (REXPMismatchException mme) { // REXP mismatch exception (we got something we didn't think we getMembers)
                    System.out.println(mme);
                    Exceptions.printStackTrace(mme);
                } catch (Exception e) { // something else
                    System.out.println("Something went wrong, but it's not the Rserve: "
                            + e.getMessage());
                    Exceptions.printStackTrace(e);
                }
//            } catch (RserveException ex) {
//                Exceptions.printStackTrace(ex);
//            }
            progressHandle.finish();
        } catch (Exception e) {
            progressHandle.finish();
        }
    }
}
