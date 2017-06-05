package de.unibielefeld.cebitec.lstutz.pca.data;

/**
 *
 * @author nilshoffmann
 */
public final class PrincipalComponent {

    private final int index;
    private final double stdev;

    public PrincipalComponent(int index, double stdev) {
        this.index = index;
        this.stdev = stdev;
    }

    public int getIndex() {
        return index;
    }

    public double getStdev() {
        return stdev;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + this.index;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PrincipalComponent other = (PrincipalComponent) obj;
        if (this.index != other.index) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PC ").append(index).append(" (Stdev. ").append(String.format("%.4f", stdev)).append(")");
        return sb.toString();
    }
}
