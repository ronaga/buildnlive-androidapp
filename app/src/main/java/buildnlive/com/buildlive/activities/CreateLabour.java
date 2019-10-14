package buildnlive.com.buildlive.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.LabourTradeSpinAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.LabourTrade;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class CreateLabour extends AppCompatActivity {
    private EditText name, mobile, referenceNo, vendorName;
    private Spinner skill, workHours, gender, type, trade;
    private Button submit, add;
    private AlertDialog.Builder builder;
    private Boolean val = true;
    private Context context;
    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity = this;
    private LabourTradeSpinAdapter labourTradeAdapter, labourTypeAdapter;
    private ArrayList<LabourTrade> labourTradeList = new ArrayList<>();
    private ArrayList<LabourTrade> labourTypeList = new ArrayList<>();
    private String labourTradeName = "0";
    private String labourTypeName = "0";


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

    @Override
    protected void onStart() {
        super.onStart();
        try {

            getLabourType();
            getLabourTrade();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_labour);

        context = this;

        utilityofActivity = new UtilityofActivity(appCompatActivity);
        utilityofActivity.configureToolbar(appCompatActivity);

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle = findViewById(R.id.toolbar_subtitle);
        toolbar_subtitle.setText(App.projectName);
        toolbar_title.setText("Create Labour");

        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        skill = findViewById(R.id.skill);
        workHours = findViewById(R.id.workingHours);
        gender = findViewById(R.id.gender);
        vendorName = findViewById(R.id.vendorName);

        type = findViewById(R.id.type);
        trade = findViewById(R.id.trade);

        submit = findViewById(R.id.submit);
        add = findViewById(R.id.findbyref);
        referenceNo = findViewById(R.id.reference_no);
        submit = findViewById(R.id.submit);


        labourTradeAdapter = new LabourTradeSpinAdapter(context, R.layout.custom_spinner, labourTradeList);
        trade.setAdapter(labourTradeAdapter);

        trade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                labourTradeName = labourTradeAdapter.getLabourTrade_id(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                labourTradeName = "0";
            }
        });

        labourTypeAdapter = new LabourTradeSpinAdapter(context, R.layout.custom_spinner, labourTypeList);
        type.setAdapter(labourTypeAdapter);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                labourTypeName = labourTypeAdapter.getName(i);

                if (labourTypeName.equals("PRW")||labourTypeName.equals("PC")) {
                    vendorName.setVisibility(View.VISIBLE);
                } else {
                    vendorName.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                labourTypeName = "0";
            }
        });


        builder = new AlertDialog.Builder(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Are you sure?").setTitle("Create Labour");

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (validate(name.getText().toString(), skill.getSelectedItem().toString(), gender.getSelectedItem().toString(), workHours.getSelectedItem().toString(), labourTypeName)) {
                                    try {
                                        sendRequest(name.getText().toString(), "", skill.getSelectedItem().toString(), gender.getSelectedItem().toString(), workHours.getSelectedItem().toString(), labourTradeName, labourTypeName, vendorName.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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
                alert.setTitle("Create Labour");
                alert.show();

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Are you sure?").setTitle("Create Labour");

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (!referenceNo.getText().toString().equals("")) {
                                    try {
                                        findByRefLabour(referenceNo.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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
                alert.setTitle("Create Labour");
                alert.show();

            }
        });


    }

    private boolean validate(String name_text, String skill, String gender, String workHours, String type) {

        val = true;

        if (TextUtils.equals(skill, "Select Skill Level")) {
            Toast.makeText(getApplicationContext(), "Please Select Skill Level", Toast.LENGTH_LONG).show();
            val = false;
        }
        if (TextUtils.equals(gender, "Select Gender")) {
            Toast.makeText(getApplicationContext(), "Please Select Gender", Toast.LENGTH_LONG).show();
            val = false;
        }
        if (TextUtils.equals(workHours, "Select Work Hours")) {
            Toast.makeText(getApplicationContext(), "Please Select Work Hours", Toast.LENGTH_LONG).show();
            val = false;
        }

        if (TextUtils.equals(type, "PRW") && (vendorName.getText().toString().isEmpty() || vendorName.getText().toString().equals(""))) {
            vendorName.setError("Enter Vendor Name");
            val = false;
        }


        if (TextUtils.isEmpty(name_text)) {
            name.setError("Enter Name");
            val = false;
        }
/*
        if (TextUtils.isEmpty(mobile_text)) {
            mobile.setError("Enter Mobile");
            val = false;
        }*/
//        if(TextUtils.isEmpty(to)){
//            to_edit.setError("Enter Payee");
//            val=false;
//        }
        console.log("BOOL" + val.toString());
        console.log("TYPE" + type);
        console.log("VENDOR NAME" + vendorName.getText().toString());
        return val;
    }


    private void sendRequest(String name, String mobile, String skill, String gender, String workHours, String labourTradeName, String labourTypeName, String vendorName) throws JSONException {
        App app = ((App) getApplication());
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", App.userId);
        params.put("project_id", App.projectId);
        params.put("labour_name", name);
        params.put("labour_mobile", mobile);
        params.put("labour_gender", gender);
        params.put("labour_working_hrs", workHours);
        params.put("labour_skill", skill);
        params.put("labour_trade", labourTradeName);
        params.put("labour_type", labourTypeName);
        params.put("vendor_name", vendorName);
        console.log("Res:" + params);

        app.sendNetworkRequest(Config.CREATE_LABOUR, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                utilityofActivity.dismissProgressDialog();
                Toast.makeText(getApplicationContext(), "Something went wrong, Try again later", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                utilityofActivity.dismissProgressDialog();
                console.log(response);
                if (response.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Request Generated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void getLabourTrade() throws JSONException {

        App app = ((App) getApplication());

        String url = Config.LABOUR_TRADE;

        url = url.replace("[0]", App.userId);
        url = url.replace("[1]", App.projectId);


        app.sendNetworkRequest(url, 1, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                utilityofActivity.dismissProgressDialog();
                Toast.makeText(context, "Something went wrong, Try again later", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                utilityofActivity.dismissProgressDialog();
                console.log("LABOUR TRADE" + response);

                try {
                    labourTradeList.clear();

                    Type labourTradeType = new TypeToken<ArrayList<LabourTrade>>() {
                    }.getType();
                    labourTradeList = new Gson().fromJson(response, labourTradeType);
                    labourTradeAdapter = new LabourTradeSpinAdapter(context, R.layout.custom_spinner, labourTradeList);
                    trade.setAdapter(labourTradeAdapter);


                    labourTradeAdapter.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void getLabourType() throws JSONException {

        App app = ((App) getApplication());

        String url = Config.LABOUR_TYPE;

        url = url.replace("[0]", App.userId);
        url = url.replace("[1]", App.projectId);


        app.sendNetworkRequest(url, 1, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                utilityofActivity.dismissProgressDialog();
                Toast.makeText(context, "Something went wrong, Try again later", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                utilityofActivity.dismissProgressDialog();
                console.log("LABOUR TYPE" + response);

                try {
                    labourTypeList.clear();

                    Type labourTradeType = new TypeToken<ArrayList<LabourTrade>>() {
                    }.getType();
                    labourTypeList = new Gson().fromJson(response, labourTradeType);
                    labourTypeAdapter = new LabourTradeSpinAdapter(context, R.layout.custom_spinner, labourTypeList);
                    type.setAdapter(labourTypeAdapter);


                    labourTypeAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void findByRefLabour(String refNo) throws JSONException {
        App app = ((App) getApplication());
        String requestUrl = Config.UPDATE_LABOUR_PROJECT;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", App.projectId);
        requestUrl = requestUrl.replace("[2]", refNo);

//        HashMap<String, String> params = new HashMap<>();
//        params.put("user_id", App.userId);
//        params.put("project_id", App.projectId);
//        params.put("code", refNo);
        console.log("Res: " + requestUrl);

        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                utilityofActivity.dismissProgressDialog();
                Toast.makeText(getApplicationContext(), "Something went wrong, Try again later", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                utilityofActivity.dismissProgressDialog();
                console.log(response);
                if (response.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Labour already associated with this project", Toast.LENGTH_SHORT).show();
                } else if (response.equals("-1")) {
                    Toast.makeText(getApplicationContext(), "Invalid code, Please Enter Correct Code", Toast.LENGTH_SHORT).show();
                } else {

                    builder.setTitle("Labour Created");

                    //Setting message manually and performing action on button click
                    builder.setMessage(response + " Added Successfully")
                            .setCancelable(false);

                    //Creating dialog box
                    AlertDialog alert = builder.create();

                    //Setting the title manually
                    alert.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alert.dismiss();
                            finish();
                        }
                    });
                    alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alert.dismiss();
                        }
                    });

                    alert.show();

                }
            }
        });
    }


}
