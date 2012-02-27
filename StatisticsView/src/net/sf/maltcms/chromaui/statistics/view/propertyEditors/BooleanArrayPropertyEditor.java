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
public class BooleanArrayPropertyEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        boolean[] d = (boolean[]) getValue();
        if (d == null) {
            return "No array set";
        }
        return Arrays.toString(d);
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException {
        boolean[] d;

        try {
            String tmp = string.substring(1);
            tmp = tmp.substring(0,tmp.length()-1);
            String[] values = tmp.split(",");
            d = new boolean[values.length];
            for (int i = 0; i < values.length; i++) {
                d[i] = Boolean.parseBoolean(values[i].trim());
            }
//            System.out.println(Arrays.toString(d));
            setValue((boolean[])d);
        } catch (Exception pe) {
            throw new IllegalArgumentException(pe);
        }
    }
}
