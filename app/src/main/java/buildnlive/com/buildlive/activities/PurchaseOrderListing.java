package buildnlive.com.buildlive.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.BuildConfig;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.ActivityImagesAdapter;
import buildnlive.com.buildlive.adapters.DailyWorkActivityAdapter;
import buildnlive.com.buildlive.adapters.PurchaseOrderListingAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Activity;
import buildnlive.com.buildlive.elements.Order;
import buildnlive.com.buildlive.elements.OrderItem;
import buildnlive.com.buildlive.elements.Packet;
import buildnlive.com.buildlive.utils.AdvancedRecyclerView;
import buildnlive.com.buildlive.utils.Config;

public class PurchaseOrderListing extends AppCompatActivity {
    private App app;
    private String id;
    private RecyclerView list;
    private PurchaseOrderListingAdapter adapter;
    private List<OrderItem> itemList;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order_listing);
        list = findViewById(R.id.items);
        itemList = new ArrayList<>();
        submit = findViewById(R.id.submit);
        adapter = new PurchaseOrderListingAdapter(getApplicationContext(), itemList, new PurchaseOrderListingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {

            }
        });
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list.setAdapter(adapter);
        app = (App) getApplication();
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        fetchOrders();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pushOrders();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void pushOrders() throws JSONException{
        String url = Config.REQ_PURCHASE_ORDER_UPDATE;
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", App.userId);
        JSONArray array = new JSONArray();
        for(int i=0; i<itemList.size(); i++){
            if(itemList.get(i).isIncluded()) {
                JSONObject obj = new JSONObject();
                obj.put("qty_received", itemList.get(i).getQuantity());
                obj.put("comments", "Done");
                obj.put("purchase_order_list_id", itemList.get(i).getOrderId());
                array.put(obj);
            }
        }
        params.put("purchase_order_list", array.toString());
        app.sendNetworkRequest(url, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
            }
        });
    }

    private void fetchOrders() {
        String url = Config.REQ_PURCHASE_ORDER_LISTING;
        url = url.replace("[0]", App.userId).replace("[1]", id);
        app.sendNetworkRequest(url, 0, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                try {
                   itemList.clear();
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        itemList.add(new OrderItem().parseFromJSON(array.getJSONObject(i)));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {

                }
            }
        });
    }
}