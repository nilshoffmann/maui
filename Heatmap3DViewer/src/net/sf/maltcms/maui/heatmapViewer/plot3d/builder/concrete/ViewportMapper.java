/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete;

import java.awt.Rectangle;
import org.jzy3d.plot3d.builder.Mapper;

/**
 *
 * @author nilshoffmann
 */
public abstract class ViewportMapper extends Mapper{

    public abstract Rectangle getClippedViewport(Rectangle roi);
    
}
