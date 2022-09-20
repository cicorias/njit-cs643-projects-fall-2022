package org.cicoria.njit.aws.ml;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AwsClassifyHelperTest {

    @Test
    void getLabelsForS3Image() {
        var helper = new AwsClassifyHelper();

        String bucket = "test-bucket-njit";
        String key = "1.jpg";
        // String expected = "Car";
        int expectedValueMin = 90;
        var rv = helper.GetLabelsForS3Image(bucket, key);

        var actual = rv.get("car");
        assertTrue(actual > expectedValueMin);

    }
}