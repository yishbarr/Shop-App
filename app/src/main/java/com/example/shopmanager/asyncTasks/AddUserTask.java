package com.example.shopmanager.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shopmanager.connection.ServerConnection;
import com.example.shopmanager.constants.ServerRequests;
import com.example.shopmanager.models.User;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class AddUserTask extends AsyncTask<User, Void, Void> {
    @Override
    protected Void doInBackground(User... users) {
        try {
            ServerConnection connection = new ServerConnection();
            ObjectOutputStream toServer = connection.getToServer();
            toServer.writeObject(ServerRequests.addAccount.toString());
            toServer.writeObject(users[0].email);
            toServer.writeObject(users[0].shop);
            toServer.writeObject(users[0].uid);
            connection.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        Log.d("Database", "Added User");
    }
}
