/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author mwilhelm
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
