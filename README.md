# Analysing Graph of Email Communications using Hadoop

This project aims at tracking down which email user to whom a given email user wrote emails the most, in a large-scaled settings with around 70 million records, each of which represents a connection between email addresses, by a MapReduce program in Java.

## Table of Contents

* [Data Source](#data-source)
* [Keywords](#keywords)
* [License](#license) 

## Data Source

The data set contains an entire email communication network from a popular social network site. The network is organized as a directed graph where each node represents a person's email address and an edge between two nodes that has a weight storing the number of times an email address has sent an email to the other email address. Each data file stores a list of edges as tab-separated-values (tsv). Each line in the tsv files represents a single edge consisting of three columns: (source node ID, target node ID, edge weight), each of which is separated by a tab (\t). Node IDs and weights are all positive integers.

Due to the size of graph2.tsv (around 1.12 GB when unzipped), it is not pushed into this repository. Please download the file from [here](https://drive.google.com/file/d/12EvT7J-wMZBOuVGrLISXTDgBwsgf3A1P/view?usp=sharing).

## Instructions

This project makes use of VirtualBox, as it provides a no-cost solution alternative to the cloud-based ones. Please first download and install VirtualBox 6.x.x as well as the Oracle VM Virtual Extension Pack, if you have not done so. Then, install Hadoop, Java and Maven on the virtual machine. Afterwards, please create a a shared folder so that files can be automatically mounted between the host operating system and the virtual machine.

To create a folder named "graph-vm" in the virtual machine, you can open the terminal (or command prompt on Windows) and enter the following command:

```
$ mkdir ~/graph-vm
```

and then mount the shared folder by typing:

```
$ sudo mount -t vboxsf graph-local graph-vm
```

Please be reminded that the two files, i.e. "graph-vm" and "grpah-local", requires to be remounted if the virtual machine is restarted.

When it is ready to move files out of the virtual machine, files can be copied to the mounted shared folder by the command as follows:

```
$ sudo cp -r <file name to be copied> ~/graph-vm
```

To check whether you have sucessfully mounted the shared folder, please locate if a folder named "graph-vm" icon appears on your host machine. If it is, please click the icon to access the folder, and the folder will be linked to "graph-local" folder in your host machine.

After setting up the virtual machine, please place the following files into the virtual machine: (1) the src directory, (2) pom.xml, (3) run1.sh, and (4) run2.sh.

We need to load the data sets into the Hadoop Distributed File System (HDFS), an abstract file system that stores files on clusters. Hadoop code will directly access the files on HDFS. Paths on the HDFS are similar to those on the UNIX system, but you need to use hadoop fs commands at the front. For example:

```
hadoop fs -ls
```

The following command loads the data files into a directory named "data" in the HDFS:

```
hadoop fs -mkdir data
hadoop fs -put <path to the data file>/graph1.tsv data
hadoop fs -put <path to the data file>/graph2.tsv data
```

To check whether the files are on HDFS, try:

```
hadoop fs -ls data
```

To compile, simply call Mavan in the corresponding directory by the command as follows:

```
mvn package
```

It will generate a single JAR file in the target directory.

The default outputing mechanism of Hadoop will create multiple files on the specified output directory with names like part-00000, part-00001, etc. These files will be merged and downloaded to a local directory by the supplied shell script files, run1.sh and run2.sh. The scripts accept two arguments. The first argument (args[0]) is the path for the input graph file on HDFS, e.g. data/graph1.tsv, whereas the second argument (args[1]) is the path for the output directory on HDFS, e.g. data/output1. The shell scripts are executed by entering the following commands:

```
$ ./run1.sh
$ ./run2.sh
```

It will (1) run the JAR on Hadoop with the input file on HDFS (the first argument) and output directory on HDFS (the second argument) specified, (2) merge outputs from output directory and download to local file system, and then (3) remove the output directory on HDFS.

The end results are tab separted values (tsv) files, in which each line contains (1) a node ID representing each unique email user, as well as a tuple followed by a tab with (2) the other email user to whom the former has sent emails the most and (3) the number of times the former has sent emails to the latter. In the case of tie-break in the weight (i.e. the frequency of sending emails), the email user to whom emails were sent to the most is simply the one who has the lowest id. Moreover, the node without any outgoing edges will be omitted; that is, email users who did not send out any emails will be excluded.

## Keywords

Apache Hadoop; Edge; Graph Analytics; Hadoop Distributed File System (HDFS); Node; Virtual Machine (VM).

## License

This repository is covered under the [MIT License](https://github.com/alfred-kctang/random-forest-pulsar-stars/blob/master/LICENSE).
