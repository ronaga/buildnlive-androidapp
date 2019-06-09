package buildnlive.com.buildlive.activities;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.RepairItem;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class RepairRequestForm extends AppCompatActivity {
    private Button submit;
    private ProgressBar progress;
    private boolean val=true;
    private TextView hider,name,max_quantity;
    private EditText comment,quantity;


    private AlertDialog.Builder builder;
    private Context context;
    private RepairItem selectedItem;
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
        setContentView(R.layout.activity_repair_form);

        context=this;

        utilityofActivity= new UtilityofActivity(this);

        utilityofActivity=new UtilityofActivity(appCompatActivity);
        utilityofActivity.configureToolbar(appCompatActivity);

        TextView toolbar_title=findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle=findViewById(R.id.toolbar_subtitle);
        toolbar_subtitle.setText(App.projectName);
        toolbar_title.setText("Repair Request");

        Bundle bundle=getIntent().getExtras();


        if(bundle!=null){
            selectedItem= (RepairItem) bundle.getSerializable("Items");
        }


        builder = new AlertDialog.Builder(context);


        name = findViewById(R.id.name);
        comment = findViewById(R.id.comment);
        quantity = findViewById(R.id.quantity);
        max_quantity = findViewById(R.id.max_quantity);

        progress = findViewById(R.id.progress);
        hider = findViewById(R.id.hider);


        name.setText(selectedItem.getName());
        max_quantity.setText(selectedItem.getQty());
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
                                if(!quantity.getText().toString().equals("")&&(Integer.parseInt(quantity.getText().toString())<=Integer.parseInt(selectedItem.getQty())))
                                {
                                    try {
                                        sendRequest(selectedItem.getItem_rent_id(),quantity.getText().toString(),comment.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else {
                                    quantity.setError("Enter Correct Quantity");
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
                alert.setTitle("Request Repair");
                alert.show();


            }
        });




    }






    private void sendRequest(String item_rent_id,String quantity,String comment) throws JSONException {
        App app= ((App)getApplication());
        HashMap<String, String> params = new HashMap<>();

        params.put("user_id", App.userId);
        params.put("project_id", App.projectId);
//
        JSONObject jsonObject=new JSONObject();

        jsonObject.put("item_rent_id", item_rent_id).put("description",comment).put("quantity",quantity);

        params.put("repair_list", jsonObject.toString());
        console.log("Repair "+params);


        app.sendNetworkRequest(Config.GET_REPAIR_ITEM, 1, params, new Interfaces.NetworkInterfaceListener() {
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
                Toast.makeText(context,"Error"+error,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
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
