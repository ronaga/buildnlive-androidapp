package buildnlive.com.buildlive.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.ViewRequestAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Request;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;
import io.realm.Realm;
import io.realm.RealmResults;

public class ViewRequestsFragment extends Fragment {
    private RecyclerView items;
    private static App app;
    private ProgressBar progress;
    private TextView hider;
    private Context context;
    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity;
    private ArrayList<Request> dataList=new ArrayList();
    private  ViewRequestAdapter adapter;


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

    public static ViewRequestsFragment newInstance(App a) {
        app = a;
        return new ViewRequestsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_requests, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        utilityofActivity=new UtilityofActivity(appCompatActivity);
        items = view.findViewById(R.id.requests);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
//        Realm realm = Realm.getDefaultInstance();
//        RealmResults<Request> requests = realm.where(Request.class).equalTo("belongsTo", App.belongsTo).findAllAsync();

        adapter = new ViewRequestAdapter(getContext(), dataList);
        items.setAdapter(adapter);
        items.setLayoutManager(new LinearLayoutManager(getContext()));
        refresh();
    }

    private void refresh() {
        String url = Config.GET_REQUEST_LIST;
        url = url.replace("[0]", App.projectId);
        url = url.replace("[1]", App.userId);
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
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();

                Realm realm = Realm.getDefaultInstance();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        dataList.add(new Request().parseFromJSON(array.getJSONObject(i)));
                    }
                    adapter.notifyDataSetChanged();
//
//                    for (int i = 0; i < array.length(); i++) {
//                        final JSONObject obj = array.getJSONObject(i);
//                        realm.executeTransaction(new Realm.Transaction() {
//                            @Override
//                            public void execute(@NonNull Realm realm) {
//                                try {
//                                    Request request = new Request().parseFromJSON(obj);
//                                    realm.copyToRealmOrUpdate(request);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                    }
//                    realm.close();
                } catch (JSONException e) {

                }
            }
        });
    }
}