package buildnlive.com.buildlive.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import buildnlive.com.buildlive.adapters.AddItemAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Item;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class AddItemFragment extends Fragment{
    private static List<Item> itemsList=new ArrayList<>();
    private RecyclerView items;
    private Button next, submit;
    private ProgressBar progress;
    private TextView hider, checkout_text;
    private boolean LOADING;
    private ImageButton close,search_close,search;
    private AddItemAdapter adapter;
//    private SearchView searchView;
    private LinearLayout search_view;
    private EditText search_text;
    AlertDialog.Builder builder;
    final List<Item> newItems = new ArrayList<>();
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

    public static AddItemFragment newInstance() {
//        itemsList = u;
        return new AddItemFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        newItems.clear();
        itemsList.clear();
        AddItemAdapter.ViewHolder.CHECKOUT=false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory_new, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);;

        utilityofActivity = new UtilityofActivity(appCompatActivity);
        builder= new AlertDialog.Builder(getContext());
        items = view.findViewById(R.id.items);
                submit = view.findViewById(R.id.submit);
        next = view.findViewById(R.id.next);
        close = view.findViewById(R.id.close_checkout);
        search_view = view.findViewById(R.id.search_view);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_view.setVisibility(View.VISIBLE);
                AddItemAdapter.ViewHolder.CHECKOUT = false;
                checkout_text.setVisibility(View.GONE);
                close.setVisibility(View.GONE);
                AddItemAdapter.ViewHolder.CHECKOUT = false;
                newItems.clear();
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
                search_view.setVisibility(View.GONE);
                checkout_text.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);
//                search_textview.setVisibility(View.GONE);
                AddItemAdapter.ViewHolder.CHECKOUT = true;
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
                        builder.setMessage("Are you sure?") .setTitle("Indent Item");

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
                        alert.setTitle("Indent Item");
                        alert.show();


                    }
                });
            }
        });


//        search_textview=view.findViewById(R.id.search_textview);
        search_text=view.findViewById(R.id.search_text);
        search_close=view.findViewById(R.id.search_close);
        search=view.findViewById(R.id.search);

        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
//        adapter = new AddItemAdapter(getContext(), itemsList, listener);
//        items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        items.setAdapter(adapter);
        if (LOADING) {
            progress.setVisibility(View.VISIBLE);
            hider.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
            hider.setVisibility(View.GONE);
        }

//        searchView = view.findViewById(R.id.search_view);
//
//        // Associate searchable configuration with the SearchView
////        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
////        searchView.setSearchableInfo(searchManager
////                .getSearchableInfo(getActivity().getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                search_textview.setVisibility(View.GONE);
//            }
//        });
//        // listening to search query text change
//        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // filter recycler view when query submitted
//                adapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                // filter recycler view when text is changed
//                adapter.getFilter().filter(query);
//                return false;
//            }
//        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_text.setVisibility(View.VISIBLE);
                search_close.setVisibility(View.VISIBLE);
                search_text.requestFocus();
                if(search_text.hasFocus()){
                   InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(search_text.windowToken,InputMethodManager.SHOW_IMPLICIT )
                    if (imm != null) {
                        imm.showSoftInput(search_text, InputMethodManager.SHOW_IMPLICIT);
                    }
                }

            }
        });

        search_text.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                search(search_text.getText().toString());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(search_text.getWindowToken(), 0);
                }
                return true;
            } else {
               return false;
            }

        });

        search_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_text.setText("");
                search_text.setVisibility(View.INVISIBLE);
                search_close.setVisibility(View.INVISIBLE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                refresh();
            }
        });

        refresh();
    }

    private void sendRequest(List<Item> items) throws JSONException {
        App app = ((App) getActivity().getApplication());
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", App.userId);
        params.put("detail_id", App.projectId);
        JSONArray array = new JSONArray();
        for (Item i : items) {
            array.put(new JSONObject().put("stock_id", i.getId()).put("qty", i.getQuantity()));
        }
        params.put("item_list", array.toString());
        console.log("Res:" + params);
        app.sendNetworkRequest(Config.INVENTORY_ITEM_REQUEST, 1, params, new Interfaces.NetworkInterfaceListener() {
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
                Toast.makeText(getContext(),"Error:"+error,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
                if (response.equals("1")) {
                    Toast.makeText(getContext(), "Request Generated", Toast.LENGTH_SHORT).show();
                    AddItemAdapter.ViewHolder.CHECKOUT=false;
                    getActivity().finish();
                }
            }
        });
    }
    private void refresh() {
        App app= ((App)getActivity().getApplication());
        itemsList.clear();
        String requestUrl = Config.REQ_GET_ITEM_INVENTORY;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", App.projectId);
        console.log(requestUrl);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
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
                console.error("Network request failed with error :" + error);
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
//                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        itemsList.add(new Item().parseFromJSON(array.getJSONObject(i)));
                    }
                    console.log("data set changed");
                    adapter = new AddItemAdapter(getContext(), itemsList, listener);
                    items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    items.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    console.log(""+adapter.getItemCount());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

  private void search(String keyword) {
        App app= ((App)getActivity().getApplication());
        itemsList.clear();
        String requestUrl = Config.INVENTORY_SEARCH;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", App.projectId);
        requestUrl = requestUrl.replace("[2]", keyword);
        console.log(requestUrl);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
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
                console.error("Network request failed with error :" + error);
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
//                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        itemsList.add(new Item().parseFromJSON(array.getJSONObject(i)));
                    }
                    console.log("data set changed");
                    adapter = new AddItemAdapter(getContext(), itemsList, listener);
                    items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
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