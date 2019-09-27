package buildnlive.com.buildlive.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.AreaListSpinAdapter;
import buildnlive.com.buildlive.adapters.ShowWorkListSpinnerAdapter;
import buildnlive.com.buildlive.adapters.StructureSpinAdapter;
import buildnlive.com.buildlive.adapters.WorkListNameSpinnerAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Area;
import buildnlive.com.buildlive.elements.FreeLabour;
import buildnlive.com.buildlive.elements.ShowWorkListItem;
import buildnlive.com.buildlive.elements.Structure;
import buildnlive.com.buildlive.elements.WorkNameItem;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class CreateLabourDeploy extends AppCompatActivity {
    private App app;

    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity = this;

    private Context context;

    private String structureId, workListId, areaName, workName;

    private static ArrayList<FreeLabour> data = new ArrayList<>();
    private TextView no_content, date;

    private Button submit;

    private AlertDialog.Builder builder;

    private ShowWorkListSpinnerAdapter workListSpinAdapter;
    private StructureSpinAdapter structureSpinAdapter;
    private AreaListSpinAdapter areaListSpinAdapter;
    private WorkListNameSpinnerAdapter workListNameSpinAdapter;

    private ArrayList<Structure> structureList = new ArrayList<>();
    private ArrayList<ShowWorkListItem> workList = new ArrayList<>();
    private ArrayList<Area> areaList = new ArrayList<>();
    private ArrayList<WorkNameItem> workNameList = new ArrayList<>();

    private Spinner structureSpinner, workListSpinner, areaSpinner, workSpinner;


    private int mYear, mMonth, mDay;

    @Override
    protected void onStart() {
        super.onStart();
        setStructureSpinner();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        data.clear();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deploy_labour);

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

        submit = findViewById(R.id.submit);
        date = findViewById(R.id.date);
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


        structureSpinner = findViewById(R.id.structure);
        structureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if(i>0)
//                {
                structureId = structureSpinAdapter.getStructureId(i);
                setWorkListSpinner(structureId);
//                }
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
//                if(i>0) {
                workListId = workListSpinAdapter.getId(i);
                setAreaSpinner(workListId);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        workListSpinAdapter = new ShowWorkListSpinnerAdapter(context, R.layout.custom_spinner, workList);
        workListSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workListSpinner.setAdapter(workListSpinAdapter);

        areaSpinner = findViewById(R.id.arealist);
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if(i>0) {
                areaName = areaListSpinAdapter.getName(i);
                setWorkSpinner(workListId, areaName);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        areaListSpinAdapter = new AreaListSpinAdapter(context, R.layout.custom_spinner, areaList);
        areaListSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(areaListSpinAdapter);

        workSpinner = findViewById(R.id.work);

        workSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if(i>0) {
                workName = workListNameSpinAdapter.getId(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        workListNameSpinAdapter = new WorkListNameSpinnerAdapter(context, R.layout.custom_spinner, workNameList);
        workListNameSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workSpinner.setAdapter(workListNameSpinAdapter);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!workName.isEmpty()) {
                    if(!(date.getText().toString().equals("Date"))){
                        Intent intent = new Intent(context, CreateLabourDeployList.class);
                        intent.putExtra("workNameId", workName);
                        intent.putExtra("date", date.getText().toString());
                        startActivity(intent);
                    }
                    else
                    {
                        utilityofActivity.toast("Please Select Date");
                    }
                } else {
                    utilityofActivity.toast("Please Select Work");
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
                utilityofActivity.dismissProgressDialog();
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

        String requestURl = Config.ShowWorkList;
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
                utilityofActivity.dismissProgressDialog();
                console.error("Network request failed with error :" + error);
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                utilityofActivity.dismissProgressDialog();

                Type vendorType = new TypeToken<ArrayList<ShowWorkListItem>>() {
                }.getType();
                workList = new Gson().fromJson(response, vendorType);


                workListSpinAdapter = new ShowWorkListSpinnerAdapter(context, R.layout.custom_spinner, workList);
                workListSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                workListSpinner.setAdapter(workListSpinAdapter);


            }
        });
    }

    private void setAreaSpinner(String projectListId) {

        String requestURl = Config.Arealist;
        requestURl = requestURl.replace("[0]", projectListId);

        areaList.clear();

        console.log(requestURl);
        app.sendNetworkRequest(requestURl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
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

                Type vendorType = new TypeToken<ArrayList<Area>>() {
                }.getType();
                areaList = new Gson().fromJson(response, vendorType);


                areaListSpinAdapter = new AreaListSpinAdapter(context, R.layout.custom_spinner, areaList);
                areaListSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner.setAdapter(areaListSpinAdapter);


            }
        });
    }

    private void setWorkSpinner(String projectListId, String areaName) {

        String requestURl = Config.ShowWorkListName;

        HashMap<String, String> params = new HashMap<>();
        params.put("pms_project_work_list", projectListId);
        params.put("area", areaName);
        Log.e("Area", areaName);
        Log.e("AreaId", projectListId);

        workNameList.clear();

        console.log(requestURl);
        app.sendNetworkRequest(requestURl, Request.Method.POST, params, new Interfaces.NetworkInterfaceListener() {
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

                Type vendorType = new TypeToken<ArrayList<WorkNameItem>>() {
                }.getType();
                workNameList = new Gson().fromJson(response, vendorType);


                workListNameSpinAdapter = new WorkListNameSpinnerAdapter(context, R.layout.custom_spinner, workNameList);
                workListNameSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                workSpinner.setAdapter(workListNameSpinAdapter);


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
