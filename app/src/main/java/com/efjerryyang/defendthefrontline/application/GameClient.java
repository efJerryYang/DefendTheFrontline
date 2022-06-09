package com.efjerryyang.defendthefrontline.application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class GameClient {
    Socket socket = null;
    OutputStream outputStream;
    InputStream inputStream;

    public GameClient(String host, int port) {
        try {
            socket = new Socket(host, port);
//            socket.connect(new InetSocketAddress(),5000);
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRequest(String req) throws IOException {
        // send to server
        outputStream.write(req.getBytes());
    }

    public String receive() throws IOException {
        byte[] bytes = new byte[1024 * 4];
        int recv = 0;
        int bytesRead;
        String re = "";
        int i = 0;

        do {
            i++;
            bytesRead = inputStream.read(bytes);
            recv += bytesRead;
            re += new String(bytes, 0, bytesRead);

        } while (bytesRead >= 1024 * 4);
        System.out.println("File size sent from server: " + recv + "\tread frequency: " + i);
        return re;
    }

    public void close() throws IOException {
        socket.close();
    }

}
