/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.charts.datastructures;

import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.maltcms.chromaui.charts.tools.ChromatogramVisualizerTools;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.IndexIterator;

/**
 *
 * @author mw
 */
public class Tic2DProvider {

    private static Map<IChromatogramDescriptor, Tic2DProvider> statics = new HashMap<IChromatogramDescriptor, Tic2DProvider>();
    private IFileFragment ff;
    private IVariableFragment tic = null;
    private IVariableFragment vtic = null;

    public static Tic2DProvider getInstance(IChromatogramDescriptor filename) throws IOException {
        if (statics.containsKey(filename)) {
            return statics.get(filename);
        }
        Tic2DProvider tmp = new Tic2DProvider(filename);
        statics.put(filename, tmp);
        return tmp;
    }

    private Tic2DProvider(IChromatogramDescriptor filename) throws IOException {
        this.ff = ChromatogramVisualizerTools.getFragments(filename).getFirst();
    }

    private void loadTotalIntensity() {
        this.ff.getChild("total_intensity").setIndex(this.ff.getChild("second_column_scan_index"));
        this.tic = ff.getChild("total_intensity");
    }

    private void loadVTotalIntensity() {
        this.ff.getChild("v_total_intensity").setIndex(this.ff.getChild("second_column_scan_index"));
        this.vtic = ff.getChild("v_total_intensity");
    }

    public Array getScanlineTIC(int mod) {
        return getScanlineTICS().get(mod);
    }

    public Array getScanlineVTIC(int mod) {
        return getScanlineVTICS().get(mod);
    }

    public Array getHScanlineTIC(int mod) {
        List<Array> stics = getScanlineTICS();
        Array ret = new ArrayDouble.D1(stics.size());
        IndexIterator iter = ret.getIndexIterator();
        for (int i = 0; i < stics.size(); i++) {
            iter.setDoubleNext(((ArrayDouble.D1) stics.get(i)).get(mod));
        }
        return ret;
    }

    public Array getHScanlineVTIC(int mod) {
        List<Array> stics = getScanlineVTICS();
        Array ret = new ArrayDouble.D1(stics.size());
        IndexIterator iter = ret.getIndexIterator();
        for (int i = 0; i < stics.size(); i++) {
            iter.setDoubleNext(((ArrayDouble.D1) stics.get(i)).get(mod));
        }
        return ret;
    }

    public Array getTIC() {
        if (this.tic == null) {
            loadTotalIntensity();
        }
        return this.tic.getArray();
    }

    public List<Array> getScanlineTICS() {
        if (this.tic == null) {
            loadTotalIntensity();
        }
        return this.tic.getIndexedArray();
    }

    public Array getVTIC() {
        if (this.vtic == null) {
            loadVTotalIntensity();
        }
        return this.vtic.getArray();
    }

    public List<Array> getScanlineVTICS() {
        if (this.vtic == null) {
            loadVTotalIntensity();
        }
        return this.vtic.getIndexedArray();
    }

    public Array getHGlobalTIC() {
        return null;
    }

    public Array getVGolbalTIC() {
        return null;
    }

    public Array getHGolbalVTIC() {
        return null;
    }

    public Array getVGobalVTIC() {
        return null;
    }
}
