package buildnlive.com.buildlive.activities;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.DeployLabourAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.LabourBreakup;
import buildnlive.com.buildlive.elements.LabourDeploy;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;
import io.realm.Realm;

public class DeployLabour extends AppCompatActivity {
    private static List<LabourBreakup> itemsList = new ArrayList<>();
    private static LabourDeploy item;
    private RecyclerView items;
    private Realm realm;
    private ProgressBar progress;
    private static App app;
    private TextView hider, name;
    private Context context;
    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity = this;

    private DeployLabourAdapter.OnItemClickListener listner = new DeployLabourAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(LabourBreakup items, int pos, View view, EditText quantity) {
            if ((!quantity.getText().toString().equals("")) && Integer.parseInt(quantity.getText().toString()) <= Integer.parseInt(items.getLabour_no()))
                sendRequest(items,quantity.getText().toString());
            else
                utilityofActivity.toast("Please enter count to return");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deploy_labour);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            item = bundle.getParcelable("Item");
        }


        context = this;
        utilityofActivity = new UtilityofActivity(appCompatActivity);
        utilityofActivity.configureToolbar(appCompatActivity);

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle = findViewById(R.id.toolbar_subtitle);

        toolbar_subtitle.setText(App.projectName);

        toolbar_title.setText("Labour Deployment");


        items = findViewById(R.id.items);
        progress = findViewById(R.id.progress);
        name = findViewById(R.id.name);
        hider = findViewById(R.id.hider);

        name.setText(item.getStructure_details());

        DeployLabourAdapter adapter = new DeployLabourAdapter(context, itemsList, listner);
        items.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        items.setAdapter(adapter);

        refresh(item.getStructure_id());

    }

    private void refresh(String structure_id) {
        itemsList.clear();
        String url = Config.SEND_LABOUR_STRUCTURE;
        url = url.replace("[0]", structure_id);

        app = (App) getApplication();

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

                Type vendorType = new TypeToken<ArrayList<LabourBreakup>>() {
                }.getType();
                itemsList = new Gson().fromJson(response, vendorType);

                DeployLabourAdapter adapter = new DeployLabourAdapter(context, itemsList, listner);
                items.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                items.setAdapter(adapter);
                adapter.notifyDataSetChanged();
//                for(LabourBreakup i: itemsList){
//                    console.log(i.getItemName());
//                }
            }
        });
    }

    private void sendRequest(LabourBreakup items, String s) {
        itemsList.clear();
        String url = Config.GetLabourOut;
        url = url.replace("[0]", items.getStructure_labour_id());
        url = url.replace("[1]", s);
        url = url.replace("[2]", App.userId);
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

                if (response.equals("1")) {
                    utilityofActivity.toast("Success");
                    finish();
                }
            }
        });
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

}