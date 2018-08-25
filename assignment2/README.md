# Programming Assignment 2 - HDFS Basics
In this assignment we will explore HDFS capabilities via the shell application that comes with Hadoop.

## Assignment Preparation
Familiarize yourself with the [HDFS shell documentation](http://hadoop.apache.org/docs/r2.7.5/hadoop-project-dist/hadoop-common/FileSystemShell.html) for Hadoop 2.7.5. If you are also new to Linux and the ___bash___ shell, you should consult a command reference for Linux. 

When you are using the bash shell in your Linux VM, you are sitting on the Linux filesystem and your commands can use relative paths or absolute paths. The Linux filesystem is considered our local filesystem. 

The HDFS filesystem is considered as a remote filesystem. It is external to our Linux system (although it is actually running in our VM). Therefore the command syntax is slightly different. There is no notion of current directory when using the HDFS shell. However, you can use paths that are relative to your home directory.

### Add Hadoop commands to the PATH
If you haven't already done so, you should add the Hadoop commands to your path. If you want to do this temporarily, you can execute the following command:

```
export HADOOP_HOME=~/hadoop/current
export PATH=$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$PATH
``` 

### Startup HDFS
Now that you have hadoop on your path, you should be able to start HDFS from your current directory. Run the following command:

```
start-dfs.sh
```

### Make sure you have a home folder on HDFS

When you installed Hadoop, you created a user folder in HDFS. You can display the contents of your home directory with the following command:

```
hadoop fs -ls .
```

If you get an error, you need to create your home directory. I used the following commands to create mine:

```
hadoop fs -mkdir /user
hadoop fs -mkdir /user/pwilso12
```

## HDFS Shell Exercises

Demonstrate your knowledge of the bash and the HDFS shell by accomplishing the following objectives. You can use the ___script___ command to caputure the console output for your session. Alternatively, you can cut and paste the console output into a file.

### A) Create two directories on HDFS
1. Use the -mkdir command to create __/data__ and __/testHDFS__ in HDFS.
2. Use the -ls command to show that the directories exist.

Here is an example of what you should see:

```
[pwilso12@hadoop ~]$ hadoop fs -ls /
Found 4 items
drwxr-xr-x   - pwilso12 supergroup          0 2018-02-11 10:56 /data
drwxr-xr-x   - pwilso12 supergroup          0 2018-02-11 10:56 /testHDFS
drwx------   - pwilso12 supergroup          0 2018-02-09 22:40 /tmp
drwxr-xr-x   - pwilso12 supergroup          0 2018-02-09 22:39 /user
[pwilso12@hadoop ~]$ 
```

### B) Create some content on your local filesystem
You can use the ___echo___ command to put some content in a local file.

```
echo "HDFS test data" >testFile
```
1. Create the local file named __testFile__.
2. Print the output of this file on using the ___cat___ command.

### C) Place the content on HDFS
Use hadoop commands to accomplish the following tasks:

1. Place the file __testFile__ in the __/data__ folder on hdfs.
2. List the contents of the __/data__ folder.

### D) Manipulate content on HDFS
Use hadoop commands to perform the following:

1. Print the contents of the __testFile__ on HDFS (HINT: there is a cat command).
2. Move the file from the __/data__ folder to the __/testHDFS__ folder on hdfs.
3. Show that the file is now under the /testHDFS folder.
4. Make a copy of __testFile__ and name it __testFile2__ within the __/testHDFS__ folder.
5. Show that there are now two files under __/testHDFS__.

### E) Make observations
Using hadoop commands:

1. Show the file system utilization of your HDFS (HINT: Use the -df command).
2. Show the disk utilization for your __/testHDFS__ folder (HINT: Use the -du command).

### F) Remove content on HDFS
Use hadoop commands to perform the following:

6. Remove the __/data__ folder (HINT: use the -rmdir command). 
7. Attempt to remove the __/testHDFS__ folder the same way.
8. Force the removal of the __/testHDFS__ folder (HINT: use the -rm command with the force option).

# (BONUS) Build and run the sample code
This project includes source code you can build using maven. In our next assignment, we will use the Java API interact with HDFS. This sample code provides a simple HDFS shell using the API.

```
mvn clean package
```

Run against the local filesystem using java directly

```
java -cp target/assignment2-1.0-SNAPSHOT-jar-with-dependencies.jar edu.jhu.bdpuh.HdfsClient -lsr ~/hadoop/current/etc
```

Run against HDFS filesystem using hadoop

```
hadoop jar target/assignment2-1.0-SNAPSHOT-jar-with-dependencies.jar edu.jhu.bdpuh.HdfsClient -lsr /
```
