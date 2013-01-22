/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.chromatogram2Dviewer.datastructures;

/**
 *
 * @author mw
 */
public enum AdditionalInformationTypes {

    NONE {

        @Override
        public String toString() {
            return "None";
        }
    },
    //SCANLINE
    //    HORIZONTAL_SCANLINE{
    //        @Override
    //        public String toString() {
    //            return "horizontal Scanline";
    //        }
    //    },
    //    VERTICAL_SCANLINE{
    //        @Override
    //        public String toString() {
    //            return "vertical Scanline";
    //        }
    //    },
    //GLOBAL
    // TIC
    HORIZONTAL_GLOBAL_TIC {

        @Override
        public String toString() {
            return "global TIC";
        }
    },
    VERTICAL_GLOBAL_TIC {

        @Override
        public String toString() {
            return "global TIC";
        }
    },
    // VTIC
    HORIZONTAL_GLOBAL_VTIC {

        @Override
        public String toString() {
            return "global VTIC";
        }
    },
    VERTICAL_GLOBAL_VTIC {

        @Override
        public String toString() {
            return "global VTIC";
        }
    },
    //LOCAL
    // TIC
    HORIZONTAL_LOCAL_TIC {

        @Override
        public String toString() {
            return "local scanline TIC";
        }
    },
    VERTICAL_LOCAL_TIC {

        @Override
        public String toString() {
            return "local scanline TIC";
        }
    },
    // VTIC
    HORIZONTAL_LOCAL_VTIC {

        @Override
        public String toString() {
            return "local scanline VTIC";
        }
    },
    VERTICAL_LOCAL_VTIC {

        @Override
        public String toString() {
            return "local scanline VTIC";
        }
    },
    //MEAN MS
    HORIZONTAL_MEANMS {

        @Override
        public String toString() {
            return "mean mass spectra";
        }
    },
    VERTICAL_MEANMS {

        @Override
        public String toString() {
            return "mean mass spectra";
        }
    },
    //MAX MS
    HORIZONTAL_MAXMS {

        @Override
        public String toString() {
            return "maximum mass spectra";
        }
    },
    VERTICAL_MAXMS {

        @Override
        public String toString() {
            return "maximum mass spectra";
        }
    },
    //PEAKLIST
    PEAKLIST {

        @Override
        public String toString() {
            return "peaklist";
        }
    };
}
