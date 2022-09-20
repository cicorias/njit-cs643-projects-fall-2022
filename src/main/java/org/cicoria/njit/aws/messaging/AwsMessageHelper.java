package org.cicoria.njit.aws.messaging;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.*;

import java.util.ArrayList;
import java.util.List;

//https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/java/example_code/sqs/src/main/java/aws/example/sqs
//https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code
public class AwsMessageHelper {
    private String queueName;
    private AmazonSQS queue;

    private String queueUrl;
    private boolean ready = false;
    public AwsMessageHelper(String queueName){
        this.queueName = queueName;
        create(queueName);
    }

    private void create(String queueName) {
        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

        // Creating a Queue
        CreateQueueRequest create_request = new CreateQueueRequest(queueName + ".fifo")
//                .addAttributesEntry("DelaySeconds", "0")
                .addAttributesEntry(QueueAttributeName.FifoQueue.name(), "true")
                .addAttributesEntry(QueueAttributeName.MessageRetentionPeriod.name(), "86400");

        try {
            CreateQueueResult result = sqs.createQueue(create_request);
        } catch (AmazonSQSException e) {
            if (!e.getErrorCode().equals("QueueAlreadyExists")) {
                throw e;
            }
        }

        this.ready = true;
        this.queue = sqs;
        this.queueUrl = queue.getQueueUrl(this.queueName).getQueueUrl();
    }

    public void Delete() {
        queue.deleteQueue(this.queueUrl);
    }
    public void Send(String message) {
        SendMessageRequest msgRequest = new SendMessageRequest();
        msgRequest.setMessageBody(message);
        msgRequest.setMessageGroupId("group1");
        queue.sendMessage(queueUrl, message);
    }

    public List<String> Get() {
        List<Message> messages = queue.receiveMessage(queueUrl).getMessages();
        List<String> messageValues = new ArrayList<>();
        // delete messages from the queue
        for (Message m : messages) {
            messageValues.add(m.getBody());
            queue.deleteMessage(queueUrl, m.getReceiptHandle());
        }

        return messageValues;
    }
}
