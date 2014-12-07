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
package net.sf.maltcms.db.search.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import maltcms.datastructures.ms.IMetabolite;
import net.sf.maltcms.chromaui.project.api.descriptors.IDatabaseDescriptor;
import net.sf.maltcms.db.search.api.IQueryResult;

/**
 *
 * @author nilshoffmann
 */
@Data
public class QueryResult<T> implements
        IQueryResult<T> {

    private final T scan;
    private final double retentionIndex;
    private final Map<IMetabolite, Double> metabolitesToScore;
    private final Map<IMetabolite, Double> metabolitesToRi;
    private final IDatabaseDescriptor databaseDescriptor;

    @Override
    public List<IMetabolite> getMetabolites() {
        return new ArrayList<>(metabolitesToScore.keySet());
    }

    @Override
    public double getScoreFor(IMetabolite metabolite) {
        return metabolitesToScore.get(metabolite);
    }

    @Override
    public double getRiFor(IMetabolite metabolite) {
        if (metabolitesToRi == null) {
            return Double.NaN;
        }
        if (metabolitesToRi.containsKey(metabolite)) {
            return metabolitesToRi.get(metabolite);
        }
        return Double.NaN;
    }
}
