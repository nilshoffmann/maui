/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api;

import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.VariableFragment;
import cross.datastructures.workflow.DefaultWorkflow;
import cross.datastructures.workflow.WorkflowSlot;
import cross.tools.StringTools;
import java.io.BufferedWriter;
import java.io.IOException;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;
import java.io.File;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import maltcms.io.csv.CSVWriter;
import net.sf.maltcms.chromaui.db.api.exceptions.AuthenticationException;
import net.sf.maltcms.chromaui.project.api.types.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.types.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.types.TreatmentGroup;
import net.sf.maltcms.chromaui.project.spi.project.ChromAUIProject;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.IndexIterator;

/**
 *
 * @author nilshoffmann
 */
public class DBProjectFactory {

    public static final String PROJECT_FILE = "maui.mpr";

    public static IChromAUIProject getDefault() {
        return new ChromAUIProject();
    }

    public static IChromatogramDescriptor getChromatogramDescriptor(File f, ISeparationType st, IDetectorType dt, ITreatmentGroupDescriptor tg) {
        ChromatogramDescriptor gcd = new ChromatogramDescriptor();
        gcd.setResourceLocation(f.getAbsolutePath());
        gcd.setSeparationType(st);
        gcd.setDetectorType(dt);
        gcd.setDisplayName(f.getName());
        gcd.setTreatmentGroup(tg);
        return gcd;
    }

    public static File[] toFileArray(String s, String split) {
        String[] str = s.split(split);
        File[] f = new File[str.length];
        for (int i = 0; i < str.length; i++) {
            f[i] = new File(str[i]);
        }
        return f;
    }

    public static void createGroupFile(FileObject targetDirectory, Map<File, String> fileToGroup) {
        try {
            FileObject fo = FileUtil.createData(targetDirectory, "groups.csv");
            StringBuilder sb = new StringBuilder();
            sb.append("FILE");
            sb.append("\t");
            sb.append("GROUP");
            sb.append("\n");

            for (File f : fileToGroup.keySet()) {
                sb.append(StringTools.removeFileExt(f.getName()));
                sb.append("\t");
                sb.append(fileToGroup.get(f));
                sb.append("\n");
            }
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fo.getOutputStream()));
            bw.write(sb.toString());
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    public static void createProject(Map<String, Object> props, File projdir) throws AuthenticationException {
        if (!projdir.exists()) {
            projdir.mkdirs();
        }
        //File[] new File[];
        String projectFileName = DBProjectFactory.PROJECT_FILE;
        IChromAUIProject icui = DBProjectFactory.getDefault();
//            String projectFolderName = props.get("name").toString();

        ISeparationType separationType = (ISeparationType) props.get("separationType");
        IDetectorType detectorType = (IDetectorType) props.get("detectorType");

        Double modulationTime = (Double) props.get("modulationTime");

        try {
            icui.activate(new File(projdir, projectFileName).toURI().toURL());
            Object o = props.get("input.dataInfo");
            Map<File, String> fileToGroup = (Map<File, String>) props.get("groupMapping");
            File[] inputFiles = toFileArray((String) o, ",");
            LinkedHashMap<File, IChromatogramDescriptor> fileToDescriptor = new LinkedHashMap<File, IChromatogramDescriptor>();
            IChromatogramDescriptor[] icds = new IChromatogramDescriptor[inputFiles.length];
//            IFileFragment[] fragments = new IFileFragment[inputFiles.length];
            int i = 0;
            for (File f : inputFiles) {
                IFileFragment ff = getProjectFileForResource(projdir, f);
                System.out.println("Adding FileFragment: " + ff);
                if (modulationTime != null) {
                    IVariableFragment ivf = new VariableFragment(ff, "modulation_time");
                    ArrayDouble.D0 modTime = new ArrayDouble.D0();
                    modTime.set(modulationTime.doubleValue());
                    ivf.setArray(modTime);

                    IVariableFragment sr = new VariableFragment(ff, "scan_rate");
                    ArrayDouble.D0 scanRate = new ArrayDouble.D0();
                    IVariableFragment sdur = ff.getChild("scan_duration");
                    final Array durationarray = sdur.getArray();

                    final IndexIterator iter = durationarray.getIndexIterator();
                    double scanDuration = iter.getDoubleNext();
                    scanRate.set(1.0d / scanDuration);
                    sr.setArray(scanRate);
                }

                ff.save();
                icds[i] = DBProjectFactory.getChromatogramDescriptor(new File(ff.getAbsolutePath()), separationType, detectorType, new TreatmentGroup(fileToGroup.get(f)));
                fileToDescriptor.put(f, icds[i]);
                i++;
            }
            //ChromatogramContainer cc = DBProjectFactory.getChromatogramContainer(icui.getCrudProvider());
//            cc.add(icds);
//            cc.setDisplayName("Chromatograms");
//            System.out.println("Adding container: " + cc);
//            icui.addContainer(cc);

//            TreatmentGroupContainer tgc = new TreatmentGroupContainer();
//            tgc.setDisplayName("Treatment Groups");

            LinkedHashSet<String> groups = new LinkedHashSet<String>();
            groups.addAll(fileToGroup.values());
            LinkedHashMap<String, Set<File>> groupToFile = new LinkedHashMap<String, Set<File>>();
            for (String group : groups) {
                for (File key : fileToGroup.keySet()) {
                    if (fileToGroup.get(key).equals(group)) {
                        if (groupToFile.containsKey(group)) {
                            Set<File> s = groupToFile.get(group);
                            s.add(key);
                        } else {
                            Set<File> s = new LinkedHashSet<File>();
                            s.add(key);
                            groupToFile.put(group, s);
                        }

                    }
                }
            }

            System.out.println("Group to file: " + groupToFile);

            for (String group : groupToFile.keySet()) {
//                TreatmentGroup tg = new TreatmentGroup(group);
                TreatmentGroupContainer tgc = new TreatmentGroupContainer();
                Set<File> files = groupToFile.get(group);
                Set<IChromatogramDescriptor> s = new LinkedHashSet<IChromatogramDescriptor>();
                for (File f : files) {
//                    s.add(
                    tgc.add(fileToDescriptor.get(f));
                }
                tgc.add(s.toArray(new IChromatogramDescriptor[s.size()]));
                tgc.setName(group);
//                tg.setName(group);
//                tgc.add(tg);
                icui.addContainer(tgc);
            }
//            icui.addContainer(tgc);

            FileObject output = null;
            if (props.containsKey("output.basedir")) {
                try {
                    output = FileUtil.createFolder((File) props.get("output.basedir"));
                    icui.setOutputDir(output);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else {
                try {
                    output = FileUtil.createFolder((File) new File(projdir, "output"));
                    icui.setOutputDir(output);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            try {
                createGroupFile(FileUtil.createData(new File(projdir, "data")), fileToGroup);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }

            icui.getCrudProvider().close();
            //DBProjectFactory.openProject(icui);
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private static IFileFragment getProjectFileForResource(File projdir, File f) {
        File dataDir = new File(projdir, "data");
        try {
            FileUtil.createFolder(dataDir);
            IFileFragment fragment = new FileFragment(dataDir, f.getName());
            fragment.addSourceFile(new FileFragment(f));
            return fragment;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    public static void openProject(IChromAUIProject iap) {
        OpenProjects.getDefault().open(new Project[]{iap}, false, true);
    }
}
