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
package net.sf.maltcms.chromaui.ui.support.api.outline;

/**
 *
 * @author Nils Hoffmann
 */
public class ColumnDescriptor {

        public final String name;
        public final String displayName;
        public final String shortDescription;

        public ColumnDescriptor(String name, String displayName,
                String shortDescription) {
            this.name = name;
            this.displayName = displayName;
            this.shortDescription = shortDescription;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ColumnDescriptor other = (ColumnDescriptor) obj;
            if ((this.name == null) ? (other.name != null) : !this.name.equals(
                    other.name)) {
                return false;
            }
            if ((this.displayName == null) ? (other.displayName != null) : !this.displayName.
                    equals(other.displayName)) {
                return false;
            }
            if ((this.shortDescription == null) ? (other.shortDescription != null) : !this.shortDescription.
                    equals(other.shortDescription)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 47 * hash + (this.name != null ? this.name.hashCode() : 0);
            hash = 47 * hash + (this.displayName != null ? this.displayName.
                    hashCode() : 0);
            hash = 47 * hash + (this.shortDescription != null ? this.shortDescription.
                    hashCode() : 0);
            return hash;
        }
    }
