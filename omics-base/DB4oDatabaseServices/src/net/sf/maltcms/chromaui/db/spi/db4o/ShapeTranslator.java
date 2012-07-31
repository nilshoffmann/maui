/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.db.spi.db4o;

import com.db4o.ObjectContainer;
import com.db4o.config.ObjectConstructor;
import java.awt.Shape;
import java.awt.geom.Path2D;

/**
 *
 * @author hoffmann
 */
public class ShapeTranslator implements ObjectConstructor {

    @Override
    public Object onStore(ObjectContainer objectContainer, Object objToStore) {
        Shape s = (Shape)objToStore;
        Path2DPersistenceHelper pph = new Path2DPersistenceHelper(s);
        return pph;
    }

    @Override
    public void onActivate(ObjectContainer objectContainer, Object targetObject, Object storedObject) {
        
    }

    @Override
    public Class storedClass() {
        return String.class;
    }

    @Override
    public Object onInstantiate(ObjectContainer objectContainer, Object storedObject) {
        return null;
    }
    
}
