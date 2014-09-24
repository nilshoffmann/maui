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
package maltcms.ui.fileHandles.properties.graph;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.general.IconNodeWidget;

/**
 *
 * @author Mathias Wilhelm
 */
public class PipelineInputWidget extends IconNodeWidget {

    private List<File> input = new ArrayList<>();

    public PipelineInputWidget(Scene scene, TextOrientation to) {
        super(scene, to);
    }

    public PipelineInputWidget(Scene scene) {
        super(scene);
    }

    public void addInputFile(File f) {
        if (f != null && f.exists() && f.isFile()) {
            this.input.add(f);
        }
    }

    public List<File> getInputFiles() {
        return this.input;
    }

    public void setInputFiles(List<File> input) {
        if (input != null) {
            this.input = input;
        }
    }
}
