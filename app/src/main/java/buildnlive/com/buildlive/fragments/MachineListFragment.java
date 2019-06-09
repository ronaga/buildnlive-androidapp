package buildnlive.com.buildlive.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import buildnlive.com.buildlive.activities.Inventory;
import buildnlive.com.buildlive.activities.IssuedItems;
import buildnlive.com.buildlive.activities.IssuedItemsForm;
import buildnlive.com.buildlive.activities.MachineListForm;
import buildnlive.com.buildlive.adapters.AddItemAdapter;
import buildnlive.com.buildlive.adapters.IssueItemListAdapter;
import buildnlive.com.buildlive.adapters.MachineListAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Machine;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class MachineListFragment extends Fragment {

    private RecyclerView recyclerView;
    private static App app;
    private ArrayList<Machine> inventory;
    private ProgressBar progress;
    private TextView hider;
    private MachineListAdapter issueItemListAdapter;
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


    MachineListAdapter.OnItemClickListener listener= (item, pos, view) -> {
        Intent intent=new Intent(getActivity(), MachineListForm.class);
        Bundle bundle= new Bundle();
        bundle.putSerializable("Items",item);
        intent.putExtras(bundle);
        startActivity(intent);

    };

    public static MachineListFragment newInstance(App a) {
        app = a;
        return new MachineListFragment();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_jobsheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        utilityofActivity=new UtilityofActivity(appCompatActivity);
        recyclerView= view.findViewById(R.id.items);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);


        inventory = new ArrayList<>();


        issueItemListAdapter=new MachineListAdapter(context,inventory,listener);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        DividerItemDecoration dividerItemDecoration= new DividerItemDecoration(context, LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(issueItemListAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        refresh();


    }

    private void refresh() {
        inventory.clear();
        String requestUrl = Config.GET_MACHINE_LIST;
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
                        inventory.add(new Machine().parseFromJSON(array.getJSONObject(i)));
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



}
