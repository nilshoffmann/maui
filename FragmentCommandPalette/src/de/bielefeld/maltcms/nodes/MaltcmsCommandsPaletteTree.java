/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.nodes;

import de.bielefeld.maltcms.tools.MaltcmsServiceTools;
import java.util.List;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author mwilhelm
 */
public class MaltcmsCommandsPaletteTree extends Children.Keys {

    private Package pack;

    public MaltcmsCommandsPaletteTree(Package pack) {
        this.pack = pack;
    }

    @Override
    protected Node[] createNodes(Object key) {
        if (key instanceof Package) {
            Package obj = (Package) key;
            return new Node[]{new PackageNode(obj, new MaltcmsCommandsPaletteTree(obj))};
        }
        if (key instanceof Command) {
            Command obj = (Command) key;
            return new Node[]{new CommandNode(obj)};
        }
        return null;
    }

    @Override
    protected void addNotify() {
        super.addNotify();

        Object[] objs = null;

        int i = 0;
        if (this.pack == null) {
            List<Package> packages = MaltcmsServiceTools.getPackagesWithCommands();
            objs = new Object[packages.size()];
            for (Package p : packages) {
                objs[i++] = p;
            }
        } else {
            List<Command> commands = this.pack.getCommands();
            objs = new Object[commands.size()];
            for (Command c : commands) {
                objs[i++] = c;
            }
        }

        setKeys(objs);
    }
}
