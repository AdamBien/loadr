package com.airhacks.loadr;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class HookerIT {

    Hooker cut;

    @Before
    public void initialize() {
        this.cut = new Hooker("http://airhacks.com");
    }

    @Test
    public void perform() {
        String result = this.cut.invoke();
        assertThat(result, not(is("-")));
        System.out.println("result = " + result);
    }

}
