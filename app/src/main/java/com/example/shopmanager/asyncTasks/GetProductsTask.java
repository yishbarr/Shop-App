package com.example.shopmanager.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shopmanager.connection.ServerConnection;
import com.example.shopmanager.constants.LogTags;
import com.example.shopmanager.constants.ServerRequests;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class GetProductsTask extends AsyncTask<String, Void, Map<String, Map<String, String>>> {
    private int response;

    @Override
    protected Map<String, Map<String, String>> doInBackground(String... strings) {
        Map<String, Map<String, String>> products;
        try {
            //Server connection
            ServerConnection connection = new ServerConnection();
            ObjectOutputStream toServer = connection.getToServer();
            //Request type
            toServer.writeObject(ServerRequests.getProducts.toString());
            //UID of user.
            toServer.writeObject(strings[0]);
            ObjectInputStream fromServer = connection.getFromServer();
            //Map of products
            products = (Map<String, Map<String, String>>) fromServer.readObject();
            //Response number
            response = fromServer.readInt();
            connection.getSocket().close();
            return products;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Map<String, Map<String, String>> products) {
        super.onPostExecute(products);
        String DATABASE_TAG = LogTags.DATABASE.toString();
        if (products != null) {
            //Add products to table

        } else {
            Log.w(DATABASE_TAG, "Failed to get products.");
        }
    }
}
