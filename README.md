# Soundulous
### Music Visualizer, created for Mobile Computing (CS 434) final project
### Hari Ganesan, Rafi Khan, Mike Levine, Jacob Metrick
##### Yale University, November 2012

Overview
==========

> soundulous: eclipse project for android app  
> server: server code for processing and dispersing visualization data

Server
==========

All the server code for receiving requests from the phone and rendering the visualization. This directory is an Eclipse project, so can be opened in Eclipse    
The project can be started (from root directory) by

    cd server/src && ./run.sh


Soundulous
==========

All the Android app code for the project. This directory is also an Eclipse app. Download onto an Android (or run on an emulator) AFTER the server has been started, create a session and it will start receiving the visualization autmoatically.