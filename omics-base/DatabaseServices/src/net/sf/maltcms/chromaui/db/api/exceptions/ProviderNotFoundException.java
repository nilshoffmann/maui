/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.db.api.exceptions;

/**
 *
 * @author hoffmann
 */
public class ProviderNotFoundException extends RuntimeException{

    public ProviderNotFoundException(Throwable cause) {
        super(cause);
    }

    public ProviderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProviderNotFoundException(String message) {
        super(message);
    }

    public ProviderNotFoundException() {
    }

}
