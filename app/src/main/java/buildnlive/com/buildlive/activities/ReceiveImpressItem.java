package buildnlive.com.buildlive.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import java.util.ArrayList;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.ImpressItem;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class ReceiveImpressItem extends AppCompatActivity {
    private ImpressItem ImpressItem;
    private Spinner reason;
    private Context context;
    private ArrayList<String> array=new ArrayList<>();
    private EditText quantity;
    private static App app;
    private ProgressBar progress;
    private TextView hider;
    private String issue_id="0",item_record_id="0",qty="0",type="default";
    private Button submit;
    AlertDialog.Builder builder;
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
        setContentView(R.layout.activity_receive_impress);

        context=this;
        utilityofActivity=new UtilityofActivity(this);
        app = (App) getApplication();

        utilityofActivity.configureToolbar(appCompatActivity);

        TextView toolbar_title=findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle=findViewById(R.id.toolbar_subtitle);
        toolbar_subtitle.setText(App.projectName);
        toolbar_title.setText("Receive");

        builder= new AlertDialog.Builder(this);

        reason=findViewById(R.id.type);
        quantity=findViewById(R.id.quantity);
        progress=findViewById(R.id.progress);
        hider=findViewById(R.id.hider);
        submit=findViewById(R.id.submit);

        Bundle bundle=getIntent().getExtras();

        if(bundle!=null) {
            ImpressItem =  bundle.getParcelable("Item");
            console.log("Bundle: "+ImpressItem);
        }

                setIssueArray();
                issue_id=ImpressItem.getImpress_id();
                console.log("Array issue:"+array.get(0));


        if(!array.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, array);
            reason.setAdapter(arrayAdapter);
        }


//        qty=quantity.getText().toString();

        reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = reason.getSelectedItem().toString();
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
                                if(!(type.equals("Select Type"))) {
                                    returnItem();
                                }
                                else Toast.makeText(context,"Select Type",Toast.LENGTH_LONG).show();
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
        array.add("Select Type");
        array.add("Received");
        array.add("Rejected");
    }

    private void returnItem() {
        String url = Config.GetReceiveImpress;
        url = url.replace("[0]", issue_id );
        url = url.replace("[1]", App.userId);
        url = url.replace("[2]", type);
        console.log(url);
        app.sendNetworkRequest(url, 0, null, new Interfaces.NetworkInterfaceListener() {
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
