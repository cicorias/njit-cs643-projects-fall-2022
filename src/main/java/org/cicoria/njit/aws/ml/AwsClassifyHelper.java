package org.cicoria.njit.aws.ml;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AwsClassifyHelper {

    private RekognitionClient rekognitionClient;
    public AwsClassifyHelper(){
        rekognitionClient = RekognitionClient.builder()
            .build();
    }

   public Map<String, Float> GetLabelsForS3Image(String bucket, String objectKey) {
       Map<String, Float> results = null;
       try {
           S3Object s3Object = S3Object.builder()
                   .bucket(bucket)
                   .name(objectKey)
                   .build();

           Image myImage = Image.builder()
                   .s3Object(s3Object)
                   .build();

           DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
                   .image(myImage)
                   .maxLabels(10)
                   .build();

           DetectLabelsResponse labelsResponse = rekognitionClient.detectLabels(detectLabelsRequest);
           List<Label> labels = labelsResponse.labels();
           results = new HashMap<>();
           for (Label label: labels) {
               results.put(label.name().toLowerCase(), label.confidence());
           }

       } catch (RekognitionException e) {
            throw e;
       }

        return results;
   }
}
