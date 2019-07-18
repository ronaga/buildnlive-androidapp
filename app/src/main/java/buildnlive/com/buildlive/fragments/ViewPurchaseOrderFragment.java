package buildnlive.com.buildlive.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import buildnlive.com.buildlive.activities.PurchaseOrderListing;
import buildnlive.com.buildlive.adapters.ViewPurchaseOrderAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Order;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class ViewPurchaseOrderFragment extends Fragment {
    private RecyclerView list;
    private List<Order> orders;
    private ProgressBar progress;
    private ViewPurchaseOrderAdapter adapter;
    private TextView hider,no_content;
    private boolean LOADING;
    private static App app;
    private Context context;
    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.appCompatActivity = (AppCompatActivity) activity;
    }

    public static ViewPurchaseOrderFragment newInstance(App a) {
        app = a;
        return new ViewPurchaseOrderFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        requestPurchaseOrder();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_purchase_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        utilityofActivity=new UtilityofActivity(appCompatActivity);

        list = view.findViewById(R.id.list);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
        no_content = view.findViewById(R.id.no_content);

        orders = new ArrayList<>();
        adapter = new ViewPurchaseOrderAdapter(getContext(), orders, new ViewPurchaseOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Order item, int pos, View view) {
                Intent intent = new Intent(getContext(), PurchaseOrderListing.class);
                intent.putExtra("id", item.getOrderId());
                intent.putExtra("type_id", item.getType_id());
                startActivity(intent);
            }
        });
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void requestPurchaseOrder() {
        String url = Config.REQ_PURCHASE_ORDER;
        url = url.replace("[0]", App.userId).replace("[1]", App.projectId);
        app.sendNetworkRequest(url, 0, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
                console.log("Res:" + response);
                orders.clear();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Order order = new Order().parseFromJSON(object);
                        orders.add(order);
                    }
                    if (orders.isEmpty()) {
                        no_content.setVisibility(View.VISIBLE);
                    } else no_content.setVisibility(View.GONE);
                } catch (JSONException e) {

                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}