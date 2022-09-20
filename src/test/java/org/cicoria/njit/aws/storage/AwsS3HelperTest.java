package org.cicoria.njit.aws.storage;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

class AwsS3HelperTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void getFiles() throws MalformedURLException {
        AwsS3Helper helper = new AwsS3Helper();
        var rv = helper.getFiles("njit-cs-643", "us-east-1");
        assertEquals(10, rv.size());

    }

    @org.junit.jupiter.api.Test
    void getFile() {
        AwsS3Helper helper = new AwsS3Helper();

        var rv = helper.getFile("njit-cs-643", "1.jpg");
    }
}