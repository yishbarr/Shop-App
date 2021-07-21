package com.example.shopmanager.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TableRow;

import com.example.shopmanager.connection.ServerConnection;
import com.example.shopmanager.constants.LogTags;
import com.example.shopmanager.constants.ServerRequests;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;

public class DeleteProductTask extends AsyncTask<String, Void, Void> {
    private final WeakReference<TableRow> safeRow;

    public DeleteProductTask(TableRow row) {
        super();
        safeRow = new WeakReference<>(row);
    }

    @Override
    protected Void doInBackground(String... strings) {
        //Server connection to delete from Firebase.
        try {
            ServerConnection connection = new ServerConnection();
            ObjectOutputStream toServer = connection.getToServer();
            //Request type
            toServer.writeObject(ServerRequests.deleteProduct.toString());
            toServer.writeObject(strings[0]);
            toServer.writeObject(strings[1]);
            connection.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        safeRow.get().removeAllViews();
        Log.d(LogTags.DATABASE.toString(), "Deleted product.");
    }
}
