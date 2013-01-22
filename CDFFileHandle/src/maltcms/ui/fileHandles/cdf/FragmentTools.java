/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
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
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Exceptions;

/**
 *
 * @author nilshoffmann
 */
public class FragmentTools {

    public static List<IVariableFragment> getChildren(IFileFragment fragment) {
        IDataSourceFactory dsf = Factory.getInstance().getDataSourceFactory();
        try {
            List<IVariableFragment> variables = dsf.getDataSourceFor(fragment).readStructure(fragment);
            return variables;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return Collections.emptyList();
    }

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
                if (sf != null && sf.getArray() != null) {
                    Collection<String> c = ArrayTools.getStringsFromArray(sf.getArray());
                    for (String s : c) {
                        Logger.getLogger(FragmentTools.class.getName()).log(Level.INFO, "Processing file " + s);
                        URI uri = URI.create(s);
                        File file = new File(uri);
                        if (file.isAbsolute()) {
                            parentsToExplore.add(new FileFragment(file));
                        } else {
                            try {
                                file = new File(cross.datastructures.tools.FileTools.resolveRelativeFile(new File(fragment.getAbsolutePath()).getParentFile(), new File(s)));
                                parentsToExplore.add(new FileFragment(file.getAbsoluteFile()));
                            } catch (IOException ex) {
                                Logger.getLogger(FragmentTools.class.getName()).log(Level.SEVERE, null, ex);
                            }
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
