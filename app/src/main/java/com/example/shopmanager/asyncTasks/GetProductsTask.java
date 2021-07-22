package com.example.shopmanager.asyncTasks;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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

import com.example.shopmanager.R;
import com.example.shopmanager.activities.EditProduct;
import com.example.shopmanager.connection.ServerConnection;
import com.example.shopmanager.constants.LogTags;
import com.example.shopmanager.constants.ServerRequests;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class GetProductsTask extends AsyncTask<Void, Void, Map<String, Map<String, String>>> {
    private final WeakReference<Context> safeContext;
    private final WeakReference<TableLayout> safeTable;
    private final WeakReference<Resources> safeResources;
    private final WeakReference<List<String>> safeUsedIds;
    private final String uid;

    public GetProductsTask(Context context, TableLayout table, Resources resources, String uid, List<String> usedIds) {
        super();
        safeContext = new WeakReference<>(context);
        this.safeTable = new WeakReference<>(table);
        this.safeResources = new WeakReference<>(resources);
        this.safeUsedIds = new WeakReference<>(usedIds);
        this.uid = uid;
    }

    @Override
    protected Map<String, Map<String, String>> doInBackground(Void... voids) {
        Map<String, Map<String, String>> products;
        try {
            //Server connection
            ServerConnection connection = new ServerConnection();
            ObjectOutputStream toServer = connection.getToServer();
            //Request type
            toServer.writeObject(ServerRequests.getProducts.toString());
            //UID of user.
            toServer.writeObject(uid);
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
            int marginStart = dpToPx(20);
            //Set up each row with details and action buttons.
            products.forEach((key, product) -> {
                TableRow row = new TableRow(safeContext.get());
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(layoutParams);
                TextView[] textArr = new TextView[4];
                Intent intent = new Intent(safeContext.get(), EditProduct.class);
                product.forEach((pKey, prop) -> {
                    TextView text = new TextView(safeContext.get());
                    text.setText(prop);
                    text.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    switch (pKey) {
                        case "id":
                            textArr[0] = text;
                            intent.putExtra("id", prop);
                            safeUsedIds.get().add(prop);
                            break;
                        case "name":
                            textArr[1] = text;
                            intent.putExtra("name", prop);
                            break;
                        case "quantity":
                            textArr[2] = text;
                            intent.putExtra("quantity", prop);
                            break;
                        case "shelf":
                            textArr[3] = text;
                            intent.putExtra("shelf", prop);
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
                int textSize = dpToPx(5);
                //Edit Button
                Button edit = new Button(safeContext.get());
                edit.setOnClickListener(view -> safeContext.get().startActivity(intent));
                edit.setBackgroundColor(safeResources.get().getColor(R.color.submit));
                edit.setText(R.string.edit_product_button);
                edit.setBackgroundTintList(ColorStateList.valueOf(safeResources.get().getColor(R.color.submit)));
                row.addView(edit);
                ViewGroup.MarginLayoutParams buttonLayoutParams = (ViewGroup.MarginLayoutParams) edit.getLayoutParams();
                buttonLayoutParams.setMarginStart(marginStart);
                buttonLayoutParams.bottomMargin = 2;
                edit.setLayoutParams(buttonLayoutParams);
                edit.setTextSize(textSize);
                //Delete Button
                Button delete = new Button(safeContext.get());
                row.addView(delete);
                delete.setLayoutParams(buttonLayoutParams);
                delete.setOnClickListener(view -> new DeleteProductTask(row).execute(textArr[0].getText().toString(), uid));
                delete.setBackgroundColor(safeResources.get().getColor(R.color.action));
                delete.setText(R.string.delete_product_button);
                delete.setTextSize(textSize);
            });

        } else {
            Log.w(DATABASE_TAG, "Failed to get products.");
        }
    }

    //Turn dp into px.
    private int dpToPx(int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, safeResources.get().getDisplayMetrics());
    }
}
