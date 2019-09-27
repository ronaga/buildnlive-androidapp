package buildnlive.com.buildlive.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.activities.CreateLabourDeploy;
import buildnlive.com.buildlive.activities.DeployLabour;
import buildnlive.com.buildlive.activities.LabourRequest;
import buildnlive.com.buildlive.activities.LabourRequestForm;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.LabourRequestItem;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class LabourRequestFragment extends Fragment {

    private RecyclerView recyclerView;
    private static App app;
    private ArrayList<LabourRequestItem> labourRequestList;
    private ProgressBar progress;
    private TextView hider;
    private LabourRequestAdapter labourRequestAdapter;
    private Context context;
    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity;


    LabourRequestAdapter.OnItemClickListener listener = (pos, view,id) -> {
        Intent intent = new Intent(getActivity(), LabourRequestForm.class);
        intent.putExtra("labourRequestId", id);
        startActivity(intent);

    };

    public static LabourRequestFragment newInstance(App a) {
        app = a;
        return new LabourRequestFragment();

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
        return inflater.inflate(R.layout.fragment_labour_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.items);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);

        utilityofActivity = new UtilityofActivity(appCompatActivity);



        labourRequestList = new ArrayList<>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, LinearLayoutManager.VERTICAL);

     /*   labourRequestAdapter=new StructureListAdapter(context, labourRequestList,listener);
        recyclerView.setAdapter(labourRequestAdapter);
     */
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        refresh();


    }

    private void refresh() {
        labourRequestList.clear();
        String requestUrl = Config.LabourDeployementRequestList;
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
                    Type vendorType = new TypeToken<ArrayList<LabourRequestItem>>() {
                    }.getType();
                    labourRequestList = new Gson().fromJson(response, vendorType);

                    labourRequestAdapter = new LabourRequestAdapter(context, labourRequestList, listener);
                    recyclerView.setAdapter(labourRequestAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
//
//                for (LabourDeploy i : labourRequestList) {
//                    items.add(i.getName());
//                    console.log("Name:"+i.getName());
//                }
            }
        });
    }


}

