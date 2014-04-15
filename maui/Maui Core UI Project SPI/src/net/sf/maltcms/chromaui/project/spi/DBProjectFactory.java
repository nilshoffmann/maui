/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2014, The authors of Maui. All rights reserved.
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
package net.sf.maltcms.chromaui.project.spi;

import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.VariableFragment;
import cross.exception.ResourceNotAvailableException;
import cross.tools.StringTools;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import net.sf.maltcms.chromaui.db.api.exceptions.AuthenticationException;
import net.sf.maltcms.chromaui.io.chromaTofPeakImporter.api.ChromaTOFImporter;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.container.SampleGroupContainer;
import net.sf.maltcms.chromaui.project.api.container.TreatmentGroupContainer;
import net.sf.maltcms.chromaui.project.api.descriptors.IChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.INormalizationDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ISampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ITreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.api.types.GCGC;
import net.sf.maltcms.chromaui.project.api.types.IDetectorType;
import net.sf.maltcms.chromaui.project.api.types.ISeparationType;
import net.sf.maltcms.chromaui.project.spi.descriptors.ChromatogramDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.SampleGroupDescriptor;
import net.sf.maltcms.chromaui.project.spi.descriptors.TreatmentGroupDescriptor;
import net.sf.maltcms.chromaui.project.spi.project.ChromAUIProject;
import net.sf.maltcms.chromaui.project.spi.runnables.ImportAndiChromFileTask;
import net.sf.maltcms.chromaui.project.spi.wizard.DBProjectVisualPanel3;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareCallable;
import org.apache.commons.io.FileUtils;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import ucar.ma2.ArrayDouble;

