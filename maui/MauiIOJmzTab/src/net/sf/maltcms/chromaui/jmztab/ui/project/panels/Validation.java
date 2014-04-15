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

package net.sf.maltcms.chromaui.jmztab.ui.project.panels;

import org.netbeans.validation.api.Problem;
import static org.netbeans.validation.api.Severity.FATAL;
import static org.netbeans.validation.api.Severity.WARNING;
import org.netbeans.validation.api.ui.ValidationUI;
import org.netbeans.validation.api.ui.swing.ValidationPanel;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;

/**
 *
 * @author Nils Hoffmann
 */
public class Validation {
    public static DialogDescriptor createDialogDescriptor(ValidationPanel vp, Object innerPane, String title) {
      final DialogDescriptor dd = new DialogDescriptor(innerPane, title);
 
      ValidationUI okButtonEnabler = new ValidationUI() {
         private NotificationLineSupport nls = dd.createNotificationLineSupport();
 
         public void showProblem(Problem problem) {
            if (problem != null) {
               switch (problem.severity()) {
                  case FATAL:
                     nls.setErrorMessage(problem.getMessage());
                     dd.setValid(false);
                     break;
                  case WARNING:
                     nls.setWarningMessage(problem.getMessage());
                     dd.setValid(true);
                     break;
                  default:
                     nls.setInformationMessage(problem.getMessage());
                     dd.setValid(true);
               }
            } else {
               nls.clearMessages();
               dd.setValid(true);
            }
         }
 
         public void clearProblem() {
            showProblem(null);
         }
      };
 
      vp.getValidationGroup().addUI(okButtonEnabler);
      vp.getValidationGroup().performValidation();
      return dd;
   }
}
