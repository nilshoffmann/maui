/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unibielefeld.gi.omicsTools.biocyc.ptools;

import java.math.BigInteger;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Nils Hoffmann
 */
public class BigIntegerAdapter extends XmlAdapter<java.lang.String,BigInteger>{

	@Override
	public java.lang.String marshal(BigInteger bigInteger) throws Exception {
		if (bigInteger != null){
			return bigInteger.toString();
		}
		else {
			return null;
		}
	}

	@Override
	public BigInteger unmarshal(java.lang.String s) throws Exception {
		try {
			return new BigInteger(s);
		} catch (NumberFormatException e) {
			System.err.println("Exception while trying to unmarshal "+s);
			return null;
		}
	}

}