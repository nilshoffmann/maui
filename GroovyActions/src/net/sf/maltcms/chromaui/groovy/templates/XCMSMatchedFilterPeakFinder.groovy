/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.maltcms.chromaui.groovy.templates

import java.text.DateFormat
import java.text.SimpleDateFormat

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.project.Project;
import net.sf.maltcms.chromaui.groovy.RawDataGroovyScript;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import maltcms.ui.fileHandles.cdf.CDFDataObject;
import org.openide.loaders.DataObject;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputWriter;
import org.openide.util.Cancellable;
import net.sf.maltcms.*;
import cross.*;
import maltcms.*;

/**
 *
 * @author nilshoffmann
 */
class XCMSMatchedFilterPeakFinder implements RawDataGroovyScript {
    
    final String name = "XCMSMatchedFilterPeakFinder"
    IChromAUIProject project
    CDFDataObject[] dataObjects
    ProgressHandle progressHandle
    
    private Process process
    
    @Override
    public void setProject(Project project) {
        this.project = (IChromAUIProject)project;
    }
    
    @Override
    public void setDataObjects(DataObject[] dataObjects) {
        this.dataObjects = (CDFDataObject[])dataObjects;
    }
    
    @Override
    public void setProgressHandle(ProgressHandle progressHandle) {
        this.progressHandle = progressHandle
    }
    
    @Override
    public boolean cancel() {
        if(process!=null) {
            process.destroy()
        }
    }
    
    @Override
    public void run() {
        progressHandle.setDisplayName(name)
        def snr = 5.0
        def fwhm = 5.0
        def max = 50
        def Date date = new Date()
        def DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
        def outdir = new File(FileUtil.toFile(project.getOutputDir()),formatter.format(date))
        outdir.mkdirs()
        progressHandle.start(dataObjects.length)
        int i = 1;
        dataObjects.each{it->
            IFileFragment rootFragment = cross.datastructures.tools.FragmentTools.getDeepestAncestor(FragmentTools.it.getFragment())
            progressHandle.progress("Processing ${rootFragment.getName()}",i)
            InputOutput io = IOProvider.getDefault().getIO(
            "Running ${name} on ${rootFragment.getName()}", false);
            //                io.setOutputVisible(true);
            io.select();
            final OutputWriter writer = io.getOut();
            try {
                def filepath=rootFragment.getAbsolutePath()
                def execution = "/vol/maltcms/maui/R/xcmsMatchedFilterPeakFinder.R --snr=${snr} --fwhm=${fwhm} --max=${max} --filepath=${filepath} --out=${outdir.absolutePath}"
                process = execution.execute()
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.
                            getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    writer.println(line);
                }
                process.waitFor()
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
            
            
            i++
        }
        progressHandle.finish()
        FileObject fo = FileUtil.toFileObject(outdir)
        fo.refresh()
    }
    
}

