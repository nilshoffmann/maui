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
package de.unibielefeld.cebitec.lstutz.pca.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;

public class CSVParser {

    private InputStream is;

    public CSVParser(InputStream input) {
        this.is = input;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }

    public ArrayList<DataModel> parse_data() {
        ArrayList<DataModel> data = new ArrayList<>();
        try {
            InputStreamReader isr = new InputStreamReader(is);
            try (BufferedReader bf = new BufferedReader(isr)) {
                StreamTokenizer st = new StreamTokenizer(bf);
                st.resetSyntax();
                st.wordChars(32, 122);
                st.whitespaceChars((int) '\t', (int) '\t');
                Heading head = new Heading();
                st.nextToken();
                head.setHeading_x(st.sval);
                st.nextToken();
                head.setHeading_y(st.sval);
                st.nextToken();
                head.setHeading_z(st.sval);
                while (st.nextToken() != StreamTokenizer.TT_EOL);
                while (st.nextToken() != StreamTokenizer.TT_EOF) {
                    DataModel d = new DataModel();
                    d.setLabel(st.sval);
                    d.setHeading(head);
                    for (int i = 0; i < 3; ++i) {
                        st.nextToken();
                        d.getCoordinates().add(Double.parseDouble(st.sval));
                    }
                    if (st.nextToken() == StreamTokenizer.TT_EOL) {
                        d.setColor(ParserUtilities.parse_hex_color("00DF00"));
                        data.add(d);
                        continue;
                    } else {
                        d.setColor(ParserUtilities.parse_hex_color(st.sval.trim()));
                    }
                    if (st.nextToken() == StreamTokenizer.TT_EOL) {
                        data.add(d);
                        continue;
                    } else {
                        d.setShape(st.sval.trim());
                    }
                    data.add(d);
                    while (st.nextToken() != StreamTokenizer.TT_EOL);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            e.getMessage();
        }
        System.out.println(data.size() + " Eintr�ge importiert...");
        return data;
    }
}
