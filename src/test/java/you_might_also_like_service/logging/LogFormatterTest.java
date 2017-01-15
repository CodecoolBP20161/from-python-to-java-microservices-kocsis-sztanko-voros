package you_might_also_like_service.logging;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LogFormatterTest {

    @Test
    public void getCustomizedFormatter() throws Exception {
        assertEquals(">>>>> ", LogFormatter.FORMAT.getCustomizedFormatter());
    }

}