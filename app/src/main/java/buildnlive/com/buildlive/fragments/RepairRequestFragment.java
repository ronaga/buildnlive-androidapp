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
import buildnlive.com.buildlive.activities.RepairRequestForm;
import buildnlive.com.buildlive.adapters.RepairAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.RepairItem;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class RepairRequestFragment extends Fragment {

    private RecyclerView recyclerView;
    private static App app;
    private ArrayList<RepairItem> repairList;
    private ProgressBar progress;
    private TextView hider;
    private RepairAdapter issueItemListAdapter;
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


    RepairAdapter.OnItemClickListener listener = (item, pos, view) -> {
        Intent intent = new Intent(getActivity(), RepairRequestForm.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Items", item);
        intent.putExtras(bundle);
        startActivity(intent);

    };

    public static RepairRequestFragment newInstance(App a) {
        app = a;
        return new RepairRequestFragment();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repair_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        utilityofActivity = new UtilityofActivity(appCompatActivity);
        recyclerView = view.findViewById(R.id.items);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);


        repairList = new ArrayList<>();


        issueItemListAdapter = new RepairAdapter(context, repairList, listener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(issueItemListAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        refresh();


    }

    private void refresh() {
        repairList.clear();
        String requestUrl = Config.SEND_REPAIR_ITEM;
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
                utilityofActivity.dismissProgressDialog();
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        repairList.add(new RepairItem().parseFromJSON(array.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                issueItemListAdapter.notifyDataSetChanged();
            }
        });
    }


}
