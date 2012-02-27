/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.normalization.api.ui;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import net.sf.maltcms.chromaui.normalization.spi.PvalueAdjustment;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class PvalueAdjustmentRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setText(((PvalueAdjustment) value).getDisplayName());
        setFont(list.getFont());
        return this;
    }
}
