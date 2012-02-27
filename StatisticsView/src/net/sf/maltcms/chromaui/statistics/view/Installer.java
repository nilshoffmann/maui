/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

    @Override
    public void restored() {
//        List<String> paths = new ArrayList<String>(Arrays.asList(PropertyEditorManager.getEditorSearchPath()));
//        paths.add("net.sf.maltcms.chromaui.statistics.view.propertyEditors");
//        PropertyEditorManager.setEditorSearchPath(paths.toArray(new String[paths.size()]));
        PropertyEditorManager.registerEditor(double[].class,
                DoubleArrayPropertyEditor.class);
        PropertyEditorManager.registerEditor(float[].class,
                FloatArrayPropertyEditor.class);
        PropertyEditorManager.registerEditor(long[].class,
                LongArrayPropertyEditor.class);
        PropertyEditorManager.registerEditor(int[].class,
                IntArrayPropertyEditor.class);
        PropertyEditorManager.registerEditor(short[].class,
                ShortArrayPropertyEditor.class);
        PropertyEditorManager.registerEditor(char[].class,
                CharArrayPropertyEditor.class);
        PropertyEditorManager.registerEditor(boolean[].class,
                BooleanArrayPropertyEditor.class);
    }
}
