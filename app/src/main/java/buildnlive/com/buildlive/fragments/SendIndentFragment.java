package buildnlive.com.buildlive.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.CategorySpinAdapter;
import buildnlive.com.buildlive.adapters.IndentItemAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Category;
import buildnlive.com.buildlive.elements.IndentItem;
import buildnlive.com.buildlive.utils.Config;

public class SendIndentFragment extends Fragment{
    private static List<IndentItem> siteIndentItem = new ArrayList<>();
    private RecyclerView items;
    private Button next, submit;
    private ProgressBar progress;
    private TextView hider, checkout_text;
    private boolean LOADING;
    private ImageButton close;
    private IndentItemAdapter adapter;
    public static String category="";
    private Spinner categorySpinner;
    private CategorySpinAdapter categoryAdapter;
    private ArrayList<Category> categoryList = new ArrayList<>();



    public IndentItemAdapter.OnItemSelectedListener listener = new IndentItemAdapter.OnItemSelectedListener() {
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

    public static SendIndentFragment newInstance() {
//        siteIndentItem = u;
        return new SendIndentFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_indent_item, container, false);

        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        items = view.findViewById(R.id.items);
        items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        submit = view.findViewById(R.id.submit);
        next = view.findViewById(R.id.next);
        close = view.findViewById(R.id.close_checkout);
        setCategorySpinner();
        categorySpinner=view.findViewById(R.id.category);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String a = categoryAdapter.getID(i);
                    if(!a.equals("0")){
                        refresh(a);

                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        categoryAdapter=new CategorySpinAdapter(getContext(), R.layout.custom_spinner,categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);





        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndentItemAdapter.ViewHolder.CHECKOUT = false;
                checkout_text.setVisibility(View.GONE);
                close.setVisibility(View.GONE);
                categorySpinner.setVisibility(View.VISIBLE);
                IndentItemAdapter.ViewHolder.CHECKOUT = false;
                adapter = new IndentItemAdapter(getContext(), siteIndentItem, listener);
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
                categorySpinner.setVisibility(View.GONE);
                IndentItemAdapter.ViewHolder.CHECKOUT = true;
                final List<IndentItem> newItems = new ArrayList<>();
                for (int i = 0; i < siteIndentItem.size(); i++) {
                    if (siteIndentItem.get(i).isUpdated()) {
                        newItems.add(siteIndentItem.get(i));
                    }
                }

                adapter = new IndentItemAdapter(getContext(), newItems, new IndentItemAdapter.OnItemSelectedListener() {
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
//        adapter = new IndentItemAdapter(getContext(), siteIndentItem, listener);
//        items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        items.setAdapter(adapter);
        if (LOADING) {
            progress.setVisibility(View.VISIBLE);
            hider.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
            hider.setVisibility(View.GONE);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        siteIndentItem.clear();
//        adapter.notifyItemRangeChanged(0,siteIndentItem.size());
    }

    private void sendRequest(List<IndentItem> items) throws JSONException {
        App app= ((App)getActivity().getApplication());
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", App.userId);
        JSONArray array = new JSONArray();
        for (IndentItem i : items) {
            array.put(new JSONObject().put("stock_id", i.getId()).put("required_qty", i.getQuantity()));
        }
        params.put("request_list", array.toString());
        console.log("Res:" + params);
        app.sendNetworkRequest(Config.SEND_REQUEST_SITE_LIST, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                if (response.equals("Success")) {
                    Toast.makeText(getContext(), "Request Generated", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        });
    }


    private void setCategorySpinner() {
        App app= ((App)getActivity().getApplication());
        String requestURl= Config.REQ_SENT_CATEGORIES ;
        requestURl = requestURl.replace("[0]", App.userId);
        categoryList.clear();
        app.sendNetworkRequest(requestURl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {

                console.error("Network request failed with error :" + error);
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
//                        categoryList.add(array.getJSONObject(i).getString("name"));
                        categoryList.add(new Category().parseFromJSON(array.getJSONObject(i)));
                    }
                    categoryAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void refresh(String a) {
        App app= ((App)getActivity().getApplication());
        siteIndentItem.clear();
        String requestUrl = Config.GET_SITE_LIST;
        requestUrl = requestUrl.replace("[1]", App.userId);
        requestUrl = requestUrl.replace("[0]", a);
        console.log(requestUrl);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {

                console.error("Network request failed with error :" + error);
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                try {
                    JSONArray array = new JSONArray(response);
                  for (int i = 0; i < array.length(); i++) {
                        siteIndentItem.add(new IndentItem().parseFromJSON(array.getJSONObject(i)));
                    }
                    console.log("data set changed");
                    adapter = new IndentItemAdapter(getContext(), siteIndentItem, listener);
                    items.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    console.log(""+adapter.getItemCount());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
