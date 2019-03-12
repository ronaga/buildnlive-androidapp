package buildnlive.com.buildlive.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.activities.ManageLabour;
import buildnlive.com.buildlive.adapters.ContractorAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Contractor;
import buildnlive.com.buildlive.utils.Config;
import io.realm.Realm;

public class ManageLabourFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ArrayList<Contractor> contractorList = new ArrayList<>();
    private ContractorAdapter adapter;
    private App app;
    private Fragment fragment;
    private android.app.AlertDialog.Builder builder;
    public static String name_s,user_id_s;
    private ProgressBar progress;
    private TextView hider;
    private CoordinatorLayout coordinatorLayout;

    public static ManageLabourFragment newInstance() {
        return new ManageLabourFragment();
    }

    private ContractorAdapter.OnItemClickListener listner=new ContractorAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Contractor contractor, int pos, View view) {
            name_s= contractor.getContractor_name();
            user_id_s=contractor.getContractor_id();
            startActivity(new Intent(getActivity(),ManageLabour.class));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_labour, container, false);;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Realm realm =Realm.getDefaultInstance();
        recyclerView=view.findViewById(R.id.items);
        app=(App) getActivity().getApplication();
        builder = new android.app.AlertDialog.Builder(getContext());
        progress=view.findViewById(R.id.progress);
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        hider=view.findViewById(R.id.hider);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);
        adapter = new ContractorAdapter(getContext(),contractorList,listner);
        recyclerView.setAdapter(adapter);
//        floatingActionButton=view.findViewById(R.id.add);

    }

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    private void refresh() {
        App app= ((App)getActivity().getApplication());
        contractorList.clear();
        String requestUrl = Config.GET_CONTRACTORS;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", App.projectId);
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
//                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Something went wrong, Try again later", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        contractorList.add(new Contractor().parseFromJSON(array.getJSONObject(i)));
                    }
                    console.log("data set changed");
                    adapter = new ContractorAdapter(getContext(), contractorList, listner);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
