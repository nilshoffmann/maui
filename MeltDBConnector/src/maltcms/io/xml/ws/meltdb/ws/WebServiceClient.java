/**
 * 
 */
package maltcms.io.xml.ws.meltdb.ws;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import JavaServices.Web.MELT.CreatePeak;
import JavaServices.Web.MELT.Eic_peak;
import JavaServices.Web.MELT.Peak;

/**
 * @author Nils.Hoffmann@CeBiTec.Uni-Bielefeld.DE

 *
 */
public class WebServiceClient {
	
	private JPanel panel = null;
	private JPanel controlpanel = null;
	
	private JList projectsList = null;
	private JList expList = null;
	private JList chromList = null;
	private JList compList = null;
	private JList peaksList = null;
	
	private List<Peak> userPeaks = new ArrayList<Peak>();
	
	private final MeltDBSession ms;

	private ExecutorService es = Executors.newCachedThreadPool();
	
	private int activePeakID = -1;
	
	
	public WebServiceClient(MeltDBSession ms) {
		this.ms = ms;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final MeltDBSession ms = new MeltDBSession("hoffmann", "47GrudWl");
		final WebServiceClient wsc = new WebServiceClient(ms);
		final Runnable r = new Runnable() {
			
			@Override
			public void run() {
				final JFrame jf = new JFrame();
				jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				jf.add(wsc.getPanel());
				wsc.getProjectsList();
				jf.setVisible(true);
				jf.pack();
			}
		};
		SwingUtilities.invokeLater(r);

	}
	
	public JPanel getControlPanel() {
		if(this.controlpanel == null) {
			this.controlpanel = new JPanel();
		}
		return this.controlpanel;
	}
	
	public JPanel getPanel() {
		if(this.panel == null) {
			this.panel = new JPanel();
			this.panel.setLayout(new BoxLayout(this.panel,BoxLayout.Y_AXIS));
			this.panel.add(getControlPanel());
			JScrollPane plsp = new JScrollPane(getProjectsList());
			plsp.setBorder(BorderFactory.createTitledBorder("Projects"));
			this.panel.add(plsp);
			JScrollPane elsp = new JScrollPane(getExperimentsList());
			elsp.setBorder(BorderFactory.createTitledBorder("Experiments"));
			this.panel.add(elsp);
			JScrollPane clsp = new JScrollPane(getChromatogramsList());
			clsp.setBorder(BorderFactory.createTitledBorder("Chromatograms"));
			this.panel.add(clsp);
//			JScrollPane lcsp = new JScrollPane(getLabeledCompoundsList());
//			lcsp.setBorder(BorderFactory.createTitledBorder("Labeled Compounds"));
//			this.panel.add(lcsp);
			JScrollPane lcsp = new JScrollPane(getPeaksList());
			lcsp.setBorder(BorderFactory.createTitledBorder("Peaks"));
			this.panel.add(lcsp);
			JFrame jf = new JFrame();
			jf.add(addPeakPanel());
			jf.setVisible(true);
			jf.pack();
		}
		return this.panel;
	}
	
	public JPanel addPeakPanel() {
		JPanel jp = new JPanel();
		//BoxLayout bl = new BoxLayout(jp,BoxLayout.Y_AXIS);
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
		String[] labels = new String[]{"rt","rt_min","rt_max","scan_index","area","intensity"};
		SortedMap<String,JTextArea> sm = new TreeMap<String,JTextArea>();
		for(String s:labels) {
			JTextArea jta = getLabeledTextArea(s);
			jp.add(jta);
			//jp.add(Box.createVerticalGlue());
			sm.put(s,jta);
		}
		PeakCreateAction pca = new PeakCreateAction("Create Peak");
		pca.setMap(sm);
		JButton jb = new JButton(pca);
		jp.add(jb);
		
		SubmitPeaksAction spa = new SubmitPeaksAction("Submit Peaks");
		JButton jc = new JButton(spa);
		jp.add(jc);
		//rt, rt_min, rt_max, scan_index, area, intensity
		return jp;
	}
	
	public void addUserPeak(Peak cp) {
		this.userPeaks.add(cp);
	}
	
	public List<Peak> getUserPeaks() {
		return this.userPeaks;
	}
	
	public class SubmitPeaksAction extends AbstractAction {

		/**
         * 
         */
        private static final long serialVersionUID = -2036573505164092880L;

		/**
         * 
         */
        public SubmitPeaksAction() {
	        super();
	        // TODO Auto-generated constructor stub
        }

		/**
         * @param name
         * @param icon
         */
        public SubmitPeaksAction(String name, Icon icon) {
	        super(name, icon);
	        // TODO Auto-generated constructor stub
        }

		/**
         * @param name
         */
        public SubmitPeaksAction(String name) {
	        super(name);
	        // TODO Auto-generated constructor stub
        }

