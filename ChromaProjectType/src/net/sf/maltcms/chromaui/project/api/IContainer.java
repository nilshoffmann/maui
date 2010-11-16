/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.project.api;

import net.sf.maltcms.chromaui.db.api.ICrudProvider;
import net.sf.maltcms.chromaui.db.spi.DB4oCrudProvider;

/**
 *
 * @author hoffmann
 */
public interface IContainer extends Comparable{

    String getName();

    void setName(String s);

    int getPrecedence();

    void setPrecedence(int i);

    ICrudProvider getCrudProvider();

}
