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
package net.sf.maltcms.maui.heatmapViewer.actions;

import cross.datastructures.fragments.IVariableFragment;
import cross.exception.ResourceNotAvailableException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.SwingUtilities;
import maltcms.datastructures.ms.IChromatogram;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.quadTree.QuadTree;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareRunnable;
import net.sf.maltcms.maui.heatmapViewer.HeatmapViewerTopComponent;
import net.sf.maltcms.maui.heatmapViewer.chart.controllers.LabeledMouseSelector;
import net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete.ArrayD2Mapper;
import net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete.QuadTreeMapper;
import net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete.SurfaceFactory;
import net.sf.maltcms.maui.heatmapViewer.plot3d.builder.concrete.ViewportMapper;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.chart.controllers.ControllerType;
import org.jzy3d.chart.controllers.mouse.camera.CameraMouseController;
import org.jzy3d.events.ControllerEvent;
import org.jzy3d.events.ControllerEventListener;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.CompileableComposite;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NotImplementedException;
import ucar.ma2.Array;
import ucar.ma2.ArrayInt;
import ucar.ma2.MAMath;
import ucar.ma2.MAMath.MinMax;

@ActionID(
		category = "ContainerNodeActions/ChromatogramNode/Open",
		id = "net.sf.maltcms.maui.heatmapViewer.actions.ViewAs3DHeatmap")
@ActionRegistration(
		displayName = "#CTL_ViewAs3DHeatmap")
@Messages("CTL_ViewAs3DHeatmap=Open as 3D Heatmap")
public final class ViewAs3DHeatmap implements ActionListener {

	private final IChromatogramDescriptor context;

	public ViewAs3DHeatmap(IChromatogramDescriptor context) {
		this.context = context;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (context.getSeparationType().getFeatureDimensions() == 2) {
			ViewAs3DHeatmapRunnable vr = new ViewAs3DHeatmapRunnable();
			ViewAs3DHeatmapRunnable.createAndRun("Creating 3D Heatmap View", vr);
		} else {
			NotifyDescriptor nd = new NotifyDescriptor.Message("Can not open chromatogram with " + context.getSeparationType().getFeatureDimensions() + " separation dimension(s)!", NotifyDescriptor.Message.INFORMATION_MESSAGE);
			DialogDisplayer.getDefault().notify(nd);
		}
	}

	private class ViewAs3DHeatmapRunnable extends AProgressAwareRunnable {

