package buildnlive.com.buildlive.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.activities.PurchaseOrder;
import buildnlive.com.buildlive.activities.PurchaseOrderListing;
import buildnlive.com.buildlive.adapters.ViewAttendanceAdapter;
import buildnlive.com.buildlive.adapters.ViewPurchaseOrderAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Order;
import buildnlive.com.buildlive.elements.Worker;
import buildnlive.com.buildlive.utils.Config;
import io.realm.RealmResults;

public class ViewPurchaseOrderFragment extends Fragment {
    private RecyclerView list;
    private List<Order> orders;
    private ProgressBar progress;
    private ViewPurchaseOrderAdapter adapter;
    private TextView hider;
    private boolean LOADING;
    private static App app;

    public static ViewPurchaseOrderFragment newInstance(App a) {
        app = a;
        return new ViewPurchaseOrderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_purchase_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = view.findViewById(R.id.list);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
        orders = new ArrayList<>();
        adapter = new ViewPurchaseOrderAdapter(getContext(), orders, new ViewPurchaseOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Order item, int pos, View view) {
                Intent intent = new Intent(getContext(), PurchaseOrderListing.class);
                intent.putExtra("id", item.getOrderId());
                startActivity(intent);
            }
        });
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        requestPurchaseOrder();
    }

    private void requestPurchaseOrder() {
        String url = Config.REQ_PURCHASE_ORDER;
        url = url.replace("[0]", App.userId).replace("[1]", App.projectId);
        app.sendNetworkRequest(url, 0, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);}

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.log("Res:" + response);
                orders.clear();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Order order = new Order().parseFromJSON(object);
                        orders.add(order);
                    }
                } catch (JSONException e) {

                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}