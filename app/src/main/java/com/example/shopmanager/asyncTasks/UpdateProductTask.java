package com.example.shopmanager.asyncTasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.shopmanager.R;
import com.example.shopmanager.connection.ServerConnection;
import com.example.shopmanager.constants.ServerRequests;
import com.example.shopmanager.constants.TaskResponses;
import com.example.shopmanager.models.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;

public class UpdateProductTask extends AsyncTask<Product, Void, Integer> {
    private final int SUCCESS = TaskResponses.SUCCESS.ordinal();
    private final String uid;
    private final WeakReference<TextView> safeNotification;
    private final WeakReference<Resources> safeResources;

    public UpdateProductTask(String uid, TextView notification, Resources resources) {
        super();
        this.uid = uid;
        this.safeNotification = new WeakReference<>(notification);
        this.safeResources = new WeakReference<>(resources);
    }

    @Override
    protected Integer doInBackground(Product... products) {
        int success = TaskResponses.DATABASE_FAILED.ordinal();
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
            toServer.writeObject(ServerRequests.addProduct.toString());
            toServer.writeObject(productObject.toString());
            toServer.writeObject(uid);
            success = SUCCESS;
            connection.getSocket().close();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        safeNotification.get().setVisibility(View.VISIBLE);
        if (integer == SUCCESS) {
            safeNotification.get().setText(R.string.update_product_success);
            safeNotification.get().setTextColor(safeResources.get().getColor(R.color.success));
        } else {
            safeNotification.get().setText(R.string.update_product_failure);
            safeNotification.get().setTextColor(safeResources.get().getColor(R.color.failure));
        }
    }
}
