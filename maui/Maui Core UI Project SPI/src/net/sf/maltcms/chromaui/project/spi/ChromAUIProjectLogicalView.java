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
package net.sf.maltcms.chromaui.project.spi;

import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.spi.nodes.ChromAUIProjectNode;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 * Implements @see{LogicalViewProvider} for @see{ChromaProject}.
 * Creates a virtual view, which is created from the bound xml file
 * in ChromaProject.
 *
 * @author Nils Hoffmann
 */
public class ChromAUIProjectLogicalView implements LogicalViewProvider {

    private final IChromAUIProject project;

    /**
     * Build a logical view for the given @see{ChromAUIProject} instance.
     * @param project
     */
    public ChromAUIProjectLogicalView(IChromAUIProject project) {
        this.project = project;
    }

    /**
     * Create a logical view, based on @see{ProjectNode}.
     * Automatically adds subnodes/children.
     * @return an instance of @see{ProjectNode} if @see{DataObject} exists, otherwise returns an empty @see{AbstractNode} instance.
     */
    @Override
    public org.openide.nodes.Node createLogicalView() {
        try {

            ChromAUIProjectNode pn = new ChromAUIProjectNode(project);
            return pn;

        } catch (Exception donfe) {
            Exceptions.printStackTrace(donfe);
            //Fallback-the directory couldn't be created -
            //read-only filesystem or something evil happened
            return new AbstractNode(Children.LEAF);
        }
    }

    @Override
    public Node findPath(Node node, Object o) {
        return null;
    }

    /** This is the node you actually see in the project tab for the project */
    
}
