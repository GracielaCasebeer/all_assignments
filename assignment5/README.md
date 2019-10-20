# Student Information
Author: Graciela A Casebeer 

Email: gcasebe1@jhu.edu

# Programming Assignment 5 - Map Reduce Wrangling
In this assignment you will be working with a structured data set and extracting insights from the data.
[Data Wrangling](https://en.wikipedia.org/wiki/Data_wrangling) is a term of art that refers to the process of preparing a data set for use with automated tools.
Here we are doing a bit of data wrangling to create the graph.
The first part is provided for you by a python script.

## Overview
Now that you are comfortable writing mapreduce, we are going to get a little more advanced. 
The structured data we are working with are MIME formatted email messages that make up the [Enron Corpus](https://www.cs.cmu.edu/~enron/).
We will build a graph and analyze its properties.

## Starting Point
This project includes two components that will help you get started. 
There is a python script that will transform the MIME formatted emails into json so we can handle them more easily within a Mapper.
You are also provided a Mapper implementation that demonstrates how you can read the JSON objects.
We are using GSON as it is already packaged with the hadoop jars.

You should examine the project and tests.

```
$ mvn test
```
Before you can run this project, you will need to prepare the data.

# Map Reduce Wrangling
Continuing our study of Hadoop MapReduce through programming exercises.

## Instructions
1. Fill out the Student Information section above with your Name 
and jhu email id.
1. Perform the data preparation steps.
1. Complete the map reduce programming challenges.
1. Answer the questions in the analysis section. 
1. Submit your assignment:
   1. Push your changes to gitlab.
   1. Download the tar.gz archive of your project
   1. Rename the archive using your jhu username 
   (e.g. my submittal would be pwilso12.hw4.tar.gz). 
   1. submit to blackboard

## Preparing the data
You will need to download the dataset and generate the json data before you can use it with the project.

1. Download the [enron data set](https://www.cs.cmu.edu/~enron/enron_mail_20150507.tar.gz)
1. Find a good place to put your data until you are ready to transfer to hdfs. For example, from your home directory, make a data directory.
1. Unpack the archive and observe the directory structure. There are over 500K emails organized into directories. Fully expanded, the data occupies about 1.6G of disk space.<br>
```$ cd data
   $ tar xvf ~/Downloads/enron_mail_20150507.tar.gz
```
1. Use the python program to generate the json data. This step may take quite some time to complete.<br>
```$ cd ../assignment5
   $ src/resources/process-emails.py ../data/maildir ../data/enronemails.json
```
1. Put the json file on your hdfs. 

## Programming Challenges
You should implement all programming challenges. 
While I encourage you to write tests using MRUnit, 
doing so is optional.
Using the MRUnit package will save you time since you do not need 
to have your cluster available to debug your code.

You already have a main for the App class that processes the 
command line arguments and invokes the wordcount job.
You should add a package for each challenge and add code to the App
class to configure and run the job on command.
See the Command Line Interface Specification section for details.

### Weighted graph
You need a map reduce job that will produce an edge list with properties for each edge.
The output of the job (the reduce phase) should have the following format:

```
<sender> <recipient> <to-freq> <cc-freq>
```
There should only be one pair of sender/recepient in the output and the frequencies are simply the total counts where the recipient was included in the email.

In the map phase, you will process the email structure and emit an edge for each recipient in the to and cc parts of the header.
One email could produce 10s of edges.
Be sure you structure your map output key such that you can aggregate the results in a reducer.

### Degree Centrality
Create a new job that can calculate degree centrality of each node in your graph. This is a very simple calcuation: in-degree is the number of edges coming in and out-degree is the number of edges going out.

The output of the job should have the following format.

```
<email> <in-degree> <out-degree>
```

For example, given the following graph

```
curly larry 20 10 
larry curly 30 40
larry moe 25 30 
moe curly 20 45
moe larry 15 35
```
we should have the following output.

```
curly 2 1
larry 2 2
moe 1 2
```

Parameterize the job to allow filtering and thresholding by each of the properties on our edges.
The user can supply flags to set one or more thresholds for filtering. 
For example, ```-ct 20``` indicates that the edge must have a \<cc-freq\> value of at least 20. 
You should use the job configuration to pass properties to your job.

## Analysis
Please provide your analysis of the email data based upon what you can determine from the graph and your degree centrality computation.
You are free to analyze the output of your job using any means you have available.

### Describe the resulting graph
For example, how many nodes and edges are there? 

First of all, the output of enron-stats gives us a file with 84,983 lines, which means we have almost 85 thousand different email addresses in the data.

The resulting graph contains 334,546 lines, which means that there are 334.5k sender-receiver unique pairs. In most of the cases, the sender either send the email to the receiver or cc'd the reciver, but didn't do both (e.g. to and cc).

In very few situations we find that an email was sent with the receiver both in the to and cc header. Examples:
zimin.lu@enron.com  stinson.gibner@enron.com    97  206
zimin.lu@enron.com  vince.kaminski@enron.com    118 174
zimin.lu@enron.com  zimin.lu@enron.com  10  6
yvette.connevey@enron.com   pat.clynes@enron.com    108 13
v.weldon@enron.com  mike.roberts@enron.com  12  21
tana.jones@enron.com    anthony.campos@enron.com    181 18


### What can degree centrality tell us?
Are there relationships that really stick out? 

Degree centrality tells how "popular" a person is via email based on how much email he or she receives (in-degree) or how how "talkative" a person is based on how many emails he or she sends (out-degree).

In the data, most people were either in one extreme (in-degree > 0) or the other (out-degree > 0), but not both.

However, there were a few exceptions. Examples:
adrian.woolcock@enron.com   18  93
adriane.schultea@enron.com  24  12
afilas@keyspanenergy.com    5   159
outlook.team@enron.com  39  1566

Particularly the last item, outlook.team@enron.com makes sense. Their out-degree is far superior than their in-degree. This is probably because their work role may be more of a broadcaster of information than a reciever of it.

### Any additional insights you have

I can see how this analysis may be used in social media to determine influencers and followers.

## Command Line Interface Specification

Command | Notes
----------|---------------
enron-stats \<inputPath\> \<outputPath\> | The starting point. You don't need to change this.
enron-graph \<inputPath\> \<outputPath\> | Builds the edge lists with properties
degree-centrality [-tt #] [-ct #] \<inputPath\> \<outputPath\> | Produce the degree centrality for graph. The -tt flag sets the threshold for \<to-freq\> and -ct for \<cc-freq\>. 


# Student Observations
Please answer each section or state "none".

## Problems Encountered / how you resolved them
I had problems with some maven dependencies. Once I purged my maven directory, things worked well.

## Resources you found helpful
Google, Bing

## Describe any help you recieved
None

## Make recommendations for improvement
None
