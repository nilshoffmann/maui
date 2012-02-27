/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.ui.support.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class FileTools {

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel sink = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            sink = new FileOutputStream(destFile).getChannel();
            sink.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (sink != null) {
                sink.close();
            }
        }
    }
}
