/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api;

/**
 *
 * @author hoffmann
 */
public class AContainer implements IContainer {

    private String name = getClass().getSimpleName();
    private int precedence = 0;
    private final ICrudProvider cp;

    public AContainer(ICrudProvider db) {
        this.cp = db;
    }

    @Override
    public ICrudProvider getCrudProvider() {
        return cp;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String s) {
        this.name = s;
    }

    @Override
    public int getPrecedence() {
        return this.precedence;
    }

    @Override
    public void setPrecedence(int i) {
        this.precedence = i;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof IContainer) {
            IContainer ic = ((IContainer) o);
            if (getPrecedence() < ic.getPrecedence()) {
                return -1;
            } else if (getPrecedence() > ic.getPrecedence()) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }
}
