package org.cicoria.njit.aws.messaging;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AwsMessageHelperTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
    @Test
    void sendOne() {
        var helper = new AwsMessageHelper("foobar0");
        helper.Send("hello world");
        helper.Delete();
    }


    @Test
    void sendTenMessages() {
        var helper = new AwsMessageHelper("foobar1");

        for (int i = 0; i < 10; i++){
            helper.Send("Message: " + i);
        }

        helper.Delete();
    }


    @Test
    void sendTenAndGetMessages() {
        var helper = new AwsMessageHelper("foobar2");

        for (int i = 0; i < 10; i++){
            helper.Send("Message: " + i);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<String> actual = new ArrayList<String>();
        for(int i = 0; i < 10; i++){
            var m = helper.Get();
            actual.add(m.get(0));
        }
        var result = helper.Get();

        assertEquals(10, actual.size(), "retrieve 10 messages");

        helper.Delete();

    }
}