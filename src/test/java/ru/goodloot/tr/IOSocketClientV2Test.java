package ru.goodloot.tr;

import org.junit.Test;
import ru.goodloot.tr.iosocket.IOSocketClientV2;

/**
 * IOSocketClientV2Test class
 *
 * Description ...
 *
 * @author Artur Mukhametianov
 * @created 20 нояб. 2014 г.
 */
public class IOSocketClientV2Test {

    @Test
    public void test() throws Exception {

        IOSocketClientV2 client = new IOSocketClientV2();
        client.start("https://anxpro.com/streaming/3");
        System.in.read();
    }
}
