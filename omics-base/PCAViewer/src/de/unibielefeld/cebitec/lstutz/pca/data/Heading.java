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
package de.unibielefeld.cebitec.lstutz.pca.data;

public class Heading {

    private String heading_x;
    private String heading_y;
    private String heading_z;

    public Heading(String x, String y, String z) {
        this.heading_x = x;
        this.heading_y = y;
        this.heading_z = z;
    }

    public Heading() {

    }

    public String getHeading_x() {
        return heading_x;
    }

    public String getHeading_y() {
        return heading_y;
    }

    public String getHeading_z() {
        return heading_z;
    }

    public void setHeading_x(String heading_x) {
        this.heading_x = heading_x;
    }

    public void setHeading_y(String heading_y) {
        this.heading_y = heading_y;
    }

    public void setHeading_z(String heading_z) {
        this.heading_z = heading_z;
    }
}
