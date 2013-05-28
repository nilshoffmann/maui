/*
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.project.spi.descriptors;

import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IVariableFragment;
import java.util.Arrays;
import junit.framework.Assert;
import maltcms.datastructures.ms.IChromatogram;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import org.junit.Test;
import ucar.ma2.Array;

/**
 *
 * @author Nils Hoffmann
 */
public class CachingChromatogram1DTest {

	@Test
	public void testRTtoIndexAssignment() {
		FileFragment f = new FileFragment();
		IVariableFragment sat = f.addChild("scan_acquisition_time");
		double[] sats = new double[]{0.432, 0.442, 0.463, 0.472, 0.491, 1.21, 1.35};
		sat.setArray(Array.factory(sats));
		f.save();
		IChromatogramDescriptor icd = DescriptorFactory.newChromatogramDescriptor();
		icd.setResourceLocation(f.getUri().toString());
		IChromatogram ic1d = icd.getChromatogram();
		double[] searchTimes = new double[]{0.438, 0.477, 0.6, 1.21};
		int[] expectedIndices = new int[]{1, 3, 4, 5};
		for (int i = 0; i < expectedIndices.length; i++) {
			System.out.println("Start Test "+(i+1)+"/"+expectedIndices.length);
			double time = searchTimes[i];
			int expectedIndex = expectedIndices[i];
			Assert.assertEquals(expectedIndex, getIndexForRt(time, sats));
			System.out.println("End Test "+(i+1)+"/"+expectedIndices.length);
		}
	}

	public int getIndexForRt(double rt, double[] rts) {
		System.out.println("Query value: "+rt);
		int idx = Arrays.binarySearch(rts, rt);
		if (idx >= 0) {// exact hit
			System.out.println("Value at index: "+idx+" = "+rts[idx]);
			return idx;
		} else {// imprecise hit, find closest element
			int insertionPosition = (-idx)-1;
			if(insertionPosition<0) {
				throw new IllegalArgumentException("Insertion index is out of bounds! "+insertionPosition+"<"+0);
			}
			if(insertionPosition>=rts.length) {
				throw new IllegalArgumentException("Insertion index is out of bounds! "+insertionPosition+">="+rts.length);
			}
			System.out.println("Would insert before "+insertionPosition);
			double current = rts[Math.min(rts.length - 1, insertionPosition)];
			System.out.println("Value at insertion position: "+current);
			double previous = rts[Math.max(0, insertionPosition-1)];
			System.out.println("Value before insertion position: "+previous);
			if (Math.abs(rt - previous) <= Math.abs(
					rt - current)) {
				int index = Math.max(0, insertionPosition-1);
				System.out.println("Returning "+index);
				return index;
			} else {
				System.out.println("Returning "+insertionPosition);
				return insertionPosition;
			}
		}
	}
}