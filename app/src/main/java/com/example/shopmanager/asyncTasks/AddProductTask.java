package com.example.shopmanager.asyncTasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.shopmanager.R;
import com.example.shopmanager.connection.ServerConnection;
import com.example.shopmanager.constants.LogTags;
import com.example.shopmanager.constants.ServerRequests;
import com.example.shopmanager.constants.TaskResponses;
import com.example.shopmanager.models.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AddProductTask extends AsyncTask<Product, Void, Integer> {
    private final int ID_EXISTS = TaskResponses.ID_EXISTS.ordinal();
    private final int SUCCESS = TaskResponses.SUCCESS.ordinal();
    private final WeakReference<Resources> safeResources;
    private final WeakReference<TextView> safeNotification;
    private final String uid;

    public AddProductTask(TextView notification, Resources resources, String uid) {
        super();
        safeNotification = new WeakReference<>(notification);
        safeResources = new WeakReference<>(resources);
        this.uid = uid;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Integer doInBackground(Product... products) {
        int success = TaskResponses.DATABASE_FAILED.ordinal();
        String id = products[0].id;

        JSONObject productObject = new JSONObject();
        try {
            //Server connection to check id
            ServerConnection connection = new ServerConnection();
            ObjectOutputStream toServer = connection.getToServer();
            toServer.writeObject(ServerRequests.getIds.toString());
            toServer.writeObject(uid);
            ArrayList<String> keys = (ArrayList<String>) connection.getFromServer().readObject();
            connection.getSocket().close();
            for (String key : keys) {
                if (key.contentEquals(id))
                    return ID_EXISTS;
            }
            //Create object to send to server
            productObject.put("name", products[0].name);
            productObject.put("id", id);
            productObject.put("quantity", products[0].quantity);
            productObject.put("shelf", products[0].shelf);
            //Server connection to add product
            connection = new ServerConnection();
            toServer = connection.getToServer();
            //Request type
            toServer.writeObject(ServerRequests.addProduct.toString());
            //Send the product object.
            toServer.writeObject(productObject.toString());
            //Send UID
            toServer.writeObject(uid);
            success = SUCCESS;
            connection.getSocket().close();
        } catch (IOException | JSONException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        String DATABASE_TAG = LogTags.DATABASE.toString();
        //Error or Success message for user and for developer.
        if (integer == ID_EXISTS) {
            Log.d(DATABASE_TAG, "ID exists.");
            safeNotification.get().setText(R.string.add_product_id_exists);
            safeNotification.get().setTextColor(safeResources.get().getColor(R.color.failure));
        } else if (integer == SUCCESS) {
            Log.d(DATABASE_TAG, "Added Product.");
            safeNotification.get().setText(R.string.add_product_success);
            safeNotification.get().setTextColor(safeResources.get().getColor(R.color.success));
        } else {
            Log.w(DATABASE_TAG, "Adding Product failed.");
            safeNotification.get().setText(R.string.add_product_failure);
            safeNotification.get().setTextColor(safeResources.get().getColor(R.color.failure));
        }
        safeNotification.get().setVisibility(View.VISIBLE);
    }
}
