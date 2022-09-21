package org.cicoria.njit.aws.ml;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AwsTextRecognizerTest {

    @Test
    void getTextForS3Image() {
        var helper = new AwsTextRecognizer();

        var bucket = "test-bucket-njit";
        var key = "4.jpg";

        var expected = "yhi9 otz";
        var actual = helper.GetTextForS3Image(bucket, key);

        assertTrue(actual.toLowerCase().contains(expected.toLowerCase()));
//        assertEquals(expected, actual);
    }

    @Test
    void getTextForS3Image3() {
        var helper = new AwsTextRecognizer();

        var bucket = "test-bucket-njit";
        var key = "3.jpg";

        var expected = "ROSE graby пусдо HDD - 45 P P 11:50 85% NE COMPETELING PARKING";
        var actual = helper.GetTextForS3Image(bucket, key);

        assertTrue(actual.toLowerCase().contains(expected.toLowerCase()));
//        assertEquals(expected, actual);
    }

    @Test
    void getTextForS3Image7() {
        var helper = new AwsTextRecognizer();

        var bucket = "test-bucket-njit";
        var key = "7.jpg";

        var expected = "LP 610 LB";
        var actual = helper.GetTextForS3Image(bucket, key);

        assertTrue(actual.toLowerCase().contains(expected.toLowerCase()));
//        assertEquals(expected, actual);
    }
}