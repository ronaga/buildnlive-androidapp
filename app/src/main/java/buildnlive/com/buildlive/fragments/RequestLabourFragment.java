package buildnlive.com.buildlive.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import buildnlive.com.buildlive.adapters.LabourSpinAdapter;
import buildnlive.com.buildlive.adapters.RequestLabourAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Category;
import buildnlive.com.buildlive.elements.IndentItem;
import buildnlive.com.buildlive.elements.LabourInfo;
import buildnlive.com.buildlive.elements.LabourVendor;
import buildnlive.com.buildlive.utils.Config;

public class RequestLabourFragment extends Fragment {
    private static List<LabourInfo> labourList = new ArrayList<>();
    private RecyclerView items;
    private Button next, submit;
    private ProgressBar progress;
    private TextView hider, checkout_text,search_textview,avail;
    private ImageButton close;
    private RequestLabourAdapter adapter;
    public static String category="";
    private Spinner categorySpinner;
    private LabourSpinAdapter categoryAdapter;
    private ArrayList<LabourVendor> categoryList = new ArrayList<>();
    private android.support.v7.widget.SearchView searchView;
    AlertDialog.Builder builder;
    private static String a;

    public RequestLabourAdapter.OnItemSelectedListener listener = new RequestLabourAdapter.OnItemSelectedListener() {
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

    public static RequestLabourFragment newInstance() {
//        siteIndentItem = u;
        return new RequestLabourFragment();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        categoryList.clear();
        RequestLabourAdapter.ViewHolder.CHECKOUT=false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_request_labour, container, false);

        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        items = view.findViewById(R.id.items);
        items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        builder= new AlertDialog.Builder(getContext());
        searchView = view.findViewById(R.id.search_view);
        avail=view.findViewById(R.id.avail_text);

        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
        submit = view.findViewById(R.id.submit);
        next = view.findViewById(R.id.next);
        close = view.findViewById(R.id.close_checkout);
        setCategorySpinner();
        categorySpinner=view.findViewById(R.id.contractor);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                a = categoryAdapter.getID(i);
                if(!a.equals("0")){
                    refresh(a);
                    searchView.setVisibility(View.VISIBLE);
                    search_textview.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        categoryAdapter=new LabourSpinAdapter(getContext(), R.layout.custom_spinner,categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);





        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh(a);
                RequestLabourAdapter.ViewHolder.CHECKOUT = false;
                checkout_text.setVisibility(View.GONE);
                close.setVisibility(View.GONE);
                categorySpinner.setVisibility(View.VISIBLE);
                adapter = new RequestLabourAdapter(getContext(), labourList, listener);
                items.setAdapter(adapter);
                submit.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
            }
        });
        checkout_text = view.findViewById(R.id.checkout_text);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avail.setVisibility(View.GONE);
                search_textview.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                checkout_text.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);
                categorySpinner.setVisibility(View.GONE);
                RequestLabourAdapter.ViewHolder.CHECKOUT = true;
                final List<LabourInfo> newItems = new ArrayList<>();
                for (int i = 0; i < labourList.size(); i++) {
                    if (labourList.get(i).isUpdated()) {
                        newItems.add(labourList.get(i));
                    }
                }

                adapter = new RequestLabourAdapter(getContext(), newItems, new RequestLabourAdapter.OnItemSelectedListener() {
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
                        builder.setMessage("Are you sure?") .setTitle("Request Item");

                        //Setting message manually and performing action on button click
                        builder.setMessage("Do you want to Submit?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        try {
                                            sendRequest(newItems);
                                        } catch (JSONException e) {

                                        }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();

                                    }
                                });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("Request Item");
                        alert.show();


                    }
                });
            }
        });
//        adapter = new RequestLabourAdapter(getContext(), labourList, listener);
//        items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        items.setAdapter(adapter);


        search_textview=view.findViewById(R.id.search_textview);
        searchView = view.findViewById(R.id.search_view);

        // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager
//                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_textview.setVisibility(View.GONE);
            }
        });
        // listening to search query text change
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        labourList.clear();
//        adapter.notifyItemRangeChanged(0,siteIndentItem.size());
    }

    private void sendRequest(List<LabourInfo> items) throws JSONException {
        App app= ((App)getActivity().getApplication());
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", App.userId);
        params.put("project_id",App.projectId);
        JSONArray array = new JSONArray();
        for (LabourInfo i : items) {
            array.put(new JSONObject().put("daily_labour_id", i.getId()).put("qty", i.getQuantity()));
        }
        params.put("labour_transfer", array.toString());
        console.log("Res:" + params);
        app.sendNetworkRequest(Config.SEND_LABOUR_LIST, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Error"+error,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                if (response.equals("1")) {
                    Toast.makeText(getContext(), "Request Generated", Toast.LENGTH_SHORT).show();
                    RequestLabourAdapter.ViewHolder.CHECKOUT=false;
                    getActivity().finish();
                }
            }
        });
    }


    private void setCategorySpinner() {
        App app= ((App)getActivity().getApplication());
        String requestURl= Config.GET_LABOUR_VENDOR_LIST ;
        requestURl = requestURl.replace("[0]", App.userId);
        requestURl = requestURl.replace("[1]", App.projectId);
        categoryList.clear();
        console.log(requestURl);
        app.sendNetworkRequest(requestURl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.error("Network request failed with error :" + error);
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
//                        categoryList.add(array.getJSONObject(i).getString("name"));
                        categoryList.add(new LabourVendor().parseFromJSON(array.getJSONObject(i)));
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
        labourList.clear();
        String requestUrl = Config.GET_LABOUR_LIST;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", App.projectId);
        requestUrl = requestUrl.replace("[2]", a);
        console.log(requestUrl);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.error("Network request failed with error :" + error);
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        labourList.add(new LabourInfo().parseFromJSON(array.getJSONObject(i)));
                    }
                    console.log("data set changed");
                    adapter = new RequestLabourAdapter(getContext(), labourList, listener);
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
