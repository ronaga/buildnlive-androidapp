package buildnlive.com.buildlive.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.ViewIssueAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.ViewIssue;
import buildnlive.com.buildlive.utils.Config;

public class ReturnIssuedItem extends AppCompatActivity {
    private ViewIssue viewIssue;
    private Spinner reason;
    private Context context;
    private ArrayList<String> array=new ArrayList<>();
    private EditText quantity;
    private static App app;
    private ProgressBar progress;
    private TextView hider;
    private String issue_id="0",item_record_id="0",qty="0",type="0";
    private Button submit;
    AlertDialog.Builder builder;

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
        setContentView(R.layout.activity_return_item);

        context=this;
        app = (App) getApplication();

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();

        if(supportActionBar!=null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
        TextView toolbar_title=findViewById(R.id.toolbar_title);
        toolbar_title.setText("Return");

        builder= new AlertDialog.Builder(this);

        reason=findViewById(R.id.type);
        quantity=findViewById(R.id.quantity);
        progress=findViewById(R.id.progress);
        hider=findViewById(R.id.hider);
        submit=findViewById(R.id.submit);

        Bundle bundle=getIntent().getExtras();

        if(bundle!=null) {
            viewIssue = (ViewIssue) bundle.getSerializable("Item");
            console.log("Bundle: "+viewIssue);
        }

        if(viewIssue!=null) {
            if(!viewIssue.getIssueId().equals("0")){
                setIssueArray();
                issue_id=viewIssue.getIssueId();
                console.log("Array issue:"+array.get(0));
            }
            else if(!viewIssue.getItem_record_id().equals("0")){
                setRecordArray();
                item_record_id=viewIssue.getItem_record_id();
                console.log("Record"+array.get(0));
            }
        }

        if(!array.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, array);
            reason.setAdapter(arrayAdapter);
        }


//        qty=quantity.getText().toString();

        reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0) {
                    type = reason.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        if(!(qty.equals(""))&&!(type.equals("Select Type"))) {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    builder.setMessage("Do you want to Submit?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    if(imm!=null) {
                                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                                    }
                                    if(!(quantity.getText().toString().equals(""))&&!(type.equals("Select Type"))) {
                                        returnItem();
                                    }
                                    else Toast.makeText(context,"Please Fill Properly",Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    if(imm!=null) {
                                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                                    }
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
//        }
    }

    private void setIssueArray(){
        array.add("Return");
        array.add("Wasted");
    }
    private void setRecordArray(){
        array.add("Return");
        array.add("Damaged");
    }

    private void returnItem() {
        String url = Config.RETURN_ISSUED_ITEM;
        url = url.replace("[0]", issue_id );
        url = url.replace("[1]", item_record_id);
        url = url.replace("[2]", quantity.getText().toString());
        url = url.replace("[3]", type);
        console.log(url);
        app.sendNetworkRequest(url, 0, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log("Response:" + response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                if(response.equals("1")){
                    Toast.makeText(getApplicationContext(), "Request Generated", Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Something went wrong,Please try again later", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
