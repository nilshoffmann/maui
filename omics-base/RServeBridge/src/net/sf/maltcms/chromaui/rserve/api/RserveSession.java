/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.rserve.api;

/**
 *
 * @author hoffmann
 */
public interface RserveSession {
    public String getHostIp();
    public int getHostPort();
    public String getUserName();
    public String getPassword();
}
