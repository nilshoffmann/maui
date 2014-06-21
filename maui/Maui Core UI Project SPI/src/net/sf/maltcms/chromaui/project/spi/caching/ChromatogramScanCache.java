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
package net.sf.maltcms.chromaui.project.spi.caching;

import static cross.cache.CacheFactory.getDefault;
import cross.cache.ICacheDelegate;
import cross.cache.ICacheElementProvider;
import cross.cache.ehcache.AutoRetrievalEhcacheDelegate;
import cross.cache.ehcache.EhcacheDelegate;
import java.util.prefs.Preferences;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.openide.util.NbPreferences;

/**
 *
 * @author Nils Hoffmann
 */
public class ChromatogramScanCache {

    /**
     * Create a volatile self-populating cache with {@code name} for the given
     * chromatogram class. The settings of the cache are retrieved via the
     * {@code NbPreferences.forModule(c)}, based on the class of the
     * chromatogram. The actual settings are stored below this node for each
     * project individually.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param cacheName the name of the cache
     * @param provider the element provider
     * @return the cache delegate
     */
    public static <K, V> ICacheDelegate<K, V> createVolatileAutoRetrievalCache(String cacheName, ICacheElementProvider<K, V> provider) {
        Preferences prefs = NbPreferences.forModule(ChromatogramScanCache.class);
        int maxEntriesLocalHeap = prefs.getInt("maxEntriesLocalHeap", 1000);
        long timeToIdleSeconds = prefs.getLong("timeToIdle", 30l);
        long timeToLiveSeconds = prefs.getLong("timeToLive", 60l);
        MemoryStoreEvictionPolicy msep = MemoryStoreEvictionPolicy.fromString(prefs.get("memoryStoreEvictionPolicy", MemoryStoreEvictionPolicy.LRU.toString()));
        return createVolatileAutoRetrievalCache(cacheName, provider, maxEntriesLocalHeap, timeToIdleSeconds, timeToLiveSeconds, msep);
    }

    /**
     * Create a volatile self-populating cache with {@code name} for the given
     * chromatogram class. The settings of the cache are retrieved via the
     * {@code NbPreferences.forModule(c)}, based on the cl
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param cacheName the name of the cache
     * @param c the chromatogram class
     * @param provider the element provider
     * @param maxEntriesLocalHeap the maximim number of scans to keep in memory
     * @param timeToIdleSeconds the time required until an item in the cache is
     * idle
     * @param timeToLiveSeconds the time required until an item is finally
     * removed from the cache
     * @param memoryStoreEvictionPolicy the strategy for removing stale items
     * from the cache
     * @return the cache delegate
     */
    public static <K, V> ICacheDelegate<K, V> createVolatileAutoRetrievalCache(String cacheName, ICacheElementProvider<K, V> provider, int maxEntriesLocalHeap, long timeToIdleSeconds, long timeToLiveSeconds, MemoryStoreEvictionPolicy memoryStoreEvictionPolicy) {
        CacheManager cacheManager = getDefault();
        if (cacheManager.cacheExists(cacheName)) {
            return new EhcacheDelegate<>(cacheManager.getCache(cacheName));
        }
        CacheConfiguration cacheConfig = new CacheConfiguration()
                .name(cacheName)
                .maxEntriesLocalHeap(maxEntriesLocalHeap)
                .eternal(false)
                .timeToIdleSeconds(timeToIdleSeconds)
                .timeToLiveSeconds(timeToLiveSeconds)
                .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU)
                .persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.NONE))
                .transactionalMode(CacheConfiguration.TransactionalMode.OFF);
        Cache cache = new Cache(cacheConfig);
        cacheManager.addCache(cache);
        AutoRetrievalEhcacheDelegate<K, V> ared = new AutoRetrievalEhcacheDelegate<>(cache, provider);
        return ared;
    }
}
