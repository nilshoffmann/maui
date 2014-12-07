/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.charts.datastructures;

import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.IndexIterator;

/**
 *
 * @author Mathias Wilhelm
 */
public class TicProvider {

    private static Map<IChromatogramDescriptor, TicProvider> statics = new HashMap<>();
    private IFileFragment ff;
    private IVariableFragment tic = null;
    private IVariableFragment vtic = null;

    /**
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static TicProvider getInstance(IChromatogramDescriptor filename) throws IOException {
        if (statics.containsKey(filename)) {
            return statics.get(filename);
        }
        TicProvider tmp = new TicProvider(filename);
        statics.put(filename, tmp);
        return tmp;
    }

    private TicProvider(IChromatogramDescriptor filename) throws IOException {
        this.ff = filename.getChromatogram().getParent();
        loadTotalIntensity();
    }

    private void loadTotalIntensity() {
        //this.ff.getChild("total_intensity").setIndex(this.ff.getChild("second_column_scan_index"));
        this.tic = ff.getChild("total_intensity");
    }

    private void loadVTotalIntensity() {
        this.ff.getChild("v_total_intensity").setIndex(this.ff.getChild("second_column_scan_index"));
        this.vtic = ff.getChild("v_total_intensity");
    }

    /**
     *
     * @param mod
     * @return
     */
    public Array getScanlineTIC(int mod) {
        return getScanlineTICS().get(mod);
    }

    /**
     *
     * @param mod
     * @return
     */
    public Array getScanlineVTIC(int mod) {
        return getScanlineVTICS().get(mod);
    }

    /**
     *
     * @param mod
     * @return
     */
    public Array getHScanlineTIC(int mod) {
        List<Array> stics = getScanlineTICS();
        Array ret = new ArrayDouble.D1(stics.size());
        IndexIterator iter = ret.getIndexIterator();
        for (Array stic : stics) {
            iter.setDoubleNext(((ArrayDouble.D1) stic).get(mod));
        }
        return ret;
    }

    /**
     *
     * @param mod
     * @return
     */
    public Array getHScanlineVTIC(int mod) {
        List<Array> stics = getScanlineVTICS();
        Array ret = new ArrayDouble.D1(stics.size());
        IndexIterator iter = ret.getIndexIterator();
        for (Array stic : stics) {
            iter.setDoubleNext(((ArrayDouble.D1) stic).get(mod));
        }
        Logger.getLogger(getClass().getName()).info("This is a text");
        return ret;
    }

    /**
     *
     * @return
     */
    public Array getTIC() {
        if (this.tic == null) {
            loadTotalIntensity();
        }
        return this.tic.getArray();
    }

    /**
     *
     * @return
     */
    public List<Array> getScanlineTICS() {
        if (this.tic == null) {
            loadTotalIntensity();
        }
        return this.tic.getIndexedArray();
    }

    /**
     *
     * @return
     */
    public Array getVTIC() {
        if (this.vtic == null) {
            loadVTotalIntensity();
        }
        return this.vtic.getArray();
    }

    /**
     *
     * @return
     */
    public List<Array> getScanlineVTICS() {
        if (this.vtic == null) {
            loadVTotalIntensity();
        }
        return this.vtic.getIndexedArray();
    }

    /**
     *
     * @return
     */
    public Array getHGlobalTIC() {
        return null;
    }

    /**
     *
     * @return
     */
    public Array getVGolbalTIC() {
        return null;
    }

    /**
     *
     * @return
     */
    public Array getHGolbalVTIC() {
        return null;
    }

    /**
     *
     * @return
     */
    public Array getVGobalVTIC() {
        return null;
    }
}
