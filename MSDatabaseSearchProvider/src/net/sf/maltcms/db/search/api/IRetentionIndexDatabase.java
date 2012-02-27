/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.db.search.api;

import net.sf.maltcms.db.search.api.ri.RetentionIndexCalculator;


/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public interface IRetentionIndexDatabase {

    public RetentionIndexCalculator getRetentionIndexCalculator();

    public double getRi(double rt);
}
