/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.charts;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 *
 * @author mwilhelm
 */
public class MetricNumberFormatter extends NumberFormat {

    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
//                    System.out.println("befor: " + number);

        int digitcount = (int) Math.log10(number);
        String e = "";
        if (digitcount >= 3) {
            number /= 1000;
            e = "k";
        }
        if (digitcount >= 6) {
            number /= 1000;
            e = "M";
        }

        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(1);

        StringBuffer sb = new StringBuffer(formatter.format(number) + e);
//                    System.out.println("after: " + sb);
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
