package buildnlive.com.buildlive.activities;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Machine;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class MachineListForm extends AppCompatActivity {

    private Button submit;
    private ProgressBar progress;
    private boolean val = true;
    private TextView hider, name, loginTime, logoutTime;
    private EditText description, service_time;

    private static String loginTimeText, logoutTimeText, serviceTimeText, descriptionText;

    private boolean LOADING;
    private AlertDialog.Builder builder;
    private Machine selectedItem;
    private Context context;
    private int mHour, mMinute;
    private String mYear, mMonth, mDay, sHour, sMinute;
    private final Calendar c = Calendar.getInstance();

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
        setContentView(R.layout.activity_machine_list_form);

        context = this;
        utilityofActivity = new UtilityofActivity(this);
        utilityofActivity=new UtilityofActivity(appCompatActivity);
        utilityofActivity.configureToolbar(appCompatActivity);

        TextView toolbar_title=findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle=findViewById(R.id.toolbar_subtitle);

        toolbar_subtitle.setText(App.projectName);

        toolbar_title.setText("Job Sheet");

        Bundle bundle = getIntent().getExtras();


        if (bundle != null) {
            selectedItem = (Machine) bundle.getSerializable("Items");
        }


        builder = new AlertDialog.Builder(context);


        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        service_time = findViewById(R.id.service_time);
        loginTime = findViewById(R.id.login_time);
        logoutTime = findViewById(R.id.logout_time);


        name.setText(selectedItem.getName());

        loginTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time

                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                loginTime.setText(hourOfDay + ":" + minute);

                                if (c.get(Calendar.MONTH) < 10) {
                                    mMonth = "0" + c.get(Calendar.MONTH);
                                } else mMonth = "" + c.get(Calendar.MONTH);

                                if (c.get(Calendar.DATE) < 10) {
                                    mDay = "0" + c.get(Calendar.DATE);
                                } else mDay = "" + c.get(Calendar.DATE);

                                if (hourOfDay < 10) {
                                    sHour = "0" + hourOfDay;
                                } else sHour = "" + hourOfDay;

                                if (minute < 10) {
                                    sMinute = "0" + minute;
                                } else sMinute = "" + minute;

                                loginTimeText = c.get(Calendar.YEAR) + "-" + mMonth + "-" + mDay + " " + sHour + ":" + sMinute + ":" + "00";
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });

        logoutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time

                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
//                     c.get(Calendar.SECOND);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                logoutTime.setText(hourOfDay + ":" + minute);

                                if (c.get(Calendar.MONTH) < 10) {
                                    mMonth = "0" + (c.get(Calendar.MONTH) + 1);
                                } else mMonth = "" + (c.get(Calendar.MONTH) + 1);

                                if (c.get(Calendar.DATE) < 10) {
                                    mDay = "0" + c.get(Calendar.DATE);
                                } else mDay = "" + c.get(Calendar.DATE);

                                if (hourOfDay < 10) {
                                    sHour = "0" + hourOfDay;
                                } else sHour = "" + hourOfDay;

                                if (minute < 10) {
                                    sMinute = "0" + minute;
                                } else sMinute = "" + minute;

                                logoutTimeText = c.get(Calendar.YEAR) + "-" + mMonth + "-" + mDay + " " + sHour + ":" + sMinute + ":" + "00";
                            }

                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });


        progress = findViewById(R.id.progress);
        hider = findViewById(R.id.hider);

        if (LOADING) {
            progress.setVisibility(View.VISIBLE);
            hider.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
            hider.setVisibility(View.GONE);
        }


        submit = findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                name=name_edit.getText().toString();

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (validate(loginTime.getText().toString(), logoutTime.getText().toString(), description.getText().toString())) {
                                    console.log("From Validate");
                                    try {
                                        sendRequest(selectedItem.getAsset_id(), selectedItem.getInventory_item_rent_id(), loginTimeText, logoutTimeText
                                                , service_time.getText().toString(), description.getText().toString());
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
                alert.setTitle("Job Sheet");
                alert.show();


            }
        });


    }


    private boolean validate(String loginTimeText, String logoutTimeText, String descriptionText) {
        val = true;


        if (TextUtils.equals(loginTimeText, "Login Time")) {
            Toast.makeText(context, "Select Login Time", Toast.LENGTH_LONG).show();
            val = false;
        }

        if (TextUtils.equals(logoutTimeText, "Logout Time")) {
            Toast.makeText(context, "Select Logout Time", Toast.LENGTH_LONG).show();
            val = false;
        }
        if (TextUtils.isEmpty(descriptionText)) {
            description.setError("Enter Description");
            val = false;
        }

        return val;
    }

    private void sendRequest(String asset_id, String inventory_item_rent_id, String log_in_time, String log_out_time,
                             String service_time, String work_description) throws JSONException {
        App app = ((App) getApplication());
        HashMap<String, String> params = new HashMap<>();
        params.put("asset_jobsheet", App.userId);
//        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("asset_id", asset_id).put("project_id", App.projectId).put("user_id", App.userId)
                .put("inventory_item_rent_id", inventory_item_rent_id).put("log_in_time", log_in_time)
                .put("log_out_time", log_out_time).put("service_time", service_time)
                .put("work_description", work_description);

        params.put("asset_jobsheet", jsonObject.toString());
        console.log("JobSheet " + params);


        app.sendNetworkRequest(Config.SEND_MACHINE_LIST, 1, params, new Interfaces.NetworkInterfaceListener() {
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
                Toast.makeText(context, "Error" + error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                utilityofActivity.dismissProgressDialog();
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                if (response.equals("1")) {
                    Toast.makeText(context, "Request Generated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(context, "Check Your Network", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


}
