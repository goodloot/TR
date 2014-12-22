package ru.goodloot.tr.iosocket;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;

/**
 * IOSocketClientV2 class
 *
 * Description ...
 *
 * @author Artur Mukhametianov
 * @created 20 нояб. 2014 г.
 */
public class IOSocketClientV2 {

    public void start(String url) throws Exception {

        SocketIO socket = new SocketIO(url);
        socket.addHeader("User-Agent", "Mozilla/5.0");
//        socket.addHeader("resource", "streaming/3");
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
        SocketIO.setDefaultSSLSocketFactory(sslContext);

        socket.connect(new IOCallback() {

            @Override
            public void onMessage(JSONObject json, IOAcknowledge ack) {

                try {
                    System.out.println("Server said:" + json.toString(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(String data, IOAcknowledge ack) {

                System.out.println("Server said: " + data);
            }

            @Override
            public void onError(SocketIOException socketIOException) {

                System.out.println("an Error occured");
                socketIOException.printStackTrace();
            }

            @Override
            public void onDisconnect() {

                System.out.println("Connection terminated.");
            }

            @Override
            public void onConnect() {

                System.out.println("Connection established");
            }

            @Override
            public void on(String event, IOAcknowledge ack, Object... args) {

                System.out.println("Server triggered event '" + event + "'");
            }
        });

        // This line is cached until the connection is establisched.
//        socket.send("Hello Server!");
    }
}
