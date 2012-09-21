/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptoolsbindingtest;

import de.unibielefeld.gi.omicsTools.biocyc.ptools.CommonName;
import de.unibielefeld.gi.omicsTools.biocyc.ptools.Gene;
import de.unibielefeld.gi.omicsTools.biocyc.ptools.PGDB;
import de.unibielefeld.gi.omicsTools.biocyc.ptools.Pathway;
import de.unibielefeld.gi.omicsTools.biocyc.ptools.PtoolsXml;
import de.unibielefeld.gi.omicsTools.biocyc.ptools.Strain;
import de.unibielefeld.gi.omicsTools.biocyc.ptools.Synonym;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author hoffmann
 */
public class PToolsBindingTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            JAXBContext jc = JAXBContext.newInstance("de.unibielefeld.gi.omicsTools.biocyc.ptools");
            Unmarshaller u = jc.createUnmarshaller();
            PtoolsXml organisms = (PtoolsXml) u.unmarshal(PToolsBindingTest.class.getResourceAsStream("organisms.xml"));
            System.out.println("Organisms is " + organisms.getClass().getName());
            for (PGDB p : organisms.getMetadata().getPGDB()) {
                System.out.println("Species: " + p.getSpecies().getContent());
                Strain strain = p.getStrain();
                if (strain != null) {
                    String strainName = (strain == null) ? "" : strain.getContent();
                    System.out.println("Strain: " + strainName);
                }
            }
            System.out.println(organisms.toString());
            PtoolsXml pathways = (PtoolsXml) u.unmarshal(PToolsBindingTest.class.getResourceAsStream("ecoli-pathways.xml"));
            List<Object> children = pathways.getCompoundOrGOTermOrGene();
            for (Object obj : children) {
                if (obj instanceof Pathway) {
                    Pathway pw = (Pathway) obj;
                    System.out.println(pw.getID());
                    for (Object obj2 : pw.getCitationOrCommentOrCommonName()) {
                        if (obj2 instanceof CommonName) {
                            System.out.println("Pathway name: " + ((CommonName) obj2).getContent());
                        } else if (obj2 instanceof Synonym) {
                            System.out.println("Pathway synonym: " + ((Synonym) obj2).getContent());
                        }
                    }
                }
            }
            System.out.println("Pathways is " + pathways.getClass().getName());
            PtoolsXml gene = (PtoolsXml) u.unmarshal(PToolsBindingTest.class.getResourceAsStream("ecoli-genes-trpA.xml"));
            System.out.println("Gene is " + gene.getClass().getName());
            for (Object geneObj : gene.getCompoundOrGOTermOrGene()) {
                if (geneObj instanceof Gene) {
                    Gene geneNode = (Gene) geneObj;
                    for (Object geneChildObj : geneNode.getCitationOrCommentOrCommonName()) {
                        if (geneChildObj instanceof CommonName) {
                            System.out.println("Common name: " + ((CommonName) geneChildObj).getContent());

                        } else if (geneChildObj instanceof Synonym) {
                            System.out.println("Synonym: " + ((Synonym) geneChildObj).getContent());
                        }
                    }
                }
            }

            //retrieve from server
            String addr = "http://BioCyc.org/xmlquery?[x:x<-ecoli^^genes,x^name=\"trpA\"]&detail=full";
//            String addr = "http://BioCyc.org/xmlquery?%5bx:x%3c-ecoli^^pathways%5d";
//            String addr = "http://BioCyc.org/xmlquery?[x:x<-ecoli^^pathways]";
            File tmpFile = File.createTempFile("biocycquery", ".xml");
            try {
                URL url = new URL(addr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //String encoding = new sun.misc.BASE64Encoder().encode("username assword".getBytes());
                //conn.setRequestProperty("Authorization", "Basic " + encoding);
                conn.setRequestMethod("GET");

                conn.connect();
                InputStream in = conn.getInputStream();
                System.out.println("Header:");
                System.out.println(conn.getHeaderFields());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("iso-8859-1")));
                BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFile));
                String line = "";
                while((line=reader.readLine())!=null) {
//                    System.out.println(line);
                    bw.write(line);
                }
                conn.disconnect();
                reader.close();
                bw.flush();
                bw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("made it here");
            }
            PtoolsXml testGene = (PtoolsXml) u.unmarshal(tmpFile);
            System.out.println("Gene is " + testGene.getClass().getName());
            for (Object geneObj : testGene.getCompoundOrGOTermOrGene()) {
                if (geneObj instanceof Gene) {
                    Gene geneNode = (Gene) geneObj;
                    for (Object geneChildObj : geneNode.getCitationOrCommentOrCommonName()) {
                        if (geneChildObj instanceof CommonName) {
                            System.out.println("Common name: " + ((CommonName) geneChildObj).getContent());

                        } else if (geneChildObj instanceof Synonym) {
                            System.out.println("Synonym: " + ((Synonym) geneChildObj).getContent());
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PToolsBindingTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(PToolsBindingTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
