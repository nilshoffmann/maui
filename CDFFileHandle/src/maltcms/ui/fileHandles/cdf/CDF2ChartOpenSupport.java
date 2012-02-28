/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.cdf;

import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IFileFragment;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import maltcms.ui.fileHandles.serialized.JFCTopComponent;
import org.jfree.chart.JFreeChart;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.DataObject;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author nilshoffmann
 */
/**
 *
 * @author nilshoffmann
 */
public class CDF2ChartOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public CDF2ChartOpenSupport(CDFDataObject.Entry entry) {
        super(entry);
    }
    private List<CDFDataObject> auxDataObjects = new LinkedList<CDFDataObject>();

    public CDF2ChartOpenSupport(CDFDataObject.Entry entry, DataObject... auxDataObjects) {
        this(entry);
        addDataObjects(Arrays.asList(auxDataObjects));
    }

    public CDF2ChartOpenSupport(CDFDataObject.Entry entry, List<DataObject> auxDataObjects) {
        this(entry);
        addDataObjects(auxDataObjects);
    }

    private void addDataObjects(List<DataObject> l) {
        for (DataObject dobj : l) {
            if (dobj instanceof CDFDataObject) {
                auxDataObjects.add((CDFDataObject) dobj);
            }
        }
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        final HashSet<IFileFragment> fragments = new LinkedHashSet<IFileFragment>();
        for (CDFDataObject dataObject : auxDataObjects) {
            fragments.add(new FileFragment(new File(dataObject.getPrimaryFile().getPath())));
        }


        Chromatogram1DChartProvider c1d = new Chromatogram1DChartProvider();
        JFCTopComponent jtc = new JFCTopComponent();
        JFreeChart jfc = c1d.provideChart(new LinkedList<IFileFragment>(fragments));
        if (jfc == null) {
            return null;
        }
        jtc.setChart(jfc);
        jtc.setDisplayName("Chart");
        return jtc;
    }
}
