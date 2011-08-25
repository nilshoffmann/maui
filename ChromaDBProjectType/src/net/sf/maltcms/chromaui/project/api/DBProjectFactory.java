/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.api;

import net.sf.maltcms.chromaui.project.api.container.TreatmentGroupContainer;
import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.VariableFragment;
import cross.tools.StringTools;
import java.io.BufferedWriter;
import java.io.IOException;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.spi.descriptors.ChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;
import java.io.File;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.sf.maltcms.chromaui.db.api.exceptions.AuthenticationException;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.INormalizationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.TreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.spi.project.ChromAUIProject;
import net.sf.maltcms.chromaui.project.spi.wizard.DBProjectVisualPanel3;
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

    public static IChromatogramDescriptor getChromatogramDescriptor(File f,
            ISeparationType st, IDetectorType dt, ITreatmentGroupDescriptor tg) {
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

    public static void createGroupFile(FileObject targetDirectory,
            Map<File, String> fileToGroup) {
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
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fo.
                    getOutputStream()));
            bw.write(sb.toString());
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    public static void createProject(Map<String, Object> props, File projdir) throws AuthenticationException, IOException {
        if (!projdir.exists()) {
            projdir.mkdirs();
        }
        //File[] new File[];
        String projectFileName = DBProjectFactory.PROJECT_FILE;
        IChromAUIProject icui = DBProjectFactory.getDefault();
//            String projectFolderName = props.get("name").toString();

        ISeparationType separationType = (ISeparationType) props.get(
                "separationType");
        IDetectorType detectorType = (IDetectorType) props.get("detectorType");

        Double modulationTime = (Double) props.get("modulationTime");

        try {
            icui.activate(new File(projdir, projectFileName).toURI().toURL());
            icui.getCrudProvider();
            Object o = props.get("input.dataInfo");
            Map<File, String> fileToGroup = (Map<File, String>) props.get(
                    "groupMapping");
            File[] inputFiles = toFileArray((String) o, ",");
            Boolean copyFiles = (Boolean) props.get("copy.files");
            if (copyFiles.booleanValue()) {
                System.out.println("Copying files to user project directory!");
                try {
                    FileObject originaldatadir = FileUtil.createFolder(new File(
                            projdir, "original"));
                    int i = 0;
                    for (File file : inputFiles) {
                        long spaceLeft = FileUtil.toFile(originaldatadir).
                                getFreeSpace();
                        long fileSize = file.length();
                        if (spaceLeft - (100 * 1024 * 1024) > fileSize) {
                            FileUtil.copyFile(FileUtil.toFileObject(file),
                                    originaldatadir, file.getName());
                            File newFile = FileUtil.toFile(originaldatadir.
                                    getFileObject(
                                    file.getName()));
                            String group = fileToGroup.get(file);
                            fileToGroup.remove(file);
                            fileToGroup.put(newFile, group);
                            inputFiles[i++] = newFile;
                        } else {
                            throw new RuntimeException(
                                    "Less than 100 MBytes of space left on " + originaldatadir.
                                    getPath() + " for file " + file.getName() + " (required: " + (fileSize / 1024 / 1024) + "MByte free: " + (spaceLeft / 1024 / 1024) + "). Not copying file!");
                        }
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                    throw ex;
                }
            }

            LinkedHashMap<File, IChromatogramDescriptor> fileToDescriptor = new LinkedHashMap<File, IChromatogramDescriptor>();
            IChromatogramDescriptor[] icds = new IChromatogramDescriptor[inputFiles.length];
//            IFileFragment[] fragments = new IFileFragment[inputFiles.length];
            int i = 0;
            for (File f : inputFiles) {
                IFileFragment ff = getProjectFileForResource(projdir, f);
                System.out.println("Adding FileFragment: " + ff);
                if (modulationTime != null) {
                    IVariableFragment ivf = new VariableFragment(ff,
                            "modulation_time");
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
                icds[i] = DBProjectFactory.getChromatogramDescriptor(new File(ff.
                        getAbsolutePath()), separationType, detectorType,
                        new TreatmentGroupDescriptor(fileToGroup.get(f)));
                fileToDescriptor.put(f, icds[i]);
                i++;
            }

            HashMap<File, INormalizationDescriptor> normalizationDescriptors = (HashMap<File, INormalizationDescriptor>) props.
                    get(DBProjectVisualPanel3.PROP_FILE_TO_NORMALIZATION);
            for (File file : normalizationDescriptors.keySet()) {
                fileToDescriptor.get(file).setNormalizationDescriptor(
                        normalizationDescriptors.get(file));
            }

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
//                TreatmentGroupDescriptor tg = new TreatmentGroupDescriptor(group);
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
                    output = FileUtil.createFolder((File) props.get(
                            "output.basedir"));
                    icui.setOutputDir(output);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else {
                try {
                    output = FileUtil.createFolder((File) new File(projdir,
                            "output"));
                    icui.setOutputDir(output);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            try {
                createGroupFile(FileUtil.createData(new File(projdir, "data")),
                        fileToGroup);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }

            icui.getCrudProvider().close();
            //DBProjectFactory.openProject(icui);
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
            throw ex;
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
