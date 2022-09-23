# CS 643 NJIT Programming Assignment 1

## How to Run

The roles for the solution are the following but see the Requirements section for more detail:

1. Initiator - This part reads each Image, determines if it has a Car with confidence > 90%, then send a queue message
2. Worker - Reads any queue messages and determines what Text is contained, writes the output file and terminates when a "-1" message arrives


## Two EC2 instances are required
Both use the same git repo or tar/zip file for the contents and different Bash Shell scripts are used to execute the functionality.


### EC2 Instance Creation
From the AWS Console under EC2 choose to launch instances:

#### Name and tags
Use any arbitray name you choose -- not required

#### Application and OS Images (Amazon Machine Image)
Choose the Azure AMI Image - `Amazon Linux 2 Kernel 5.10 AMI`
Keep it at `x64-bit (x86)`

#### Instance type
Keep it at the `t2.micro` size

#### Key pair (login)
Choose the `vockey` that should already be setup when using AWS Educate labs

#### Network settings
Choose your default network

Security Group: ensure that just port `22/ssh` is enabled and `My IP` setting.  This is to allow SSH traffic from the workstation currently on.

It is not necessary to open `http/https` (`80/443`) as there is nothing in the project that uses that.

#### Configure storage
Leave the defaults.

#### Advanced details
Here best to choose an IAM profile that allows the instance permissions for calling other AWS Services used in this project.

##### IAM instance profile
Choose `LabInstanceProfile` -- this will enable automatic EC2 instance credentials and access to each of the AWS services used by the solution.

##### User data
This is the `cloud-init` script that automatically does environment setup, git clone, and build.

Paste in exactly below (do NOT include the three tick marks for the markdown script indicator)

```bash
#!/bin/bash
yum update -y
amazon-linux-extras install java-openjdk11 -y
yum install git -y
mkdir /njit
chmod 777 njit
cd /njit
git clone https://github.com/cicorias/njit-cs643-projects-fall-2022.git
chmod -R 777 *
cd njit-cs643-projects-fall-2022
./mvnw clean package -DskipTests=true
chmod -R 777 *
```
### Summmary

#### Number of instances
We use two in this demonstration -- set it to `2`


>DONE: Now choose `Launch instance` -- after a few minutes both images should be ready to login.

# Manual Setup
Only if you didn't choose the cloud-init / user data option.

## Environment Installation Requirements
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

## AWS Credentials
There needs to be in the `~/.aws` path two files
1. `~/.aws/config`
1. `~/.aws/credentials`


>NOTE: Obtain the contents from the Lab Environment [https://awsacademy.instructure.com/](https://awsacademy.instructure.com/).

The credentials are located after you "start the lab" under "Learner Lab -> AWS Details -> AWS CLI".

###
```
[default]
region = us-east-1
```

###

```
[default]
aws_access_key_id=dddd
aws_secret_access_key=ddddd
aws_session_token=SOME REALLY LONG KEY M=
```



# Login to each instance
While order really doesn't matter, it doesn't hurt to start the Worker role first as it just polls looking for messages.
## From Machine 1 (Worker)
>NOTE: The worker will poll the queue and continue forever until it sees any messages or the last `-1` message to terminate.
1. first git clone or unpack the zip file (see above)
2. run `./mvnw clean package -DskipTests=true `
3. run `./runWorker.sh`

## From Machine 2 (Initiator)

1. first git clone or unpack the zip file (see above)
2. run `./mvnw clean package -DskipTests=true `
3. run `./runInitiator.sh`


>NOTE: if any of the commands here fail, there was something that broke in the cloud-init step.  Thus you have to install java, git, etc. manuall.

You can follow the script provided in the user-data section above.

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


# Cloud init - user data for EC2

```bash
#!/bin/bash
yum update -y
amazon-linux-extras install java-openjdk11 -y
yum install git -y
mkdir /njit
chmod 777 njit
cd /njit
git clone https://github.com/cicorias/njit-cs643-projects-fall-2022.git
chmod -R 777 *
cd njit-cs643-projects-fall-2022
./mvnw clean package -DskipTests=true
chmod -R 777 *
```