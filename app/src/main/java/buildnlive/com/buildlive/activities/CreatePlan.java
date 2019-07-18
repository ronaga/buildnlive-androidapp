package buildnlive.com.buildlive.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.FreeLabourAdapter;
import buildnlive.com.buildlive.adapters.StructureSpinAdapter;
import buildnlive.com.buildlive.adapters.WorkListSpinAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.FreeLabour;
import buildnlive.com.buildlive.elements.Structure;
import buildnlive.com.buildlive.elements.WorkList;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class CreatePlan extends AppCompatActivity {
    private App app;

    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity = this;

    private WorkListSpinAdapter workListSpinAdapter;

    private StructureSpinAdapter structureSpinAdapter;
    private ArrayList<Structure> structureList = new ArrayList<>();
    private Spinner structureSpinner, workListSpinner;
    private Context context;
    private String structureId, workListId, selectedQuantity;


    private TextView no_content, date, available,units;
    private ArrayList<WorkList> workList = new ArrayList<>();


    private EditText quantity;
    private Button submit;


    private RecyclerView items;
    private FreeLabourAdapter labourAdapter;


    private AlertDialog.Builder builder;


    private int mYear, mMonth, mDay, mHour, mMinute;

    private static ArrayList<FreeLabour> newItems = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        setStructureSpinner();

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        context = this;

        app = (App) getApplication();

        builder = new AlertDialog.Builder(this);


        utilityofActivity = new UtilityofActivity(appCompatActivity);
        utilityofActivity.configureToolbar(appCompatActivity);

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle = findViewById(R.id.toolbar_subtitle);
        toolbar_subtitle.setText(App.projectName);

        toolbar_title.setText("Create Plan");

        no_content = findViewById(R.id.no_content);
        quantity = findViewById(R.id.quantity);
        date = findViewById(R.id.date);
        available = findViewById(R.id.available);
        units = findViewById(R.id.units);



        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        submit = findViewById(R.id.submit);


        structureSpinner = findViewById(R.id.structure);
        structureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                structureId = structureSpinAdapter.getStructureId(i);
                setWorkListSpinner(structureId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        structureSpinAdapter = new StructureSpinAdapter(context, R.layout.custom_spinner, structureList);
        structureSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        structureSpinner.setAdapter(structureSpinAdapter);

        workListSpinner = findViewById(R.id.worklist);
        workListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                workListId = workListSpinAdapter.getId(i);
                selectedQuantity = workListSpinAdapter.getQuantity(i);
                available.setText("Left: "+selectedQuantity);

                units.setText("Unit: "+workListSpinAdapter.getUnits(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        workListSpinAdapter = new WorkListSpinAdapter(context, R.layout.custom_spinner, workList);
        workListSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workListSpinner.setAdapter(workListSpinAdapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("Are you sure?").setTitle("Submit");

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    if (Integer.parseInt(selectedQuantity) >= Integer.parseInt(quantity.getText().toString()))
                                        sendRequest(workListId, quantity.getText().toString(), date.getText().toString());
                                    else utilityofActivity.toast("Enter Corrected Quantity");
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

    private void sendRequest(String pms_project_work_list_id, String qty, String date) throws JSONException {
        App app = ((App) getApplication());

        String requestURl = Config.GetDailyPlanning;

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", App.userId);
        params.put("project_id", App.projectId);
        params.put("structure_id", structureId);
        params.put("pms_project_work_list_id", pms_project_work_list_id);
        params.put("qty", qty);
        params.put("date", date);

        console.log(requestURl);


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
                    finish();
                }
            }
        });
    }


    private void setStructureSpinner() {

        String requestURl = Config.SendStructures;
        requestURl = requestURl.replace("[0]", App.userId);
        requestURl = requestURl.replace("[1]", App.projectId);

        structureList.clear();

        console.log(requestURl);
        app.sendNetworkRequest(requestURl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                utilityofActivity.showProgressDialog();
                console.error("Network request failed with error :" + error);
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                utilityofActivity.dismissProgressDialog();

                Type vendorType = new TypeToken<ArrayList<Structure>>() {
                }.getType();
                structureList = new Gson().fromJson(response, vendorType);

                structureSpinAdapter.notifyDataSetChanged();

                structureSpinAdapter = new StructureSpinAdapter(context, R.layout.custom_spinner, structureList);
                structureSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                structureSpinner.setAdapter(structureSpinAdapter);


            }
        });
    }


    private void setWorkListSpinner(String structureId) {

        String requestURl = Config.SendWorkList;
        requestURl = requestURl.replace("[0]", App.userId);
        requestURl = requestURl.replace("[1]", App.projectId);
        requestURl = requestURl.replace("[2]", structureId);

        workList.clear();

        console.log(requestURl);
        app.sendNetworkRequest(requestURl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                utilityofActivity.showProgressDialog();
                console.error("Network request failed with error :" + error);
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                utilityofActivity.dismissProgressDialog();

                Type vendorType = new TypeToken<ArrayList<WorkList>>() {
                }.getType();
                workList = new Gson().fromJson(response, vendorType);


                workListSpinAdapter.notifyDataSetChanged();

                workListSpinAdapter = new WorkListSpinAdapter(context, R.layout.custom_spinner, workList);
                workListSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                workListSpinner.setAdapter(workListSpinAdapter);




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
