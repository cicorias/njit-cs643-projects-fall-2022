package org.cicoria.njit.aws.messaging;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code
public class AwsMessageHelper {
    private String queueName;
    private SqsClient queue;

    private String queueUrl;
    public AwsMessageHelper(String queueName){
        this.queueName = queueName + ".fifo";
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
        attributes.put(QueueAttributeName.CONTENT_BASED_DEDUPLICATION, "true");
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
    }
    public void Send(String message) {
        SendMessageRequest msgRequest = SendMessageRequest
                .builder()
                .messageBody(message)
                .queueUrl(this.queueUrl)
                .messageGroupId("group1")
                .build();

        queue.sendMessage(msgRequest);
    }

    public List<String> Get() {
        ReceiveMessageRequest request = ReceiveMessageRequest
                .builder()
                .queueUrl(this.queueUrl)
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
}
