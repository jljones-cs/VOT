# VOT
Application for detecting voice onset times in recordings.

The project is setup for building in NetBeans (v8.1). To run this in NetBean you would load it through the NetBeans open project dialog. However, you will also need to add the database driver, sqlite-jdbc-3.15.1.jar, to the classpath. If you want the driver to be available to other projects first add it to NetBeans' libary. >Tools>Libraries>New Library> and name it something like 'sqlite'. Now find the jar file which is in the lib folder of VOT_0_5. 

You will also need to add it to the sources in the project itself. In NetBeans expand the tree directory for the project, then right-click 'Libraries' and select 'Add Library'. Find 'sqlite' in the list and then click 'Add Library'.
