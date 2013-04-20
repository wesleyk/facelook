15-214 Homework 9: Facelook
===========================

Authors: Wesley Kim, Nikhil Tibrewal, Jessica Tumarkin

Semester: Spring 2012

Build/Run Instructions:

Note: This project was built/written on the Java IDE Eclipse,
and it is strongly recommended that it is run on Eclipse.
The following instructions assume Eclipse is being used.
The IDE can be downloaded here: www.eclipse.org/downloads/.
Also, it is necessary to have Java running on your machine.

1) The first time the project is run, you must make sure that no
.db files exist in the hw9 directory. These are not included
in the default repository anyways, so it shouldn't be an issue.

2) The sqllitejdbc-v056.jar in the lib directory must be
linked to the correct file, otherwise you will get a
red exclamation point on your hw9 directory in Eclipse.

To fix this, you can right click the .jar file,
go to "Build Path" -> "Configure Build Path" -> "Libraries" tab.
Then, you can click the "sqllitejdbc-v056.jar", hit "Edit" to the left,
and point the file to its actual location on your machine.

3) The Server.java file in the edu.cmu.cs214.hw9.server package must be run as our server.
Database sharding is used, and we make some assumptions about the number of servers running
along with the ports that they are run on.
Thus, five instances of the server must be run, on ports 15210, 15211, 15212, 15213, and 15214.

4) The FacelookInitGUI.java file is our client main method.
Run this once the server is running to begin the application!

Thanks for reading, we hope you enjoy our project.
Also, thanks to the 15-214 Staff for providing the base code for the project (the entire GUI).
