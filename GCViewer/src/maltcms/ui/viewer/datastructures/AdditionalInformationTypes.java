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

    NONE,
    //SCANLINE
    HORIZONTAL_SCANLINE,
    VERTICAL_SCANLINE,
    //GLOBAL
    // TIC
    HORIZONTAL_GLOBAL_TIC,
    VERTICAL_GLOBAL_TIC,
    // VTIC
    HORIZONTAL_GLOBAL_VTIC,
    VERTICAL_GLOBAL_VTIC,
    //LOCAL
    // TIC
    HORIZONTAL_LOCAL_TIC,
    VERTICAL_LOCAL_TIC,
    // VTIC
    HORIZONTAL_LOCAL_VTIC,
    VERTICAL_LOCAL_VTIC,
    //MEAN MS
    HORIZONTAL_MEANMS,
    VERTICAL_MEANMS,
    //MAX MS
    HORIZONTAL_MAXMS,
    VERTICAL_MAXMS,
    //PEAKLIST
    PEAKLIST;
}
