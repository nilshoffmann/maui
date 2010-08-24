/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer.extensions;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

/**
 *
 * @author mwilhelm
 */
public class RetentionTimeNumberFormatter extends NumberFormat {

    private double timePerScanIndex = 0.1d;
    private NumberFormat formatter = new DecimalFormat("#0.00", DecimalFormatSymbols.getInstance(Locale.US));

    public RetentionTimeNumberFormatter(double timePerScanindex) {
        super();
        this.timePerScanIndex = timePerScanindex;
    }

    public RetentionTimeNumberFormatter(double timePerScanindex, String formatString) {
        super();
        this.timePerScanIndex = timePerScanindex;
        this.formatter = new DecimalFormat(formatString, DecimalFormatSymbols.getInstance(Locale.US));
    }

    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
        double newnumber = number * this.timePerScanIndex;
        StringBuffer sb = new StringBuffer();
        sb.append(this.formatter.format(newnumber));
        return sb;
    }

    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Number parse(String source, ParsePosition parsePosition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
