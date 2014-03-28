/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
package net.sf.maltcms.chromaui.project.api.beans;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.net.URI;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;

/**
 *
 * @author Nils Hoffmann
 */
public class URIPropertyEditor extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory {

    private InplaceEditor ed = null;

    public URIPropertyEditor() {
    }

//	@Override
//	public String getAsText() {
//		URI uri = (URI) getValue();
//		if (uri != null) {
//			
//			return link;
//		} else {
//			return "";
//		}
//	}
//
//	@Override
//	public void setAsText(String string) throws IllegalArgumentException {
//		if(string != null && !string.isEmpty()) {
////		throw new IllegalArgumentException("Editing of URIs is not supported!");
//			setValue(URI.create(string));
//		}
//	}
    @Override
    public void attachEnv(PropertyEnv pe) {
        pe.registerInplaceEditorFactory(this);
    }

    @Override
    public InplaceEditor getInplaceEditor() {
        if (ed == null) {
            ed = new Inplace();
        }
        return ed;
    }

//	@Override
//	public boolean supportsCustomEditor() {
//		return true;
//	}
    private static class Inplace implements InplaceEditor {

//		private final JXDatePicker picker = new JXDatePicker();
        private final JLabel label = new JLabel();
        private URI value;
        private PropertyEditor editor = null;

        public Inplace() {
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    URI uri = (URI) getValue();
                    if (uri != null) {
                        try {
                            if (Desktop.isDesktopSupported()) {
                                Desktop.getDesktop().browse(uri);
                            }
                        } catch (IllegalArgumentException | IOException iae) {
                        }
                    } else {
                        throw new NullPointerException();
                    }
                }
            });
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void connect(PropertyEditor propertyEditor, PropertyEnv env) {
            editor = propertyEditor;
            reset();
        }

        @Override
        public JComponent getComponent() {
            return label;
        }

        @Override
        public void clear() {
            //avoid memory leaks:
            editor = null;
            model = null;
        }

        @Override
        public Object getValue() {
            return this.value;
        }

        @Override
        public void setValue(Object object) {
            this.value = (URI) object;
            setLabel(((URI) object));
        }

        @Override
        public boolean supportsTextEntry() {
            return false;
        }

        @Override
        public void reset() {
//			URI d = (URI) ;
//			if (d != null) {
            setValue(editor.getValue());
//			}
        }

        private void setLabel(URI uri) {
            label.setText(getHtmlLabel(uri));
        }

        public String getHtmlLabel(URI uri) {
            return "<html><font color=\"#000099\"><u>" + uri + "</u></font></html>";
        }

        @Override
        public KeyStroke[] getKeyStrokes() {
            return new KeyStroke[0];
        }

        @Override
        public PropertyEditor getPropertyEditor() {
            return editor;
        }

        @Override
        public PropertyModel getPropertyModel() {
            return model;
        }
        private PropertyModel model;

        @Override
        public void setPropertyModel(PropertyModel propertyModel) {
            this.model = propertyModel;
        }

        @Override
        public boolean isKnownComponent(Component component) {
            return component == label || label.isAncestorOf(component);
        }

        @Override
        public void addActionListener(ActionListener actionListener) {
            //do nothing - not needed for this component
        }

        @Override
        public void removeActionListener(ActionListener actionListener) {
            //do nothing - not needed for this component
        }
    }
}
