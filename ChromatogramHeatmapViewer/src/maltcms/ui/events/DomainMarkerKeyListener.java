/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maltcms.ui.events;

import cross.event.EventSource;
import cross.event.IEvent;
import cross.event.IListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;

/**
 *
 * @author nilshoffmann
 */
public class DomainMarkerKeyListener implements KeyListener, XYItemEntityEventSource{

    private XYPlot xyp;

    private final EventSource<XYItemEntity> esource = new EventSource<XYItemEntity>(1);

    public DomainMarkerKeyListener(XYPlot xyp) {
        this.xyp = xyp;
    }
    
    public void setPlot(XYPlot plot) {
        this.xyp = plot;
    }

    public void keyTyped(KeyEvent e) {
        double oldPos = xyp.getDomainCrosshairValue();
        if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
            xyp.setDomainCrosshairValue(oldPos++);
//            xyp.getDataset().get
//            final IEvent<XYItemEntity> v = new XYItemEntityClickedEvent();
//            fireEvent(v);
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT) {
            xyp.setDomainCrosshairValue(oldPos--);
        }
    }

    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }

    public void addListener(IListener<IEvent<XYItemEntity>> il) {
        esource.addListener(il);
    }

    public void fireEvent(IEvent<XYItemEntity> ievent) {
        esource.fireEvent(ievent);
    }

    public void removeListener(IListener<IEvent<XYItemEntity>> il) {
        esource.removeListener(il);
    }

}
