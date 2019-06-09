package buildnlive.com.buildlive.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import buildnlive.com.buildlive.adapters.ViewJobSheetAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.ViewJobSheet;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;
import io.realm.Realm;

public class ViewMachineFragment extends Fragment {
    private static List<ViewJobSheet> itemsList=new ArrayList<>();
    private RecyclerView items;
    private Realm realm;
    private ProgressBar progress;
    private static App app;
    private TextView hider;
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

    public static ViewMachineFragment newInstance(App a) {
        app=a;
        return new ViewMachineFragment();
    }

    private ViewJobSheetAdapter.OnItemClickListener listner=new ViewJobSheetAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(ViewJobSheet items, int pos, View view) {

        }

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_jobsheet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        realm = Realm.getDefaultInstance();
//        itemsList = realm.where(Issue.class).equalTo("belongsTo", App.belongsTo).findAll();
        items = view.findViewById(R.id.items);
        utilityofActivity= new UtilityofActivity(appCompatActivity);
        progress=view.findViewById(R.id.progress);
        hider=view.findViewById(R.id.hider);
        ViewJobSheetAdapter adapter = new ViewJobSheetAdapter(getContext(), itemsList,listner);
        items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        items.setAdapter(adapter);
        refresh();
    }


    private void refresh() {
        itemsList.clear();
        String url = Config.VIEW_MACHINE_LIST;
        url = url.replace("[0]", App.userId);
        url = url.replace("[1]", App.projectId);
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
                console.log("Response:" + response);
                progress.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
                hider.setVisibility(View.GONE);
//                Realm realm = Realm.getDefaultInstance();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        itemsList.add(new ViewJobSheet().parseFromJSON(array.getJSONObject(i)));


//                        realm.executeTransaction(new Realm.Transaction() {
//                            @Override
//                            public void execute(Realm realm) {
//                                try {
//                                    RequestList request = new RequestList().parseFromJSON(obj);
//                                    realm.copyToRealmOrUpdate(request);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
                    }
                    console.log("Size Of JobSheet: "+itemsList.size());
//                      realm.close();

                } catch (JSONException e) {
                }

                ViewJobSheetAdapter adapter = new ViewJobSheetAdapter(getContext(),itemsList,listner);
                items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                items.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });
    }
}