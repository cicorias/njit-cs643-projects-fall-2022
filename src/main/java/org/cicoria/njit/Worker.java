package org.cicoria.njit;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.cicoria.njit.aws.messaging.AwsMessageHelper;
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
        logger.debug(sb.toString());

        writeOutput(sb);

        logger.info("worker done...");


    }

    void writeOutput(StringBuilder sb) {
        String pattern = "yyyy-MM-dd-HH-mm-ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        String fileName = "output-" + date + ".txt";

        try (OutputStream os = new FileOutputStream(fileName)) {
            logger.info("output written to file {}", fileName);
            os.write(sb.toString().getBytes(), 0, sb.toString().length());
            os.write(System.lineSeparator().getBytes(), 0, System.lineSeparator().length());
            os.flush();

        } catch (Exception e) {
            logger.error("error writing output", e);
        }

    }
}
