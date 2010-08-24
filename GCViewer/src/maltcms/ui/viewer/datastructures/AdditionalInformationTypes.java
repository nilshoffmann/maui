/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maltcms.ui.viewer.datastructures;

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
