package buildnlive.com.buildlive.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.activities.StoreRequestForm;
import buildnlive.com.buildlive.adapters.IssueItemListAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Item;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class StoreRequestFragment extends Fragment {

    private RecyclerView recyclerView;
    private static App app;
    private ArrayList<Item> inventory;
    private ProgressBar progress;
    private TextView hider;
    private IssueItemListAdapter issueItemListAdapter;
    private Context context;
    private LinearLayout search_view;
    private EditText search_text;
    private ImageButton search_close,search;
    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity;



    IssueItemListAdapter.OnItemClickListener listener= (item, pos, view) -> {
        Intent intent=new Intent(getActivity(), StoreRequestForm.class);
        Bundle bundle= new Bundle();
        bundle.putParcelable("Items",item);
        intent.putExtras(bundle);
        startActivity(intent);

    };

    public static StoreRequestFragment newInstance(App a) {
        app = a;
        return new StoreRequestFragment();

    }

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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_issue_item_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView= view.findViewById(R.id.items);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);

        utilityofActivity = new UtilityofActivity(appCompatActivity);

        search_view = view.findViewById(R.id.search_view);
        search_text=view.findViewById(R.id.search_text);
        search_close=view.findViewById(R.id.search_close);
        search=view.findViewById(R.id.search);


        search.setOnClickListener(v -> {
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

        search_close.setOnClickListener(v -> {
            search_text.setText("");
            search_text.setVisibility(View.INVISIBLE);
            search_close.setVisibility(View.INVISIBLE);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            refresh();
        });

        inventory = new ArrayList<>();


        issueItemListAdapter=new IssueItemListAdapter(context,inventory,listener);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        DividerItemDecoration dividerItemDecoration= new DividerItemDecoration(context, LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(issueItemListAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        refresh();


    }

    private void refresh() {
        inventory.clear();
        String requestUrl = Config.REQ_GET_INVENTORY;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", App.projectId);

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
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        inventory.add(new Item().parseFromJSON(array.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//
//                for (Item i : inventory) {
//                    items.add(i.getName());
//                    console.log("Name:"+i.getName());
//                }
                issueItemListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void search(String keyword) {
        inventory.clear();
        String requestUrl = Config.INVENTORY_ITEM_SEARCH;
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
                utilityofActivity.dismissProgressDialog();
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        inventory.add(new Item().parseFromJSON(array.getJSONObject(i)));
                    }
                    console.log("data set changed");

                    issueItemListAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
