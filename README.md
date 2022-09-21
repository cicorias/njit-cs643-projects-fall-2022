# CS 643 NJIT Programming Assignment 1

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