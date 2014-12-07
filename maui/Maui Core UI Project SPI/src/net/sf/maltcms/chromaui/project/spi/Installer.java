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

import cross.Factory;
import cross.cache.CacheType;
import cross.datastructures.fragments.CachedList;
import cross.datastructures.fragments.Fragments;
import cross.datastructures.fragments.ImmutableVariableFragment2;
import org.openide.modules.ModuleInstall;
import org.openide.util.NbPreferences;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        //configure cached list
        String keyPrefetchOnMiss = CachedList.class.getName()
                + ".prefetchOnMiss";
        String keyCacheSize = CachedList.class.getName() + ".cacheSize";
        String keyUseCachedList = ImmutableVariableFragment2.class.getName() + ".useCachedList";
        boolean useCachedList = NbPreferences.forModule(Installer.class).node("cross").getBoolean(keyUseCachedList, false);
        boolean prefetchOnMiss = NbPreferences.forModule(Installer.class).node("cross").getBoolean(keyPrefetchOnMiss, false);
        int cacheSize = NbPreferences.forModule(Installer.class).node("cross").getInt(keyCacheSize, 1024);
        ImmutableVariableFragment2.useCachedIndexedAccess = useCachedList;
        Factory.getInstance().getConfiguration().setProperty(keyPrefetchOnMiss, prefetchOnMiss);
        Factory.getInstance().getConfiguration().setProperty(keyCacheSize, cacheSize);
        Fragments.setDefaultFragmentCacheType(CacheType.EHCACHE);
    }
}
