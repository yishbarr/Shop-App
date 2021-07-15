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

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;

public class AddProductTask extends AsyncTask<Product, Void, Integer> {
    public final int DATABASE_FAILED = ServerResponses.DATABASE_FAILED.getResponse();
    private final WeakReference<TextView> notification;
    private final WeakReference<Resources> resources;

    public AddProductTask(TextView notification, Resources resources) {
        this.notification = new WeakReference<>(notification);
        this.resources = new WeakReference<>(resources);
    }

    @Override
    protected Integer doInBackground(Product... products) {
        int success = DATABASE_FAILED;
        try {
            //Server connection
            ServerConnection connection = new ServerConnection();
            ObjectOutputStream toServer = connection.getToServer();
            //Request type
            toServer.writeObject(ServerRequests.addProduct.toString());
            //Send all properties of the product.
            toServer.writeObject(products[0].name);
            toServer.writeObject(products[0].id);
            toServer.writeInt(products[0].quantity);
            toServer.writeInt(products[0].shelf);
            //Response number
            success = connection.getFromServer().readInt();
            //Close socket
            connection.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        String DATABASE_TAG = LogTags.DATABASE.toString();
        //Error or Success message for user and for developer.
        if (integer == ServerResponses.SUCCESS.getResponse()) {
            Log.d(DATABASE_TAG, "Added Product.");
            notification.get().setText(R.string.add_product_success);
            notification.get().setTextColor(resources.get().getColor(R.color.success));
        } else if (integer == ServerResponses.DATABASE_FAILED.getResponse()) {
            Log.w(DATABASE_TAG, "Adding Product failed.");
            notification.get().setText(R.string.add_product_failure);
            notification.get().setTextColor(resources.get().getColor(R.color.failure));
        } else if (integer == ServerResponses.ID_EXISTS.getResponse()) {
            Log.w(DATABASE_TAG, "ID already exists.");
            notification.get().setText(R.string.add_product_id_exists);
            notification.get().setTextColor(resources.get().getColor(R.color.failure));
        }
        notification.get().setVisibility(View.VISIBLE);
    }
}
