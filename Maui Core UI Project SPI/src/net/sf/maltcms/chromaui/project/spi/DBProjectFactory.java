/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.maltcms.chromaui.project.spi;

import cross.exception.ResourceNotAvailableException;
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
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.INormalizationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.SampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.TreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.spi.project.ChromAUIProject;
import net.sf.maltcms.chromaui.project.spi.wizard.DBProjectVisualPanel3;
import org.apache.commons.io.FileUtils;
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
            ISeparationType st, IDetectorType dt) {
        ChromatogramDescriptor gcd = new ChromatogramDescriptor();
        gcd.setResourceLocation(f.getAbsolutePath());
        gcd.setSeparationType(st);
        gcd.setDetectorType(dt);
        gcd.setDisplayName(f.getName());
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
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fo.getOutputStream()));
            bw.write(sb.toString());
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    public static void initGroups(Map<String, Object> props, File projdir) throws AuthenticationException, IOException {
        if (!projdir.exists()) {
            projdir.mkdirs();
        }
        String projectFileName = DBProjectFactory.PROJECT_FILE;
        IChromAUIProject icui = DBProjectFactory.getDefault();
        ISeparationType separationType = (ISeparationType) props.get(
                "separationType");
        IDetectorType detectorType = (IDetectorType) props.get("detectorType");
        try {
            icui.activate(new File(projdir, projectFileName).toURI().toURL());
            icui.getCrudProvider();
            Object o = props.get("input.dataInfo");
            Map<File, String> fileToGroup = (Map<File, String>) props.get(
                    "groupMapping");
            File[] inputFiles = toFileArray((String) o, ",");
            fileToGroup = importChromatograms(props, projdir, inputFiles, fileToGroup);
            LinkedHashMap<File, IChromatogramDescriptor> fileToDescriptor = new LinkedHashMap<File, IChromatogramDescriptor>();
            initChromatograms(props, inputFiles, projdir,
                    separationType, detectorType, fileToDescriptor);
            addNormalizationDescriptors(props, fileToDescriptor);

            LinkedHashSet<String> groups = new LinkedHashSet<String>();
            LinkedHashMap<String, Set<File>> groupToFile = new LinkedHashMap<String, Set<File>>();
            initGroups(groups, fileToGroup, groupToFile);

            System.out.println("Group to file: " + groupToFile);
            addTreatmentGroups(groupToFile, fileToDescriptor, icui);
            createSubdirectories(props, icui, projdir, fileToGroup);
            icui.closeSession();
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
            throw ex;
        }
    }

    private static void createSubdirectories(Map<String, Object> props,
            IChromAUIProject icui, File projdir,
            Map<File, String> fileToGroup) {
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
                output = FileUtil.createFolder(new File(projdir,
                        "output"));
                icui.setOutputDir(output);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        try {
            FileUtil.createFolder(new File(projdir, "reports"));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        try {
            createGroupFile(FileUtil.createData(new File(projdir, "data")),
                    fileToGroup);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private static void addTreatmentGroups(
            LinkedHashMap<String, Set<File>> groupToFile,
            LinkedHashMap<File, IChromatogramDescriptor> fileToDescriptor,
            IChromAUIProject icui) {
        for (String group : groupToFile.keySet()) {
//                TreatmentGroupDescriptor tg = new TreatmentGroupDescriptor(group);
            ITreatmentGroupDescriptor treatmentGroupDescriptor = new TreatmentGroupDescriptor();
            treatmentGroupDescriptor.setName(group);
            treatmentGroupDescriptor.setDisplayName(group);
            TreatmentGroupContainer tgc = new TreatmentGroupContainer();
            tgc.setTreatmentGroup(
                    treatmentGroupDescriptor);
            ISampleGroupDescriptor sgd = new SampleGroupDescriptor();
            sgd.setName(group);
            sgd.setDisplayName(group);
            Set<File> files = groupToFile.get(group);
//                Set<IChromatogramDescriptor> s = new LinkedHashSet<IChromatogramDescriptor>();
            for (File f : files) {
//                    s.addMembers(
                IChromatogramDescriptor descr = fileToDescriptor.get(f);
                descr.setTreatmentGroup(treatmentGroupDescriptor);
                tgc.addMembers(descr);
            }
//                tgc.addMembers(s.toArray(new IChromatogramDescriptor[s.size()]));
            tgc.setName(group);
            tgc.setDisplayName(group);
//                tg.setName(group);
//                tgc.addMembers(tg);
            icui.addContainer(tgc);
        }
//            icui.addContainer(tgc);
    }

    private static void initGroups(LinkedHashSet<String> groups,
            Map<File, String> fileToGroup,
            LinkedHashMap<String, Set<File>> groupToFile) {
        groups.addAll(fileToGroup.values());
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
    }

    private static Map<File, String> importChromatograms(Map<String, Object> props,
            File projdir, File[] inputFiles,
            Map<File, String> fileToGroup) throws RuntimeException, IOException {
        Boolean copyFiles = (Boolean) props.get("copy.files");
        if (copyFiles.booleanValue()) {
            Map<File,String> newFileToGroup = new LinkedHashMap<File,String>();
            System.out.println("Copying files to user project directory!");
            try {
                File originalData = new File(projdir, "original");
                FileObject originaldatadir = FileUtil.createFolder(originalData);
                int i = 0;
                for (File file : fileToGroup.keySet()) {
                    long spaceLeft = FileUtil.toFile(originaldatadir).
                            getFreeSpace();
                    long fileSize = file.length();
                    if (spaceLeft - (100 * 1024 * 1024) > fileSize) {
                        File newFile = new File(originalData,
                                file.getName());
                        FileUtils.copyFile(file, newFile);
                        String group = fileToGroup.get(file);
                        //fileToGroup.remove(file);
                        newFileToGroup.put(newFile, group);
                        inputFiles[i] = newFile;
                    } else {
                        throw new RuntimeException(
                                "Less than 100 MBytes of space left on " + originaldatadir.getPath() + " for file " + file.getName() + " (required: " + (fileSize / 1024 / 1024) + "MByte free: " + (spaceLeft / 1024 / 1024) + "). Not copying file!");
                    }
                    i++;
                }
                return newFileToGroup;
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
                throw ex;
            }
        }
        return fileToGroup;
    }

    private static void initChromatograms(Map<String, Object> props, File[] inputFiles, File projdir,
            ISeparationType separationType, IDetectorType detectorType,
            LinkedHashMap<File, IChromatogramDescriptor> fileToDescriptor) throws ResourceNotAvailableException {
        IChromatogramDescriptor[] icds = new IChromatogramDescriptor[inputFiles.length];
        //            IFileFragment[] fragments = new IFileFragment[inputFiles.length];
        Double modulationTime = (Double) props.get("modulationTime");
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
            //                ITreatmentGroupDescriptor tgd = new TreatmentGroupDescriptor();
            //                tgd.setName(fileToGroup.getMembers(f));
            icds[i] = DBProjectFactory.getChromatogramDescriptor(new File(ff.getAbsolutePath()), separationType, detectorType);
            //                ISampleGroupDescriptor sgd = new SampleGroupDescriptor();
            //                sgd.setName(icds[i].getDisplayName());
            //                icds[i].setSampleGroup(sgd);
            fileToDescriptor.put(f, icds[i]);
            i++;
        }
    }

    private static void addNormalizationDescriptors(Map<String, Object> props,
            LinkedHashMap<File, IChromatogramDescriptor> fileToDescriptor) {
        HashMap<File, INormalizationDescriptor> normalizationDescriptors = (HashMap<File, INormalizationDescriptor>) props.get(DBProjectVisualPanel3.PROP_FILE_TO_NORMALIZATION);
        for (File file : normalizationDescriptors.keySet()) {
            fileToDescriptor.get(file).setNormalizationDescriptor(
                    normalizationDescriptors.get(file));
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
