package buildnlive.com.buildlive.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.AddItemAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Item;
import buildnlive.com.buildlive.utils.Config;

public class AddItemFragment extends Fragment implements Interfaces.SyncListener {
    private static List<Item> itemsList;
    private RecyclerView items;
    private Button next, submit;
    private ProgressBar progress;
    private TextView hider, checkout_text;
    private boolean LOADING;
    private ImageButton close;
    private AddItemAdapter adapter;

    public AddItemAdapter.OnItemSelectedListener listener = new AddItemAdapter.OnItemSelectedListener() {
        @Override
        public void onItemCheck(boolean checked) {
            if (checked) {
                next.setVisibility(View.VISIBLE);
            } else {
                next.setVisibility(View.GONE);
            }
        }

        @Override
        public void onItemInteract(int pos, int flag) {

        }
    };

    public static AddItemFragment newInstance(List<Item> u) {
        itemsList = u;
        return new AddItemFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        items = view.findViewById(R.id.items);
        submit = view.findViewById(R.id.submit);
        next = view.findViewById(R.id.next);
        close = view.findViewById(R.id.close_checkout);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItemAdapter.ViewHolder.CHECKOUT = false;
                checkout_text.setVisibility(View.GONE);
                close.setVisibility(View.GONE);
                AddItemAdapter.ViewHolder.CHECKOUT = false;
                adapter = new AddItemAdapter(getContext(), itemsList, listener);
                items.setAdapter(adapter);
                submit.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
            }
        });
        checkout_text = view.findViewById(R.id.checkout_text);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkout_text.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);
                AddItemAdapter.ViewHolder.CHECKOUT = true;
                final List<Item> newItems = new ArrayList<>();
                for (int i = 0; i < itemsList.size(); i++) {
                    if (itemsList.get(i).isUpdated()) {
                        newItems.add(itemsList.get(i));
                    }
                }

                adapter = new AddItemAdapter(getContext(), newItems, new AddItemAdapter.OnItemSelectedListener() {
                    @Override
                    public void onItemCheck(boolean checked) {

                    }

                    @Override
                    public void onItemInteract(int pos, int flag) {
                        if (flag == 100) {
                            newItems.remove(pos);
                            adapter.notifyItemRemoved(pos);
                        }
                    }
                });

                items.setAdapter(adapter);
                submit.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            sendRequest(newItems);
                        } catch (JSONException e) {

                        }
                    }
                });
            }
        });
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
        adapter = new AddItemAdapter(getContext(), itemsList, listener);
        items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        items.setAdapter(adapter);
        if (LOADING) {
            progress.setVisibility(View.VISIBLE);
            hider.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
            hider.setVisibility(View.GONE);
        }
    }

    private void sendRequest(List<Item> items) throws JSONException {
        App app = ((App) getActivity().getApplication());
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", App.userId);
        JSONArray array = new JSONArray();
        for (Item i : items) {
            array.put(new JSONObject().put("stock_id", i.getId()).put("qty", i.getQuantity()));
        }
        params.put("item_list", array.toString());
        console.log("Res:" + params);
        app.sendNetworkRequest(Config.INVENTORY_ITEM_REQUEST, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                if (response.equals("1")) {
                    Toast.makeText(getContext(), "Request Generated", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        });
    }

    @Override
    public void onSyncStart() {
        LOADING = true;
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
            hider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSyncError(String error) {

    }

    @Override
    public void onSync(Object object) {
        itemsList = (List<Item>) object;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSyncFinish() {
        LOADING = false;
        if (progress != null) {
            progress.setVisibility(View.GONE);
            hider.setVisibility(View.GONE);
        }
    }
}