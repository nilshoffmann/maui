package org.jzy3d.demos;



import java.awt.Rectangle;

import org.jzy3d.chart.Chart;
import org.jzy3d.global.Settings;
import org.jzy3d.ui.ChartLauncher;


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

