package buildnlive.com.buildlive.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.BreakUpAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.LabourModel;
import buildnlive.com.buildlive.fragments.ViewLabourReportFragment;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.adapters.BreakUpAdapter;
import buildnlive.com.buildlive.elements.LabourModel;

public class ManageLabourReport extends AppCompatActivity {

    private TextView name;
    private TextView date;
    private RecyclerView items;
    private BreakUpAdapter adapter;
    private String user,quantity;
    private static ArrayList<LabourModel> labourModelList=new ArrayList<>();
    private Context context;
    private ProgressBar progress;
    private TextView hider;
    private CoordinatorLayout coordinatorLayout;

    BreakUpAdapter.OnItemClickListener listener= new BreakUpAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(LabourModel items, int pos, View view) {

        }

        @Override
        public void onItemCheck(boolean checked) {

        }

        @Override
        public void onItemInteract(int pos, int flag) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        refresh();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_report);
        context=this;
        progress=findViewById(R.id.progress);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        hider=findViewById(R.id.hider);

        name=findViewById(R.id.name);
        date=findViewById(R.id.date);
        items=findViewById(R.id.item);
        
        name.setText(ViewLabourReportFragment.name_s);
        date.setText(ViewLabourReportFragment.date_s);
        name.setEnabled(false);
        date.setEnabled(false);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void refresh() {
        App app= ((App) getApplication());
        labourModelList.clear();
        String requestUrl = Config.VIEW_DETAILED_LABOUR_REPORT;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", ViewLabourReportFragment.daily_report_id_s);
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
//                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
                final Snackbar snackbar = Snackbar.make(coordinatorLayout, "Something went wrong, Try again later", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                }).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        labourModelList.add(new LabourModel().parseFromJSON(array.getJSONObject(i)));
                    }
                    console.log("data set changed");
                    adapter = new BreakUpAdapter(context, labourModelList, listener);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(items.getContext(),LinearLayoutManager.VERTICAL);
                    items.addItemDecoration(dividerItemDecoration);
                    items.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                    items.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
