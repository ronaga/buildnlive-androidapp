package buildnlive.com.buildlive.activities;

import android.app.AlertDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.utils.Config;

public class CreateActivity extends AppCompatActivity {
    private EditText name,duration,quantity;
    private Spinner units,duration_type,status;
    private Button submit;
    private AlertDialog.Builder builder;
    private ProgressBar progress;
    private TextView hider;
    private Boolean val=true;
    private String masterWorkId;
    private String workListId;

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
        setContentView(R.layout.activity_create_activity);

        Bundle bundle=getIntent().getExtras();

        if (bundle != null) {
            workListId= bundle.getString("workListId");
            masterWorkId=  bundle.getString("masterWorkId");
        }

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView toolbar_title=findViewById(R.id.toolbar_title);
        toolbar_title.setText("Create Labour");

        name=findViewById(R.id.name);
        duration=findViewById(R.id.duration);
        quantity=findViewById(R.id.quantity);
        duration_type=findViewById(R.id.duration_type);
        status=findViewById(R.id.status);
        units=findViewById(R.id.units);


        submit=findViewById(R.id.submit);
        progress=findViewById(R.id.progress);
        hider=findViewById(R.id.hider);

        builder = new AlertDialog.Builder(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Are you sure?") .setTitle("Create Activity");

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (validate(name.getText().toString(),masterWorkId,units.getSelectedItem().toString(),workListId,
                                        duration.getText().toString(),duration_type.getSelectedItem().toString(),
                                        quantity.getText().toString(),status.getSelectedItem().toString())) {
                                    try {
                                        sendRequest(name.getText().toString(),masterWorkId,units.getSelectedItem().toString(),workListId,
                                                duration.getText().toString(),duration_type.getSelectedItem().toString(),
                                                quantity.getText().toString(),status.getSelectedItem().toString());
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
                alert.setTitle("Create Activity");
                alert.show();

            }
        });


    }
    private boolean validate(String name_text, String masterWorkId, String units,String workListId,String duration_text,String duration_type,
    String qty,String status)
    {
        if(TextUtils.equals(units,"Select Unit")){
            Toast.makeText(getApplicationContext(),"Please Select Unit",Toast.LENGTH_LONG).show();
            val=false;
        }
        else if(TextUtils.equals(duration_type,"Select Duration Type")){
            Toast.makeText(getApplicationContext(),"Please Select Duration Type",Toast.LENGTH_LONG).show();
            val=false;
        }

        else if(TextUtils.equals(status,"Select Status")){
            Toast.makeText(getApplicationContext(),"Please Select Status",Toast.LENGTH_LONG).show();
            val=false;
        }



        else if(TextUtils.isEmpty(name_text)){
            name.setError("Enter Name");
            val=false;
        }

        else if(TextUtils.isEmpty(duration_text)){
            duration.setError("Enter Duration");
            val=false;
        }
        else if(TextUtils.isEmpty(qty)){
            quantity.setError("Enter Quantity");
            val=false;
        }
        else val=true;

        return val;
    }


    private void sendRequest(String name, String masterWorkId, String units,String workListId,String duration,String duration_type,
                             String qty,String status) throws JSONException {
        App app= ((App) getApplication());
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", App.userId);
        params.put("project_id", App.projectId);
        params.put("name", name);
        params.put("master_work_id", masterWorkId);
        params.put("units", units);
        params.put("project_work_list_id", workListId);
        params.put("duration", duration);
        params.put("duration_type", duration_type);
        params.put("qty", qty);
        params.put("status", status);
        console.log("Res:" + params);
        app.sendNetworkRequest(Config.CREATE_ACTIVITY, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Something went wrong, Try again later", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.log(response);
                if (response.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Request Generated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }



}
