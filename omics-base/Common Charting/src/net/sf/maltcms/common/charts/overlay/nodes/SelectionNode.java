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
package net.sf.maltcms.common.charts.overlay.nodes;

import java.awt.Image;
import java.beans.IntrospectionException;
import net.sf.maltcms.common.charts.api.selection.ISelection;
import org.openide.explorer.view.CheckableNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;

/**
 *
 * @author Nils Hoffmann
 */
public class SelectionNode extends BeanNode<ISelection> implements CheckableNode {

    private boolean checkable = true;
    private boolean checkEnabled = true;
    
    public SelectionNode(ISelection bean) throws IntrospectionException {
        super(bean);
    }

    public SelectionNode(ISelection bean, Children children) throws IntrospectionException {
        super(bean, children);
    }

    public SelectionNode(ISelection bean, Children children, Lookup lkp) throws IntrospectionException {
        super(bean, children, lkp);
    }
 
    @Override
    public boolean isCheckable() {
        return checkable;
    }

    @Override
    public boolean isCheckEnabled() {
        return checkEnabled;
    }

    @Override
    public Boolean isSelected() {
        return getBean().isVisible();
    }

    @Override
    public void setSelected(Boolean selected) {
        getBean().setVisible(selected);
    }
    
    @Override
    public Image getIcon(int type) {
        if(isSelected()) {
            return ImageUtilities.loadImage(
                "net/sf/maltcms/common/charts/resources/SelectionVisible.png");
        }else{
            return ImageUtilities.loadImage(
                "net/sf/maltcms/common/charts/resources/SelectionHidden.png");
        }
//        Image descrImage = 
//        int w = descrImage.getWidth(null);
//        int h = descrImage.getHeight(null);
//        if (descr != null) {
//            Color c = descr.getTreatmentGroup().getColor();
//            if (c != null) {
//                BufferedImage bi = new BufferedImage(w / 10, h / 10,
//                        BufferedImage.TYPE_INT_ARGB);
//                Graphics2D g2 = bi.createGraphics();
//
//                g2.setColor(c);
//                g2.fillRect(0, 0, bi.getWidth(), bi.getHeight());
//                descrImage = ImageUtilities.mergeImages(descrImage, bi,
//                        w - bi.getWidth(), h - bi.getHeight());
//            }
//
//        }
//        return descrImage;
    }
    
}
