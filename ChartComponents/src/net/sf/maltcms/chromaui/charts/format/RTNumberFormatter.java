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
package net.sf.maltcms.chromaui.charts.format;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.Locale;
import net.sf.maltcms.chromaui.charts.units.RTUnit;
import net.sf.maltcms.chromaui.charts.units.RTUnitTransformer;

/**
 *
 * @author Mathias Wilhelm
 */
public class RTNumberFormatter extends DecimalFormat {

    private NumberFormat formatter = new DecimalFormat("#0.00",
            DecimalFormatSymbols.getInstance(Locale.US));
    private RTUnit unit = RTUnit.SECONDS;

    public RTNumberFormatter(RTUnit unit) {
        super();
        this.unit = unit;
    }

    public RTNumberFormatter(RTUnit unit, String formatString) {
        super();
        this.unit = unit;
        this.formatter = new DecimalFormat(formatString, DecimalFormatSymbols.
                getInstance(Locale.US));
    }

    public void setRTUnit(RTUnit unit) {
        this.unit = unit;
    }

    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo,
            FieldPosition pos) {
        double newnumber = RTUnitTransformer.transform(number, unit);
        this.formatter.setMinimumFractionDigits(0);
        this.formatter.setMaximumFractionDigits(2);
        this.formatter.setMinimumIntegerDigits(1);
        StringBuffer sb = new StringBuffer();
        sb.append(this.formatter.format(newnumber));
        return sb;
    }
}
