package com.airhacks.loadr;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class DeployTest {

    @Test
    public void extractApplicationName() {
        String expected = "duke";
        String actual = Deploy.extractApplicationName(" ./test-deployment/" + expected + ".war");
        assertThat(actual, is(expected));
    }

    @Test
    public void extractApplicationNameWithoutEnding() {
        String expected = "duke";
        String actual = Deploy.extractApplicationName(expected);
        assertThat(actual, is(expected));
    }

}
