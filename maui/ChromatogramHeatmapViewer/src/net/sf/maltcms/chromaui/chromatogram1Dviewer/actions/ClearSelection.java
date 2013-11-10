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
package net.sf.maltcms.chromaui.chromatogram1Dviewer.actions;

import cross.exception.ResourceNotAvailableException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IChromatogram1D;
import maltcms.datastructures.ms.IScan;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.LookupUtils;
import net.sf.maltcms.common.charts.api.dataset.ADataset1D;
import net.sf.maltcms.common.charts.api.dataset.ADataset2D;
import net.sf.maltcms.common.charts.api.overlay.SelectionOverlay;
import net.sf.maltcms.common.charts.api.selection.IClearable;
import net.sf.maltcms.common.charts.api.selection.ISelection;
import net.sf.maltcms.common.charts.api.selection.XYSelection;
import org.jfree.data.general.Dataset;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.InstanceCookie;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.NotImplementedException;
import ucar.ma2.Array;

/**
 *
 * @author Nils Hoffmann
 */
@ActionID(
		category = "OverlayNodeActions/SelectionOverlay",
		id = "net.sf.maltcms.chromaui.chromatogram1Dviewer.actions.ClearSelection")
@ActionRegistration(
		displayName = "#CTL_ClearSelection")
@NbBundle.Messages("CTL_ClearSelection=Clear")
public final class ClearSelection implements ActionListener {

	private final Node context;

	public ClearSelection(Node context) {
		this.context = context;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		Object obj;
		try {
			obj = context.getLookup().lookup(InstanceCookie.class).instanceCreate();
			if (obj instanceof IClearable) {
				((IClearable) obj).clear();
			}
		} catch (IOException ex) {
			Exceptions.printStackTrace(ex);
		} catch (ClassNotFoundException ex) {
			Exceptions.printStackTrace(ex);
		}
	}
}