		/* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
        	boolean b = true;
//        	try{
//        		int[] ids = mp.createPeaks(getPeaks().toArray(new CreatePeak[]{}));
//        		System.out.println(Arrays.toString(ids));
//        		for(int id:ids) {
//        			if(id<=0) {
//        				b = false;
//        				System.err.println("Problem occurred while adding userPeaks");
//        			}
//        		}
//        	}catch(RemoteException e1) {
//        		e1.printStackTrace();
//        	}
        	for(Peak cp:getUserPeaks()) {
        		try {
            		System.out.println("Peak @"+cp.getRt());
    	            int ids = ms.createPeak(ms.getActiveProject(),ms.getActiveChromatogram(),cp.getRt(),cp.getRt_min(),cp.getRt_max(),cp.getScan_index(),cp.getArea(),cp.getIntensity());
    	            System.out.println("Id: "+ids);
	            	if(ids<=0) {
	            		b = false;
	            		System.err.println("Problem occurred while adding userPeaks");
	            	}
                } catch (RemoteException e1) {
    	            // TODO Auto-generated catch block
    	            e1.printStackTrace();
                }
        	}
        	//all clear, userPeaks have been transmitted successfully
        	if(b) {
            	getUserPeaks().clear();
            }
        }
		
	}
	
	public class PeakCreateAction extends AbstractAction {

		/**
         * 
         */
        private static final long serialVersionUID = -5584984611571026324L;

		private SortedMap<String,JTextArea> sm;
		
		private List<CreatePeak> peaks = new ArrayList<CreatePeak>();
		private MeltDBSession ms;
		
		/**
         * @param name
         * @param icon
         */
        public PeakCreateAction(String name, Icon icon) {
	        super(name, icon);
	        // TODO Auto-generated constructor stub
        }

		/**
         * @param name
         */
        public PeakCreateAction(String name) {
	        super(name);
	        // TODO Auto-generated constructor stub
        }

		public PeakCreateAction() {
			
		}
		
		public void setSession(MeltDBSession mdbs) {
			this.ms = mdbs;
		}
		
		public void setMap(SortedMap<String,JTextArea> sm) {
			this.sm = sm;
		}
		
		/* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
        	if(this.sm != null) {
        		String project = this.ms.getActiveProject();
        		String chrom = this.ms.getActiveChromatogram();
        		String rt = this.sm.get("rt").getText();
        		String rt_min = this.sm.get("rt_min").getText();
        		String rt_max = this.sm.get("rt_max").getText();
        		int scan_index = Integer.parseInt(this.sm.get("scan_index").getText());
        		float area = Float.parseFloat(this.sm.get("area").getText());
        		float intensity = Float.parseFloat(this.sm.get("intensity").getText());
        		System.out.println("Adding peak");
    			Peak p = new Peak(-1,rt,rt_min,rt_max,scan_index,area,intensity);
//        			int id = this.ms.createPeak(project, chrom, rt, rt_min, rt_max, scan_index, area, intensity);
//        			if(id>=0) {
//        				
//        				this.peaks.add(this.ms.getPeak(project, chrom, id));
//        			}
    			addUserPeak(p);
        	}
	        
        }
		
	}
	
	private JTextArea getLabeledTextArea(String label) {
		JTextArea jta = new JTextArea();
		jta.setBorder(BorderFactory.createTitledBorder(label));
		return jta;
	}
	
	public JList getLabeledCompoundsList() {
		if(this.compList == null) {
			this.compList = new JList(new String[]{});
			this.compList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			ListSelectionListener lsl = new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
					System.out.println("Selected: "+((JList)e.getSource()).getModel().getElementAt(e.getFirstIndex()));
					
				}
			};
			this.compList.addListSelectionListener(lsl);
		}
		return this.compList;
	}
	
	public JList getChromatogramsList() {
		if(this.chromList == null) {
			this.chromList = new JList(new String[]{});
			this.chromList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			ListSelectionListener lsl = new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
					ms.setActiveChromatogram((String)((JList)e.getSource()).getSelectedValue());
					System.out.println("Active Chromatogram is: "+ms.getActiveChromatogram());
					disableComponent(getLabeledCompoundsList());
					Runnable fetch = new Runnable() {
						
						@Override
						public void run() {
							try{
								final String[] s = ms.getLabeledCompoundsByChromatogram(ms.getActiveProject(),ms.getActiveChromatogram());
								Runnable update = new Runnable() {
									
									@Override
									public void run() {
										
										if(s!=null) {
											DefaultListModel dlm = new DefaultListModel();
											
											for(String str:s) {
												dlm.addElement(str);
											}
											getLabeledCompoundsList().setModel(dlm);
											enableComponent(getLabeledCompoundsList());
										}else{
											disableComponent(getLabeledCompoundsList());
										}
									}
								};
								SwingUtilities.invokeLater(update);
							}catch(RemoteException re) {
								handleRemoteException(re);
							}
							
						}
					};
					es.submit(fetch);
				}
			};
			this.chromList.addListSelectionListener(lsl);
		}
		return this.chromList;
	}
	
	public JList getPeaksList() {
		if(this.peaksList == null) {
			this.peaksList = new JList(new String[]{});
			this.peaksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			final AbstractAction pfa = new AbstractAction("Fetch peaks") {
				
				/**
                 * 
                 */
                private static final long serialVersionUID = 1L;

