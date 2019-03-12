package buildnlive.com.buildlive.activities;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.BuildConfig;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.ActivityImagesAdapter;
import buildnlive.com.buildlive.adapters.CategorySpinAdapter;
import buildnlive.com.buildlive.adapters.ItemSpinAdapter;
import buildnlive.com.buildlive.adapters.SingleImageAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Category;
import buildnlive.com.buildlive.elements.IndentItem;
import buildnlive.com.buildlive.elements.Item;
import buildnlive.com.buildlive.elements.Machine;
import buildnlive.com.buildlive.elements.Packet;
import buildnlive.com.buildlive.utils.AdvancedRecyclerView;
import buildnlive.com.buildlive.utils.Config;

public class MachineListForm extends AppCompatActivity {

    private Button submit;
    private ProgressBar progress;
    private boolean val=true;
    private TextView hider,name,loginTime,logoutTime;
    private EditText description,service_time;

    private static String loginTimeText,logoutTimeText,serviceTimeText,descriptionText;

    private boolean LOADING;
    private AlertDialog.Builder builder;
    private Machine selectedItem;
    private Context context;
    private int  mHour, mMinute;
    private String mYear, mMonth, mDay,sHour,sMinute;
    private final Calendar c = Calendar.getInstance();

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

        context=this;

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView textView=findViewById(R.id.toolbar_title);
        textView.setText("Job Sheet");
        Bundle bundle=getIntent().getExtras();


        if(bundle!=null){
            selectedItem= (Machine) bundle.getSerializable("Items");
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

                                if(c.get(Calendar.MONTH)<10)
                                {
                                    mMonth="0"+c.get(Calendar.MONTH);
                                }
                                else mMonth=""+c.get(Calendar.MONTH);

                                if(c.get(Calendar.DATE)<10)
                                {
                                    mDay="0"+c.get(Calendar.DATE);
                                }
                                else mDay=""+c.get(Calendar.DATE);

                                if(hourOfDay<10)
                                {
                                    sHour="0"+hourOfDay;
                                }
                                else sHour=""+hourOfDay;

                                if(minute<10)
                                {
                                    sMinute="0"+minute;
                                }
                                else sMinute=""+minute;

                                loginTimeText=c.get(Calendar.YEAR)+"-"+mMonth+"-"+mDay+" "+sHour+":"+sMinute+":"+"00";
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

                                if(c.get(Calendar.MONTH)<10)
                                {
                                    mMonth="0"+(c.get(Calendar.MONTH)+1);
                                }
                                else mMonth=""+(c.get(Calendar.MONTH)+1);

                                if(c.get(Calendar.DATE)<10)
                                {
                                    mDay="0"+c.get(Calendar.DATE);
                                }
                                else mDay=""+c.get(Calendar.DATE);

                                if(hourOfDay<10)
                                {
                                    sHour="0"+hourOfDay;
                                }
                                else sHour=""+hourOfDay;

                                if(minute<10)
                                {
                                    sMinute="0"+minute;
                                }
                                else sMinute=""+minute;

                                logoutTimeText=c.get(Calendar.YEAR)+"-"+mMonth+"-"+mDay+" "+sHour+":"+sMinute+":"+"00";
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



        submit=findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                name=name_edit.getText().toString();

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(validate(loginTime.getText().toString(),logoutTime.getText().toString(),description.getText().toString()))
                                {
                                    console.log("From Validate");
                                    try {
                                        sendRequest(selectedItem.getAsset_id(),selectedItem.getInventory_item_rent_id(),loginTimeText,logoutTimeText
                                        ,service_time.getText().toString(),description.getText().toString());
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



    private boolean validate(String loginTimeText,String logoutTimeText,String descriptionText)
    {
        val=true;


        if(TextUtils.equals(loginTimeText,"Login Time")){
            Toast.makeText(context,"Select Login Time",Toast.LENGTH_LONG).show();
            val=false;
        }

        if(TextUtils.equals(logoutTimeText,"Logout Time")){
            Toast.makeText(context,"Select Logout Time",Toast.LENGTH_LONG).show();
            val=false;
        }
        if(TextUtils.isEmpty(descriptionText)){
            description.setError("Enter Description");
            val=false;
        }

        return val;
    }

    private void sendRequest(String asset_id,String inventory_item_rent_id,String log_in_time,String log_out_time,
                             String service_time,String work_description) throws JSONException {
        App app= ((App)getApplication());
        HashMap<String, String> params = new HashMap<>();
        params.put("asset_jobsheet", App.userId);
//        JSONArray array = new JSONArray();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("asset_id", asset_id).put("project_id", App.projectId).put("user_id", App.userId)
                .put("inventory_item_rent_id",inventory_item_rent_id).put("log_in_time",log_in_time)
                .put("log_out_time",log_out_time).put("service_time",service_time)
                .put("work_description",work_description);

        params.put("asset_jobsheet", jsonObject.toString());
        console.log("JobSheet "+params);


        app.sendNetworkRequest(Config.SEND_MACHINE_LIST, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);;
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                Toast.makeText(context,"Error"+error,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                if(response.equals("1")) {
                    Toast.makeText(context, "Request Generated", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(context, "Check Your Network", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }







}
