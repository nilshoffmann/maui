/*
 * $license$
 *
 * $Id$
 */
package net.sf.maltcms.chromaui.normalization.spi;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public enum PvalueAdjustment {

    bonferroni, holm, hochberg, hommel, BH, BY, none;

    public String getDisplayName() {
        PvalueAdjustment pva = this;
        switch (pva) {
            case holm:
                return "Holm";
            case hochberg:
                return "Hochberg";
            case hommel:
                return "Hommel";
            case bonferroni:
                return "Bonferroni";
            case BH:
                return "Benjamini & Hochberg FDR";
            case BY:
                return "Benjamini & Yekutieli";
            default:
                return "None";
        }
    }
};
