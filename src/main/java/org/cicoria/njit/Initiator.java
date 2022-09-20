package org.cicoria.njit;

import org.cicoria.njit.aws.messaging.AwsMessageHelper;
import org.cicoria.njit.aws.ml.AwsClassifyHelper;
import org.cicoria.njit.aws.storage.AwsS3Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Initiator {
    private static Logger logger = LoggerFactory.getLogger(Initiator.class);

    public void Run(String bucket, String region, String label){
        // System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");
        AwsS3Helper s3Helper = new AwsS3Helper();
        //get list of each file

        var allFiles = s3Helper.getFiles(bucket, region);
        //for each determine if car > 90%

        AwsClassifyHelper classifyHelper = new AwsClassifyHelper();
        AwsMessageHelper messageHelper = new AwsMessageHelper("sharedimages");
        for(var key : allFiles.keySet()){
            String b2 = allFiles.get(key);
            logger.info("examining: {} -- {}", key, b2);
            var classifyResult = classifyHelper.GetLabelsForS3Image(b2, key);
            for(var ckey : classifyResult.keySet()){
                var cvalue = classifyResult.get(ckey);
                logger.info("result: {} -- {}", ckey, cvalue);

                if (ckey.equalsIgnoreCase(label)) {
                    logger.info("we have a car match: {} -- {}", key, cvalue);
                    if (cvalue > 90) {
                        logger.info("sending message: {} -- {}", key, b2);
                        messageHelper.Send(key);
                    }
                }

                // if (ckey.toLowerCase().equals(label.toLowerCase()) && cvalue > 90.0) {
                //     messageHelper.Send(key);
                //     logger.warn("sent: {} -- {}", key, b2);
                // }
            }

        }

        messageHelper.Send("-1");
    }
}
