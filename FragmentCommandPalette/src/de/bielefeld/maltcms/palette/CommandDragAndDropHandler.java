/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.palette;

import de.bielefeld.maltcms.nodes.CommandNode;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.netbeans.spi.palette.DragAndDropHandler;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.ExTransferable;

/**
 *
 * @author mwilhelm
 */
public class CommandDragAndDropHandler extends DragAndDropHandler {

    private static final DataFlavor MyCustomDataFlavor = new DataFlavor(Object.class, "MyDND");

    @Override
    public void customize(ExTransferable exTransferable, Lookup lookup) {
        final CommandNode item = lookup.lookup(CommandNode.class);
        if (null != item) {
            exTransferable.put(new ExTransferable.Single(MyCustomDataFlavor) {

                @Override
                protected Object getData() throws IOException, UnsupportedFlavorException {
                    return null;
                }
            });
        }
    }
}
