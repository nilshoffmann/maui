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
package net.sf.maltcms.chromaui.statistics.view;

import java.beans.PropertyEditorManager;
import net.sf.maltcms.chromaui.statistics.view.propertyEditors.BooleanArrayPropertyEditor;
import net.sf.maltcms.chromaui.statistics.view.propertyEditors.CharArrayPropertyEditor;
import net.sf.maltcms.chromaui.statistics.view.propertyEditors.DoubleArrayPropertyEditor;
import net.sf.maltcms.chromaui.statistics.view.propertyEditors.FloatArrayPropertyEditor;
import net.sf.maltcms.chromaui.statistics.view.propertyEditors.IntArrayPropertyEditor;
import net.sf.maltcms.chromaui.statistics.view.propertyEditors.LongArrayPropertyEditor;
import net.sf.maltcms.chromaui.statistics.view.propertyEditors.ShortArrayPropertyEditor;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {
    private static final long serialVersionUID = -2738680935457035752L;

    @Override
    public void restored() {
//        List<String> paths = new ArrayList<String>(Arrays.asList(PropertyEditorManager.getEditorSearchPath()));
//        paths.add("net.sf.maltcms.chromaui.statistics.view.propertyEditors");
//        PropertyEditorManager.setEditorSearchPath(paths.toArray(new String[paths.size()]));
//        PropertyEditorManager.registerEditor(double[].class,
//                DoubleArrayPropertyEditor.class);
//        PropertyEditorManager.registerEditor(float[].class,
//                FloatArrayPropertyEditor.class);
//        PropertyEditorManager.registerEditor(long[].class,
//                LongArrayPropertyEditor.class);
//        PropertyEditorManager.registerEditor(int[].class,
//                IntArrayPropertyEditor.class);
//        PropertyEditorManager.registerEditor(short[].class,
//                ShortArrayPropertyEditor.class);
//        PropertyEditorManager.registerEditor(char[].class,
//                CharArrayPropertyEditor.class);
//        PropertyEditorManager.registerEditor(boolean[].class,
//                BooleanArrayPropertyEditor.class);
    }
}