		@Override
		public void run() {
			if (context.getSeparationType().getFeatureDimensions() == 2) {
				try {
					progressHandle.start();
					progressHandle.progress("Loading heatmap data");
					IChromatogram chrom = context.getChromatogram();
					if (chrom instanceof IChromatogram2D) {
						IChromatogram2D chrom2d = (IChromatogram2D) chrom;

						//check for first_column_elution_time and second_column_elution_time
						try {
							IVariableFragment fcet = chrom2d.getParent().getChild("first_column_elution_time", true);
							Array fcetArray = fcet.getArray();
							MinMax fmm = MAMath.getMinMax(fcetArray);
							IVariableFragment scet = chrom2d.getParent().getChild("second_column_elution_time", true);
							Array scetArray = scet.getArray();
							MinMax smm = MAMath.getMinMax(scetArray);
							IVariableFragment tic = chrom2d.getParent().getChild("total_intensity", true);
							Array ticArray = tic.getArray();
							Array modTimeArray = null;
							try {
								IVariableFragment modTime = chrom2d.getParent().getChild("modulation_time");
								modTimeArray = modTime.getArray();
							} catch (ResourceNotAvailableException rnae) {
							}
							MinMax tmm = MAMath.getMinMax(ticArray);
							int length = fcet.getDimensions()[0].getLength();
							final Rectangle2D bounds = new Rectangle2D.Double(fmm.min, smm.min, fmm.max - fmm.min, smm.max - smm.min);
							QuadTree<Integer> qt = new QuadTree<Integer>(bounds);
							for (int i = 0; i < length; i++) {
								qt.put(new Point2D.Float(fcetArray.getFloat(i), scetArray.getFloat(i)), ticArray.getInt(i));
							}
							double radiusx = 10;
							if(modTimeArray!=null) {
								radiusx = modTimeArray.getDouble(0)*3;
							}
							QuadTreeMapper qtm = new QuadTreeMapper(qt, bounds, radiusx, 10);
							fcetArray = null;
							scetArray = null;
							ticArray = null;
							chrom2d.getParent().clearArrays();
							SurfaceFactory sf = new SurfaceFactory();
							AbstractDrawable ad = sf.createImplicitlyGriddedSurface(qtm,bounds,(int)(300),(int)(400));
							System.err.println(ad.getBounds());
							CompileableComposite cc = new CompileableComposite();
							cc.add(ad);

							sf.applyStyling(cc);

							progressHandle.progress("Creating Top Component");

							Chart chart = new Chart(Quality.Intermediate, "newt");
							chart.getScene().getGraph().add(cc);
//							chart.getScene().getGraph().add(new Sphere());
							LabeledMouseSelector lms = new LabeledMouseSelector(chart);
							chart.getCanvas().addKeyListener(lms);

							chart.getAxeLayout().setXAxeLabel("Retention Time 1");
							chart.getAxeLayout().setYAxeLabel("Retention Time 2");
							chart.getAxeLayout().setZAxeLabel("Relative Intensity");

							chart.getView().setMaximized(true);
							chart.getView().getCamera().setScreenGridDisplayed(false);
							CameraMouseController mouse = new CameraMouseController();
							chart.addController(mouse);
							mouse.addControllerEventListener(new ControllerEventListener() {
								public void controllerEventFired(ControllerEvent e) {
									if (e.getType() == ControllerType.PAN) {
										System.out.println("Mouse[PAN]: " + e.getValue());

									} else if (e.getType() == ControllerType.SHIFT) {
										System.out.println("Mouse[SHIFT]: " + e.getValue());
									} else if (e.getType() == ControllerType.ZOOM) {
										System.out.println("Mouse[ZOOM]: " + e.getValue());
									} else if (e.getType() == ControllerType.ROTATE) {
										System.out.println("Mouse[ROTATE]:" + e.getValue());
									}
								}
							});
							//SurfaceViewerPanel svp1 = new SurfaceViewerPanel(chart);
							ChartLauncher.openChart(chart, new Rectangle(800, 600),
									"HeatmapViewer");
//							} catch (IOException ex) {
//								Logger.getLogger(HeatmapViewer.class.getName()).log(Level.SEVERE,
//										null, ex);
//							}

						} catch (ResourceNotAvailableException rnae) {
							final int modulations = chrom2d.getNumberOfModulations();
							final int spm = chrom2d.getNumberOfScansPerModulation();
							final ArrayInt.D2 surface = new ArrayInt.D2(modulations, spm);
							int scanIndex = 0;
							progressHandle.progress("Creating surface");
							Array tic = chrom2d.getParent().getChild("total_intensity").getArray();
							for (int i = 0; i < modulations; i++) {
								for (int j = 0; j < spm; j++) {
									int height = (int) tic.getInt(scanIndex++);
									surface.set(i, j, height);
								}
							}
							final ViewportMapper m = new ArrayD2Mapper(surface);
							progressHandle.progress("Creating Top Component");
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									final HeatmapViewerTopComponent hvtc = new HeatmapViewerTopComponent();
									hvtc.open();
									hvtc.requestActive();
									hvtc.setMapper(m);
								}
							});
						}



						progressHandle.progress("Done!");
					}
				} finally {
					progressHandle.finish();
				}
			} else {
				throw new NotImplementedException("No support for " + context.getSeparationType().getFeatureDimensions() + " separation dimensions!");
			}
		}
	}
}
