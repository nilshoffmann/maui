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
public class LongArrayPropertyEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        long[] d = (long[]) getValue();
        if (d == null) {
            return "No array set";
        }
        return Arrays.toString(d);
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException {
        long[] d;

        try {
            String tmp = string.substring(1);
            tmp = tmp.substring(0, tmp.length() - 1);
            String[] values = tmp.split(",");
            d = new long[values.length];
            for (int i = 0; i < values.length; i++) {
                d[i] = (long)Long.parseLong(values[i].trim());
            }
//            System.out.println(Arrays.toString(d));
            setValue((long[]) d);
        } catch (Exception pe) {
            throw new IllegalArgumentException(pe);
        }
    }
}
