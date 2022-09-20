package org.cicoria.njit.aws.storage;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


//https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/java/example_code/s3/src/main/java/aws/example/s3/ListObjects.java
//https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code
public class AwsS3Helper {

    public Map<String, String> getFiles(String bucket, String region) {
        Map<String, String> rv = new HashMap<String, String>();

        S3Client s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        ListObjectsRequest listObjects = ListObjectsRequest
                .builder()
                .bucket(bucket)
                .build();

        ListObjectsResponse result = s3.listObjects(listObjects);
        List<S3Object> objects = result.contents();
        for (S3Object os : objects) {
            rv.put(os.key(), bucket);
        }
        return rv;
    }
}
