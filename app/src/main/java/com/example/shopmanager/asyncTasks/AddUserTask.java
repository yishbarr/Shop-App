package com.example.shopmanager.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shopmanager.connection.ServerConnection;
import com.example.shopmanager.constants.LogTags;
import com.example.shopmanager.constants.ServerRequests;
import com.example.shopmanager.models.User;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class AddUserTask extends AsyncTask<User, Void, Void> {
    public AddUserTask() {
        super();
    }

    @Override
    protected Void doInBackground(User... users) {
        try {
            //Server connection
            ServerConnection connection = new ServerConnection();
            ObjectOutputStream toServer = connection.getToServer();
            //Request type
            toServer.writeObject(ServerRequests.addAccount.toString());
            //Send user properties
            toServer.writeObject(users[0].email);
            toServer.writeObject(users[0].shop);
            toServer.writeObject(users[0].uid);
            //Close socket
            connection.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        Log.d(LogTags.DATABASE.toString(), "Added User");
    }
}
