package de.unibielefeld.cebitec.lstutz.pca.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import org.openide.util.NotImplementedException;

/**
 * Class is currently not implemented!
 * @author hoffmann
 */
@Deprecated
public class XMLParser {
	
	private InputStream is;
	private BufferedReader bf;
	
	public XMLParser(InputStream input){
            throw new NotImplementedException();
//		this.is=input;
//		InputStreamReader isr = new InputStreamReader(is);
//		this.bf= new BufferedReader(isr);
	}
		
	public void setIs(InputStream is) {
            throw new NotImplementedException();
//		this.is = is;
//		InputStreamReader isr = new InputStreamReader(is);
//		this.bf= new BufferedReader(isr);
	}
	
	public ArrayList<DataModel> parse_data(){
            throw new NotImplementedException();
//		ArrayList<DataModel> data = new ArrayList<DataModel>();
//		try{
//			SAXBuilder builder = new SAXBuilder();
//			Document doc = builder.build( bf );
//	
//			Element root = doc.getRootElement();			
//			
//			Heading head = new Heading();
//			head.setHeading_x(root.getAttributeValue("X-Axis"));
//			head.setHeading_y(root.getAttributeValue("Y-Axis"));
//			head.setHeading_z(root.getAttributeValue("Z-Axis"));
//			for(Element cluster : (List<Element>)root.getChildren()){
//			
//				Color3f s = ParserUtilities.parse_hex_color(cluster.getAttributeValue("color"));
//				
//				for(Element item : (List<Element>)cluster.getChildren()){
//					DataModel d = new DataModel();
//					ArrayList<Double> coords = new ArrayList<Double>();
//					coords.add(Double.parseDouble(item.getAttributeValue("x")));
//					coords.add(Double.parseDouble(item.getAttributeValue("y")));
//					coords.add(Double.parseDouble(item.getAttributeValue("z")));
//					d.setCoordinates(coords);
//					d.setColor(s);
//					d.setShape(item.getAttributeValue("shape"));
//					d.setLabel(item.getAttributeValue("name"));
//					d.setHeading(head);
//					d.setLink(item.getAttributeValue("URL"));
//					d.setAnnotation(item.getAttributeValue("annotation"));
//					data.add(d);
//				}
//			}
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//		return data;
	}

}