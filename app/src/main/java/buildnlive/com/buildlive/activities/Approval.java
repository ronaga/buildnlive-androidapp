package buildnlive.com.buildlive.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.ApprovalItemAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Approval.ApprovalItem;
import buildnlive.com.buildlive.elements.Approval.ApproveIndentItem;
import buildnlive.com.buildlive.elements.Approval.ApproveLabourItem;
import buildnlive.com.buildlive.elements.Approval.ApproveLabourReportItem;
import buildnlive.com.buildlive.elements.Approval.ApproveWorkProgressItem;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class Approval extends AppCompatActivity {

    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity = this;
    private Context context;
    private App app;
    private ArrayList<ApprovalItem> approvalItem=new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private ApprovalItemAdapter approvalItemAdapter;
    private RecyclerView items;
    private ApprovalItemAdapter.OnItemInteractListener listener= new ApprovalItemAdapter.OnItemInteractListener(){
        @Override
        public void OnAttendanceItemClick(ArrayList<ApproveLabourItem> labourItem) {
            Intent intent=new Intent(context, ApproveLabourStatus.class);
            intent.putParcelableArrayListExtra("labourList",labourItem);
            startActivity(intent);
        }

        @Override
        public void OnIssueItemClick(ArrayList<buildnlive.com.buildlive.elements.Approval.ApproveIssueItem> issueItem) {
            Intent intent=new Intent(context, ApproveIssueItemStatus.class);
            intent.putParcelableArrayListExtra("issueList",issueItem);
            startActivity(intent);
        }

        @Override
        public void OnLabourReportItemClick(ArrayList<ApproveLabourReportItem> labourReport) {
            Intent intent=new Intent(context, ApproveLabourReportItemStatus.class);
            intent.putParcelableArrayListExtra("labourReportList",labourReport);
            startActivity(intent);
        }

        @Override
        public void OnIndentItemClick(ArrayList<ApproveIndentItem> indentItem) {
            Intent intent=new Intent(context, ApproveIndentItemStatus.class);
            intent.putParcelableArrayListExtra("indentItemList",indentItem);
            startActivity(intent);
        }

        @Override
        public void OnWorkProgressItemClick(ArrayList<ApproveWorkProgressItem> workProgressItem) {
            Intent intent=new Intent(context, ApproveWorkProgressItemStatus.class);
            intent.putParcelableArrayListExtra("workProgressItemList",workProgressItem);
            startActivity(intent);
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approvals);

        context = this;
        app = (App) getApplication();
        utilityofActivity = new UtilityofActivity(appCompatActivity);
        utilityofActivity.configureToolbar(appCompatActivity);

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle = findViewById(R.id.toolbar_subtitle);
        toolbar_title.setText("Approvals");
        toolbar_subtitle.setText(App.projectName);

        items = findViewById(R.id.items);

      /*  layoutManager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        approvalItemAdapter=new ApprovalItemAdapter(context,approvalItem,listener);
        items.setLayoutManager(layoutManager);
        items.setAdapter(approvalItemAdapter);
*/
        refresh();

    }

    private void refresh() {

        String requestUrl = Config.APPROVALS;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", App.projectId);

        console.log(requestUrl);

        app.sendNetworkRequest(requestUrl, Request.Method.POST, null, new Interfaces.NetworkInterfaceListener() {
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
                try {
                   /* JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                    }*/
                    Type approvalListType = new TypeToken<ArrayList<ApprovalItem>>() {
                    }.getType();
                    approvalItem = new Gson().fromJson(response, approvalListType);


                    layoutManager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);

                    approvalItemAdapter=new ApprovalItemAdapter(context,approvalItem,listener);
                    items.setLayoutManager(layoutManager);
                    items.setAdapter(approvalItemAdapter);

                    approvalItemAdapter.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
