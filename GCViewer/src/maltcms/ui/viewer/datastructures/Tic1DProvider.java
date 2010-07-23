/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer.datastructures;

import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import maltcms.ui.viewer.tools.ChromatogramVisualizerTools;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.IndexIterator;

/**
 *
 * @author mw
 */
public class Tic1DProvider {

    private static Map<String, Tic1DProvider> statics = new HashMap<String, Tic1DProvider>();
    private IFileFragment ff;
    private IVariableFragment tic = null;
    private IVariableFragment vtic = null;

    public static Tic1DProvider getInstance(String filename) throws IOException {
        if (statics.containsKey(filename)) {
            return statics.get(filename);
        }
        Tic1DProvider tmp = new Tic1DProvider(filename);
        statics.put(filename, tmp);
        return tmp;
    }

    private Tic1DProvider(String filename) throws IOException {
        this.ff = ChromatogramVisualizerTools.getFragments(filename).getFirst();
    }

    private void loadTotalIntensity() {
        this.ff.getChild("total_intensity");
        this.tic = ff.getChild("total_intensity");
    }

    public Array getScanlineTIC(int mod) {
        throw new UnsupportedOperationException();
    }

    public Array getScanlineVTIC(int mod) {
        throw new UnsupportedOperationException();
    }

    public Array getHScanlineTIC(int mod) {
        throw new UnsupportedOperationException();
    }

    public Array getHScanlineVTIC(int mod) {
        throw new UnsupportedOperationException();
    }

    public Array getTIC() {
        if (this.tic == null) {
            loadTotalIntensity();
        }
        return this.tic.getArray();
    }

    public List<Array> getScanlineTICS() {
        throw new UnsupportedOperationException();
    }

    public Array getVTIC() {
        throw new UnsupportedOperationException();
    }

    public List<Array> getScanlineVTICS() {
        throw new UnsupportedOperationException();
    }
}