				@Override
                public void actionPerformed(ActionEvent e) {
					//ms.setActiveChromatogram((String)((JList)e.getSource()).getSelectedValue());
					//System.out.println("Active Peak is: "+ms.getActivePeak());
					disableComponent(getLabeledCompoundsList());
					Runnable fetch = new Runnable() {
						
						@Override
						public void run() {
							try{
								System.out.println("Retrieving peaks for project: "+ms.getActiveProject()+" and chromatogram: "+ms.getActiveChromatogram());
								final int[] s = ms.getAvailablePeaks(ms.getActiveProject(), ms.getActiveChromatogram());
								System.out.println("Retrieved "+s.length+" peaks!");
								Runnable update = new Runnable() {
									
									@Override
									public void run() {
										
										if(s!=null) {
											DefaultListModel dlm = new DefaultListModel();
											try{
												Peak[] peaks = ms.getPeaks(ms.getActiveProject(), ms.getActiveChromatogram(), s);
												for(Peak p:peaks) {
													activePeakID = p.getId();
													dlm.addElement("Peak at scan index: "+p.getScan_index()+" [ rt_min: "+p.getRt_min()+", (rt_apex: "+p.getRt()+"), rt_max: "+p.getRt_max()+" area: "+p.getArea()+" apexIntensity: "+p.getIntensity());
	                                                //addUserPeak(p);
	                                                System.out.println("Retrieved peak with id "+p.getId()+" "+p.getRt());
												}
											}catch(RemoteException re) {
												handleRemoteException(re);
											}
//											for(int id :s) {
//												Peak p;
//                                                try {
//	                                                p = ms.getPeak(ms.getActiveProject(), ms.getActiveChromatogram(), id);
//	                                                dlm.addElement("Peak at scan index: "+p.getScan_index()+" [ rt_min: "+p.getRt_min()+", (rt_apex: "+p.getRt()+"), rt_max: "+p.getRt_max()+" area: "+p.getArea()+" apexIntensity: "+p.getIntensity());
//	                                                //addUserPeak(p);
//	                                                System.out.println("Retrieved peak with id "+id+" "+p.getRt());
//                                                } catch (RemoteException e) {
//	                                                handleRemoteException(e);
//                                                }
//                                                //dlm.addElement(id);
//                                                //addUserPeak(new Peak(id,"NA","NA","NA",-1,Float.NaN,Float.NaN));
//											}
											getPeaksList().setModel(dlm);
											getPeaksList().addMouseListener(new MouseAdapter() {
												@Override
								                public void mouseClicked(MouseEvent e) {
									                super.mouseClicked(e);
									                if(e.getButton() == MouseEvent.BUTTON3) {
									                	JPopupMenu jpm = new JPopupMenu();
									                	AbstractAction fetchEICPeaks = new AbstractAction("Get EIC peaks") {
															
															/**
                                                             * 
                                                             */
                                                            private static final long serialVersionUID = 1L;

															@Override
															public void actionPerformed(ActionEvent e) {
																try {
	                                                                Eic_peak[] eicPeaks = ms.getEICPeaks(ms.getActiveProject(), ms.getActiveChromatogram(), activePeakID);
	                                                                if(eicPeaks==null) {
	                                                                	System.out
                                                                                .println("EIC peaks is null!");
	                                                                }else{
	                                                                	for(Eic_peak ep:eicPeaks) {
	                                                                		System.out.println(ep.getId()+" "+ep.getMz()+" "+ep.getMz_min()+" "+ep.getMz_max());
	                                                                	}
	                                                                }
                                                                } catch (RemoteException e1) {
	                                                                // TODO Auto-generated catch block
	                                                                e1.printStackTrace();
                                                                }
																
															}
														};
									                	jpm.add(fetchEICPeaks);
									                	jpm.show(e.getComponent(), e.getX(), e.getY());
									                }
								                }
											});
											enableComponent(getPeaksList());
										}else{
											disableComponent(getPeaksList());
										}
									}
								};
								SwingUtilities.invokeLater(update);
							}catch(RemoteException re) {
								handleRemoteException(re);
							}
							
						}
					};
					es.submit(fetch);
	                
                }
			};
			this.chromList.addMouseListener(new MouseAdapter() {

				@Override
                public void mouseClicked(MouseEvent e) {
	                super.mouseClicked(e);
	                if(e.getButton() == MouseEvent.BUTTON3) {
	                	JPopupMenu jpm = new JPopupMenu();
	                	jpm.add(pfa);
	                	jpm.show(e.getComponent(), e.getX(), e.getY());
	                }
                }
				
			});
			
		}
		return this.peaksList;
	}
	
	public JList getExperimentsList() {
		if(this.expList == null) {
			this.expList = new JList(new String[]{});
			this.expList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			ListSelectionListener lsl = new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
					ms.setActiveExperiment((String)((JList)e.getSource()).getSelectedValue());
					if(ms.getActiveExperiment()==null || ms.getActiveExperiment().isEmpty()) {
						System.out.println("Could not complete request, selection for active experiment is empty!");
						return;
					}
					System.out.println("Active Experiment is: "+ms.getActiveExperiment());
					disableComponent(getChromatogramsList());
					disableComponent(getLabeledCompoundsList());
					Runnable fetch = new Runnable() {
						
						@Override
						public void run() {
							try{
								final String[] s = ms.getAvailableChromatogramsForExperiment(ms.getActiveProject(),ms.getActiveExperiment());
								Runnable update = new Runnable() {
									
									@Override
									public void run() {
										
										if(s!=null) {
											DefaultListModel dlm = new DefaultListModel();
											
											for(String str:s) {
												dlm.addElement(str);
											}
											getChromatogramsList().setModel(dlm);
											enableComponent(getChromatogramsList());
										}else{
											disableComponent(getChromatogramsList());
										}
									}
								};
								SwingUtilities.invokeLater(update);
							}catch(RemoteException re) {
								handleRemoteException(re);
							}
							
						}
					};
					es.submit(fetch);
				}
			};
			this.expList.addListSelectionListener(lsl);
		}
		return this.expList;
	}
	
	private void handleRemoteException(RemoteException re) {
		System.err.println("RemoteException occurred while trying to access webservice!");
		System.err.println(re.getLocalizedMessage());
	}
	
	public JList getProjectsList() {
		if(this.projectsList==null) {
			this.projectsList = new JList(new String[]{});
			controlpanel.add(new JButton(new AbstractAction("Get Projects") {
				
				/**
	             * 
	             */
	            private static final long serialVersionUID = 4181777796862959771L;

				@Override
				public void actionPerformed(ActionEvent e) {
					Runnable r = new Runnable() {
						
						@Override
						public void run() {
							try{
								final String[] s = ms.getAvailableProjects();
								Runnable update = new Runnable() {
									
									@Override
									public void run() {
										
										if(s!=null) {	
											DefaultListModel dlm = new DefaultListModel();
											for(String str:s) {
												dlm.addElement(str);
											}
											getProjectsList().setModel(dlm);
											enableComponent(getProjectsList());
										}else{
											disableComponent(getProjectsList());
										}
									}
								};
								SwingUtilities.invokeLater(update);
							}catch(RemoteException re) {
								handleRemoteException(re);
							}
							
						}
					};
					es.submit(r);
					
				}
			}));
			this.projectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			ListSelectionListener lsl = new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
					System.out.println("Selected index: "+e.getFirstIndex());
					System.out.println("Source event occurred on: "+e.getSource().getClass().getName());
					final String project = (String)((JList)e.getSource()).getSelectedValue();
					ms.setActiveProject(project);
					System.out.println("Active project: "+ms.getActiveProject());
					disableComponent(getExperimentsList());
					disableComponent(getChromatogramsList());
					disableComponent(getLabeledCompoundsList());
					Runnable fetch = new Runnable() {
						
						@Override
						public void run() {
							try{
								System.out.println("Active project: "+ms.getActiveProject());
								final String[] s = ms.getAvailableExperiments(ms.getActiveProject());
								Runnable update = new Runnable() {
									
									@Override
									public void run() {
										
										if(s!=null) {	
											DefaultListModel dlm = new DefaultListModel();
											for(String str:s) {
												dlm.addElement(str);
											}
											getExperimentsList().setModel(dlm);
											enableComponent(getExperimentsList());
										}else{
											disableComponent(getExperimentsList());
										}
									}
								};
								SwingUtilities.invokeLater(update);
							}catch(RemoteException re) {
								handleRemoteException(re);
							}
							
						}
					};
					es.submit(fetch);
				}
			};
			this.projectsList.addListSelectionListener(lsl);
		}
		
		return this.projectsList;
	}
	
	private void disableComponent(final JComponent jc) {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				jc.setEnabled(false);
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	private void enableComponent(final JComponent jc) {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				jc.setEnabled(true);
			}
		};
		SwingUtilities.invokeLater(r);
	}

}
