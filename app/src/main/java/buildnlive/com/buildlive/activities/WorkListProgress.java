package buildnlive.com.buildlive.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.ShowWorkListAdapter;
import buildnlive.com.buildlive.adapters.StructureSpinAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.ShowWorkListItem;
import buildnlive.com.buildlive.elements.Structure;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class WorkListProgress extends AppCompatActivity {
    private App app;
    private RecyclerView items;
    private TextView  no_content;
    private ShowWorkListAdapter adapter;
    private static ArrayList<ShowWorkListItem> workslist = new ArrayList<>();
    private boolean LOADING = true;
    private Context context;
    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity = this;
    private StructureSpinAdapter structureSpinAdapter;
    private ArrayList<Structure> structureList = new ArrayList<>();
    private Spinner structureSpinner;
    private String structureId;

    private ShowWorkListAdapter.OnItemClickListener listener = new ShowWorkListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(String id) {
            Intent intent = new Intent(WorkListProgress.this, WorkProgress.class);
            intent.putExtra("project_work_id", id);
            startActivity(intent);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        setStructureSpinner();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_list_progress);
        context = this;
        utilityofActivity = new UtilityofActivity(appCompatActivity);
        utilityofActivity.configureToolbar(appCompatActivity);

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle = findViewById(R.id.toolbar_subtitle);
        toolbar_title.setText("Work Progress");
        toolbar_subtitle.setText(App.projectName);

        app = (App) getApplication();
        items = findViewById(R.id.items);
        no_content = findViewById(R.id.no_content);


        structureSpinner = findViewById(R.id.structure);
        structureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                structureId = structureSpinAdapter.getStructureId(i);

                    loadWorks(structureId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        structureSpinAdapter = new StructureSpinAdapter(context, R.layout.custom_spinner, structureList);
        structureSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        structureSpinner.setAdapter(structureSpinAdapter);


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


    private void loadWorks(String structureId) {
        workslist.clear();
        String url = Config.ShowWorkList;
        url = url.replace("[0]", App.userId);
        url = url.replace("[1]", App.projectId);
        url = url.replace("[2]", structureId);
        console.log("URL:" + url);
        app.sendNetworkRequest(url, 0, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                utilityofActivity.dismissProgressDialog();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                workslist.clear();
                utilityofActivity.dismissProgressDialog();


                Type vendorType = new TypeToken<ArrayList<ShowWorkListItem>>() {
                }.getType();
                workslist = new Gson().fromJson(response, vendorType);


                if (workslist.isEmpty()) {
                    no_content.setVisibility(View.VISIBLE);
                } else no_content.setVisibility(View.GONE);


                adapter = new ShowWorkListAdapter(context, workslist, listener);
                items.setLayoutManager(new LinearLayoutManager(context));
                items.setAdapter(adapter);


            }
        });
    }


    private void setStructureSpinner() {

        String requestURl = Config.SendStructures;
        requestURl = requestURl.replace("[0]", App.userId);
        requestURl = requestURl.replace("[1]", App.projectId);

        structureList.clear();

        console.log(requestURl);
        app.sendNetworkRequest(requestURl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                utilityofActivity.dismissProgressDialog();
                console.error("Network request failed with error :" + error);
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                utilityofActivity.dismissProgressDialog();

                Type vendorType = new TypeToken<ArrayList<Structure>>() {
                }.getType();
                structureList = new Gson().fromJson(response, vendorType);

                structureSpinAdapter.notifyDataSetChanged();

                structureSpinAdapter = new StructureSpinAdapter(context, R.layout.custom_spinner, structureList);
                structureSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                structureSpinner.setAdapter(structureSpinAdapter);


            }
        });
    }

}
