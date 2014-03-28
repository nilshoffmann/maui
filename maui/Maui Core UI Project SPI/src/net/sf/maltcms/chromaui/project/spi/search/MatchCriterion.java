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
package net.sf.maltcms.chromaui.project.spi.search;

import com.db4o.query.Query;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Nils Hoffmann
 */
public abstract class MatchCriterion {
	abstract Query apply(Query q);
	
	public static final MatchCriterion Void = new MatchCriterion() {
		@Override
		Query apply(Query q) {
			return q;
		}
	};
	
	public static class NumericRange extends MatchCriterion {
		private final String attribute;
		private final double minValue;
		private final double maxValue;

		public NumericRange(String attribute, double minValue, double maxValue) {
			this.attribute = attribute;
			this.minValue = minValue;
			this.maxValue = maxValue;
		}
		
		@Override
		Query apply(Query q) {
			q.descend(attribute).constrain(minValue).greater().and(q.descend(attribute).constrain(maxValue).smaller());
			return q;
		}
	}
	
	public static class ApproximateStringMatch extends MatchCriterion{
		private final String attribute;
		private final String pattern;

		public ApproximateStringMatch(String attribute, String pattern) {
			this.attribute = attribute;
			this.pattern = pattern;
		}
		
		@Override
		Query apply(Query q) {
			q.descend(attribute).constrain(pattern).like();
			return q;
		}
	}
	
	public static class CompositeMatch extends MatchCriterion {
		
		private final List<MatchCriterion> criteria;

		public CompositeMatch(MatchCriterion...mc) {
			criteria = Arrays.asList(mc);
		}
		
		public CompositeMatch add(MatchCriterion m) {
			criteria.add(m);
			return this;
		}
		
		@Override
		Query apply(Query q) {
			Query query = q;
			for(MatchCriterion mc:criteria) {
				query = mc.apply(q);
			}
			return query;
		}
	}
}
