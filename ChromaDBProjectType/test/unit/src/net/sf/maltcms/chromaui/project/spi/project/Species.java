/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi.project;

/**
 *
 * @author nilshoffmann
 */
public class Species {
    
    private String pubmedId = "12393";
    private String ontology = "Arafa adeno";
    
    public String getPubmedId() {
        return pubmedId;
    }
    
    public String getOntology() {
        return ontology;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
    }

    public void setPubmedId(String pubmedId) {
        this.pubmedId = pubmedId;
    }
    
    
    
}
