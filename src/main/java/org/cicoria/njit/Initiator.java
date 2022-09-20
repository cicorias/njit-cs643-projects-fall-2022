package org.cicoria.njit;

import org.cicoria.njit.aws.messaging.AwsMessageHelper;
import org.cicoria.njit.aws.ml.AwsClassifyHelper;
import org.cicoria.njit.aws.storage.AwsS3Helper;
import software.amazon.awssdk.regions.servicemetadata.M2ServiceMetadata;

public class Initiator {
    public void Run(String bucket, String region, String label){
        AwsS3Helper s3Helper = new AwsS3Helper();
        //get list of each file

        var allFiles = s3Helper.getFiles(bucket, region);
        //for each determine if car > 90%

        AwsClassifyHelper classifyHelper = new AwsClassifyHelper();
        AwsMessageHelper messageHelper = new AwsMessageHelper("sharedimages");
        for(var key : allFiles.keySet()){
            String b2 = allFiles.get(key);

            var classifyResult = classifyHelper.GetLabelsForS3Image(b2, key);
            for(var ckey : classifyResult.keySet()){
                if (ckey.toLowerCase() == label.toLowerCase() && classifyResult.get(ckey) > 90.0) {
                    messageHelper.Send(ckey);
                }
            }

        }

        messageHelper.Send("-1");
    }
}
