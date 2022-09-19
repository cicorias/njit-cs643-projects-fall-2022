package org.cicoria.njit.aws.storage;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class AwsS3HelperImplTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void getFiles() throws MalformedURLException {
        AwsS3Helper helper = new AwsS3HelperImpl();
        var rv = helper.getFiles("njit-cs-643", "us-east-1");
        assertEquals(10, rv.size());

    }

    @org.junit.jupiter.api.Test
    void getFile() {
    }
}