package com.example.shopmanager.asyncTasks;

import android.os.AsyncTask;

import com.example.shopmanager.connection.ServerConnection;
import com.example.shopmanager.constants.ServerRequests;
import com.example.shopmanager.models.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class UpdateProductTask extends AsyncTask<Product, Void, Integer> {
    private final String uid;

    public UpdateProductTask(String uid) {
        super();
        this.uid = uid;
    }

    @Override
    protected Integer doInBackground(Product... products) {
        //Build JSON
        JSONObject productObject = new JSONObject();
        try {
            productObject.put("id", products[0].id);
            productObject.put("name", products[0].name);
            productObject.put("quantity", products[0].quantity);
            productObject.put("shelf", products[0].shelf);

            //Server connection to send JSON and UID.
            ServerConnection connection = new ServerConnection();
            ObjectOutputStream toServer = connection.getToServer();
            //Request type
            toServer.writeObject(ServerRequests.updateProduct.toString());
            toServer.writeObject(productObject.toString());
            toServer.writeObject(uid);
            connection.getSocket().close();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }
}
