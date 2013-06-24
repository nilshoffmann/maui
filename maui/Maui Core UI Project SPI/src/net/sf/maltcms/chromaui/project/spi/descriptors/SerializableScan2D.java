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
package net.sf.maltcms.chromaui.project.spi.descriptors;

import cross.datastructures.cache.SerializableArray;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import maltcms.datastructures.ms.IScan2D;
import maltcms.datastructures.ms.Scan2D;
import ucar.ma2.Array;

/**
 *
 * @author Nils Hoffmann
 */
@Data
public class SerializableScan2D implements Externalizable {
    
    @Setter(AccessLevel.NONE)
    private Scan2D scan;

    public SerializableScan2D() {
    }
    
    public SerializableScan2D(Scan2D scan) {
        this.scan = scan;
    }

    @Override
    public void writeExternal(ObjectOutput oo) throws IOException {
        if(scan!=null) {
            oo.writeInt(scan.getScanIndex());
            oo.writeInt(scan.getFirstColumnScanIndex());
            oo.writeInt(scan.getSecondColumnScanIndex());
            oo.writeDouble(scan.getFirstColumnScanAcquisitionTime());
            oo.writeDouble(scan.getSecondColumnScanAcquisitionTime());
            oo.writeDouble(scan.getScanAcquisitionTime());
//            oo.writeDouble(scan.getTotalIntensity());
            oo.writeObject(new SerializableArray(scan.getMasses()));
            oo.writeObject(new SerializableArray(scan.getIntensities()));
        }
    }

    @Override
    public void readExternal(ObjectInput oi) throws IOException, ClassNotFoundException {
        int scanIndex = oi.readInt();
        int fcsi = oi.readInt();
        int scsi = oi.readInt();
        double rt1 = oi.readDouble();
        double rt2 = oi.readDouble();
        double sat = oi.readDouble();
//        double tic = oi.readDouble();
        SerializableArray ma = (SerializableArray)oi.readObject();
        Array masses = ma.getArray();
        SerializableArray ia = (SerializableArray)oi.readObject();
        Array intensities = ia.getArray();
        this.scan = new Scan2D(masses,intensities,scanIndex,sat,fcsi,scsi,rt1,rt2);
    }

}
