package com.example.shopmanager.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection {
    private static final String ipAddress = "10.0.2.2";
    private static final int port = 3000;
    private final Socket socket;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;

    public ServerConnection() throws IOException {
        this.socket = new Socket(ipAddress, port);
        this.toServer = new ObjectOutputStream(socket.getOutputStream());
        this.fromServer = new ObjectInputStream(socket.getInputStream());
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getToServer() {
        return toServer;
    }

    public ObjectInputStream getFromServer() {
        return fromServer;
    }
}
