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
@TemplateRegistrations(value={
	@TemplateRegistration(scriptEngine="freemarker",displayName="Groovy Project Script",folder="Maui",content="templates/DefaultProjectScript.groovy"),
	@TemplateRegistration(scriptEngine="freemarker",displayName="Groovy CDF Script",folder="Maui",content="templates/DefaultRawDataScript.groovy"),
	@TemplateRegistration(scriptEngine="freemarker",displayName="Groovy CSV Script",folder="Maui",content="templates/DefaultCSVScript.groovy"),
	@TemplateRegistration(scriptEngine="freemarker",displayName="Groovy/R XCMS Matched Filter Peak Finder Script",folder="Maui",content="templates/XCMSMatchedFilterPeakFinder.groovy"),
})
package net.sf.maltcms.chromaui.groovy;

import org.netbeans.api.templates.TemplateRegistration;
import org.netbeans.api.templates.TemplateRegistrations;

