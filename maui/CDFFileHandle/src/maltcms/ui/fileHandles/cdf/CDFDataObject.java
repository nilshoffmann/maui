/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
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

import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IFileFragment;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.netbeans.spi.navigator.NavigatorLookupHint;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Node;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import ucar.ma2.DataType;
import ucar.nc2.Attribute;

/**
 *
 * @author Nils Hoffmann
 */
@DataObject.Registration(iconBase = "maltcms/ui/fileHandles/cdf/resources/cdflogo.png", displayName = "#LBL_CDF_File_Name", mimeType = "application/x-cdf")
@NbBundle.Messages("LBL_CDF_File_Name=CDF File")
public class CDFDataObject extends MultiDataObject implements
        IFileFragmentDataObject {

    private InstanceContent ic = new InstanceContent();
    private AbstractLookup lookup = new AbstractLookup(ic);

    /**
     *
     * @param pf
     * @param loader
     * @throws DataObjectExistsException
     * @throws IOException
     */
    public CDFDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        ic.add(new FileFragment(new File(pf.getPath())));
        ic.add(new CDFDataObjectNavigatorLookupHint());
    }

    /**
     *
     * @return
     */
    @Override
    protected Node createNodeDelegate() {
        return new DataNode(this, Children.LEAF) {
            @Override
            protected Sheet createSheet() {
                Sheet sheet = super.createSheet();
//                Sheet sheet = Sheet.createDefault();
                Sheet.Set set = Sheet.createPropertiesSet();
                set.setName("attributes");
                set.setDisplayName("Attributes");
                set.setExpert(false);
                set.setHidden(false);
                set.setPreferred(true);
                IFileFragment fragment = getFragment();
                for (final Attribute attr : fragment.getAttributes()) {
                    Node.Property prop;
                    DataType dt = attr.getDataType();
                    switch (dt) {
                        case BOOLEAN:
                            prop = new PropertySupport.ReadOnly<Boolean>(attr.getName(), Boolean.class, attr.getName(), attr.getName()) {
                                @Override
                                public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
                                    return Boolean.parseBoolean(attr.getStringValue());
                                }
                            };
                            break;
                        case DOUBLE:
                        case FLOAT:
                        case SHORT:
                        case INT:
                        case LONG:
                            prop = new PropertySupport.ReadOnly<Number>(attr.getName(), Number.class, attr.getName(), attr.getName()) {
                                @Override
                                public Number getValue() throws IllegalAccessException, InvocationTargetException {
                                    return attr.getNumericValue();
                                }
                            };
                            break;
                        default:
                            prop = new PropertySupport.ReadOnly<String>(attr.getName(), String.class, attr.getName(), attr.getName()) {
                                @Override
                                public String getValue() throws IllegalAccessException, InvocationTargetException {
                                    return attr.getStringValue();
                                }
                            };
                            break;
                    }
                    set.put(prop);
                }

                sheet.put(set);
                return sheet;

            }
        };
    }

    /**
     *
     * @return
     */
    @Override
    public Lookup getLookup() {
        return new ProxyLookup(getCookieSet().getLookup(), lookup);
    }

    /**
     *
     * @return
     */
    @Override
    public IFileFragment getFragment() {
        return getLookup().lookup(IFileFragment.class);
    }

    private static final class CDFDataObjectNavigatorLookupHint implements
            NavigatorLookupHint {

        @Override
        public String getContentType() {
            return "application/x-cdf";
        }
    };
}
