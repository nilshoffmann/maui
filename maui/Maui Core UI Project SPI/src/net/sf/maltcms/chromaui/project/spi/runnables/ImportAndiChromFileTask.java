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
package net.sf.maltcms.chromaui.project.spi.runnables;

import cross.Factory;
import cross.IFactory;
import cross.IFactoryService;
import cross.commands.fragments.IFragmentCommand;
import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.pipeline.CommandPipeline;
import cross.datastructures.tuple.TupleND;
import cross.datastructures.workflow.DefaultWorkflow;
import cross.datastructures.workflow.IWorkflow;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import lombok.Value;
import maltcms.commands.fragments.io.ANDIChromImporter;
import net.sf.maltcms.chromaui.project.api.descriptors.DescriptorFactory;
import net.sf.maltcms.chromaui.project.api.descriptors.IToolDescriptor;
import net.sf.maltcms.chromaui.ui.support.api.AProgressAwareCallable;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author Nils Hoffmann
 */
@Value
public class ImportAndiChromFileTask extends AProgressAwareCallable<List<File>> {

    private final File importDir;
    private final File[] files;

    @Override
    public List<File> call() {
        List<File> resultFiles = new LinkedList<File>();
        try {
            progressHandle.start(2);
            int peakReportsImported = 0;
            progressHandle.progress("Importing " + files.length + " chromatograms", 1);
            IToolDescriptor trd = DescriptorFactory.newToolResultDescriptor();
            trd.setName(getClass().getSimpleName());
            trd.setDisplayName(getClass().getSimpleName());
            resultFiles = importFiles(importDir, files);
            peakReportsImported += resultFiles.size();
            progressHandle.progress("Imported " + peakReportsImported + " ANDI-CHROM files.", 2);
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        } finally {
            progressHandle.finish();
        }
        return resultFiles;
    }

    protected List<File> importFiles(File outputDirectory, File... inputFiles) {
        IFactory factory = Lookup.getDefault().lookup(IFactoryService.class).getInstance(ImportAndiChromFileTask.class.getName());
        IWorkflow workflow = new DefaultWorkflow();
        workflow.setFactory((Factory) factory);
        CommandPipeline cp = new CommandPipeline();
        cp.setCheckCommandDependencies(false);
        ANDIChromImporter aci = new ANDIChromImporter();
        List<IFileFragment> fragments = new ArrayList<IFileFragment>();
        for (File f : inputFiles) {
            fragments.add(new FileFragment(f));
        }
        List<IFragmentCommand> commands = new ArrayList<IFragmentCommand>();
        commands.add(aci);
        cp.setCommands(commands);
        cp.setInput(new TupleND<IFileFragment>(fragments));
        DefaultWorkflow dw = new DefaultWorkflow();
        dw.setStartupDate(new Date());
        dw.setName("andiChromImportWorkflow");
        dw.setCommandSequence(cp);
        dw.setExecuteLocal(true);
        dw.setOutputDirectory(outputDirectory);
        List<File> outputFiles = new ArrayList<File>();
        try {
            TupleND<IFileFragment> results = dw.call();
            for (IFileFragment f : results) {
                outputFiles.add(new File(f.getUri()));
            }
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }
        return outputFiles;
    }

}
