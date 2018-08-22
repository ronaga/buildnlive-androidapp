package buildnlive.com.buildlive.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import buildnlive.com.buildlive.activities.DailyWorkProgressActivities;
import buildnlive.com.buildlive.adapters.AttendanceAdapter;
import buildnlive.com.buildlive.adapters.DailyWorkAdapter;
import buildnlive.com.buildlive.adapters.ListAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Packet;
import buildnlive.com.buildlive.elements.Work;
import buildnlive.com.buildlive.elements.Worker;
import buildnlive.com.buildlive.utils.AdvancedRecyclerView;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.Utils;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.annotations.Index;

public class DailyWorkProgressFragment extends Fragment {
    private RecyclerView items;
    private ProgressBar progress;
    private TextView hider;
    private static RealmResults<Work> works;
    private static App app;
    private DailyWorkAdapter adapter;
    private Realm realm;
    private boolean LOADING = true;

    public static DailyWorkProgressFragment newInstance(App a) {
        app = a;
        return new DailyWorkProgressFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_work_progress, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        items = view.findViewById(R.id.items);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
        realm = Realm.getDefaultInstance();
        works = realm.where(Work.class).equalTo("belongsTo", App.belongsTo).findAllAsync();
        adapter = new DailyWorkAdapter(getContext(), works, new DailyWorkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                Intent intent = new Intent(getContext(), DailyWorkProgressActivities.class);
                intent.putExtra("id", works.get(pos).getWorkListId());
                startActivity(intent);
            }
        });
        items.setLayoutManager(new LinearLayoutManager(getContext()));
        items.setAdapter(adapter);

        if (LOADING) {
            progress.setVisibility(View.VISIBLE);
            hider.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
            hider.setVisibility(View.GONE);
        }
        loadWorks();
    }

    private void loadWorks(){
        String url = Config.REQ_DAILY_WORK;
        url = url.replace("[0]", App.userId);
        url = url.replace("[1]", App.projectId);
        app.sendNetworkRequest(url, 0, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.log("Response:"+response);
                try{
                    JSONArray array = new JSONArray(response);
                    for (int i=0; i<array.length(); i++){
                        JSONObject par = array.getJSONObject(i);
                        JSONObject sch = par.getJSONObject("work_schedule");
                        final Work work = new Work().parseFromJSON(sch.getJSONObject("work_details"), par.getString("work_list_id"), sch.getString("work_duration"), sch.getString("qty"), sch.getString("schedule_start_date"), sch.getString("schedule_finish_date"), sch.getString("current_status"), par.getString("completed_activities"), par.getString("total_activities"));
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(work);
                            }
                        });
                    }
                } catch (JSONException e){

                }
            }
        });
    }
}
/*
{
    "work_list_id":"1",
    "work_schedule":
        {
            "work_details":
                {
                    "work_id":"1",
                    "work_name":"Preparation of Site\/Prelims",
                    "work_units":"Meter",
                    "work_code":"BNL001"
                },
            "schedule_id":"1",
            "work_duration":"50 Days",
            "qty":"5 ft",
            "schedule_start_date":"2018-06-25",
            "schedule_finish_date":"2018-07-10",
            "current_status":"Delayed"
        }
}
*/
