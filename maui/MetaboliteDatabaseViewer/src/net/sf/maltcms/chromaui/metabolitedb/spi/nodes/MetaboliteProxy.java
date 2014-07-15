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
package net.sf.maltcms.chromaui.metabolitedb.spi.nodes;

import cross.datastructures.tuple.Tuple2D;
import java.beans.PropertyChangeSupport;
import java.net.URI;
import maltcms.datastructures.ms.IAnchor;
import maltcms.datastructures.ms.IMetabolite;
import ucar.ma2.ArrayDouble.D1;
import ucar.ma2.ArrayInt;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class MetaboliteProxy implements IMetabolite {

    private transient PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public PropertyChangeSupport getPropertyChangeSupport() {
        if (pcs == null) {
            pcs = new PropertyChangeSupport(this);
        }
        return pcs;
    }
    private final IMetabolite metabolite;

    public MetaboliteProxy(IMetabolite metabolite) {
        this.metabolite = metabolite;
    }

    public IMetabolite getMetabolite() {
        return metabolite;
    }
    public final String PROP_SCANINDEX = "scanIndex";

    @Override
    public int getScanIndex() {
        return metabolite.getScanIndex();
    }

    @Override
    public void setScanIndex(int i) {
        int oldValue = metabolite.getScanIndex();
        metabolite.setScanIndex(i);
        pcs.firePropertyChange(PROP_SCANINDEX, oldValue, i);
    }
    public final String PROP_NAME = "name";

    @Override
    public String getName() {
        return metabolite.getName();
    }

    @Override
    public void setName(String string) {
        String oldValue = metabolite.getName();
        metabolite.setName(string);
        pcs.firePropertyChange(PROP_NAME, oldValue, string);
    }
    public final String PROP_RETENTIONTIMEUNIT = "retentionTimeUnit";

    @Override
    public String getRetentionTimeUnit() {
        return metabolite.getRetentionTimeUnit();
    }

    @Override
    public void setRetentionTimeUnit(String string) {
        String oldValue = metabolite.getRetentionTimeUnit();
        metabolite.setRetentionTimeUnit(string);
        pcs.firePropertyChange(PROP_RETENTIONTIMEUNIT, oldValue, string);
    }
    public final String PROP_RETENTIONTIME = "retentionTime";

    @Override
    public double getRetentionTime() {
        return metabolite.getRetentionTime();
    }

    @Override
    public void setRetentionTime(double d) {
        double oldValue = metabolite.getRetentionTime();
        metabolite.setRetentionTime(d);
        pcs.firePropertyChange(PROP_RETENTIONTIME, oldValue, d);
    }

    @Override
    public double getRetentionIndex() {
        return metabolite.getRetentionIndex();
    }
    public final String PROP_RETENTIONINDEX = "retentionIndex";

    @Override
    public void setRetentionIndex(double d) {
        double oldValue = metabolite.getRetentionIndex();
        metabolite.setRetentionIndex(d);
        pcs.firePropertyChange(PROP_RETENTIONINDEX, oldValue, d);
    }

    @Override
    public void update(IMetabolite im) {
        metabolite.update(im);
    }
    public final String PROP_SHORTNAME = "shortName";

    @Override
    public String getShortName() {
        return metabolite.getShortName();
    }

    @Override
    public void setShortName(String string) {
        String oldValue = metabolite.getShortName();
        metabolite.setShortName(string);
        pcs.firePropertyChange(PROP_SHORTNAME, oldValue, string);
    }
    public final String PROP_SP = "sp";

    @Override
    public String getSP() {
        return metabolite.getSP();
    }

    @Override
    public void setSP(String string) {
        String oldValue = metabolite.getSP();
        metabolite.setSP(string);
        pcs.firePropertyChange(PROP_SP, oldValue, string);
    }
    public final String PROP_MINMASS = "minMass";

    @Override
    public double getMinMass() {
        return metabolite.getMinMass();
    }

    @Override
    public void setMinMass(double d) {
        double oldValue = metabolite.getMinMass();
        metabolite.setMinMass(d);
        pcs.firePropertyChange(PROP_MINMASS, oldValue, d);
    }
    public final String PROP_MININTENSITY = "minIntensity";

    @Override
    public double getMinIntensity() {
        return metabolite.getMinIntensity();
    }

    @Override
    public void setMinIntensity(double d) {
        double oldValue = metabolite.getMinIntensity();
        metabolite.setMinIntensity(d);
        pcs.firePropertyChange(PROP_MININTENSITY, oldValue, d);
    }
    public final String PROP_MAXMASS = "maxMass";

    @Override
    public double getMaxMass() {
        return metabolite.getMaxMass();
    }

    @Override
    public void setMaxMass(double d) {
        double oldValue = metabolite.getMaxMass();
        metabolite.setMaxMass(d);
        pcs.firePropertyChange(PROP_MAXMASS, oldValue, d);
    }
    public final String PROP_MAXINTENSITY = "maxIntensity";

    @Override
    public double getMaxIntensity() {
        return metabolite.getMaxIntensity();
    }

    @Override
    public void setMaxIntensity(double d) {
        double oldValue = metabolite.getMaxIntensity();
        metabolite.setMaxIntensity(d);
        pcs.firePropertyChange(PROP_MAXINTENSITY, oldValue, d);
    }
    public final String PROP_MASSSPECTRUM = "massSpectrum";

    @Override
    public Tuple2D<D1, ArrayInt.D1> getMassSpectrum() {
        return metabolite.getMassSpectrum();
    }

    @Override
    public void setMassSpectrum(D1 d1, ArrayInt.D1 d11) {
        Tuple2D<D1, ArrayInt.D1> oldValue = metabolite.getMassSpectrum();
        metabolite.setMassSpectrum(d1, d11);
        pcs.firePropertyChange(PROP_MASSSPECTRUM, oldValue, getMassSpectrum());
    }
    public final String PROP_MW = "mw";

    @Override
    public int getMW() {
        return metabolite.getMW();
    }

    @Override
    public void setMW(int i) {
        int oldValue = metabolite.getMW();
        metabolite.setMW(i);
        pcs.firePropertyChange(PROP_MW, oldValue, i);
    }
    public final String PROP_ID = "id";

    @Override
    public String getID() {
        return metabolite.getID();
    }

    @Override
    public void setID(String string) {
        String oldValue = metabolite.getID();
        metabolite.setID(string);
        pcs.firePropertyChange(PROP_ID, oldValue, string);
    }
    public final String PROP_FORMULA = "formula";

    @Override
    public String getFormula() {
        return metabolite.getFormula();
    }

    @Override
    public void setFormula(String string) {
        String oldValue = metabolite.getFormula();
        metabolite.setFormula(string);
        pcs.firePropertyChange(PROP_FORMULA, oldValue, string);
    }
    public final String PROP_DATE = "date";

    @Override
    public String getDate() {
        return metabolite.getDate();
    }

    @Override
    public void setDate(String string) {
        String oldValue = metabolite.getDate();
        metabolite.setDate(string);
        pcs.firePropertyChange(PROP_DATE, oldValue, string);
    }
    public final String PROP_COMMENTS = "comments";

    @Override
    public String getComments() {
        return metabolite.getComments();
    }

    @Override
    public void setComments(String string) {
        String oldValue = metabolite.getComments();
        metabolite.setComments(string);
        pcs.firePropertyChange(PROP_COMMENTS, oldValue, string);
    }

    public String getId() {
        return getID();
    }

    public void setId(String id) {
        setID(id);
    }

    public final String PROP_ORIGIN = "origin";

    private String origin = "Unspecified";

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        String old = this.origin;
        this.origin = origin;
        pcs.firePropertyChange(PROP_ORIGIN, old, this.origin);
    }

    @Override
    public int compareTo(IAnchor t) {
        return metabolite.compareTo(t);
    }

    @Override
    public double getMw() {
        return getMW();
    }

    @Override
    public void setMw(double d) {
        setMW((int) d);
    }

    public static final String PROP_LINK = "link";

    @Override
    public URI getLink() {
        return metabolite.getLink();
    }

    @Override
    public void setLink(URI uri) {
        URI old = metabolite.getLink();
        metabolite.setLink(uri);
        pcs.firePropertyChange(PROP_LINK, old, uri);
    }

}
