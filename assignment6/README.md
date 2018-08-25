# Student Information
<put your name and email here for identification purposes>

# Programming Assignment 6 - Map Reduce Query
In this assignment you will be introduced to Avro as a serialization framework. We will continue working with the Enron Data Set from last time.

## Overview
With this excercise, we will create a query capability using mapreduce over the Enron Emails encoded in Avro. 

__NOTICE: This project has been updated to include a map only example with avro as input and output. There were pom changes required to get this working.__

## Starting Point
This project includes three components that will help you get started. 
The python script from the last exercise is included again in case you need it. It transforms the MIME formatted emails into json so we can write Java to encode them in Avro.
You are also provided with a working prototype of a JSON to Avro encoding.
Finally, you are also provided a Mapper implementation that demonstrates how you can read the Avro objects.

You should examine the project and tests.

```
$ mvn test
```
Before you can run this project, you will need to prepare the data.

# Map Reduce Query
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
   (e.g. my submittal would be pwilso12.hw3.tar.gz). 
   1. submit to blackboard

## Prepare the data
You may need to download the dataset and generate the json data before you can use it with the project.

1. Download the [enron data set](https://www.cs.cmu.edu/~./enron/enron_mail_20150507.tar.gz).
1. Find a good place to put your data until you are ready to transfer to hdfs.
1. Unpack the archive and observe the directory structure. There are over 500K emails organized into directories. Fully expanded, the data occupies about 1.6G of disk space.<br>
```$ tar xvf ~/Downloads/enron_mail_20150507.tar.gz
```
1. Use the python program to generate the json data. This step may take quite some time to complete.<br>
```$ resources/process-emails.py <enron-data> <json-file>
```
1. Put the json file on your hdfs. 

## Verify JSON to Avro encoding
This project comes with a simple Avro schema that encodes some of the header information into Avro datums. 

1. Build the project
2. Run the enron-avro command against the json encoded emails to generate an avro file.
3. Put the avro file on hdfs 
4. Run the enron-stats command against the avro file

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

### Expanded Email Schema
You need to expand the existing schema to include additional fields from the EmailMessage instance mapped from JSON. 

* Body
* Subject
* Date

Visit the [avro project page](https://avro.apache.org/) for more information. You should modify the schema file already provided and test it with the enron-stat job.

```
./src/main/avro/email/simple_email.avsc
```

### Email Select
Create a new job that reads avro files and creates a reduced subset of the emails using the selection criteria passed on the command line. 
The output of this job should be avro file(s) using the same email schema.
Desigining the job in this fashion allows you to incrementally reduce your data with multiple queries.
See the API section for basic command format.

The following selection criteria should be supported:

Type | Options | Comments
-----|---------|---------
time | -start \<date\><br> -end \<date\> | One of these flags must be used but both should not be required. The date should be in the [ISO-8601 format](https://en.wikipedia.org/wiki/ISO_8601). Either date only or date and time. For example, 2002-01-02 or 2002-01-02T08:30:00Z should be allowed. 
address | -from \<pattern\><br> -to \<pattern\><br> -cc \<pattern\> -and| One of these must be expressed but all can be used. The pattern should be a valid regex expression surrounded by quotes. When multiple criteria are given the default is a logical OR of the selection criteria.
subject | \<pattern\> | Select emails that match the regex pattern in the subject
body | \<pattern\> | Select emails that match the regex pattern in the body

__IMPORTANT__: This should be a map only job. There is no need for a reduce step.

### Email Histogram
Create a job that produces a histogram of the emails.
The bin size can be 1 hour, 1 day, 1 month, or 1 year.
The output should be formatted as follows:

```
<bin-id> <count> 
```
The bin-id should be the starting point of the bin. For 1 hour bins, it will be the starting date and hour: e.g ```yyyy-mm-ddThh```.
For all others, it will be the starting date of the bin.

## Analysis
Answer the following questions using your query tools.

### What is the approximate date range of the data?
<some of these dates are outliers and should be disregarded>

### Which month had the most emails sent by enron employees?
<this will require a select and before the histogram>

### What is the frequency of enron only emails referencing happy hour?
<consider both the subject and body>

### Any additional insights you have


## Command Line Interface Specification

Command | Notes
----------|---------------
enron-avro \<localInputPath\> \<localOutputPath\> | Transform emails from JSON format to Avro.
enron-stats \<inputPath\> \<outputPath\> | The starting point. You don't need to change this.
email-select \<type\> \<options\> \<inputPath\> \<outputPath\> | Select emails based upon the criteria provided.
email-histogram \<bin-type\>  \<inputPath\> \<outputPath\> | Create a histogram based on the bin-type. The bin-type is ```hour```, ```day```, ```month```, or ```year```.


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
