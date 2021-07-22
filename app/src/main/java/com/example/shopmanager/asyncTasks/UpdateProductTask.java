package com.example.shopmanager.asyncTasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.shopmanager.R;
import com.example.shopmanager.connection.ServerConnection;
import com.example.shopmanager.constants.ServerRequests;
import com.example.shopmanager.models.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;

public class UpdateProductTask extends AsyncTask<Product, Void, Integer> {
    private final String uid;
    private WeakReference<TextView> safeNotification;
    private WeakReference<Resources> safeResources;

    public UpdateProductTask(String uid, TextView notification, Resources resources) {
        super();
        this.uid = uid;
        this.safeNotification = new WeakReference<>(notification);
        this.safeResources = new WeakReference<>(resources);
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
        safeNotification.get().setText(R.string.update_product_success);
        safeNotification.get().setTextColor(safeResources.get().getColor(R.color.success));
        safeNotification.get().setVisibility(View.VISIBLE);
    }
}
