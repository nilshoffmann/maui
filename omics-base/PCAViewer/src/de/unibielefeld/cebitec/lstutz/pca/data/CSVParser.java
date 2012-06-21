package de.unibielefeld.cebitec.lstutz.pca.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;

public class CSVParser {
	
	private InputStream is;
	
	public CSVParser(InputStream input){
		this.is=input;
	}
		
	public void setIs(InputStream is) {
		this.is = is;
	}

	public ArrayList<DataModel> parse_data(){
		ArrayList<DataModel> data = new ArrayList<DataModel>();
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader bf = new BufferedReader(isr);
			StreamTokenizer st = new StreamTokenizer(bf);
			st.resetSyntax();
			st.wordChars(32, 122);
			st.whitespaceChars((int)'\t', (int)'\t');
			Heading head = new Heading();
			st.nextToken();
			head.setHeading_x(st.sval);
			st.nextToken();
			head.setHeading_y(st.sval);
			st.nextToken();
			head.setHeading_z(st.sval);
			while(st.nextToken()!=StreamTokenizer.TT_EOL);
			while(st.nextToken()!=StreamTokenizer.TT_EOF){
				DataModel d = new DataModel();
				d.setLabel(st.sval);
				d.setHeading(head);
				for(int i = 0; i < 3; ++i){
					st.nextToken();
					d.getCoordinates().add(Double.parseDouble(st.sval));
				}
				if(st.nextToken()==StreamTokenizer.TT_EOL){
					d.setColor(ParserUtilities.parse_hex_color("00DF00"));
					data.add(d);
					continue;
				} else {
					d.setColor(ParserUtilities.parse_hex_color(st.sval.trim()));
				}
				if(st.nextToken()==StreamTokenizer.TT_EOL){
					data.add(d);
					continue;
				} else {
					d.setShape(st.sval.trim());
				}
				data.add(d);
				while(st.nextToken()!=StreamTokenizer.TT_EOL);
			}
			bf.close();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		System.out.println(data.size()+" Eintr�ge importiert...");
		return data;
	}
}