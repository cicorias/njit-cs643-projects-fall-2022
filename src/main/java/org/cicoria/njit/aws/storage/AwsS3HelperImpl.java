package org.cicoria.njit.aws.storage;

import java.net.URL;
import java.util.Dictionary;
import java.util.List;
import java.util.stream.Stream;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

//https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/java/example_code/s3/src/main/java/aws/example/s3/ListObjects.java
public class AwsS3HelperImpl implements AwsS3Helper {
    @Override
    public Dictionary<String, URL> getFiles(URL bucket) {
        System.out.format("Objects in S3 bucket %s:\n", bucket);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();
        ListObjectsV2Result result = s3.listObjectsV2(bucket.toString());
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        for (S3ObjectSummary os : objects) {
            System.out.println("* " + os.getKey());
        }
        return null;
    }

    @Override
    public Stream getFile(URL objectPath) {
        return null;
    }
}
