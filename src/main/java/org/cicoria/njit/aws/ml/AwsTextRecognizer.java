package org.cicoria.njit.aws.ml;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.util.List;

public class AwsTextRecognizer {

    private RekognitionClient rekognitionClient;
    public AwsTextRecognizer(){
        rekognitionClient = RekognitionClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }

    public String GetTextForS3Image(String bucket, String objectKey) {

        S3Object s3Object = S3Object.builder()
                .bucket(bucket)
                .name(objectKey)
                .build();

        Image myImage = Image.builder()
                .s3Object(s3Object)
                .build();

        DetectTextRequest request = DetectTextRequest
                .builder()
                .image(myImage)
                .build();

        DetectTextResponse response = rekognitionClient.detectText(request);
        List<TextDetection> textCollection = response.textDetections();

        StringBuilder sb = new StringBuilder();
        String separator = "";
        for(TextDetection s: textCollection){
            if (s.type() == TextTypes.LINE) {
                sb.append(separator).append(s.detectedText());
                separator = " ";
            }
        }

        return sb.toString();
    }
}
