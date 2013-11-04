/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters
({
    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(value=de.unibielefeld.gi.omicsTools.biocyc.ptools.BigIntegerAdapter.class,type=java.math.BigInteger.class),
    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(value=de.unibielefeld.gi.omicsTools.biocyc.ptools.BigDecimalAdapter.class,type=java.math.BigDecimal.class)
})

package de.unibielefeld.gi.omicsTools.biocyc.ptools;
