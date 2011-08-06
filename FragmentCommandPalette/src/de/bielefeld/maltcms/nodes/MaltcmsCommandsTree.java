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
public class MaltcmsCommandsTree extends Children.Keys {

    private Package pack;

    public MaltcmsCommandsTree(Package pack) {
        this.pack = pack;
    }

    @Override
    protected Node[] createNodes(Object key) {
        if (key instanceof Package) {
            Package obj = (Package) key;
            return new Node[]{new PackageNode(obj, new MaltcmsCommandsTree(obj))};
        }
        if (key instanceof Command) {
            Command obj = (Command) key;
            return new Node[] {new CommandNode(obj)};
        }
        return null;
    }

    @Override
    protected void addNotify() {
        super.addNotify();

        Object[] objs = null;

        if (this.pack == null) {
            List<Package> packages = MaltcmsServiceTools.getRootPackages();
            objs = new Object[packages.size()];
            int i = 0;
            for (Package p : packages) {
                objs[i++] = p;
            }
        } else {
            List<Package> packages = this.pack.getSubPackages();
            List<Command> commands = this.pack.getCommands();
            objs = new Object[packages.size() + commands.size()];
            int i = 0;
            for (Package p : packages) {
                objs[i++] = p;
            }
            for (Command c : commands) {
                objs[i++] = c;
            }
        }

        setKeys(objs);
    }
}
