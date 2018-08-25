# Programming Assignment 1 - Hadoop Installation
In this assignment you will verify your Hadoop Installation by running sample code that comes with the Hadoop.

# Perform Installation of Hadoop
This was demonstrated in class, but not everyone was able to complete. Videos have been posted in Module 2 under the Installation Instructions. 
If you have any issues viewing the videos, post on the 

NOTE: You should create etc/hadoop/mapred-site.xml from the etc/hadoop/mapred-site.xml.template file (rename it or make a copy). 

# Show that your namenode is running
Take a screenshot of the namenode by browsing to http://localhost:50070

# Run the hadoop example map reduce job
The hadoop documentation provides you with an example command to run a map reduce job.
Use the script command to save your console output in a file. For example

$ script <filename>

This command will start a new shell session and record all keyboard commands and output to a file.
After you run your map reduce job, use the exit command to close out the file being written to by script.

$ exit


# Show that you used yarn
Take a screenshot of the ResourceManager UI by browsing to http://localhost:8088

# (Bonus) Build the sample code that is included in this git project
You will need to install the maven build tool:

$ sudo yum install maven2

From the project folder (where this README.md is located), build the project

$ mvn package

