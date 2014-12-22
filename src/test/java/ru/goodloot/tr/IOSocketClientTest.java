package ru.goodloot.tr;

import org.junit.Test;
import ru.goodloot.tr.iosocket.IOSocketClient;

/**
 * IOSocketClientTest class
 *
 * Description ...
 *
 * @author Artur Mukhametianov
 * @created 19 нояб. 2014 г.
 */
public class IOSocketClientTest {

    @Test
    public void test() throws Exception {

//        Logger logger = Logger.getLogger("foo");

        IOSocketClient client = new IOSocketClient();
        client.start("https://anxpro.com/streaming/3/1");
        System.out.println("hello");

        System.in.read();
//        Thread.sleep(1000 * 20);
    }
}
