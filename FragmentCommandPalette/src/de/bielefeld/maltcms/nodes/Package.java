/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bielefeld.maltcms.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mwilhelm
 */
public class Package {

    private String fqdn;
    private List<Package> subPackages = new ArrayList<Package>();
    private List<Command> commands = new ArrayList<Command>();

    public Package(String fqdn) {
        this.fqdn = fqdn;
    }

    public String getFqdn() {
        return fqdn;
    }

    public void setFqdn(String fqdn) {
        this.fqdn = fqdn;
    }

    public String getName() {
        String[] s = this.fqdn.split("\\.");
        return s[s.length - 1];
    }

    public void addPackage(Package pack) {
        this.subPackages.add(pack);
    }

    public void addCommand(Command com) {
        this.commands.add(com);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public List<Package> getSubPackages() {
        return subPackages;
    }

    public boolean subPackageContains(String s) {
        for (Package p : this.subPackages) {
            if (p.getFqdn().equals(s)) {
                return true;
            }
        }
        return false;
    }
}
