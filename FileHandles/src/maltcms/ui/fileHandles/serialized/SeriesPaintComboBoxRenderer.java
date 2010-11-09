/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.serialized;

import cross.datastructures.tuple.Tuple2D;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;

/**
 *
 * @author nilshoffmann
 */
public class SeriesPaintComboBoxRenderer implements ListCellRenderer {

    private XYPlot xyp = null;

    public SeriesPaintComboBoxRenderer(XYPlot xyp) {
        this.xyp = xyp;
    }

    @Override
    public Component getListCellRendererComponent(JList jlist, Object o, int i, boolean bln, boolean bln1) {
        Tuple2D<Comparable, Paint> t = (Tuple2D<Comparable, Paint>) o;
        ColorPanel cp = new ColorPanel(t.getFirst(), t.getSecond());
        cp.setXYPlot(this.xyp);
        return cp;
    }

    private BufferedImage createColorImage(Paint p) {
        BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        g2.setPaint(p);
        g2.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        return bi;
    }

    public class ColorPanel extends JPanel implements ActionListener {

        private Paint p;
        private Comparable c;
        private JButton button;
        private XYPlot xyp = null;

        public ColorPanel(Comparable c, Paint p) {
            this.c = c;
            this.p = p;
            add(new JLabel(c.toString()));
            button = new JButton(new ImageIcon(createColorImage(p)));
            button.setActionCommand("SET COLOR");
            button.addActionListener(this);
            add(button);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (ae.getActionCommand().equals("SET COLOR")) {
                if (this.p instanceof Color) {
                    this.p = JColorChooser.showDialog(this.getTopLevelAncestor(), "Choose a new color", (Color) this.p);
                    updateXYPlot();
                }
            }
        }

        private void updateXYPlot() {
            if (this.xyp != null) {
                int seriesCount = xyp.getSeriesCount();
                for (int j = 0; j < xyp.getDatasetCount(); j++) {
                    XYItemRenderer xyi = xyp.getRenderer(j);
                    for (int i = 0; i < seriesCount; i++) {
                        Comparable s = xyp.getDataset(j).getSeriesKey(i);
                        if(s.equals(this.c)) {
                            xyi.setSeriesPaint(i, this.p);
                        }
                    }
                }
            }
        }

        public void setXYPlot(XYPlot xypl) {
            this.xyp = xypl;
        }
    }
}
