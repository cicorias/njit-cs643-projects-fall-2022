package org.cicoria.njit.aws.messaging;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code
public class AwsMessageHelper {
    private static Logger logger = LoggerFactory.getLogger(AwsMessageHelper.class);
    private String queueName;
    private SqsClient queue;
    private String nonce;

    private String queueUrl;
    public AwsMessageHelper(String queueName){
        this.queueName = queueName + ".fifo";
        this.nonce = String.valueOf(System.currentTimeMillis());
        create();
    }

    private void create() {
        this.queue = SqsClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        // Creating a Queue
        HashMap<QueueAttributeName, String> attributes = new HashMap<>();
        attributes.put(QueueAttributeName.FIFO_QUEUE, "true");
        attributes.put(QueueAttributeName.MESSAGE_RETENTION_PERIOD, "86400");
        // attributes.put(QueueAttributeName.CONTENT_BASED_DEDUPLICATION, "true");
        attributes.put(QueueAttributeName.RECEIVE_MESSAGE_WAIT_TIME_SECONDS, "5");
        CreateQueueRequest create_request = CreateQueueRequest
                .builder()
                .queueName(queueName)
                .attributes(attributes)
                .build();

        try {
            this.queue.createQueue(create_request);
        } catch (QueueNameExistsException e) {
            // do nothing..
        }

        GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                .queueName(this.queueName)
                .build();

        this.queueUrl = this.queue.getQueueUrl(getQueueRequest).queueUrl();
    }

    public void Delete() {
        DeleteQueueRequest request = DeleteQueueRequest
                .builder()
                .queueUrl(this.queueUrl)
                .build();

        queue.deleteQueue(request);
        this.Close();
    }
    public void Send(String message) {
        SendMessageRequest msgRequest = SendMessageRequest
                .builder()
                .messageBody(message)
                .queueUrl(this.queueUrl)
                .messageGroupId("group1")
                .messageDeduplicationId(getMessageDeduplicationId(message))
                .build();

        queue.sendMessage(msgRequest);
    }

    public void Close() {
        queue.close();
    }


    public List<String> Get(){
        return this.Get(1);
    }

    public List<String> Get(int waitTimeSeconds) {

        ReceiveMessageRequest request = ReceiveMessageRequest
                .builder()
                .queueUrl(this.queueUrl)
                .waitTimeSeconds(waitTimeSeconds)
                .build();


        List<Message> messages = queue.receiveMessage(request).messages();
        List<String> messageValues = new ArrayList<>();
        // delete messages from the queue
        for (Message m : messages) {
            messageValues.add(m.body());
            DeleteMessageRequest deleteMessage = DeleteMessageRequest
                    .builder()
                    .queueUrl(this.queueUrl)
                    .receiptHandle(m.receiptHandle())
                    .build();
            queue.deleteMessage(deleteMessage);
        }

        return messageValues;
    }

    private String getMessageDeduplicationId(String message) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                (message + this.nonce).getBytes(StandardCharsets.UTF_8));
                return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        }
        //NOTE: just a backup...
        return String.valueOf(System.currentTimeMillis());
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
