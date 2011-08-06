/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.nodes;

import java.awt.Image;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Utilities;

/**
 *
 * @author mwilhelm
 */
public class RootNode extends AbstractNode {

    /** Creates a new instance of RootNode */
    public RootNode(Children children) {
        super(children);
    }

//    @Override
//    public Image getIcon(int type) {
//        return Utilities.loadImage("org/netbeans/myfirstexplorer/right-rectangle.png");
//    }
//
//    @Override
//    public Image getOpenedIcon(int type) {
//        return Utilities.loadImage("org/netbeans/myfirstexplorer/down-rectangle.png");
//    }
}
