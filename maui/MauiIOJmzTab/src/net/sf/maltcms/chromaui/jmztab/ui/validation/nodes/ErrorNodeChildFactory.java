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

package net.sf.maltcms.chromaui.jmztab.ui.validation.nodes;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabError;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabErrorList;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabErrorType;


public class ErrorNodeChildFactory extends ChildFactory<MZTabError>{

    private final MZTabErrorList errorList;
    
    public ErrorNodeChildFactory(MZTabErrorList errorList) {
        this.errorList = errorList;
    }
    
    private class VoidErrorType extends MZTabErrorType {

        @Override
        public String getCause() {
            return "Validation succeeded!";
        }

        @Override
        public String getOriginal() {
            return "Validation succeeded!";
        }

        @Override
        public Level getLevel() {
            return Level.Info;
        }

        @Override
        public Category getCategory() {
            return Category.Logical;
        }

        @Override
        public Integer getCode() {
            return 0;
        }
        
    }
    
    private class VoidError extends MZTabError {

        public VoidError(VoidErrorType type, int lineNumber, String... values) {
            super(type, lineNumber, "Validation passed without errors!");
        }
        
    }
    
    @Override
    protected boolean createKeys(List<MZTabError> list) {
        if(errorList.isEmpty()) {
            list.add(new VoidError(new VoidErrorType(), -1));
            return true;
        }
        for (int i = 0; i < errorList.size(); i++) {
            list.add(errorList.getError(i));
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(final MZTabError key) {
        if(key instanceof VoidError) {
            AbstractNode node = new AbstractNode(Children.LEAF) {

                @Override
                public Image getIcon(int type) {
                    return ImageUtilities.loadImage("net/sf/maltcms/chromaui/jmztab/resources/MzTabInfo.png");
                }
                
            };
            node.setDisplayName("No validation messages at current error level!");
            return node;
        }
        try {
            BeanNode<MZTabError> errorNode = new BeanNode<MZTabError>(key) {

                @Override
                public Image getIcon(int type) {
                    switch(key.getType().getLevel()) {
                        case Info:
                            return ImageUtilities.loadImage("net/sf/maltcms/chromaui/jmztab/resources/MzTabInfo.png");
                        case Warn:
                            return ImageUtilities.loadImage("net/sf/maltcms/chromaui/jmztab/resources/MzTabWarning.png");
                        case Error:
                            return ImageUtilities.loadImage("net/sf/maltcms/chromaui/jmztab/resources/MzTabError.png");
                    }
                    return super.getIcon(type);
                }
                
            };
            errorNode.setDisplayName(key.getType().getLevel().toString());
            errorNode.setShortDescription(key.getMessage());
            return errorNode;
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return Node.EMPTY;
    }

}
