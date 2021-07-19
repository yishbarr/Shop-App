package com.example.shopmanager.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection {
    private final String IP_ADDRESS = "10.0.2.2";
    private final int PORT = 8080;
    private Socket socket;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;

    public ServerConnection() throws IOException {
        this.socket = new Socket(IP_ADDRESS, PORT);
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
