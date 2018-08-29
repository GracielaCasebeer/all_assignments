# Student Information
Author:  
Email: 

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
Challenging but very helpful in understanding the java api for hadoop programming.

## Problems Encountered / how you resolved them
I was unable to implement the -crc option for the -get command. I suspect I was supposed to download that with the file 
on my local but was not able to find the right functions in the api for it.

## Resources you found helpful
Google 

## Describe any help you recieved
none

## Make recommendations for improvement
none
