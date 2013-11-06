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
package org.jzy3d.demos;


import org.jzy3d.chart.Chart;
import org.jzy3d.chart.Settings;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.maths.Rectangle;


public class Launcher {
	
	public static void openDemo(IDemo demo) throws Exception{
		openDemo(demo, DEFAULT_WINDOW_DIMENSIONS);
	}
	
	public static void openDemo(IDemo demo, Rectangle rectangle) throws Exception{
		openDemo(demo, rectangle, true);
	}
	
	public static void openDemo(IDemo demo, Rectangle rectangle, boolean screenshot) throws Exception{
		Settings.getInstance().setHardwareAccelerated(true);
		Chart chart = demo.getChart();
		
		ChartLauncher.instructions();
		ChartLauncher.openChart(chart, rectangle, demo.getName());
		if(screenshot)
			ChartLauncher.screenshot(demo.getChart(), "./data/screenshots/"+demo.getName()+".png");
	}
	
	public static void openStaticDemo(IDemo demo) throws Exception{
		openStaticDemo(demo, DEFAULT_WINDOW_DIMENSIONS);
	}
	
	public static void openStaticDemo(IDemo demo, Rectangle rectangle) throws Exception{
		Settings.getInstance().setHardwareAccelerated(true);
		Chart chart = demo.getChart();
		
		ChartLauncher.openStaticChart(chart, rectangle, demo.getName());
		ChartLauncher.screenshot(demo.getChart(), "./data/screenshots/"+demo.getName()+".png");
	}
	
	public static Rectangle DEFAULT_WINDOW_DIMENSIONS = new Rectangle(0,0,600,600);
}

