/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.nodes;

import de.bielefeld.maltcms.nodes.propertySupport.CommandPropertySupport;
import de.bielefeld.maltcms.nodes.propertySupport.CommandPropertySupportFactory;
import de.bielefeld.maltcms.nodes.propertySupport.DynamicPropertySet;
import de.bielefeld.maltcms.nodes.propertySupport.StaticPropertySet;
import de.bielefeld.maltcms.tools.MaltcmsServiceTools;
import java.util.ArrayList;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author mwilhelm
 */
public class CommandNode extends AbstractNode {

    private Command com;
    private PropertySet[] psa;

    public CommandNode(Command com) {
        super(Children.LEAF, Lookups.fixed(new Object[]{com}));
        setDisplayName(com.getName());
        this.com = com;
    }

    protected void initPropertySets() {

        List<PropertySet> lps = new ArrayList<PropertySet>();

        Sheet.Set set = Sheet.createPropertiesSet();
        lps.add(set);
        Command command = getLookup().lookup(Command.class);

        for (String s : command.getKeySetForCategory(Command.PROPERTIES)) {
            CommandPropertySupport t = CommandPropertySupportFactory.getSupportFor(command, Command.PROPERTIES, s);

            String value = command.getValueForCategory(Command.PROPERTIES, s);
            System.out.print(value + ": " + command.isCategory(value) + " - ");
            if (command.isCategory(value)) {
                String shortName = MaltcmsServiceTools.getLast(s);
                System.out.print(shortName);
                DynamicPropertySet tps = new DynamicPropertySet(shortName, value, command);
                t.setPropertyChangeListener(tps);
                tps.setValue("tabName", shortName);
                lps.add(tps);
            }
            System.out.println("");
            set.put(t);
        }

        StaticPropertySet sps = new StaticPropertySet("Variables", Command.VARIABLES, command);
        sps.setValue("tabName", "Variables");
        lps.add(sps);

        this.psa = new PropertySet[lps.size()];
        int i = 0;
        for (PropertySet ps : lps) {
            this.psa[i++] = ps;
        }
    }

    @Override
    public PropertySet[] getPropertySets() {
        if (this.psa == null) {
            initPropertySets();
        }
        return psa;
    }

    @Override
    public CommandNode clone() {
        return new CommandNode(this.com.clone());
    }
}
