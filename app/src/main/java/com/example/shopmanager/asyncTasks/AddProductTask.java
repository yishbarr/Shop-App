package com.example.shopmanager.asyncTasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.shopmanager.R;
import com.example.shopmanager.connection.ServerConnection;
import com.example.shopmanager.constants.LogTags;
import com.example.shopmanager.constants.ServerRequests;
import com.example.shopmanager.constants.ServerResponses;
import com.example.shopmanager.models.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;

public class AddProductTask extends AsyncTask<Product, Void, Integer> {
    private static final int WAITING = 10;
    private final int DATABASE_FAILED = ServerResponses.DATABASE_FAILED.getResponse();
    private final Resources resources;
    private final WeakReference<TextView> safeNotification;
    private String uid;

    public AddProductTask(TextView notification, Resources resources, String uid) {
        super();
        safeNotification = new WeakReference<>(notification);
        this.uid = uid;
        WeakReference<Resources> safeResources = new WeakReference<>(resources);
        this.resources = safeResources.get();
    }

    @Override
    protected Integer doInBackground(Product... products) {
        int success = DATABASE_FAILED;
        JSONObject productObject = new JSONObject();
        try {
            productObject.put("name", products[0].name);
            productObject.put("id", products[0].id);
            productObject.put("quantity", products[0].quantity);
            productObject.put("shelf", products[0].shelf);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            //Server connection
            ServerConnection connection = new ServerConnection();
            ObjectOutputStream toServer = connection.getToServer();
            //Request type
            toServer.writeObject(ServerRequests.addProduct.toString());
            //Send the product object.
            toServer.writeObject(productObject.toString());
            //Send UID
            toServer.writeObject(uid);
            Object successObj = connection.getFromServer().readObject();
            success = (int) successObj;
            connection.getSocket().close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        String DATABASE_TAG = LogTags.DATABASE.toString();
        //Error or Success message for user and for developer.
        if (integer == ServerResponses.ID_EXISTS.getResponse()) {
            Log.d(DATABASE_TAG, "ID exists.");
            safeNotification.get().setText(R.string.add_product_id_exists);
            safeNotification.get().setTextColor(resources.getColor(R.color.failure));
        } else if (integer == ServerResponses.SUCCESS.getResponse()) {
            Log.d(DATABASE_TAG, "Added Product.");
            safeNotification.get().setText(R.string.add_product_success);
            safeNotification.get().setTextColor(resources.getColor(R.color.success));
        } else {
            Log.w(DATABASE_TAG, "Adding Product failed.");
            safeNotification.get().setText(R.string.add_product_failure);
            safeNotification.get().setTextColor(resources.getColor(R.color.failure));
        }
        safeNotification.get().setVisibility(View.VISIBLE);
    }
}
