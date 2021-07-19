package com.example.shopmanager.asyncTasks;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.shopmanager.connection.ServerConnection;
import com.example.shopmanager.constants.LogTags;
import com.example.shopmanager.constants.ServerRequests;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.Map;

public class GetProductsTask extends AsyncTask<String, Void, Map<String, Map<String, String>>> {
    private final WeakReference<Context> safeContext;
    private final WeakReference<TableLayout> safeTable;
    private final WeakReference<Resources> safeResources;

    public GetProductsTask(Context context, TableLayout table, Resources resources) {
        super();
        safeContext = new WeakReference<>(context);
        this.safeTable = new WeakReference<>(table);
        this.safeResources = new WeakReference<>(resources);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
            connection.getSocket().close();
            return products;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPostExecute(Map<String, Map<String, String>> products) {
        super.onPostExecute(products);
        String DATABASE_TAG = LogTags.DATABASE.toString();
        if (products != null) {
            //Add products to table
            //Turn dp into px
            int marginStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, safeResources.get().getDisplayMetrics());
            products.forEach((key, product) -> {
                TableRow row = new TableRow(safeContext.get());
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(layoutParams);
                TextView[] textArr = new TextView[4];
                product.forEach((pKey, prop) -> {
                    TextView text = new TextView(safeContext.get());
                    text.setText(prop);
                    text.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    switch (pKey) {
                        case "id":
                            textArr[0] = text;
                            break;
                        case "name":
                            textArr[1] = text;
                            break;
                        case "quantity":
                            textArr[2] = text;
                            break;
                        case "shelf":
                            textArr[3] = text;
                            break;
                    }
                });
                safeTable.get().addView(row);
                for (TextView text : textArr) {
                    row.addView(text);
                    ViewGroup.MarginLayoutParams textLayoutParams = (ViewGroup.MarginLayoutParams) text.getLayoutParams();
                    textLayoutParams.setMarginStart(marginStart);
                    text.setLayoutParams(textLayoutParams);
                }
                Button edit = new Button(safeContext.get());
                edit.setOnClickListener(view -> {

                });
                row.addView(edit);
                ViewGroup.MarginLayoutParams buttonLayoutParams = (ViewGroup.MarginLayoutParams) edit.getLayoutParams();
                buttonLayoutParams.setMarginStart(marginStart);
                edit.setLayoutParams(buttonLayoutParams);
                Button delete = new Button(safeContext.get());
                row.addView(delete);
                delete.setLayoutParams(buttonLayoutParams);
                delete.setOnClickListener(view -> {

                });
            });

        } else {
            Log.w(DATABASE_TAG, "Failed to get products.");
        }
    }
}
