/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.normalization.api.ui;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakGroupDescriptor;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class CompoundListRenderer extends DefaultListCellRenderer {
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

        setText("@"+String.format("%.2f",((IPeakGroupDescriptor) value).getMedianApexTime())+": "+((IPeakGroupDescriptor) value).getPeakAnnotationDescriptors().get(0).getName());
        setFont(list.getFont());
        return this;
    }
}
