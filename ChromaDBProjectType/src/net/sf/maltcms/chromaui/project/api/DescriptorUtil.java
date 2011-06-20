/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api;

import java.io.File;
import java.io.IOException;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;

/**
 *
 * @author nilshoffmann
 */
public class DescriptorUtil {
    
    public DataObject getDataObject(IResourceDescriptor id) throws DataObjectNotFoundException, IOException {
        return DataObject.find(FileUtil.createData(new File(id.getResourceLocation())));
    }
    
}
