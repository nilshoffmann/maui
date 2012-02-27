/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.statistics.view.propertyEditors;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class DoubleArrayPropertyEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        double[] d = (double[]) getValue();
        if (d == null) {
            return "No array set";
        }
        return Arrays.toString(d);
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException {
        double[] d;

        try {
            String tmp = string.substring(1);
            tmp = tmp.substring(0,tmp.length()-1);
            String[] values = tmp.split(",");
            d = new double[values.length];
            for (int i = 0; i < values.length; i++) {
                d[i] = Double.parseDouble(values[i].trim());
            }
//            System.out.println(Arrays.toString(d));
            setValue((double[])d);
        } catch (Exception pe) {
            throw new IllegalArgumentException(pe);
        }
    }
}
