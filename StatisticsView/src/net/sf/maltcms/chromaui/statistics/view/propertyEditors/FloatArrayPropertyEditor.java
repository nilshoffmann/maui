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
public class FloatArrayPropertyEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        float[] d = (float[]) getValue();
        if (d == null) {
            return "No array set";
        }
        return Arrays.toString(d);
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException {
        float[] d;

        try {
            String tmp = string.substring(1);
            tmp = tmp.substring(0,tmp.length()-1);
            String[] values = tmp.split(",");
            d = new float[values.length];
            for (int i = 0; i < values.length; i++) {
                d[i] = Float.parseFloat(values[i].trim());
            }
//            System.out.println(Arrays.toString(d));
            setValue((float[])d);
        } catch (Exception pe) {
            throw new IllegalArgumentException(pe);
        }
    }
}
