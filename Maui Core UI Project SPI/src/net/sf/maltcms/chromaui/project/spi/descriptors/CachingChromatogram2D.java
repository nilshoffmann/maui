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
package net.sf.maltcms.chromaui.project.spi.descriptors;

import cross.Factory;
import cross.annotations.Configurable;
import cross.cache.CacheFactory;
import cross.cache.ICacheDelegate;
import cross.cache.ICacheElementProvider;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.ImmutableVariableFragment2;
import cross.datastructures.fragments.VariableFragment;
import cross.exception.ResourceNotAvailableException;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import maltcms.datastructures.ms.IChromatogram2D;
import maltcms.datastructures.ms.IExperiment2D;
import maltcms.datastructures.ms.IScan2D;
import maltcms.datastructures.ms.Scan2D;
import maltcms.tools.MaltcmsTools;
import org.apache.commons.configuration.Configuration;
import ucar.ma2.Array;

/**
 *
 * Use
 *
 * @see maltcms.datastructures.ms.Chromatogram1D instead!
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@Slf4j
public class CachingChromatogram2D implements IChromatogram2D, ICacheElementProvider<Integer, SerializableScan2D> {

	private IFileFragment parent;
	private final String scanAcquisitionTimeUnit = "seconds";
	@Configurable(name = "var.scan_acquisition_time")
	private String scan_acquisition_time_var = "scan_acquisition_time";
	private String first_column_elution_time_var = "first_column_elution_time";
	private String second_column_elution_time_var = "second_column_elution_time";
	private List<Array> massValues;
	private List<Array> intensityValues;
	private Array firstColumnElutionTimeArray;
	private Array secondColumnElutionTimeArray;
	private ICacheDelegate<Integer, SerializableScan2D> whm;
	private int scans;
	private boolean initialized = false;
	private double satOffset = 0;
	private double modulationTime = -1;
	private double scanRate = 0.0;
	private RtProvider rtProvider = null;
	private int prefetchSize = 100000;
	private int spm = -1;
	private int modulations = -1;
	private AtomicBoolean loading = new AtomicBoolean(false);
	private static ExecutorService prefetchLoader = Executors.newFixedThreadPool(2);
	private int[] lastPrefetchRange = new int[]{-1, -1};

	public CachingChromatogram2D(final IFileFragment e) {
		this.parent = e;
		whm = CacheFactory.createAutoRetrievalCache(UUID.nameUUIDFromBytes(e.getUri().toString().getBytes()).toString(), this);
//        fillCache(scans,mzV,iV);
		init();
	}

	private void init() {
//        if (!initialized) {
		final String mz = Factory.getInstance().getConfiguration().getString(
				"var.mass_values", "mass_values");
		final String intens = Factory.getInstance().getConfiguration().getString(
				"var.intensity_values", "intensity_values");
		final String scan_index = Factory.getInstance().getConfiguration().
				getString("var.scan_index", "scan_index");
		final IVariableFragment index = this.parent.getChild(scan_index);
		this.scans = MaltcmsTools.getNumberOfScans(this.parent);
		final IVariableFragment mzV = this.parent.getChild(mz);
		mzV.setIndex(index);
		activateCache(mzV);
		massValues = mzV.getIndexedArray();
		setPrefetchSize(scans, massValues);
		final IVariableFragment iV = this.parent.getChild(intens);
		iV.setIndex(index);
		activateCache(iV);
		intensityValues = iV.getIndexedArray();
		setPrefetchSize(scans, intensityValues);
		this.satOffset = parent.getChild("scan_acquisition_time").getArray().
				getDouble(0);
		modulationTime = parent.getChild("modulation_time").getArray().getDouble(0);
		scanRate = parent.getChild("scan_rate").getArray().getDouble(0);
		spm = (int) (Math.ceil(modulationTime * scanRate));
		modulations = (int) (scans / spm);
//        this.prefetchSize = scans/10;
		try {
			this.parent.getChild("first_column_elution_time");
			this.parent.getChild("second_column_elution_time");
			rtProvider = new ArbitraryModulationRtProvider("first_column_elution_time", "second_column_elution_time", parent);
		} catch (ResourceNotAvailableException rnae) {
			rtProvider = new UniformModulationRtProvider("scan_acquisition_time", parent);
//                IScanLine scanLine = ScanLineCacheFactory.getDefaultScanLineCache(parent);
			System.out.println("Generating first and second column elution time!");
			IVariableFragment fcmt = new VariableFragment(this.parent, "first_column_elution_time");
			IVariableFragment scmt = new VariableFragment(this.parent, "second_column_elution_time");
			Array satA = this.parent.getChild("scan_acquisition_time").getArray();
			Array fceta = Array.factory(satA.getElementType(), satA.getShape());
			Array sceta = Array.factory(satA.getElementType(), satA.getShape());
			for (int i = 0; i < this.scans; i++) {
//                    System.out.println("Processing scan "+i);
//                    Point p = scanLine.mapIndex(i);
//                    final Tuple2D<Array, Array> t = scanLine.getSparseMassSpectra(p.x, p.y);
				double[] rts = rtProvider.getRts(i);
				fceta.setDouble(i, rts[0]);
				sceta.setDouble(i, rts[1]);
			}
			fcmt.setArray(fceta);
			scmt.setArray(sceta);
		}
//            initialized = true;
//        }
	}

//    protected void fillCache(final int scans, final IVariableFragment masses, final IVariableFragment intensities) {
//        Runnable r1 = new Runnable() {
//
//            @Override
//            public void run() {
//                masses.getIndexedArray().get((scans / 4) - 1);
//            }
//        };
//        Runnable r2 = new Runnable() {
//
//            @Override
//            public void run() {
//                intensities.getIndexedArray().get((scans / 4) - 1);
//            }
//        };
//        ExecutorService es = Executors.newFixedThreadPool(1);
//        es.submit(r1);
//        es.submit(r2);
//        es.shutdown();
//    }
	protected void setPrefetchSize(int scans, List<Array> list) {
//        if (list instanceof CachedList) {
//            ((CachedList) list).setCacheSize(prefetchSize);
//            ((CachedList) list).setPrefetchOnMiss(true);
//        }
	}

	protected void activateCache(IVariableFragment ivf) {
		if (ivf instanceof ImmutableVariableFragment2) {
			System.out.println("Using cached access on variable: " + ivf);
			((ImmutableVariableFragment2) ivf).setUseCachedList(true);
		}
		if (ivf instanceof VariableFragment) {
			System.out.println("Using cached access on variable: " + ivf);
			((VariableFragment) ivf).setUseCachedList(true);
		}
	}

	protected Scan2D acquireFromCache(final int i) {
		if (whm.get(i) == null) {
			whm.put(Integer.valueOf(i), provide(i));
			if (!loading.get()) {
				Runnable r = new Runnable() {
					@Override
					public void run() {
						int minBound = Math.max(0, i - prefetchSize);
						int maxBound = Math.min(getNumberOfScans(), i + prefetchSize);
						for (int j = minBound; j <= maxBound; j++) {
							whm.put(Integer.valueOf(j), provide(j));
						}
						lastPrefetchRange = new int[]{minBound, maxBound};
						loading.compareAndSet(true, false);
					}
				};
				prefetchLoader.submit(r);
			}
		}
		return whm.get(Integer.valueOf(i)).getScan();
	}

	protected Scan2D buildScan(int i) {
		return acquireFromCache(i);
	}

	@Override
	public void configure(final Configuration cfg) {
		this.scan_acquisition_time_var = cfg.getString(
				"var.scan_acquisition_time", "scan_acquisition_time");
		this.first_column_elution_time_var = cfg.getString("var.first_column_elution_time");
		this.second_column_elution_time_var = cfg.getString("var.second_column_elution_time");
	}

	@Override
	public List<Array> getIntensities() {
		return intensityValues;
	}

	@Override
	public List<Array> getMasses() {
		return massValues;
	}

	/**
	 * @param scan scan index to load
	 */
	@Override
	public Scan2D getScan(final int scan) {
//        init();
		return buildScan(scan);
	}

	@Override
	public String getScanAcquisitionTimeUnit() {
		return this.scanAcquisitionTimeUnit;
	}

	public List<Scan2D> getScans() {
//        init();
		ArrayList<Scan2D> al = new ArrayList<Scan2D>();
		for (int i = 0; i < getNumberOfScans(); i++) {
			al.add(buildScan(i));
		}
		return al;
	}

	/**
	 * This iterator acts on the underlying collection of scans in
	 * Chromatogram1D, so be careful with concurrent access / modification!
	 */
	@Override
	public Iterator<IScan2D> iterator() {

		final Iterator<IScan2D> iter = new Iterator<IScan2D>() {
			private int currentPos = 0;

			@Override
			public boolean hasNext() {
//                init();
				if (this.currentPos < getScans().size() - 1) {
					return true;
				}
				return false;
			}

			@Override
			public IScan2D next() {
				return getScan(this.currentPos++);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException(
						"Can not remove scans with iterator!");
			}
		};
		return iter;
	}

	public void setExperiment(final IExperiment2D e) {
		this.parent = e;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see maltcms.datastructures.ms.IChromatogram#getScanAcquisitionTime()
	 */
	@Override
	public Array getScanAcquisitionTime() {
//        init();
		return this.parent.getChild(this.scan_acquisition_time_var).getArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see maltcms.datastructures.ms.IChromatogram#getNumberOfScans()
	 */
	@Override
	public int getNumberOfScans() {
//        init();
		return MaltcmsTools.getNumberOfScans(this.parent);
	}

	@Override
	public int getIndexFor(double scan_acquisition_time) {
		double[] d = (double[]) getScanAcquisitionTime().get1DJavaArray(
				double.class);
		double[] ds = (double[]) getScanAcquisitionTime().get1DJavaArray(
				double.class);
		Arrays.sort(ds);
		if (!Arrays.equals(d, ds)) {
			throw new IllegalStateException("scan_acquisition_time values must be sorted!");
		}
		int idx = Arrays.binarySearch(d, scan_acquisition_time);
		if (idx >= 0) {// exact hit
			log.info("sat {}, scan_index {}",
					scan_acquisition_time, idx);
			return idx;
		} else {// imprecise hit, find closest element
			//insertion index
			//insert after
			idx = -idx - 1;
			int insertAfter = Math.max(0, Math.min(idx, d.length - 1));
			int next = Math.min(idx + 1, d.length - 1);
			if (insertAfter == next) {
				return insertAfter;
			}
			System.out.println("InsertAfter: "+insertAfter + " @"+d[insertAfter]);
			System.out.println("Next: "+next+" @"+d[next]);
			//FIXME validate
			double currentSat = d[insertAfter];
			double nextSat = d[next];
			if (Math.abs(scan_acquisition_time - currentSat) < Math.abs(
					scan_acquisition_time - nextSat)) {
				log.info("sat {}, scan_index {}",
						scan_acquisition_time, insertAfter);
				return insertAfter;
			} else {
				log.info("sat {}, scan_index {}",
						scan_acquisition_time, next);
				return next;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see maltcms.datastructures.ms.IChromatogram#getParent()
	 */
	@Override
	public IFileFragment getParent() {
//        init();
		return this.parent;
	}

	@Override
	public SerializableScan2D provide(Integer k) {
//        init();
		final Array masses = massValues.get(k);
		final Array intens = intensityValues.get(k);
		final double[] rts = rtProvider.getRts(k);
		Scan2D s = new Scan2D(masses, intens, k,
				this.parent.getChild(scan_acquisition_time_var).getArray().
				getDouble(k), k, k, rts[0], rts[1]);
		return new SerializableScan2D(s);
	}

	@Override
	public IScan2D getScan2D(int i, int i1) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getNumberOfModulations() {
		return modulations;
	}

	@Override
	public int getNumberOfScansPerModulation() {
		return spm;
	}

	@Override
	public int getNumberOf2DScans() {
		init();
		return this.parent.getChild(this.scan_acquisition_time_var, true).getDimensions()[0].getLength();
	}

	@Override
	public double getModulationDuration() {
		return this.modulationTime;
	}

	@Override
	public String getSecondColumnScanAcquisitionTimeUnit() {
		return "seconds";
	}

	@Override
	public Point getPointFor(int i) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Point getPointFor(double d) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public RtProvider getRtProvider() {
		return this.rtProvider;
	}

	public abstract class RtProvider {

		abstract double[] getRts(int idx);
	}

	public class UniformModulationRtProvider extends RtProvider {

		private final String rtVariable;
		private final IFileFragment resource;
		private final double satOffset;
		private final double modulationTime;
		private final double scanRate;
		private final double spm;
		private final double scanDuration;
		private final Array satArray;

		public UniformModulationRtProvider(String rtVariable, IFileFragment resource) {
			this.rtVariable = rtVariable;
			this.resource = resource;
			this.satOffset = resource.getChild(rtVariable).getArray().
					getDouble(0);
			modulationTime = resource.getChild("modulation_time").getArray().getDouble(0);
			this.scanRate = resource.getChild("scan_rate").getArray().getDouble(0);
			spm = modulationTime * scanRate;
			this.satArray = resource.getChild(rtVariable).getArray();
			this.scanDuration = 1.0d / this.scanRate;
		}

		@Override
		double[] getRts(int idx) {
			final int scanspermodulation = (int) (this.scanRate * this.modulationTime);
			final int scanLineIdx = ((int) idx) / scanspermodulation;
			double sat1 = satOffset + (scanLineIdx * this.modulationTime);
			double sat2 = (((((float) idx) % ((float) scanspermodulation))) * this.scanDuration);
			return new double[]{sat1, sat2};
		}
	}

	public class ArbitraryModulationRtProvider extends RtProvider {

		private final String rt1Variable;
		private final String rt2Variable;
		private final IFileFragment resource;
		private final Array rt1, rt2;

		public ArbitraryModulationRtProvider(String rt1Variable, String rt2Variable, IFileFragment resource) {
			this.rt1Variable = rt1Variable;
			this.rt2Variable = rt2Variable;
			this.resource = resource;
			this.rt1 = resource.getChild(rt1Variable).getArray();
			this.rt2 = resource.getChild(rt2Variable).getArray();
		}

		@Override
		double[] getRts(int idx) {
			return new double[]{rt1.getDouble(idx), rt2.getDouble(idx)};
		}
	}
}
