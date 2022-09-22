# CS 643 NJIT Programming Assignment 1

## How to Run

The roles for the solution are the following but see the Requirements section for more detail:

1. Initiator - This part reads each Image, determines if it has a Car with confidence > 90%, then send a queue message
2. Worker - Reads any queue messages and determines what Text is contained, writes the output file and terminates when a "-1" message arrives


## Two EC2 instances are required
Both use the same git repo or tar/zip file for the contents and different Bash Shell scripts are used to execute the functionality.

### Environment Installation Requirements
The following tools are needed to run the solution

1. Java JRE/JDK version 11 or higher
2. git client -- in order to pull down the latest code if you don't have a tar/zip to unpack.

>NOTE: run the following on each instance:

```bash
sudo amazon-linux-extras install java-openjdk11 -y
sudo yum install git -y
git clone https://github.com/cicorias/njit-cs643-projects-fall-2022.git
cd njit-cs643-projects-fall-2022

```

#### AWS Credentials
There needs to be in the `~/.aws` path two files
1. `~/.aws/config`
1. `~/.aws/credentials`


>NOTE: Obtain the contents from the Lab Environment [https://awsacademy.instructure.com/](https://awsacademy.instructure.com/).

The credentials are located after you "start the lab" under "Learner Lab -> AWS Details -> AWS CLI".

##### 
```
[default]
region = us-east-1
```

####

```
[default]
aws_access_key_id=dddd
aws_secret_access_key=ddddd
aws_session_token=SOME REALLY LONG KEY M=
```

While order really doesn't matter, it doesn't hurt to start the Worker role first as it just polls looking for messages.
### From Machine 1 (Worker)
>NOTE: The worker will poll the queue and continue forever until it sees any messages or the last `-1` message to terminate.
1. first git clone or unpack the zip file (see above)
2. run `./mvnw clean package -DskipTests=true `
3. run `./runWorker.sh`

### From Machine 2 (Initiator)

1. first git clone or unpack the zip file (see above)
2. run `./mvnw clean package -DskipTests=true `
3. run `./runInitiator.sh`







# Requirements of Project
This solution is required to do the following:

## Role Initiator

1. Read 10 images from Shared S3 Bucket
2. For each Image
   3. Classify if has car with confidence > 90%
   4. if Car drop SQS Message using key/index
5. Then last message is index "-1" as indication of done

## Role Worker

1. At start listen or poll SQS
2. If message arrives and is NOT "-1"
   3. Detect Text
   4. If has text, write to output
5. When message is "-1"
   6. exit program

### File Format:
```text
index, recognized text
```

## General Requirements
* Use AWS EC2 Images Virtual Machines
* Run Initiator on first VM; Worker on second VM
* Use Shared AWS S3 Bucket for Source Images
* Use AWS SQS for queue between Initiator and Worker
* Use AWS Rekognition for Image Classification
* Use AWS Rekognition for Text Recognition
* 


# Notes
Resolve multiple bindings [https://www.baeldung.com/slf4j-classpath-multiple-bindings](https://www.baeldung.com/slf4j-classpath-multiple-bindings)