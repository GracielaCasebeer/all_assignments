# Student Information
<put your name and email here for identification purposes>

# Programming Assignment 4 - Map Reduce Introduction
In this assignment you will modify a maven project by adding code to 
implement a series of map reduce jobs.

## Overview
The previous assignments introduced you to maven, ToolRunner, and the 
Hadoop FileSystem.
While these previous projects would run outside of the Hadoop cluster,
this project requires a cluster in order to run. 
In this assignment I introduce MRUnit, a test framework for Hadoop 
map reduce, to help you build and debug your project without using the 
cluster.
The starting point section describes what functionality you start with. 
The command line interface specification section details how your program 
will be run when completed.

## Starting Point
This project includes a Word Count job complete with unit tests.
Notice that I have created a separate package called wordcount and you 
will find three classes in the package.
The jhu.App class is still the main entry point interpreting the
arguments to the commands.
Note that I do not pass arguments to the mapreduce job in the same way.
Instead, we pass parameters using the Hadoop Configuration object.

You should verify the project will run in your environment.
```
$ mvn clean package
$ hdfs dfs -put /etc/hadoop
$ yarn jar \
target/hw3-mapred-intro-1.0-SNAPSHOT-jar-with-dependencies.jar \
jhu.App wordcount hadoop words
$ hdfs dfs -cat words/part* | head
```
If everything works, you will see a list of the first 10 words and counts.

## Anatomy of a map reduce job
There are five major user controlled components of a job:
1. Driver - Configures the framework and controls the job
1. Mapper - implements the mapping function
1. Combiner - implements optional local reduce function
1. Partitioner - implements the partitioning function
1. Reducer - implements the reduce function

## MRUnit
The MRUnit framework is limited to testing mappers and reducers. 
The driver code will have to be tested on the cluster.
You will find three unit tests for the word count job. 
The mapper and reducer are tested independently and the driver 
test simply tests the mapper and reducer together. 
See https://cwiki.apache.org/confluence/display/MRUNIT/Index for 
more information.

### Test and Package
Maven allows you to run just the tests. 
```
$ mvn test
```
When you package your jar, the tests will also be run.
If there are failures, the jar will not be built.
```
$ mvn clean package
```

# Map Reduce Introduction
An introduction to Hadoop MapReduce through programming exercises.

## Instructions
1. Fill out the Student Information section above with your Name 
and jhu email id.
1. Complete the map reduce programming challenges.
1. Submit your assignment:
   1. Push your changes to gitlab.
   1. Download the tar.gz archive of your project
   1. Rename the archive using your jhu username 
   (e.g. my submittal would be pwilso12.hw3.tar.gz). 
   1. submit to blackboard

## Programming Challenges
You should be able to implement both programming challenges. 
While I encourage you to write tests using MRUnit, 
doing so is optional.
Using the MRUnit package will save you time since you do not need 
to have your cluster available to debug your code.

You already have a main for the App class that processes the 
command line arguments and invokes the wordcount job.
You should add a package for each challenge and add code to the App
class to configure and run the job on command.
See the Command Line Interface Specification section for details.

### Word Count improved
When you run the wordcount job over the hadoop configuration, 
it counts a lot of the words that are not very interesting.
In fact the highest frequency word is whitespace.
Take a look at the mapreduce-quickstart project for clues.
Consider improving the regex matching by taking advantage of 
predefined character classes. See ﻿
[oracle regex tutorial]
(https://docs.oracle.com/javase/tutorial/essential/regex/pre_char_classes.html).
Improve the existing Word Count job in the following ways:
1. Eliminate non-alphabet characters
1. Make all words lower case
1. Ignore stop words
1. Add a combiner

Test out your wordcount using [The Adventures of Alice In Wonderland](http://www.gutenberg.org/files/11/11-0.txt). 

### Search Index
Create a new job that builds a search index. 
Instead of counting occurrences of a word, it tracks where the word appeared.
Think about the mapper output key and value types you will need.
Don't forget to match the mapper output value class in your driver with what you 
choose for your mapper and reducer.
The reduce step should process a list of values and produce a list of word and file
pairs with the corresponding offsets.
```
<word> <filename> <offset-1> <offset-2> ...
```

## Command Line Interface Specification

Command | Notes
----------|---------------
wordcount \<inputPath\> \<outputPath\> | Your improved version of word count
searchindex \<inputPath\> \<outputPath\> | Builds a search index


# Student Observations
Please answer each section or state "none".

## Problems Encountered / how you resolved them
<did you encounter any interesting issues? 
How did you resolve them?>

## Resources you found helpful
<did you find a really good reference?
Please share them here>

## Describe any help you recieved
<did you discuss the problems you were having with others?
Discussion is encouraged as long as you do not share specific solutions>

## Make recommendations for improvement
<How should I improve this assignment?>
