/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.db.spi.db4o.options;

import java.awt.Component;

/**
 *
 * @author hoffmann
 */
public class ValidationMessage {
    
    public static enum Type{
        CLEAR,WARNING,ERROR,INFO;
    };
    
    private final Component source;
    private final Type type;
    private final String message;

    public ValidationMessage(Component source, Type type, String message) {
        this.source = source;
        this.type = type;
        this.message = message;
    }

    public Component getSource() {
        return source;
    }

    public Type getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
    
}
