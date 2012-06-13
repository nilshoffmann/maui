/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.project.api.descriptors;

import java.awt.Color;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public interface IColorizableDescriptor {

    Color getColor();

    void setColor(Color color);
}
