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
import java.awt.Point;
import java.util.Arrays;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ColorColumnRenderer extends JLabel implements TableCellRenderer {

    private Color colorSelected;
    private Color colorFocus;
    private Color colorNormal;
    private Color colorActive;
    private int[] selectedRows;

    public ColorColumnRenderer(Color selected, int[] selectedRows) {
        setOpaque(true);
        colorNormal = Color.WHITE;
        colorFocus = Color.lightGray;
        colorSelected = new Color(164,164,164);
        colorActive = selected;
        this.selectedRows = selectedRows;
    }

    public void setBackground(Color c, int row) {
        Color color = new Color(c.getRed(),c.getGreen(),c.getBlue());
        if(row%2==0) {
            setBackground(color);
        }else{
            setBackground(reduceBrightness(color,32));
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
        if (hasFocus) {
            setForeground(Color.BLACK);
            setBackground(colorFocus,row);
        } else if (isSelected) {
            setForeground(Color.BLACK);
            setBackground(colorSelected,row);
        } else {
            if(selectedRows.length==0) {
                setForeground(Color.BLACK);
                setBackground(colorNormal,row);
            }else{
                int idx = Arrays.binarySearch(selectedRows, row);
                if(idx>=0) {
                    setForeground(Color.BLACK);
                    setBackground(colorActive,row);
                }else{
                    setForeground(Color.BLACK);
                    setBackground(colorNormal,row);
                }
            }
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
