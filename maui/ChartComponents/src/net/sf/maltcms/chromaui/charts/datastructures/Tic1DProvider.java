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
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import ucar.ma2.Array;

/**
 *
 * @author Mathias Wilhelm
 */
public class Tic1DProvider {

    private static Map<IChromatogramDescriptor, Tic1DProvider> statics = new HashMap<>();
    private IFileFragment ff;
    private IVariableFragment tic = null;
    private IVariableFragment vtic = null;

    /**
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static Tic1DProvider getInstance(IChromatogramDescriptor filename) throws IOException {
        if (statics.containsKey(filename)) {
            return statics.get(filename);
        }
        Tic1DProvider tmp = new Tic1DProvider(filename);
        statics.put(filename, tmp);
        return tmp;
    }

    private Tic1DProvider(IChromatogramDescriptor filename) throws IOException {
        this.ff = filename.getChromatogram().getParent();
    }

    private void loadTotalIntensity() {
        this.ff.getChild("total_intensity");
        this.tic = ff.getChild("total_intensity");
    }

    /**
     *
     * @param mod
     * @return
     */
    public Array getScanlineTIC(int mod) {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param mod
     * @return
     */
    public Array getScanlineVTIC(int mod) {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param mod
     * @return
     */
    public Array getHScanlineTIC(int mod) {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param mod
     * @return
     */
    public Array getHScanlineVTIC(int mod) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @return
     */
    public Array getVTIC() {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @return
     */
    public List<Array> getScanlineVTICS() {
        throw new UnsupportedOperationException();
    }
}
