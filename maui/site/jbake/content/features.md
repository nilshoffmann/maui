title=Features
date=2014-09-28
type=page
status=published
~~~~~~
<h2>File Formats</h2>
+ ANDI-CHROM, ANDI-MS, netCDF 
+ mzML
+ mzData, mzXML (limited)
+ msp (NIST AMDIS &copy; mass spec format)
+ LECO ChromaTOF &copy; peak list

<h2>Interactivity</h2>
+ all domain objects are interlinked and retrievable from specialized views 

<h2>Visualization</h2>
+ 1D intensity profile, side and top view
+ 1D extracted ion profile
+ 2D intensity profile (heatmap)
+ 2D fold change plot
+ 3D intensity profile (height mapped heatmap)
+ 3D PCA plot
+ visualization of tabular data, e.g. ANOVA reports
+ synchronization of viewports

<h2>Extendability</h2>

<h2>Integration</h2>
+ Export of mass spectra to clipboard, compatible to GMD database input format
+ 
<ul>
    <li>action for peak group database identification.</li>
    <li>combo boxes to select, which principle components are displayed.</li>
    <li>FoldChangeViewer module to display volcano plots on pairs of treatment groups, based on anova p-values.</li>
    <li>Action to create SVG application screenshots.</li>
    <li>possibility to hide sample nodes in peak group and peak container views.</li>
    <li>exception handling for unexpected exceptions, prematurely closing the database.</li>
    <li>a default ehcache configuration.</li>
    <li>additional actions for easy creation of user peak databases.</li>
    <li>additional action for identification of selected peaks.</li>
    <li>action to add a newly imported database to an existing database container.</li>
    <li>svg export of charts.</li>
    <li>xcms matched filter peak finding template.</li>
    <li>project-specific peak normalization remembering the last selected group.</li>
    <li>search ability for peaks.</li>
    <li>project-specific peak pvalue adjustment remembering the last selected method.</li>
    <li>database resize actions to Options panel.</li>
    <li>panning (CTRL+MOUSE) and mouse-wheel zooming ability to all charts.</li>
    <li>checking for peaks with empty mass spectra which are now skipped during conversion.</li>
    <li>a guard against accidental null setting of PeakAnnotationDescriptor displayName.</li>
    <li>a new Action to export the mass spectrum to the clipboard in a format compatible with Golm Metabolite Database's web frontend.</li>
    <li>a project submenu provider and switched implementation for getActions in ChromAUIProjectNode to the new system.</li>
    <li>flashing of scans in Chromatogram Views on selection from navigator view.</li>
    <li>category datasets to charting support, adapted other classes to changes.</li>
    <li>a warning dialog if different Descriptors should be deleted.</li>
    <li>new Methods to IMauiProject for descriptor and container retrieval by id.</li>
    <li>chromatogram creation to DescriptorFactory.</li>
    <li>msLevel attribute to Scans on creation.</li>
    <li>support for different radius for neighbor search in horizontal and vertical directions in 2D chromatogram views.</li>
    <li>some convenience methods to IMauiProject for simpler retrieval of the output and import directories.</li>
    <li>installed maltcms pipelines as dynamic actions into project context menu.</li>
    <li>additional field for maltcms runtime arguments.</li>
    <li>better error messages and progress indication to PeakAlignmentImporter.</li>
    <li>CheckDatabaseConsistency Action to check a closed database for errors.</li>
    <li>ui elements to change block size in Chromatogram2DPanel.</li>
    <li>Peak overlays are now better visible for 1D chromatogram views</li>
    <li>Peak overlays are colored according to peak descriptor container color, or by treatment group color</li>
    <li>a diff button to mass spectral viewer to show a difference spectrum of two mass spectra</li>
    <li>Peak groups now show a percentage of covered chromatograms</li>
    <li>Welcome center to aid with first steps in Maui</li>
    <li>Viewport synchronization for chromatogram views</li>
    <li>Selection system for chromatogram views</li>
    <li>Improved integration and configuration of external tools</li>
    <li>Import of ChromaTOF peak lists as pseudo-chromatograms</li>
    <li>Completed integration of Anova and PCA via RServe</li>
    <li>Interactive visualization of PCA results</li>
</ul>
