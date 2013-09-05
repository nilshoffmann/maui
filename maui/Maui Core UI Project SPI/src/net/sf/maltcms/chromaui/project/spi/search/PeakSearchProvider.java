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
package net.sf.maltcms.chromaui.project.spi.search;

import com.db4o.ObjectSet;
import com.db4o.query.Query;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JComponent;
import net.sf.maltcms.chromaui.project.api.IChromAUIProject;
import net.sf.maltcms.chromaui.project.api.descriptors.IPeakAnnotationDescriptor;
import net.sf.maltcms.chromaui.project.api.nodes.INodeFactory;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.search.SearchPattern;
import org.netbeans.api.search.SearchRoot;
import org.netbeans.api.search.SearchScopeOptions;
import org.netbeans.api.search.provider.SearchInfo;
import org.netbeans.api.search.provider.SearchListener;
import org.netbeans.spi.search.provider.SearchComposition;
import org.netbeans.spi.search.provider.SearchProvider;
import org.netbeans.spi.search.provider.SearchProvider.Presenter;
import org.netbeans.spi.search.provider.SearchResultsDisplayer;
import org.openide.NotificationLineSupport;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Nils Hoffmann
 */
/**
 * Provider for searching recently modified files.
 */
@ServiceProvider(service = SearchProvider.class)
public class PeakSearchProvider extends SearchProvider {

    @Override
    public Presenter createPresenter(boolean replaceMode) {
        return new CustomPresenter(this);
    }

    @Override
    public boolean isReplaceSupported() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Peaks";
    }
	
	private class PeakSelectionCriteria {
		
	}
	
    /**
     * Presenter is an object that holds a UI component for setting search
     * criteria.
     */
    private class CustomPresenter extends Presenter {

        private PeakSearchPanel panel = null;

        public CustomPresenter(SearchProvider searchProvider) {
            super(searchProvider, false);
        }

        /**
         * Get UI component that can be added to the search dialog.
         */
        @Override
        public synchronized JComponent getForm() {
            if (panel == null) {
                panel = new PeakSearchPanel();
            }
            return panel;
        }

        @Override
        public HelpCtx getHelpCtx() {
            return new HelpCtx("org.netbeans.modules.search.ui.prototype.about");  //NOI18N
        }

        /**
         * Create search composition for criteria specified in the form.
         */
        @Override
        public SearchComposition<?> composeSearch() {
            return new CustomComposition(panel.getSelectedProjects(), this, panel.getMatchCriterion());
        }

        /**
         * Here we return always true, but could return false e.g. if file name
         * pattern is empty.
         */
        @Override
        public boolean isUsable(NotificationLineSupport notifySupport) {
            return true;
        }
    }

    /**
     * Custom algorithm that check date of last modification of searched files.
     */
    private class CustomComposition extends SearchComposition<IPeakAnnotationDescriptor> {

		MatchCriterion matchCriterion;
		Collection<? extends Project> projectsToSearch;
        SearchResultsDisplayer<IPeakAnnotationDescriptor> resultsDisplayer;
        private final Presenter presenter;
        AtomicBoolean terminated = new AtomicBoolean(false);

        public CustomComposition(Collection<? extends Project> projectsToSearch, Presenter presenter, MatchCriterion matchCriterion) {
            this.projectsToSearch = projectsToSearch;
            this.presenter = presenter;
			this.matchCriterion = matchCriterion;
        }

        @Override
        public void start(SearchListener listener) {
			for(Project project:projectsToSearch) {
				if(project instanceof IChromAUIProject) {
					IChromAUIProject chromauiproject = (IChromAUIProject)project;
					Query q = chromauiproject.getCrudProvider().createSession().getSODAQuery();
					q.constrain(IPeakAnnotationDescriptor.class);
					q = matchCriterion.apply(q);
					ObjectSet<IPeakAnnotationDescriptor> os = q.execute();
					for(IPeakAnnotationDescriptor ipad:os) {
						ipad.setProject(chromauiproject);
						getSearchResultsDisplayer().addMatchingObject(ipad);
					}
				}
			}
        }

        @Override
        public void terminate() {
            terminated.set(true);
        }

        @Override
        public boolean isTerminated() {
            return terminated.get();
        }

        /**
         * Use default displayer to show search results.
         */
        @Override
        public synchronized SearchResultsDisplayer<IPeakAnnotationDescriptor> getSearchResultsDisplayer() {
            if (resultsDisplayer == null) {
                resultsDisplayer = createResultsDisplayer();
            }
            return resultsDisplayer;
        }

        private SearchResultsDisplayer<IPeakAnnotationDescriptor> createResultsDisplayer() {

            /**
             * Object to transform matching objects to nodes.
             */
            SearchResultsDisplayer.NodeDisplayer<IPeakAnnotationDescriptor> nd =
                    new SearchResultsDisplayer.NodeDisplayer<IPeakAnnotationDescriptor>() {
						
                        @Override
                        public org.openide.nodes.Node matchToNode(
                                final IPeakAnnotationDescriptor match) {
							INodeFactory nodeFactory = Lookup.getDefault().lookup(INodeFactory.class);
                            return new FilterNode(nodeFactory.createDescriptorNode(match, Children.LEAF, Lookups.fixed(match.getProject())));
                        }
                    };
            return SearchResultsDisplayer.createDefault(nd, this,
                    presenter, "Peaks matching");
        }
    }
}
