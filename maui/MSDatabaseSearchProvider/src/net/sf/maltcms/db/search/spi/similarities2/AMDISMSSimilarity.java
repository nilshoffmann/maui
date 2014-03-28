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
package net.sf.maltcms.db.search.spi.similarities2;

import cross.datastructures.tuple.Tuple2D;
import java.util.HashMap;
import maltcms.math.functions.similarities.ArrayWeightedCosine;
import maltcms.tools.ArrayTools;
import maltcms.tools.MaltcmsTools;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.MAMath;
import ucar.ma2.MAMath.MinMax;

/**
 *
 * @author nilshoffmann
 */
public class AMDISMSSimilarity {

	private final ArrayWeightedCosine awc = new ArrayWeightedCosine();
	private final HashMap<Array,Double> minMassCache = new HashMap<Array,Double>();
	private final HashMap<Array,Double> maxMassCache = new HashMap<Array,Double>();

	public double apply(Tuple2D<Array, Array> referenceMassSpectrum,
			Tuple2D<Array, Array> queryMassSpectrum) {
		double resolution = 1.0;

		MinMax mm1 = null;
		if (minMassCache.containsKey(referenceMassSpectrum.getFirst()) && maxMassCache.containsKey(referenceMassSpectrum.getFirst())) {
			mm1 = new MinMax(minMassCache.get(referenceMassSpectrum.getFirst()), maxMassCache.get(referenceMassSpectrum.getFirst()));
		} else {
			mm1 = MAMath.getMinMax(referenceMassSpectrum.getFirst());
			minMassCache.put(referenceMassSpectrum.getFirst(), mm1.min);
			maxMassCache.put(referenceMassSpectrum.getFirst(), mm1.max);
		}
		MinMax mm2 = null;
		if (minMassCache.containsKey(queryMassSpectrum.getFirst()) && maxMassCache.containsKey(queryMassSpectrum.getFirst())) {
			mm2 = new MinMax(minMassCache.get(queryMassSpectrum.getFirst()), maxMassCache.get(queryMassSpectrum.getFirst()));
		} else {
			mm2 = MAMath.getMinMax(queryMassSpectrum.getFirst());
			minMassCache.put(queryMassSpectrum.getFirst(), mm2.min);
			maxMassCache.put(queryMassSpectrum.getFirst(), mm2.max);
		}
		// Union, greatest possible interval
		double max = Math.max(mm1.max, mm2.max);
		double min = Math.min(mm1.min, mm2.min);
		int bins = MaltcmsTools.getNumberOfIntegerMassBins(min, max, resolution);

		ArrayDouble.D1 ira = null, iqa = null;
		ArrayDouble.D1 mra = new ArrayDouble.D1(bins);
		ira = new ArrayDouble.D1(bins);
		ArrayTools.createDenseArray(referenceMassSpectrum.getFirst(),
				referenceMassSpectrum.getSecond(),
				new Tuple2D<Array, Array>(mra, ira), ((int) Math.floor(min)),
				((int) Math.ceil(max)), bins,
				resolution, 0.0d);
		ArrayDouble.D1 mqa = new ArrayDouble.D1(bins);
		iqa = new ArrayDouble.D1(bins);
		ArrayTools.createDenseArray(queryMassSpectrum.getFirst(),
				queryMassSpectrum.getSecond(),
				new Tuple2D<Array, Array>(mqa, iqa),
				((int) Math.floor(min)), ((int) Math.ceil(max)), bins,
				resolution, 0.0d);
		return awc.apply(mqa, iqa);
	}
}
