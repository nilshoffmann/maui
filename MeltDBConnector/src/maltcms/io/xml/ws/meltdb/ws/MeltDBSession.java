/**
 * 
 */
package maltcms.io.xml.ws.meltdb.ws;

import java.rmi.RemoteException;

import JavaServices.Web.MELT.Eic_peak;
import JavaServices.Web.MELT.MELTWebServicePortTypeProxy;
import JavaServices.Web.MELT.Peak;

/**
 * @author Nils.Hoffmann@CeBiTec.Uni-Bielefeld.DE

 *
 */
public final class MeltDBSession {

	private String activeChromatogram;

	private String activeExperiment;

	private String activeProject;
	
	private String activePeak;

	private MELTWebServicePortTypeProxy mp = null;

	private final String password;

	private final String username;

	public MeltDBSession(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public int createPeak(String project,
            String chromatogram, String rt, String rtMin, String rtMax,
            int scanIndex, float area, float intensity) throws RemoteException {
	    return getWSProxy().createPeak(this.username,this.password, project, chromatogram, rt, rtMin,
	            rtMax, scanIndex, area, intensity);
    }
	
	public String[] getLabeledCompoundsByChromatogram(
            String project, String chromatogram) throws RemoteException {
	    return mp.getLabeledCompoundsByChromatogram(this.username,this.password, project,
	            chromatogram);
    }

	public String getActiveChromatogram() {
    	return activeChromatogram;
    }
	
	public String getActiveExperiment() {
    	return activeExperiment;
    }
	
	public String getActiveProject() {
    	return activeProject;
    }
	
	public String[] getAvailableChromatograms(
            String project) throws RemoteException {
	    return getWSProxy().getAvailableChromatograms(this.username, this.password, project);
    }

	public String[] getAvailableChromatogramsForExperiment(String project, String experiment)
            throws RemoteException {
	    return getWSProxy().getAvailableChromatogramsForExperiment(this.username, this.password, project,
	            experiment);
    }

	public String[] getAvailableExperiments(
            String project) throws RemoteException {
	    return getWSProxy().getAvailableExperiments(this.username, this.password, project);
    }

	public int[] getAvailablePeaks(String project,
            String chromatogram) throws RemoteException {
	    return getWSProxy().getAvailablePeaks(this.username, this.password,project, chromatogram);
    }

	public String[] getAvailableProjects()
            throws RemoteException {
	    return getWSProxy().getAvailableProjects(this.username, this.password);
    }

	public Peak getPeak(String project,
            String chromatogram, int id) throws RemoteException {
	    return getWSProxy().getPeak(this.username, this.password, project, chromatogram, id);
    }
	
	public Peak[] getPeaks(String project, String chromatogram, int[] ids) throws RemoteException {
		return getWSProxy().getPeaks(this.username, this.password, project, chromatogram, ids);
	}
	
	public Eic_peak[] getEICPeaks(String project, String chromatogram, int id) throws RemoteException {
		return getWSProxy().getEICPeaks(this.username, this.password, project, chromatogram, id);
	}
	
	public String getUsername() {
    	return username;
    }
	
	private MELTWebServicePortTypeProxy getWSProxy() {
		if(this.mp == null) {
			this.mp = new  MELTWebServicePortTypeProxy();
		}
		return this.mp;
	}

	public void setActiveChromatogram(String activeChromatogram) {
    	this.activeChromatogram = activeChromatogram;
    }

	public void setActiveExperiment(String activeExperiment) {
    	this.activeExperiment = activeExperiment;
    }
	
	public void setActiveProject(String activeProject) {
    	this.activeProject = activeProject;
    }

	/**
     * @param activePeak the activePeak to set
     */
    public void setActivePeak(String activePeak) {
	    this.activePeak = activePeak;
    }

	/**
     * @return the activePeak
     */
    public String getActivePeak() {
	    return activePeak;
    }
	
}
