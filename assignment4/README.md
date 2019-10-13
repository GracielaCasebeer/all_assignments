# Student Information
Author: Graciela A Casebeer 

Email: gcasebe1@jhu.edu

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
I wanted to create a structure of the form HashMap<String,HashSet<Integer>> where the key for the hashmap was the file name being processed and the hashset was the list of positions of a given word in the file.
I tried to use a generic Object as the map output and reducer input, but I kept getting type mismatch errors. I found that the input/output key-values for mapper and reducer need to be Writable. So, I attempted to create my custom HashMap<String,HashSet<Integer>> using Writable parameters as HashMap<Text,HashSet<IntWritable>> but all the examples I found online were tailored to much simpler structures. Just combinations of Text/IntWritable types in a "plain vanilla" form, meaning that they were not part of a more sophisticated structure as a HashMap containing HashSet values.
Finally I found this sample code that taught me how to serialize/deserialize this type of structure (thanks to deleteman for his sample code!).
https://github.com/deleteman/inverted-index-java/blob/master/src/main/java/com/globant/training/invertedIndex/WordIntArrayDict.java

Another silly problem that I found and set me back quite a bit was that I was so concentrated in getting the mapper/reducer work that I neglected to see that my driver class had the wrong map output set. It was frustrating how much time a lost for this little mistake. The lesson was that I need to pay a lot more attention to detail! One must not forget how we set the job configuration in the driver. I spun my wheels for quite a while until I finally went back to the driver and there it was! I set the wrong type for the map output... I felt a so dumb, but oh well... live and learn.

## Resources you found helpful
Google, Bing, Yahoo, GitHub

## Describe any help you recieved
None

## Make recommendations for improvement
None