/**
 * Provides utility methods to create a new project.
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
            System.out.println("Adding file: " + f[i].getAbsolutePath());
        }
        return f;
    }

    public static void createGroupFile(FileObject targetDirectory,
            Map<File, String> fileToGroup, Map<File, String> fileToSampleGroup) {
        try {
            FileObject fo = FileUtil.createData(targetDirectory, "groups.csv");
            StringBuilder sb = new StringBuilder();
            sb.append("FILE");
            sb.append("\t");
            sb.append("GROUP");
            sb.append("\t");
            sb.append("SAMPLE");
            sb.append("\n");

            for (File f : fileToGroup.keySet()) {
                sb.append(StringTools.removeFileExt(f.getName()));
                sb.append("\t");
                sb.append(fileToGroup.get(f));
                String sampleGroup = fileToSampleGroup.get(f);
                sb.append("\t");
                if (sampleGroup != null) {
                    sb.append(sampleGroup);
                }
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

    public static void initGroups(ProgressHandle handle, Map<String, Object> props, File projdir) throws AuthenticationException, IOException {
        if (!projdir.exists()) {
            projdir.mkdirs();
        }
        handle.start(11);
        String projectFileName = DBProjectFactory.PROJECT_FILE;
        IChromAUIProject icui = DBProjectFactory.getDefault();
        ISeparationType separationType = (ISeparationType) props.get(
                "separationType");
        IDetectorType detectorType = (IDetectorType) props.get("detectorType");
        try {
            handle.progress("Activating database", 1);
            icui.activate(new File(projdir, projectFileName).toURI().toURL());
            icui.getCrudProvider();
            Object o = props.get("input.dataInfo");
            Map<File, String> fileToGroup = (Map<File, String>) props.get(
                    "groupMapping");
            File[] inputFiles = toFileArray((String) o, ",");
            handle.progress("Importing data", 2);
            Map<File, File> importFileMap = new LinkedHashMap<File, File>();
            fileToGroup = importChromatograms(props, projdir, inputFiles, fileToGroup, importFileMap);
            LinkedHashMap<File, IChromatogramDescriptor> fileToDescriptor = new LinkedHashMap<File, IChromatogramDescriptor>();
            handle.progress("Initializing chromatograms", 3);
            initChromatograms(props, inputFiles, projdir,
                    separationType, detectorType, fileToDescriptor);
            handle.progress("Adding normalization info", 4);
            addNormalizationDescriptors(props, importFileMap, fileToDescriptor);
            //add treatment groups
            LinkedHashSet<String> groups = new LinkedHashSet<String>();
            LinkedHashMap<String, Set<File>> groupToFile = new LinkedHashMap<String, Set<File>>();
            handle.progress("Initializing treatment groups", 5);
            initGroups(groups, fileToGroup, groupToFile);
            System.out.println("Group to file: " + groupToFile);
            handle.progress("Adding treatment groups", 6);
            addTreatmentGroups(groupToFile, fileToDescriptor, icui);
            //add sample groups
            LinkedHashSet<String> sampleGroups = new LinkedHashSet<String>();
            LinkedHashMap<String, Set<File>> sampleGroupToFile = new LinkedHashMap<String, Set<File>>();
            Map<File, String> fileToSampleGroup = (Map<File, String>) props.get(
                    "sampleGroupMapping");
            System.out.println("File to sample group 1: " + fileToSampleGroup);
            handle.progress("Mapping input files", 7);
            fileToSampleGroup = remapInputFiles(fileToSampleGroup, importFileMap);
            System.out.println("File to sample group 2: " + fileToSampleGroup);
            handle.progress("Initializing sample groups", 8);
            initSampleGroups(sampleGroups, fileToSampleGroup, sampleGroupToFile);
            System.out.println("Sample group to file: " + sampleGroupToFile);
            handle.progress("Adding sample groups", 9);
            addSampleGroups(sampleGroupToFile, fileToDescriptor, icui);
            handle.progress("Creating file layout", 10);
            createSubdirectories(props, icui, projdir, fileToGroup, fileToSampleGroup);
            icui.closeSession();
            handle.progress("Done.", 11);
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
            throw ex;
        } finally {
            handle.finish();
        }
    }

    private static Map<File, String> remapInputFiles(Map<File, String> fileToStringMap, Map<File, File> importFileMap) {
        Map<File, String> remappedMap = new LinkedHashMap<File, String>();
        for (File file : fileToStringMap.keySet()) {
            remappedMap.put(importFileMap.get(file), fileToStringMap.get(file));
        }
        return remappedMap;
    }

    private static void createSubdirectories(Map<String, Object> props,
            IChromAUIProject icui, File projdir,
            Map<File, String> fileToGroup, Map<File, String> fileToSampleGroup) {
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
                    fileToGroup, fileToSampleGroup);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private static void addSampleGroups(LinkedHashMap<String, Set<File>> sampleGroupToFile,
            LinkedHashMap<File, IChromatogramDescriptor> fileToDescriptor,
            IChromAUIProject icui) {
        for (String group : sampleGroupToFile.keySet()) {
            ISampleGroupDescriptor sampleGroupDescriptor = new SampleGroupDescriptor();
            sampleGroupDescriptor.setName(group);
            sampleGroupDescriptor.setDisplayName(group);
            SampleGroupContainer tgc = new SampleGroupContainer();
            tgc.setSampleGroup(
                    sampleGroupDescriptor);
            ISampleGroupDescriptor sgd = new SampleGroupDescriptor();
            sgd.setName(group);
            sgd.setDisplayName(group);
            Set<File> files = sampleGroupToFile.get(group);
            for (File f : files) {
                IChromatogramDescriptor descr = fileToDescriptor.get(f);
                descr.setSampleGroup(sampleGroupDescriptor);
                tgc.addMembers(descr);
            }
            tgc.setName(group);
            tgc.setDisplayName(group);
            icui.addContainer(tgc);
        }
    }

    private static void addTreatmentGroups(
            LinkedHashMap<String, Set<File>> groupToFile,
            LinkedHashMap<File, IChromatogramDescriptor> fileToDescriptor,
            IChromAUIProject icui) {
        for (String group : groupToFile.keySet()) {
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
            for (File f : files) {
                IChromatogramDescriptor descr = fileToDescriptor.get(f);
                descr.setTreatmentGroup(treatmentGroupDescriptor);
                tgc.addMembers(descr);
            }
            tgc.setName(group);
            tgc.setDisplayName(group);
            icui.addContainer(tgc);
        }
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

    private static void initSampleGroups(LinkedHashSet<String> sampleGroups,
            Map<File, String> fileToSampleGroup,
            LinkedHashMap<String, Set<File>> groupToFile) {
        sampleGroups.addAll(fileToSampleGroup.values());
        for (String sampleGroup : sampleGroups) {
            for (File key : fileToSampleGroup.keySet()) {
                if (fileToSampleGroup.get(key).equals(sampleGroup)) {
                    if (groupToFile.containsKey(sampleGroup)) {
                        Set<File> s = groupToFile.get(sampleGroup);
                        s.add(key);
                    } else {
                        Set<File> s = new LinkedHashSet<File>();
                        s.add(key);
                        groupToFile.put(sampleGroup, s);
                    }

                }
            }
        }
    }

    private static Map<File, String> importChromatograms(Map<String, Object> props,
            File projdir, File[] inputFiles,
            Map<File, String> fileToGroup, Map<File, File> importFileMap) throws RuntimeException, IOException {
        Boolean copyFiles = (Boolean) props.get("copy.files");
        if (copyFiles) {
            Map<File, String> newFileToGroup = new LinkedHashMap<File, String>();
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
                        importFileMap.put(file, newFile);
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
        } else {
            for (File file : fileToGroup.keySet()) {
                importFileMap.put(file, file);
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
        Double scanRate = (Double) props.get("scanRate");
        int i = 0;
        for (File f : inputFiles) {
            IFileFragment ff = getProjectFileForResource(projdir, f);
            System.out.println("Adding FileFragment: " + ff.getName());
            if (modulationTime != null) {
                IVariableFragment ivf = new VariableFragment(ff,
                        "modulation_time");
                ArrayDouble.D0 modTime = new ArrayDouble.D0();
                modTime.set(modulationTime.doubleValue());
                ivf.setArray(modTime);
            }
            if (scanRate != null) {
                System.out.println("Using user-defined scan_rate=" + scanRate);
                IVariableFragment sr = new VariableFragment(ff, "scan_rate");
                ArrayDouble.D0 scanRateArr = new ArrayDouble.D0();
                scanRateArr.set(scanRate);
                sr.setArray(scanRateArr);
            } else {
                if (separationType instanceof GCGC) {
                    try {
                        IVariableFragment scanRateVar = ff.getChild("scan_rate");
                    } catch (ResourceNotAvailableException rnae) {
                        System.err.println("Variable scan_rate not available in source file: " + ff.getName());
                        throw rnae;
                    }
                }
            }

            ff.save();
            icds[i] = DBProjectFactory.getChromatogramDescriptor(new File(ff.getUri()), separationType, detectorType);
            fileToDescriptor.put(f, icds[i]);
            i++;
        }
    }

    private static void addNormalizationDescriptors(Map<String, Object> props, Map<File, File> importFileMap,
            LinkedHashMap<File, IChromatogramDescriptor> fileToDescriptor) {
        HashMap<File, INormalizationDescriptor> normalizationDescriptors = (HashMap<File, INormalizationDescriptor>) props.get(DBProjectVisualPanel3.PROP_FILE_TO_NORMALIZATION);
        for (File file : normalizationDescriptors.keySet()) {
            fileToDescriptor.get(importFileMap.get(file)).setNormalizationDescriptor(
                    normalizationDescriptors.get(file));
        }
    }

    private static IFileFragment getProjectFileForResource(File projdir, File f) {
        File dataDir = new File(projdir, "data");
        if (!f.getName().contains(".")) {
            throw new IllegalArgumentException("Source file does not have a valid file extension: " + f);
        }
        try {
            FileUtil.createFolder(dataDir);
            IFileFragment fragment = null;
            if (f.getName().toLowerCase().endsWith(".csv") || f.getName().toLowerCase().endsWith(".txt")) {
                try {
                    //try chromatof csv parser
                    File importDir = new File(projdir, "import");
                    importDir = new File(importDir, "DBProjectFactory");
                    FileUtil.createFolder(importDir);
                    List<File> files = AProgressAwareCallable.createAndRun("Importing report " + f.getName(), ChromaTOFImporter.importAsVirtualChromatograms(importDir, f)).get();
                    System.out.println("Received files: " + files);
                    fragment = new FileFragment(dataDir, files.get(0).getName());
                    fragment.addSourceFile(new FileFragment(files.get(0)));
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else {
                FileFragment sourceFileFragment = new FileFragment(f);
                try {
                    //try chromatof csv parser
                    File importDir = new File(projdir, "import");
                    importDir = new File(importDir, "DBProjectFactory");
                    sourceFileFragment.getChild("ordinate_values", true);
                    try {
                        List<File> files = ImportAndiChromFileTask.createAndRun("Importing ANDI-CHROM file " + f.getName(), new ImportAndiChromFileTask(new File(importDir, StringTools.removeFileExt(f.getName())), new File[]{f})).get();
                        fragment = new FileFragment(dataDir, files.get(0).getName());
                        fragment.addSourceFile(new FileFragment(files.get(0)));
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (ExecutionException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                } catch (ResourceNotAvailableException rnae) {
                    fragment = new FileFragment(dataDir, StringTools.removeFileExt(f.getName()) + ".cdf");
                    System.out.println("Adding data file " + fragment.getName());
                    fragment.addSourceFile(new FileFragment(f));
                }
            }
            System.out.println("Returning fragment: " + fragment.getUri());
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
