package buildnlive.com.buildlive.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.IssueItemStatusAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Approval.ApproveIssueItem;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class ApproveIssueItemStatus extends AppCompatActivity {

    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity = this;
    private Context context;
    private App app;
    private LinearLayoutManager layoutManager;
    private IssueItemStatusAdapter labourStatusAdapter;
    private RecyclerView items;
    private Intent intent;
    private ArrayList<ApproveIssueItem> approveIssueItem;
    private ArrayList<ApproveIssueItem> resultList=new ArrayList<>();
    private Button submit;
    private AlertDialog.Builder builder;

    private IssueItemStatusAdapter.OnItemClickListener listener=new IssueItemStatusAdapter.OnItemClickListener() {
        @Override
        public void onItemUnchecked(@NotNull ApproveIssueItem project, int pos) {
            if(!resultList.isEmpty())
            {
                resultList.remove(project);
            }
        }

        @Override
        public void onItemClick(@NotNull ApproveIssueItem project, int pos) {
            resultList.add(project);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_issue_item);


        context = this;
        app = (App) getApplication();
        utilityofActivity = new UtilityofActivity(appCompatActivity);
        utilityofActivity.configureToolbar(appCompatActivity);

        builder = new AlertDialog.Builder(context);

        intent=getIntent();
        approveIssueItem = intent.getParcelableArrayListExtra("issueList");

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle = findViewById(R.id.toolbar_subtitle);
        submit = findViewById(R.id.submit);
        toolbar_title.setText("Approvals");
        toolbar_subtitle.setText(App.projectName);

        items = findViewById(R.id.items);

        layoutManager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);

        labourStatusAdapter =new IssueItemStatusAdapter(context, approveIssueItem,listener);
        items.setLayoutManager(layoutManager);
        items.setAdapter(labourStatusAdapter);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("Are you sure?") .setTitle("Submit");

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    sendRequest(resultList);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Submit");
                alert.show();


            }
        });


    }

    private void sendRequest(ArrayList<ApproveIssueItem> items) throws JSONException {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", App.userId);
        params.put("type", "issueitem");
        JSONArray array = new JSONArray();
        for (ApproveIssueItem i : items) {
            array.put(new JSONObject().put("id", i.getIssueId()).put("status", i.getStatus()));
        }
        params.put("approval_list", array.toString());
        console.log("Res:" + params);
        app.sendNetworkRequest(Config.GET_APPROVALS, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                utilityofActivity.dismissProgressDialog();
                Toast.makeText(context,"Error:"+error,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                utilityofActivity.dismissProgressDialog();
                console.log(response);
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
