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
package de.unibielefeld.gi.kotte.laborprogramm.centralLookup.impl;

import de.unibielefeld.gi.kotte.laborprogramm.centralLookup.CentralLookup;
import de.unibielefeld.gi.kotte.laborprogramm.centralLookup.ICentralLookupFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.util.Lookup;

/**
 *
 * @author Nils Hoffmann
 */
public class CentralLookupFactoryTest {

	/**
	 * Test of getDefault method, of class CentralLookupFactory.
	 */
	@Test
	public void testGetDefault() {
		CentralLookup lookupExpected = CentralLookup.getDefault();
		ICentralLookupFactory lookupFactory = Lookup.getDefault().lookup(ICentralLookupFactory.class);
		assertSame(lookupExpected, lookupFactory.getDefault());
	}

	/**
	 * Test of getNamed method, of class CentralLookupFactory.
	 */
	@Test
	public void testGetNamed() {
		ICentralLookupFactory lookupFactory = Lookup.getDefault().lookup(ICentralLookupFactory.class);
		CentralLookupFactory clf = new CentralLookupFactory();
		//fail if the classes change, e.g. by an additional ServiceProvider
		assertEquals(lookupFactory.getClass(),clf.getClass());
		CentralLookup lookupExpected = clf.getNamed("testLookup");
		CentralLookup lookupReceived = lookupFactory.getNamed("testLookup");
		assertEquals(lookupExpected.getClass(),lookupReceived.getClass());
	}
	
	/**
	 * Test of removeNamed method, of class CentralLookupFactory.
	 */
	@Test
	public void testRemoveNamed() {
		CentralLookupFactory clf = new CentralLookupFactory();
		CentralLookup lookupExpected = clf.getNamed("testLookup");
		clf.removeNamed("testLookup");
		CentralLookup lookupReceived = clf.getNamed("testLookup");
		assertNotSame(lookupExpected,lookupReceived);
	}
}