/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.cdf;

import cross.Factory;
import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.exception.ResourceNotAvailableException;
import cross.io.IDataSourceFactory;
import cross.datastructures.tools.ArrayTools;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import maltcms.ui.charts.JFreeChartViewer.ChromatogramOpenAction;
import org.openide.util.Exceptions;

/**
 *
 * @author nilshoffmann
 */
public class FragmentTools {

    public static List<IVariableFragment> getAggregatedVariables(IFileFragment fragment) {
        IDataSourceFactory dsf = Factory.getInstance().getDataSourceFactory();
        HashMap<String, IVariableFragment> names = new HashMap<String, IVariableFragment>();
        List<IVariableFragment> allVars = new ArrayList<IVariableFragment>();
        List<IFileFragment> parentsToExplore = new LinkedList<IFileFragment>();
//        System.out.println("Parent files " + parentsToExplore);
        parentsToExplore.add(fragment);
        while (!parentsToExplore.isEmpty()) {
            IFileFragment parent = parentsToExplore.remove(0);
            try {
                IVariableFragment sf = parent.getChild("source_files", true);
                Collection<String> c = ArrayTools.getStringsFromArray(sf.getArray());
                for (String s : c) {
                    Logger.getLogger(FragmentTools.class.getName()).log(Level.INFO,"Processing file "+s);
                    File file = new File(s);
                    if (file.isAbsolute()) {
                        parentsToExplore.add(new FileFragment(file));
                    } else {
                        try {
                            file = new File(cross.datastructures.tools.FileTools.resolveRelativeFile(new File(fragment.getAbsolutePath()).getParentFile(), new File(s)));
                            parentsToExplore.add(new FileFragment(file.getCanonicalFile()));
                        } catch (IOException ex) {
                            Logger.getLogger(FragmentTools.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } catch (ResourceNotAvailableException rnae) {
            }
//            System.out.println("Parent files " + parentsToExplore);
            try {
                List<IVariableFragment> l = dsf.getDataSourceFor(parent).readStructure(parent);
                for (IVariableFragment ivf : l) {
                    if (!ivf.getName().equals("source_files")) {
                        if (!names.containsKey(ivf.getName())) {
                            names.put(ivf.getName(), ivf);
                            allVars.add(ivf);
                        }

                    }
                }

//                allVars.addAll(l);
            } catch (IOException ex) {
                Logger.getLogger(FragmentTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return allVars;
    }
}
