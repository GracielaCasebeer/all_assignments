# Student Information
Author: Graciela A Casebeer 
Email: gcasebe1@jhu.edu

# Programming Assignment 3 - HDFS Java API
In this assignment you will modify a maven project by adding code to implement file system functions using the HDFS Java API.

## Maven Aspects
For this project, I have added the hadoop dependencies to your pom and I have also included a plugin for building a "fat" jar. This will be needed as you add dependencies in your code. 

# Build and Run specification
```
$ mvn clean package
For local filesystem
$ java -cp target/assignment3-1.0-SNAPSHOT-jar-with-dependencies.jar edu.jhu.bdpuh.App -lsr .
To use hdfs
$ hadoop jar target/assignment3-1.0-SNAPSHOT.jar edu.jhu.bdpuh.App -lsr .
```

# About this exercise
To introduce you to the HDFS Java API, I am asking you to implement a command line interface. 

# Instructions
1. Fill out the Student Information section above with your Name and jhu email id.
1. Validate this project by following the build and run steps shown above (the ls api is already implemented). There should be no errors when you build and run.
1. Modify App.java to implement the cli described in the CLI Specification section
1. Submit your assignment:
   1. Push your changes to gitlab.
   1. Download the tar.gz archive of your project
   1. Rename the archive using your jhu username (e.g. my submittal would be pwilso12.tar.gz). 
   1. submit to blackboard

# Command Line Interface Specification
Implement a few of the commands from the HDFS shell. Try to match the functionality provided. For example, the -ls command shows the file permissions and your should show those too. You should handle error conditions, especially user errors. If there are API challenges implementing the full spec, please make note of this in your comments.

Command | Notes
----------|---------------
\-ls \[\-d\] \[\-h\] \[\-R\] \[\<path\> ...\] | You have a partially working version. Improve it to support the options
\-cat \[\-ignoreCrc\] \<src\> | Simply cat the file. What is that ignorecrc flag about?
\-get \[\-p\] \[\-ignoreCrc\] \[\-crc\] \<src\> ... \<localdst\>| Get files from hdfs and save them locally
\-rm \[\-f\] \[\-r&#124;\-R\] \[\-skipTrash\] \<src\> ...| Remove files from hdfs

# Student Observations
Having the sample code was very helpful.
The files for this assignment are all located in the "gcasebeerhw3" folder.

## Problems Encountered / how you resolved them
I had a bit of a problem implementing the -skipTrash option. I noticed that newer versions of the API have more to get the trash directory more easily. However, since we installed hadoop 2.7.3, I worked with the commands available in that version of the API.

## Resources you found helpful
Google, Bing, Yahoo

## Describe any help you recieved
none

## Make recommendations for improvement
none
