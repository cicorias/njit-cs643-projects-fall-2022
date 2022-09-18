package org.cicoria.njit.aws.storage;

import java.net.URL;
import java.util.Dictionary;
import java.util.stream.Stream;

public interface AwsS3Helper {
    Dictionary<String, URL> getFiles(URL bucket);
    Stream getFile(URL objectPath);
}
