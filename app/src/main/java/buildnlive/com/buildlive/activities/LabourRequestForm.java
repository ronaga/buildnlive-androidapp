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
import buildnlive.com.buildlive.adapters.FreeRequestedLabourAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.FreeLabour;
import buildnlive.com.buildlive.elements.LabourRequestFormItem;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class LabourRequestForm extends AppCompatActivity {
    private App app;

    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity = this;

    private Context context;

    private static ArrayList<LabourRequestFormItem.LabourList> data = new ArrayList<>();
    private static LabourRequestFormItem dataForm;
    private TextView no_content,name,date,user,work;

    private Button submit;


    private RecyclerView items;
    private FreeRequestedLabourAdapter labourAdapter;


    private AlertDialog.Builder builder;
    private static String labourRequestId;


    private static ArrayList<LabourRequestFormItem.LabourList> newItems = new ArrayList<>();

    FreeRequestedLabourAdapter.OnItemClickListener listener = new FreeRequestedLabourAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(LabourRequestFormItem.LabourList items, int pos, View view) {

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
        setContentView(R.layout.activity_create_request_form);

        context = this;

        app = (App) getApplication();

        builder = new AlertDialog.Builder(this);


        labourRequestId=getIntent().getStringExtra("labourRequestId");

        utilityofActivity = new UtilityofActivity(appCompatActivity);
        utilityofActivity.configureToolbar(appCompatActivity);

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle = findViewById(R.id.toolbar_subtitle);
        toolbar_subtitle.setText(App.projectName);

        toolbar_title.setText("Request Labour");

        no_content = findViewById(R.id.no_content);

        items = findViewById(R.id.item);
        submit = findViewById(R.id.submit);
        name = findViewById(R.id.name);
        date = findViewById(R.id.date);
        work = findViewById(R.id.work);
        user = findViewById(R.id.user);

        items.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(items.getContext(), LinearLayoutManager.VERTICAL);
        items.addItemDecoration(dividerItemDecoration);

        labourAdapter = new FreeRequestedLabourAdapter(context, data, listener);
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
                                    sendRequest(newItems);
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

    private void sendRequest(List<LabourRequestFormItem.LabourList> items) throws JSONException {
        App app = ((App) getApplication());

        String requestURl = Config.SaveLabourRequest;

        HashMap<String, String> params = new HashMap<>();

        params.put("user_id", App.userId);
        console.log(requestURl);

        JSONArray array = new JSONArray();
        for (LabourRequestFormItem.LabourList i : items) {
            array.put(new JSONObject().put("labour_request_detail_id", i.getLabourRequestDetailId()).put("qty_approved", i.getLabour_selected())
            .put("type_name",i.getTypeName()));
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
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                    finishAffinity();
                    startActivity(new Intent(context, HomeActivity.class));
                }
            }
        });
    }


    private void refresh() {
        App app = ((App) getApplication());
        data.clear();
        String requestUrl = Config.LabourDeployementRequestView;
        requestUrl = requestUrl.replace("[0]",labourRequestId);
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

                Type vendorType = new TypeToken<LabourRequestFormItem>() {
                }.getType();
                dataForm = new Gson().fromJson(response, vendorType);

                name.setText(dataForm.getName());
                work.setText(dataForm.getWorkName());
                date.setText(dataForm.getDate());
                user.setText(dataForm.getUserName());


                data=dataForm.getLabourList();
                labourAdapter.notifyDataSetChanged();

                labourAdapter = new FreeRequestedLabourAdapter(context, data, listener);
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
