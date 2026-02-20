DD-MM/YY
20-4/23 Build 1: First build for the year just updating libraries with the ground work to convert over to mongodb.
20-4/23 Build 2: Refactoring and rewriting some code.
21-4/23 Build 1: General cleanup and improvements. Layouts are generally sorted awaiting the overhaul.
23-4/23 Build 1: Decoupled the classes by using a single database object
11-6/23 Build 1: Made the final layout and alternate recycler view layout

-Rebranded-

12-6/23 Build 1: Custom Buttons, notes app bar created, light mode set up and notes floating action button removed
13-6-23 Build 1: Notes nav bar built out, minor layout fixes, new icon


25-1/26 Build 1
Added
ViewBinding support
Enabled viewBinding in module build.gradle and introduced ActivityMainBinding for type-safe view access
Migrated RecyclerViewAdapter to ListAdapter with DiffUtil for efficient list updates and animations.
Adapter now owns its own data; submitList() used for updates.
DiffUtil tracks item changes using ID for identity and name/note for content equality.

Changed
Refactored MainActivity to use ViewBinding instead of findViewById
Reworked RecyclerView setup to:
Initialize adapter once
Improved lifecycle handling by refreshing data in onResume()
Renamed app source package to com.phantomnotes.app to match applicationId.
Updated AndroidManifest.xml to match new package; launcher activity points to correct MainActivity.
ViewHolders updated to bind data safely using DatabaseObject instances.
Deletion now handled in adapter with proper notifyItemRemoved() / submitList() updates — no MainActivity coupling.
Clicks and long-clicks correctly navigate or delete notes while keeping UI and database in sync.

Fixed
Removed static references to RecyclerView, adapter, and data list to prevent memory leaks
Corrected ViewBinding import to align with applicationId
Resolved RecyclerView binding errors caused by views defined in included layouts
Ensured RecyclerView access matches actual XML hierarchy (binding.includeId.recyclerView)
SQLManager class now properly imported and referenced.
Fully qualified class references replaced with clean imports.
Refactor: RecyclerViewAdapter Modernization
Removed all legacy notifyDataSetChanged() calls.
Adapter no longer accesses static lists or static references to MainActivity.
Eliminated potential memory leaks from static references.

Removed
Unused imports and legacy patterns
