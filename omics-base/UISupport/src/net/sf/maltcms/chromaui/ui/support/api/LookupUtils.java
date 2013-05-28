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
package net.sf.maltcms.chromaui.ui.support.api;

import java.util.Collection;
import org.openide.util.Lookup;

/**
 *
 * @author Nils Hoffmann
 */
public class LookupUtils {
	
	public static <T> T ensureSingle(Lookup lookup, Class<? extends T> typeClass) throws IllegalArgumentException {
		Collection<? extends T> c = lookup.lookupAll(typeClass);
		if(c.size()>1) {
			throw new IllegalArgumentException("Lookup contained more than one instance of "+typeClass.getCanonicalName()+": "+c.size()+" instances found!");
		}
		return c.iterator().next();
	}
	
	public static <T> T ensureK(Lookup lookup, Class<? extends T> typeClass, int k) throws IllegalArgumentException {
		Collection<? extends T> c = lookup.lookupAll(typeClass);
		if(c.size()!=k) {
			throw new IllegalArgumentException("Lookup contained different number of instances of "+typeClass.getCanonicalName()+": "+c.size()+" instances found, "+k+" were required!");
		}
		return c.iterator().next();
	}
	
}
