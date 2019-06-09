package buildnlive.com.buildlive.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;

import java.util.HashMap;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class CreateLabour extends AppCompatActivity {
    private EditText name, mobile, referenceNo;
    private Spinner type;
    private Button submit, add;
    private AlertDialog.Builder builder;
    private Boolean val = true;
    private Context context;
    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity=this;


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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_labour);


        utilityofActivity=new UtilityofActivity(appCompatActivity);
        utilityofActivity.configureToolbar(appCompatActivity);

        TextView toolbar_title=findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle=findViewById(R.id.toolbar_subtitle);
        toolbar_subtitle.setText(App.projectName);
        toolbar_title.setText("Create Labour");

        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        type = findViewById(R.id.type);
        submit = findViewById(R.id.submit);
        add = findViewById(R.id.findbyref);
        referenceNo = findViewById(R.id.reference_no);
        submit = findViewById(R.id.submit);


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

                                if (validate(name.getText().toString(), mobile.getText().toString(), type.getSelectedItem().toString())) {
                                    try {
                                        sendRequest(name.getText().toString(), mobile.getText().toString(), type.getSelectedItem().toString());
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

    private boolean validate(String name_text, String mobile_text, String type) {

        if (TextUtils.equals(type, "Select Labour Type")) {
            Toast.makeText(getApplicationContext(), "Please Select Type", Toast.LENGTH_LONG).show();
            val = false;
        }


        if (TextUtils.isEmpty(name_text)) {
            name.setError("Enter Name");
            val = false;
        }

        if (TextUtils.isEmpty(mobile_text)) {
            mobile.setError("Enter Mobile");
            val = false;
        }
//        if(TextUtils.isEmpty(to)){
//            to_edit.setError("Enter Payee");
//            val=false;
//        }
        return val;
    }


    private void sendRequest(String name, String mobile, String type) throws JSONException {
        App app = ((App) getApplication());
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", App.userId);
        params.put("project_id", App.projectId);
        params.put("labour_name", name);
        params.put("labour_mobile", mobile);
        params.put("labour_type", type);
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
                }
                else if (response.equals("-1")) {
                    Toast.makeText(getApplicationContext(), "Invalid code, Please Enter Correct Code", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    builder.setTitle("Labour Created");

                    //Setting message manually and performing action on button click
                    builder.setMessage(response+" Added Successfully")
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
