package org.cicoria.njit.aws.storage;

import java.net.URL;
import java.util.Map;
import java.util.stream.Stream;

public interface AwsS3Helper {
    Map<String, String> getFiles(String bucket, String region);
    Stream getFile(URL objectPath);
}
