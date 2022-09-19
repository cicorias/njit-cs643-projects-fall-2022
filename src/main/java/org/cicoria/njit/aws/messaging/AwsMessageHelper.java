package org.cicoria.njit.aws.messaging;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.Message;

import java.util.ArrayList;
import java.util.List;

//https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/java/example_code/sqs/src/main/java/aws/example/sqs
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
        CreateQueueRequest create_request = new CreateQueueRequest(queueName)
                .addAttributesEntry("DelaySeconds", "0")
                .addAttributesEntry("MessageRetentionPeriod", "86400");

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
