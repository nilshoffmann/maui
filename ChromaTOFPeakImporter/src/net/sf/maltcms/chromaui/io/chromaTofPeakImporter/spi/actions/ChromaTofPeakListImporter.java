/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.actions;

import cross.datastructures.tuple.Tuple2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import maltcms.datastructures.peak.Peak1D;
import net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.parser.ChromaTOFParser;
import net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.parser.TableRow;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;

import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Build",
id = "net.sf.maltcms.chromaui.io.chromaTofPeakImporter.spi.actions.ChromaTofPeakListImporter")
@ActionRegistration(displayName = "#CTL_ChromaTofPeakListImporter")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1413)
})
@Messages("CTL_ChromaTofPeakListImporter=Import ChromaTOF Report")
public final class ChromaTofPeakListImporter implements ActionListener {

    private final IChromAUIProject context;

    public ChromaTofPeakListImporter(IChromAUIProject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        JFileChooser jfc = new JFileChooser();
        jfc.setAcceptAllFileFilterUsed(true);
        jfc.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "txt";
            }
        });
        jfc.setDialogTitle("Select ChromaTOF reports");
        jfc.setMultiSelectionEnabled(true);
        int result = jfc.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] files = jfc.getSelectedFiles();
            LinkedHashMap<String, IChromatogramDescriptor> chromatograms = new LinkedHashMap<String, IChromatogramDescriptor>();
            for (IChromatogramDescriptor descriptor : context.getChromatograms()) {
                String chromName = new File(descriptor.getResourceLocation()).
                        getName();
                chromName = chromName.substring(0, chromName.lastIndexOf(
                        "."));
                chromatograms.put(chromName, descriptor);
            }
            LinkedHashMap<String, File> reports = new LinkedHashMap<String, File>();
            for (File file : files) {
                String chromName = file.getName();
                chromName = chromName.substring(0, chromName.lastIndexOf(
                        "."));
                reports.put(chromName, file);
            }

            if (reports.size() != chromatograms.size()) {
                System.err.println(
                        "Not all chromatograms could be matched!");
            }
            for (File f : files) {
                String chromName = f.getName();
                chromName = chromName.substring(0, chromName.lastIndexOf(
                        "."));
                IChromatogramDescriptor chromatogram = chromatograms.get(
                        chromName);
                if (chromatogram != null) {
                    System.err.println(
                            "Could not find chromatogram for report: " + f);
                } else {
                    Tuple2D<LinkedHashSet<String>, List<TableRow>> report = ChromaTOFParser.
                            parseReport(f);
                    List<IPeakAnnotationDescriptor> peaks = new ArrayList<IPeakAnnotationDescriptor>();
                    LinkedHashSet<String> header = report.getFirst();
                    for (TableRow tr : report.getSecond()) {
                        Peak1D p = new Peak1D();
                        p.setStartTime(
                                Double.parseDouble(tr.get("INTEGRATIONBEGIN")));
                        p.setApexTime(Double.parseDouble(tr.get("R.T._(S)")));
                        p.setStopTime(Double.parseDouble(
                                tr.get("INTEGRATIONEND")));
                        p.setArea(Double.parseDouble(tr.get("AREA")));
                        p.setName(tr.get("NAME"));
                        IPeakAnnotationDescriptor descriptor = DescriptorFactory.
                                newPeakAnnotationDescriptor(p, tr.get("NAME"),
                                Double.parseDouble(tr.get("UNIQUEMASS")),
                                Double.parseDouble(tr.get("RETENTION_INDEX")),
                                Double.parseDouble(tr.get("S/N")), Double.
                                parseDouble(tr.get("FULL_WIDTH_AT_HALF_HEIGHT")),
                                Double.parseDouble(tr.get("SIMILARITY")),
                                tr.get("LIBRARY"),
                                tr.get("CAS"),
                                tr.get("FORMULA"),
                                "ChromaTOF");
                        descriptor.setPeak(p);
                        peaks.add(descriptor);
                        
                    }
                    DescriptorFactory.addPeakAnnotations(context, chromatogram, peaks);
                }

            }
        }
    }
}
