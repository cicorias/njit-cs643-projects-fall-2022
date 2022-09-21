package org.cicoria.njit;

import org.cicoria.njit.aws.messaging.AwsMessageHelper;
import org.cicoria.njit.aws.ml.AwsClassifyHelper;
import org.cicoria.njit.aws.ml.AwsTextRecognizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Worker {
    private static Logger logger = LoggerFactory.getLogger(Worker.class);
    private String queueName;
    private String bucket;
    private String region;

    public Worker(String region, String bucket, String queueName) {
        this.region = region;
        this.bucket = bucket;
        this.queueName = queueName;
    }

    public void run() throws InterruptedException {
        logger.info("running worker with queue {}", this.queueName);
        AwsMessageHelper helper = new AwsMessageHelper(this.queueName);
        AwsTextRecognizer textRecognizer = new AwsTextRecognizer();
        // 1. At start listen or poll SQS

        int count = 0;
        String message = null;
        StringBuilder sb = new StringBuilder();
        while (null == message || !message.equalsIgnoreCase("-1")) {
            if (count % 100 == 0) {
                logger.info("long polling with count: {}", count);
            }

            var messageList = helper.Get(5);
            Thread.sleep(100); // give the cpu a rest.

            if (messageList != null) {
                logger.info("got message count: {}", messageList.size());

                for (var m : messageList) {
                    logger.info("got message: {}", m);
                    message = m;
                    if (!message.equalsIgnoreCase("-1")) {
                        sb.append(m + " : " + textRecognizer.GetTextForS3Image(bucket, m) +  System.lineSeparator());
                    }
                }
            }
        }

        logger.info("writing output");
        logger.info(sb.toString());
    }
}
