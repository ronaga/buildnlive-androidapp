package buildnlive.com.buildlive.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.FreeLabourAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.FreeLabour;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class CreateLabourDeployList extends AppCompatActivity {
    private App app;

    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity = this;

    private Context context;

    private static ArrayList<FreeLabour> data = new ArrayList<>();
    private TextView no_content;

    private Button submit;

    private static String workNameId,date;

    private RecyclerView items;
    private FreeLabourAdapter labourAdapter;


    private AlertDialog.Builder builder;


    private static ArrayList<FreeLabour> newItems = new ArrayList<>();

    FreeLabourAdapter.OnItemClickListener listener = new FreeLabourAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(FreeLabour items, int pos, View view) {

        }

        @Override
        public void onItemCheck(boolean checked) {
            if (checked) {
                submit.setVisibility(View.VISIBLE);
            } else {
                submit.setVisibility(View.GONE);
            }
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
    protected void onDestroy() {
        super.onDestroy();
        data.clear();
        newItems.clear();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deploy_labour_list);

        context = this;

        app = (App) getApplication();

        builder = new AlertDialog.Builder(this);


        utilityofActivity = new UtilityofActivity(appCompatActivity);
        utilityofActivity.configureToolbar(appCompatActivity);

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle = findViewById(R.id.toolbar_subtitle);
        toolbar_subtitle.setText(App.projectName);

        toolbar_title.setText("Create Labour Deployment");

        no_content = findViewById(R.id.no_content);

        workNameId = getIntent().getStringExtra("workNameId");
        date = getIntent().getStringExtra("date");

        items = findViewById(R.id.item);
        submit = findViewById(R.id.submit);

        items.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(items.getContext(), LinearLayoutManager.VERTICAL);
        items.addItemDecoration(dividerItemDecoration);

        labourAdapter = new FreeLabourAdapter(context, data, listener);
        items.setAdapter(labourAdapter);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newItems.clear();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isUpdated()) {
                        newItems.add(data.get(i));
                    }
                }
                builder.setMessage("Are you sure?").setTitle("Submit");

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    sendRequest(newItems, workNameId);
                                } catch (JSONException e) {

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

    private void sendRequest(List<FreeLabour> items, String pms_project_work_list_id) throws JSONException {
        App app = ((App) getApplication());

        String requestURl = Config.GetLabourDeployment;
        requestURl = requestURl.replace("[0]", App.userId);
        requestURl = requestURl.replace("[1]", App.projectId);

        HashMap<String, String> params = new HashMap<>();
//        params.put("structure_id", structureId);
        params.put("pms_project_work_list_id", pms_project_work_list_id);
        params.put("date", date);
        console.log(requestURl);

        JSONArray array = new JSONArray();
        for (FreeLabour i : items) {
            array.put(new JSONObject().put("labour_type_id", i.getLabour_type_id()).put("labour_count", i.getLabour_selected()));
        }
        params.put("labour", array.toString());

        console.log("Res:" + params);
        app.sendNetworkRequest(requestURl, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                utilityofActivity.dismissProgressDialog();
                Toast.makeText(getApplicationContext(), "Error:" + error, Toast.LENGTH_LONG).show();
            }


            @Override
            public void onNetworkRequestComplete(String response) {
                utilityofActivity.dismissProgressDialog();
                console.log(response);
                if (response.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Request Generated", Toast.LENGTH_SHORT).show();
                    final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm!=null) {
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                    finishAffinity();
                    startActivity(new Intent(context,HomeActivity.class));
                }
            }
        });
    }


    private void refresh() {
        App app = ((App) getApplication());
        data.clear();
        String requestUrl = Config.FreeLabour;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", App.projectId);
        console.log(requestUrl);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
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

                Type vendorType = new TypeToken<ArrayList<FreeLabour>>() {
                }.getType();
                data = new Gson().fromJson(response, vendorType);

                labourAdapter.notifyDataSetChanged();

                labourAdapter = new FreeLabourAdapter(context, data, listener);
                items.setAdapter(labourAdapter);


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
