/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unibielefeld.gi.omicsTools.biocyc.ptools;

import java.math.BigDecimal;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Nils Hoffmann
 */
public class BigDecimalAdapter extends XmlAdapter<java.lang.String,BigDecimal>{

	@Override
	public java.lang.String marshal(BigDecimal bigDecimal) throws Exception {
		if (bigDecimal != null){
			return bigDecimal.toString();
		}
		else {
			return null;
		}
	}

	@Override
	public BigDecimal unmarshal(java.lang.String s) throws Exception {
		try {
			return new BigDecimal(s);
		} catch (NumberFormatException e) {
			System.err.println("Exception while trying to unmarshal "+s);
			return null;
		}
	}

}