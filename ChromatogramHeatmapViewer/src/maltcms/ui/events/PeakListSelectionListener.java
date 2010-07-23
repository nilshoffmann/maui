/**
 * 
 */
package maltcms.ui.events;

import cross.event.AEvent;
import cross.event.IEvent;
import cross.event.IListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import org.jfree.chart.annotations.XYAnnotation;

import cross.event.EventSource;
import javax.swing.DefaultListModel;

public class PeakListSelectionListener implements XYAnnotationEventSource, ListSelectionListener {

	
	private final DefaultListModel xyalm;

        private final EventSource<XYAnnotation> es = new EventSource<XYAnnotation>(1);
	
	public PeakListSelectionListener(DefaultListModel xyalm) {
		this.xyalm = xyalm;
	}
	/* (non-Javadoc)
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        int index = e.getFirstIndex();
        XYAnnotation xya = (XYAnnotation)this.xyalm.get(index);
        fireEvent(new AEvent<XYAnnotation>(xya, this, "XYANNOTATION_SELECT"));
    }

    public void addListener(IListener<IEvent<XYAnnotation>> il) {
        this.es.addListener(il);
    }

    public void fireEvent(IEvent<XYAnnotation> ievent) {
        this.es.fireEvent(ievent);
    }

    public void removeListener(IListener<IEvent<XYAnnotation>> il) {
        this.es.removeListener(il);
    }
	
}