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
MainActivity Refactor: 
Added
ViewBinding support
Enabled viewBinding in module build.gradle
Introduced ActivityMainBinding for type-safe view access

Changed
Refactored MainActivity to use ViewBinding instead of findViewById
Updated toolbar, FAB, and RecyclerView initialization to use binding references
Reworked RecyclerView setup to:
Initialize adapter once
Update data via list mutation and notifyDataSetChanged()
Converted options menu handling from if statements to a switch statement
Improved lifecycle handling by refreshing data in onResume()

Fixed
Removed static references to RecyclerView, adapter, and data list to prevent memory leaks
Corrected ViewBinding import to align with applicationId
Resolved RecyclerView binding errors caused by views defined in included layouts
Ensured RecyclerView access matches actual XML hierarchy (binding.includeId.recyclerView)

Removed
Deprecated view lookup pattern using findViewById
Unused imports and legacy patterns