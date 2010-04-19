/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.fileHandles.serialized;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.Callable;
import org.jfree.chart.JFreeChart;
import org.netbeans.api.progress.ProgressHandle;

/**
 *
 * @author mw
 */
public class JFCLoader implements Callable<JFreeChart> {

    private final String filename;
    private final ProgressHandle ph;

    JFCLoader(String filename, ProgressHandle ph) {
        this.filename = filename;
        this.ph = ph;
    }

    @Override
    public JFreeChart call() throws Exception {
        ph.start(100);
        JFreeChart chart = null;
        try {
            ph.progress("Opening file", 33);
            final ObjectInputStream ois = new ObjectInputStream(
                    new BufferedInputStream(new FileInputStream(
                    filename)));
            ph.progress("Reading object", 66);
            chart = (JFreeChart) ois.readObject();
            ois.close();
        } catch (final ClassNotFoundException e) {
            ph.progress("ClassNotFoundException", 80);
            e.printStackTrace();
        } catch (final IOException e) {
            ph.progress("IOException", 80);
            e.printStackTrace();
        }
        ph.progress("Finished!", 100);
        ph.finish();

        return chart;
    }
}
