package org.cicoria.njit.aws.messaging;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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
        var helper = new AwsMessageHelper("foobar0" + System.currentTimeMillis());
        helper.Send("hello world");
        helper.Delete();
    }


    @Test
    void sendTenMessages() {
        var helper = new AwsMessageHelper("foobar1" + System.currentTimeMillis());

        for (int i = 0; i < 10; i++){
            helper.Send("Message: " + i);
        }
        helper.Delete();
    }


    @Test
    void sendTenAndGetMessages() {
        var helper = new AwsMessageHelper("foobar2" + System.currentTimeMillis());
        ArrayList<String> expected = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            helper.Send("Message: " + i);
            expected.add("Message: " + i);
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

        assertEquals(10, actual.size(), "retrieve 10 messages");
        assertArrayEquals(expected.toArray(), actual.toArray());
        helper.Delete();

    }


    @Test
    void sendTenAndWithCloserThenGetMessages() {
        var helper = new AwsMessageHelper("foobar2" + System.currentTimeMillis());
        ArrayList<String> expected = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            helper.Send("Message: " + i);
            expected.add("Message: " + i);
        }

        expected.add("-1");
        helper.Send("-1");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        List<String> actual = new ArrayList<String>();
        do {
            var m = helper.Get();
            actual.add(m.get(0));
            if (m.get(0).equals("-1")){
                break;
            }
        } while (true);

       

        assertEquals(11, actual.size(), "retrieve 11 messages");
        assertArrayEquals(expected.toArray(), actual.toArray());
        helper.Delete();

    }
}