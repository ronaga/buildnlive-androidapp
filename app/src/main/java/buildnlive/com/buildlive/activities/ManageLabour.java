package buildnlive.com.buildlive.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.LabourAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.LabourModel;
import buildnlive.com.buildlive.fragments.ManageLabourFragment;
import buildnlive.com.buildlive.utils.Config;

public class ManageLabour extends AppCompatActivity {
    private TextView name;
//    private TextView name1,name2,name3,name4,name5,name6,name7,name8,name9,name10,name11;
//    private EditText quantity1,quantity2,quantity3,quantity4,quantity5,quantity6,quantity7,quantity8,quantity9,quantity10,quantity11;

    private RecyclerView items;
    private LabourAdapter labourAdapter;
    private Button submit;
    private AlertDialog.Builder builder;
    private String user,quantity;
    private static ArrayList<LabourModel> data=new ArrayList<>();
    private static ArrayList<LabourModel> newItems=new ArrayList<>();
    private int mYear, mMonth, mDay;
    private Calendar c;
    private Context context;
    private ProgressBar progress;
    private TextView hider,no_content;
    private CoordinatorLayout coordinatorLayout;

    LabourAdapter.OnItemClickListener listener= new LabourAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(LabourModel items, int pos, View view) {

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
        setContentView(R.layout.activity_manage_labour);
        context=this;
        progress=findViewById(R.id.progress);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        hider=findViewById(R.id.hider);

        name=findViewById(R.id.name);
        builder= new AlertDialog.Builder(this);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        no_content=findViewById(R.id.no_content);
        TextView toolbar_title=findViewById(R.id.toolbar_title);
        toolbar_title.setText("Request Labour");
//        date_button=findViewById(R.id.date_button);
//        date=findViewById(R.id.date);
//        date.setEnabled(false);
//        date_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                c = Calendar.getInstance();
//                mYear = c.get(Calendar.YEAR);
//                mMonth = c.get(Calendar.MONTH);
//                mDay = c.get(Calendar.DAY_OF_MONTH);
//                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, final int year,
//                                                  final int monthOfYear, final int dayOfMonth) {
//                                date_button.setText(year+"/"+monthOfYear+"/"+dayOfMonth);
//                            }
//                        }, mYear, mMonth, mDay);
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                datePickerDialog.show();
//
//            }
//        });
//        name1=findViewById(R.id.name1);
//        name2=findViewById(R.id.name2);
//        name3=findViewById(R.id.name3);
//        name4=findViewById(R.id.name4);
//        name5=findViewById(R.id.name5);
//        name6=findViewById(R.id.name6);
//        name7=findViewById(R.id.name7);
//        name8=findViewById(R.id.name8);
//        name9=findViewById(R.id.name9);
//        name10=findViewById(R.id.name10);
//        name11=findViewById(R.id.name11);
//
//
//        quantity1=findViewById(R.id.quantity);
//        quantity2=findViewById(R.id.quantity1);
//        quantity3=findViewById(R.id.quantity2);
//        quantity4=findViewById(R.id.quantity3);
//        quantity5=findViewById(R.id.quantity4);
//        quantity6=findViewById(R.id.quantity5);
//        quantity7=findViewById(R.id.quantity6);
//        quantity8=findViewById(R.id.quantity7);
//        quantity9=findViewById(R.id.quantity8);
//        quantity10=findViewById(R.id.quantity9);
//        quantity11=findViewById(R.id.quantity10);

        items=findViewById(R.id.item);
        submit=findViewById(R.id.submit);
//        data.clear();
//        data.add(new LabourModel("Male Helper",""));
//        data.add(new LabourModel("Female Helper",""));
//        data.add(new LabourModel("Mason",""));
//        data.add(new LabourModel("Fitter",""));
//        data.add(new LabourModel("Carpenter",""));
//        data.add(new LabourModel("Welder",""));
//        data.add(new LabourModel("Plumber",""));
//        data.add(new LabourModel("Electrician",""));
//        data.add(new LabourModel("Operator",""));
//        data.add(new LabourModel("Help",""));
//        data.add(new LabourModel("Others",""));

        name.setText(ManageLabourFragment.name_s);
        name.setEnabled(false);

        items.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(items.getContext(),LinearLayoutManager.VERTICAL);
        items.addItemDecoration(dividerItemDecoration);

        labourAdapter=new LabourAdapter(context,data,listener);
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
                builder.setMessage("Are you sure?") .setTitle("Submit");

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

//    private void prepareData(){
//        data.add(new LabourModel(name1.getText().toString(),quantity1.getText().toString()));
//
//        data.add(new LabourModel(name2.getText().toString(),quantity2.getText().toString()));
//
//        data.add(new LabourModel(name3.getText().toString(),quantity3.getText().toString()));
//
//        data.add(new LabourModel(name4.getText().toString(),quantity4.getText().toString()));
//
//        data.add(new LabourModel(name5.getText().toString(),quantity5.getText().toString()));
//
//        data.add(new LabourModel(name6.getText().toString(),quantity6.getText().toString()));
//
//        data.add(new LabourModel(name7.getText().toString(),quantity7.getText().toString()));
//
//        data.add(new LabourModel(name8.getText().toString(),quantity8.getText().toString()));
//
//        data.add(new LabourModel(name9.getText().toString(),quantity9.getText().toString()));
//
//        data.add(new LabourModel(name10.getText().toString(),quantity10.getText().toString()));
//
//        data.add(new LabourModel(name11.getText().toString(),quantity11.getText().toString()));
//    }

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

    private void sendRequest(List<LabourModel> items) throws JSONException {
        App app = ((App) getApplication());
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", App.userId);
        params.put("project_id",App.projectId);
        params.put("vendor_id",ManageLabourFragment.user_id_s);
//        params.put("date",date);
        JSONArray array = new JSONArray();
        for (LabourModel i : items) {
            array.put(new JSONObject().put("labour_type", i.getName()).put("labour_count", i.getQuantity()));
        }
        params.put("labour", array.toString());
        console.log("Res:" + params);
        app.sendNetworkRequest(Config.GET_LABOUR_PROGRESS, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
//                Toast.makeText(getApplicationContext(),"Error:"+error,Toast.LENGTH_LONG).show();
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Something went wrong, Try again later", Snackbar.LENGTH_LONG);
                snackbar.show();
            }


            @Override
            public void onNetworkRequestComplete(String response) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.log(response);
                if (response.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Request Generated", Toast.LENGTH_SHORT).show();
//                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Request Generated", Snackbar.LENGTH_LONG);
//                    snackbar.show();
//                    AddItemAdapter.ViewHolder.CHECKOUT=false;
                    finish();
                }
            }
        });
    }



    private void refresh() {
        App app= ((App) getApplication());
        data.clear();
        String requestUrl = Config.LABOUR_TYPE_LIST;
        requestUrl = requestUrl.replace("[0]", App.userId);
        console.log(requestUrl);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {

                console.error("Network request failed with error :" + error);
//                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
                Snackbar snackbar= Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        data.add(new LabourModel().parseFromJSON(array.getJSONObject(i)));
                    }
                    console.log("data set changed");
                    if(data.isEmpty()){
                        no_content.setVisibility(View.VISIBLE);
                        no_content.setText("No Labour");
                    }
                    else
                    {
                        no_content.setVisibility(View.GONE);
                    }

                    items.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(items.getContext(),LinearLayoutManager.VERTICAL);
                    items.addItemDecoration(dividerItemDecoration);

                    labourAdapter=new LabourAdapter(context,data,listener);
                    items.setAdapter(labourAdapter);

//                    adapter = new EmployeeAdapter(getContext(), employeeList, listner);
//                    recyclerView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
