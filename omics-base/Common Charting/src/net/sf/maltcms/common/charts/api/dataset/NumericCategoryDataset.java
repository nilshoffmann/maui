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
package net.sf.maltcms.common.charts.api.dataset;

import java.util.List;
import net.sf.maltcms.common.charts.api.selection.IDisplayPropertiesProvider;
import net.sf.maltcms.common.charts.api.selection.ISelection;

/**
 *
 * @author Nils Hoffmann
 */
public class NumericCategoryDataset extends ACategoryDataset<List<Double>, Double> {

    public NumericCategoryDataset(List<INamedElementProvider<? extends List<Double>, ? extends Double>> l) {
        super(l, new IDisplayPropertiesProvider() {

            @Override
            public String getName(ISelection selection) {
                return selection.getName();
            }

            @Override
            public String getDisplayName(ISelection selection) {
                return selection.getDisplayName();
            }

            @Override
            public String getShortDescription(ISelection selection) {
                return selection.getShortDescription();
            }

            @Override
            public String getSourceName(ISelection selection) {
                return selection.getSource().getClass().getSimpleName();
            }

            @Override
            public String getSourceDisplayName(ISelection selection) {
                return selection.getSource().getClass().getSimpleName();
            }

            @Override
            public String getSourceShortDescription(ISelection selection) {
                return selection.getSource().getClass().getSimpleName();
            }

            @Override
            public String getTargetName(ISelection selection) {
                return getName(selection);
            }

            @Override
            public String getTargetDisplayName(ISelection selection) {
                return getDisplayName(selection);
            }

            @Override
            public String getTargetShortDescription(ISelection selection) {
                return getShortDescription(selection);
            }

        });
        for (INamedElementProvider<? extends List<Double>, ? extends Double> nep : l) {
            for (int i = 0; i < nep.size(); i++) {
                Number n = nep.getSource().get(i);
                addValue(n, nep.getKey(), i);
            }
        }
    }

}
