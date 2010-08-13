/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.csv;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 * @author bunkowski
 */
import java.awt.Color;
import java.awt.Component;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ColorColumnRenderer extends JLabel implements TableCellRenderer {

    private Color colorSelected = new Color(0, 0, 200,128).brighter();
    private Color colorFocus = new Color(0, 0, 255,128).brighter();
    private Color colorNormal;

    public ColorColumnRenderer(Color color) {
        setOpaque(true);
        colorNormal = color;
    }

    public void setBackground(Color c, int row) {
        Color color = new Color(c.getRed(),c.getGreen(),c.getBlue(),192);
        if(row%2==0) {
            setBackground(color);
        }else{
            setBackground(reduceBrightness(color,64));
            //setBackground(c.darker());
        }
    }

    private Color reduceBrightness(Color c, int amount) {
        Color ret = new Color(darker(c.getRed(),amount),darker(c.getGreen(),amount),darker(c.getBlue(),amount));
//        System.out.println("Color before: "+c+" color after: "+ret);
        return ret;
    }

    private Color increaseBrightness(Color c, int amount) {
        Color ret = new Color(brighter(c.getRed(),amount),brighter(c.getGreen(),amount),brighter(c.getBlue(),amount));
//        System.out.println("Color before: "+c+" color after: "+ret);
        return ret;
    }

    private int darker(int band, int amount) {
//        System.out.println("Band value: "+band+" amount: "+amount);
        return Math.max(0,band-amount);
    }

    private int brighter(int band, int amount) {
        return Math.min(255,band+amount);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        // die normalen Farben
        setForeground(Color.BLACK);
        if (hasFocus) {
            setBackground(colorFocus,row);
        } else if (isSelected) {
            setBackground(colorSelected,row);
        } else {
            setBackground(colorNormal,row);
        }

        setText(null);
        setIcon(null);

        if (value instanceof Date) {
            setText(((Date) value).toGMTString());
        } else if (value instanceof Icon) {
            setIcon((Icon) value);
        } else if (value instanceof Color) {
            Color color = (Color) value;
            setForeground(color);
            setText(color.getRed() + ", " + color.getGreen() + ", "
                    + color.getBlue());
        } else if (value instanceof Boolean) {
            if (((Boolean) value).booleanValue()) {
                setText("yes");
            } else {
                setText("no");
            }
        } else {
            setText(value.toString());
        }

        return this;
    }
}
