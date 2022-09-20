package org.cicoria.njit.aws.storage;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

//https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/java/example_code/s3/src/main/java/aws/example/s3/ListObjects.java
//https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code
public class AwsS3HelperImpl implements AwsS3Helper {
    @Override
    public Map<String, String> getFiles(String bucket, String region) {
        Map<String, String> rv = new HashMap<String, String>();
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
        ListObjectsV2Result result = s3.listObjectsV2(bucket);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        for (S3ObjectSummary os : objects) {
            rv.put(os.getKey(), bucket);
        }
        return rv;
    }

    @Override
    public Stream getFile(URL objectPath) {
        return null;
    }
}
