package ru.goodloot.tr.iosocket;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.Transport;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;

import javax.net.ssl.SSLContext;
import java.util.Map;

/**
 * IOSocketClient class
 *
 * Description ...
 *
 * @author Artur Mukhametianov
 * @created 19 нояб. 2014 г.
 */
public class IOSocketClient {

    public void start(String url) throws Exception {

        IO.Options options = new IO.Options();
//        options.query = "token=token";
//        options.path = "/streaming/3";
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
        options.sslContext = sslContext;
//        options.forceNew = true;

        final Socket socket = IO.socket(url, options);

        socket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                Transport transport = (Transport) args[0];
                transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {

                        Map<String, String> headers = (Map<String, String>) args[0];
                        headers.put("User-Agent", "Mozilla/5.0");
                    }
                });
            }
        });

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {

//                socket.emit("foo", "hi");
                System.out.println("EVENT_CONNECT " + args);
                // socket.disconnect();
            }

        });
        socket.connect();
    }

}
