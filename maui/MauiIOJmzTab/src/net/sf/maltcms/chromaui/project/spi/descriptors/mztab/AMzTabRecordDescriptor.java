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

package net.sf.maltcms.chromaui.project.spi.descriptors.mztab;

import com.db4o.activation.ActivationPurpose;
import net.sf.maltcms.chromaui.jmztab.ui.api.IMzTabDescriptor;
import net.sf.maltcms.chromaui.project.api.descriptors.ABasicDescriptor;
import uk.ac.ebi.pride.jmztab.model.Assay;
import uk.ac.ebi.pride.jmztab.model.CVParam;
import uk.ac.ebi.pride.jmztab.model.MZTabRecord;
import uk.ac.ebi.pride.jmztab.model.MsRun;
import uk.ac.ebi.pride.jmztab.model.StudyVariable;


public abstract class AMzTabRecordDescriptor<T extends MZTabRecord> extends ABasicDescriptor implements IMzTabDescriptor {
    public final static String PROP_RECORD = "record";
    
    private T record;
    
    public T getRecord() {
        activate(ActivationPurpose.READ);
        return record;
    }
    
    public void setRecord(T element) {
        activate(ActivationPurpose.WRITE);
        T old = this.record;
        this.record = element;
        setName(element.getClass().getSimpleName());
        setDisplayName(element.getClass().getSimpleName());
        getPropertyChangeSupport().firePropertyChange(PROP_RECORD, old, element);
    }

    public boolean setValue(String logicalPosition, Object value) {
        return getRecord().setValue(logicalPosition, value);
    }

    public Object getValue(String logicalPosition) {
        return getRecord().getValue(logicalPosition);
    }

    public String toString() {
        return getRecord().toString();
    }

    public Double getAbundanceColumnValue(Assay assay) {
        return getRecord().getAbundanceColumnValue(assay);
    }

    public void setAbundanceColumnValue(Assay assay, Double value) {
        getRecord().setAbundanceColumnValue(assay, value);
    }

    public void setAbundanceColumnValue(Assay assay, String valueLabel) {
        getRecord().setAbundanceColumnValue(assay, valueLabel);
    }

    public Double getAbundanceColumnValue(StudyVariable studyVariable) {
        return getRecord().getAbundanceColumnValue(studyVariable);
    }

    public void setAbundanceColumnValue(StudyVariable studyVariable, Double value) {
        record.setAbundanceColumnValue(studyVariable, value);
    }

    public void setAbundanceColumnValue(StudyVariable studyVariable, String valueLabel) {
        record.setAbundanceColumnValue(studyVariable, valueLabel);
    }

    public Double getAbundanceStdevColumnValue(StudyVariable studyVariable) {
        return record.getAbundanceStdevColumnValue(studyVariable);
    }

    public void setAbundanceStdevColumnValue(StudyVariable studyVariable, Double value) {
        record.setAbundanceStdevColumnValue(studyVariable, value);
    }

    public void setAbundanceStdevColumnValue(StudyVariable studyVariable, String valueLabel) {
        record.setAbundanceStdevColumnValue(studyVariable, valueLabel);
    }

    public Double getAbundanceStdErrorColumnValue(StudyVariable studyVariable) {
        return record.getAbundanceStdErrorColumnValue(studyVariable);
    }

    public void setAbundanceStdErrorColumnValue(StudyVariable studyVariable, Double value) {
        record.setAbundanceStdErrorColumnValue(studyVariable, value);
    }

    public void setAbundanceStdErrorColumnValue(StudyVariable studyVariable, String valueLabel) {
        record.setAbundanceStdErrorColumnValue(studyVariable, valueLabel);
    }

    public String getOptionColumnValue(Assay assay, String name) {
        return record.getOptionColumnValue(assay, name);
    }

    public void setOptionColumnValue(Assay assay, String name, String value) {
        record.setOptionColumnValue(assay, name, value);
    }

    public String getOptionColumnValue(Assay assay, CVParam param) {
        return record.getOptionColumnValue(assay, param);
    }

    public void setOptionColumnValue(Assay assay, CVParam param, String value) {
        record.setOptionColumnValue(assay, param, value);
    }

    public String getOptionColumnValue(StudyVariable studyVariable, String name) {
        return record.getOptionColumnValue(studyVariable, name);
    }

    public void setOptionColumnValue(StudyVariable studyVariable, String name, String value) {
        record.setOptionColumnValue(studyVariable, name, value);
    }

    public String getOptionColumnValue(StudyVariable studyVariable, CVParam param) {
        return record.getOptionColumnValue(studyVariable, param);
    }

    public void setOptionColumnValue(StudyVariable studyVariable, CVParam param, String value) {
        record.setOptionColumnValue(studyVariable, param, value);
    }

    public String getOptionColumnValue(MsRun msRun, String name) {
        return record.getOptionColumnValue(msRun, name);
    }

    public void setOptionColumnValue(MsRun msRun, String name, String value) {
        record.setOptionColumnValue(msRun, name, value);
    }

    public String getOptionColumnValue(MsRun msRun, CVParam param) {
        return record.getOptionColumnValue(msRun, param);
    }

    public void setOptionColumnValue(MsRun msRun, CVParam param, String value) {
        record.setOptionColumnValue(msRun, param, value);
    }

    public String getOptionColumnValue(String name) {
        return record.getOptionColumnValue(name);
    }

    public void setOptionColumnValue(String name, Object value) {
        record.setOptionColumnValue(name, value);
    }

    public String getOptionColumnValue(CVParam param) {
        return record.getOptionColumnValue(param);
    }

    public void setOptionColumnValue(CVParam param, Object value) {
        record.setOptionColumnValue(param, value);
    }
    
    
    
}
