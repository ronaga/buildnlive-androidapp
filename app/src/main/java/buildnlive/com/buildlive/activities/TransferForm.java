package buildnlive.com.buildlive.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.ProjectFormSpinAdapter;
import buildnlive.com.buildlive.adapters.UserFormSpinAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.CreateTransferObject;
import buildnlive.com.buildlive.elements.Project;
import buildnlive.com.buildlive.elements.User;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class TransferForm extends AppCompatActivity {

    private ProgressBar progress;
    private TextView hider, unit, unit2, max_quantity, name;
    private Button submit;
    private EditText quantity, comments;
    private Spinner receiver, project;
    private ArrayList<User> userList;
    private ArrayList<Project> projectList;

    private UserFormSpinAdapter userFormSpinAdapter;
    private ProjectFormSpinAdapter projectFormSpinAdapter;

    private String selectedProject = "", selectedReceiver = "";

    private AlertDialog.Builder builder;
    private Context context;

    private App app;
    private CreateTransferObject transferObjects;

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
        setContentView(R.layout.activity_transfer_form);

        context = this;
        app = (App) getApplication();

        utilityofActivity=new UtilityofActivity(appCompatActivity);
        utilityofActivity.configureToolbar(appCompatActivity);

        TextView toolbar_title=findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle=findViewById(R.id.toolbar_subtitle);
        toolbar_title.setText("Transfer Form");
        toolbar_subtitle.setText(App.projectName);




        userList = new ArrayList<>();
        projectList = new ArrayList<>();


        setUserSpinner();
        setProjectSpinner();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            transferObjects = (CreateTransferObject) bundle.getSerializable("Items");
        }


        progress = findViewById(R.id.progress);
        hider = findViewById(R.id.hider);

        name = findViewById(R.id.item_name);

        name.setText(transferObjects.getItem_name());

        builder = new AlertDialog.Builder(context);

        submit = findViewById(R.id.submit);
        quantity = findViewById(R.id.quantity);
        max_quantity = findViewById(R.id.max_quantity);
        comments = findViewById(R.id.comment);
        unit = findViewById(R.id.unit);
        unit2 = findViewById(R.id.unit2);
        receiver = findViewById(R.id.receiver);
        project = findViewById(R.id.project);

        unit.setText(transferObjects.getUnits());
        unit2.setText(transferObjects.getUnits());

        max_quantity.setText(transferObjects.getQty());


        userFormSpinAdapter = new UserFormSpinAdapter(context, R.layout.custom_spinner, userList);
        userFormSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        receiver.setAdapter(userFormSpinAdapter);


        projectFormSpinAdapter = new ProjectFormSpinAdapter(context, R.layout.custom_spinner, projectList);
        projectFormSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        project.setAdapter(projectFormSpinAdapter);


        receiver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedReceiver = userFormSpinAdapter.getUserId(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        project.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedProject = projectFormSpinAdapter.getProjectId(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submit.setOnClickListener(v -> {
            builder.setMessage("Do you want to Submit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if ((selectedProject.equals(""))) {
                                Toast.makeText(context, "Please Select Project", Toast.LENGTH_LONG).show();
                            }
//                            else if ((selectedReceiver.equals(""))) {
//                                Toast.makeText(context, "Please Select User", Toast.LENGTH_LONG).show();
//                            }
                            else if (quantity.getText().toString().equals("")) {
                                Toast.makeText(context, "Please enter quantity", Toast.LENGTH_LONG).show();
                            } else if (Float.parseFloat(quantity.getText().toString()) > Float.parseFloat(transferObjects.getQty())) {
                                Toast.makeText(context, "Please enter correct quantity", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    sendRequest(selectedProject, selectedReceiver, quantity.getText().toString(), comments.getText().toString(), transferObjects.getItem_type(), transferObjects.getItem_id());
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
            alert.setTitle("Transfer");
            alert.show();

        });

    }


    private void sendRequest(String selectedProject, String selectedReceiver, String quantity, String comment, String item_type, String item_id) throws JSONException {
        progress.setVisibility(View.VISIBLE);
        hider.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", App.userId);
        params.put("project_id", App.projectId);
        params.put("project_to_id", selectedProject);
        params.put("receiver_id", selectedReceiver);
        params.put("qty", quantity);
        params.put("comment", comment);
        params.put("item_type", item_type);
        params.put("item_id", item_id);

        console.log("Res " + params);
        final JSONObject obj = new JSONObject();

        app.sendNetworkRequest(Config.GET_TRANSFER_REQUEST, 1, params, new Interfaces.NetworkInterfaceListener() {
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
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log("Response:" + response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
                console.log(response);
                if (response.equals("1")) {

                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(context, "Something went wrong :(", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void setUserSpinner() {
        App app = ((App) getApplication());
        userList.clear();
        String requestURl = Config.SEND_USERS;
        requestURl = requestURl.replace("[0]", App.userId);
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

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        userList.add(new User().parseFromJSON(array.getJSONObject(i)));
                    }
                    userFormSpinAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setProjectSpinner() {
        App app = ((App) getApplication());
        projectList.clear();
        String requestURl = Config.SEND_PROJECTS;
        requestURl = requestURl.replace("[0]", App.userId);
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

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        projectList.add(new Project().parseFromJSON(array.getJSONObject(i)));
                    }
                    projectFormSpinAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


}
