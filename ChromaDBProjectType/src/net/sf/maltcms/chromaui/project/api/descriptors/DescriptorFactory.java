/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api.descriptors;

import java.io.File;
import java.io.IOException;
import net.sf.maltcms.chromaui.project.spi.descriptors.ChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.TreatmentGroupDescriptor;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;

/**
 *
 * @author nilshoffmann
 */
public class DescriptorFactory {
    
    public static DataObject getDataObject(IResourceDescriptor id) throws DataObjectNotFoundException, IOException {
        return DataObject.find(FileUtil.createData(new File(id.getResourceLocation())));
    }
    
    public static IChromatogramDescriptor newChromatogramDescriptor() {
        return new ChromatogramDescriptor();
    }
    
    public static ITreatmentGroupDescriptor newTreatmentGroupDescriptor(String name) {
        return new TreatmentGroupDescriptor(name);
    }
    
}
