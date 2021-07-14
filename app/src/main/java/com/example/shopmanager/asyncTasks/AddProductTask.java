package com.example.shopmanager.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import android.util.MutableBoolean;

import com.example.shopmanager.connection.ServerConnection;
import com.example.shopmanager.constants.ServerRequests;
import com.example.shopmanager.models.Product;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class AddProductTask extends AsyncTask<Product, Void, Boolean> {
    private final String DATABASETAG = "Database";
    private MutableBoolean success;

    public AddProductTask(MutableBoolean success) {
        this.success = success;
    }

    @Override
    protected Boolean doInBackground(Product... products) {
        boolean success = false;
        try {
            ServerConnection connection = new ServerConnection();
            ObjectOutputStream toServer = connection.getToServer();
            toServer.writeObject(ServerRequests.addProduct.toString());
            toServer.writeObject(products[0].name);
            toServer.writeObject(products[0].id);
            toServer.writeInt(products[0].quantity);
            toServer.writeInt(products[0].shelf);
            success = connection.getFromServer().readBoolean();
            connection.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean == true) {
            Log.d(DATABASETAG, "Added Product");
            success.value = true;
        } else {
            Log.d(DATABASETAG, "Added Product");
            success.value = false;
        }
    }

}
