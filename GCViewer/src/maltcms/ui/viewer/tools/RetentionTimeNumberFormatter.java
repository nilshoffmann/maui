/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maltcms.ui.viewer.tools;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;

/**
 *
 * @author nilshoffmann
 */
class RetentionTimeNumberFormatter extends DecimalFormat{

    private NumberFormat nf;

    private double scanrate, offset;

    public RetentionTimeNumberFormatter(double scanrate, double offset, String string) {
        nf = new DecimalFormat(string);
        this.scanrate = scanrate;
        this.offset = offset;
    }

    @Override
    public StringBuffer format(double d, StringBuffer sb, FieldPosition fp) {
        double v = (d*scanrate) + offset;
        sb.append(nf.format(v));
        return sb;
    }
}
