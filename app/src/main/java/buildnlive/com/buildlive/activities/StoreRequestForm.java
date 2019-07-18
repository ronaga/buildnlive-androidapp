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
import android.widget.ArrayAdapter;
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
import buildnlive.com.buildlive.adapters.AssetsSpinAdapter;
import buildnlive.com.buildlive.adapters.IssueVendorSpinAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Assets;
import buildnlive.com.buildlive.elements.Issue;
import buildnlive.com.buildlive.elements.IssueVendor;
import buildnlive.com.buildlive.elements.Item;
import buildnlive.com.buildlive.elements.ProjectMember;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;
import io.realm.Realm;
import io.realm.RealmResults;

public class StoreRequestForm extends AppCompatActivity {
    private ProgressBar progress;
    private TextView hider, max, unit, unit2, item;
    private Button submit;
    private EditText quantity, slip_no, comments;
    private Spinner receiver;
    private ArrayList<Item> inventory;
    private ArrayList<String> items, receivers;
    private static App app;
    private Realm realm;
    private ArrayAdapter item_adapter, receiver_adapter;
    private String selectedItem, selectedReceiver, itemName, receiverName, item_rent_id, vendor_id, user_type="", item_type="", asset_id;
    private AlertDialog.Builder builder;
    private static ArrayList<IssueVendor> IssueVendorList = new ArrayList<>();
    private IssueVendorSpinAdapter IssueVendorAdapter;
    private Spinner vendorSpinner;
    private static ArrayList<Assets> assetsList = new ArrayList<>();
    private AssetsSpinAdapter assetsAdapter;
    private Spinner assetsSpinner;
    private Context context;
    private Item itemList;
    private EditText receiver_person;
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
        setContentView(R.layout.activity_issue_item_list);
        context = this;

        utilityofActivity = new UtilityofActivity(this);

        setContractorSpinner();
        setAssetsSpinner();

        utilityofActivity.configureToolbar(appCompatActivity);

        TextView toolbar_title=findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle=findViewById(R.id.toolbar_subtitle);
        toolbar_title.setText("Stock Request");
        toolbar_subtitle.setText(App.projectName);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            itemList = (Item) bundle.getSerializable("Items");
        }

        app = (App) getApplication();

        slip_no = findViewById(R.id.slip_no);
        progress = findViewById(R.id.progress);
        hider = findViewById(R.id.hider);
        builder = new AlertDialog.Builder(context);
        submit = findViewById(R.id.submit);
        quantity = findViewById(R.id.quantity);
        comments = findViewById(R.id.comment);
        max = findViewById(R.id.max_quantity);
        item = findViewById(R.id.item);
        unit = findViewById(R.id.unit);
        unit2 = findViewById(R.id.unit2);
        receiver = findViewById(R.id.receiver);
        receiver_person = findViewById(R.id.receiver_edittext);

        realm = Realm.getDefaultInstance();
        items = new ArrayList<>();
        receivers = new ArrayList<>();
        inventory = new ArrayList<>();
        items.add(0, "Select Item");

        item.setText(itemList.getName());
        itemName = itemList.getName();
        item_type = itemList.getItem_type();

        console.log("Item Type" + item_type);
        selectedItem = itemList.getId();

        max.setText(itemList.getQuantity());
        unit.setText(itemList.getUnit());
        unit2.setText(itemList.getUnit());

        final RealmResults<ProjectMember> receivers1 = realm.where(ProjectMember.class).equalTo("belongsTo", App.belongsTo).findAll();
        for (ProjectMember w : receivers1) {
            receivers.add(w.getName());
        }
        receivers.add(0, "Select Member");

        receiver_adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, receivers);
        receiver_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        receiver.setAdapter(receiver_adapter);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (quantity.getText().toString().length() > 0 && selectedItem.length() > 0) {
                                    try {
                                        if (Float.parseFloat(quantity.getText().toString()) > Float.parseFloat(max.getText().toString())) {
                                            Toast.makeText(context, "Check Quantity!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
//                                        if ((!(vendor_id.equals("")) && !(selectedReceiver.equals(""))) || (vendor_id.equals("")) && (selectedReceiver.equals(""))) {
//                                            Toast.makeText(context, "Select Either Member Or Vendor", Toast.LENGTH_SHORT).show();
//                                        } else
                                        sendIssue();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(context, "Fill Data Properly!", Toast.LENGTH_LONG).show();
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
                alert.setTitle("Stock Request");
                alert.show();


            }
        });


        receiver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedReceiver = receivers1.get(position - 1).getUserId();
                    receiverName = receivers1.get(position - 1).getName();
                    user_type = "user";
                } else {
                    selectedReceiver = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        vendorSpinner = findViewById(R.id.contractor);
        vendorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vendor_id = IssueVendorAdapter.getID(i);

                if (i > 0) {
                    vendor_id = IssueVendorAdapter.getID(i);
                    user_type = "vendor";
                } else {
                    vendor_id = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        IssueVendorAdapter = new IssueVendorSpinAdapter(context, R.layout.custom_spinner, IssueVendorList);
        IssueVendorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vendorSpinner.setAdapter(IssueVendorAdapter);

        assetsSpinner = findViewById(R.id.asset);
        assetsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    item_rent_id = assetsAdapter.getRentId(i);
                    asset_id = assetsAdapter.getAssetId(i);
                } else {
                    item_rent_id = "";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        assetsAdapter = new AssetsSpinAdapter(context, R.layout.custom_spinner, assetsList);
        assetsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assetsSpinner.setAdapter(assetsAdapter);


    }


    private void sendIssue() throws JSONException {
        progress.setVisibility(View.VISIBLE);
        hider.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", App.userId);

        final JSONObject obj = new JSONObject();
        console.log("user type: " + user_type);

//        if (user_type.equals("user")) {
        obj.put("stock_id", selectedItem).put("quantity", quantity.getText().toString())
                .put("comments", comments.getText().toString())
                .put("item_rent_id", item_rent_id).put("slip_no", slip_no.getText().toString())
                .put("user_type", user_type).put("item_type", item_type).put("asset_id", asset_id)
                .put("reciver", receiver_person.getText().toString());
        if (user_type.equals("user")) {
            obj.put("receiver_id", selectedReceiver);
        }
        else obj.put("receiver_id", vendor_id);
        params.put("issue_list", obj.toString());

        console.log("Item User" + obj.toString());
//        } else if (user_type.equals("vendor")) {
//            obj.put("stock_id", selectedItem).put("quantity", quantity.getText().toString())
//                    .put("receiver_id", vendor_id).put("comments", comments.getText().toString())
//                    .put("item_rent_id", item_rent_id).put("slip_no", slip_no.getText().toString())
//                    .put("user_type", user_type).put("item_type", item_type).put("asset_id", asset_id)
//                    .put("reciver", receiver_person.getText().toString());
//            params.put("issue_list", obj.toString());
//            console.log("Item Vendor" + obj.toString());

//        }
        console.log("ISSUE DATA:" + params);
        app.sendNetworkRequest(Config.SEND_STOCK_ITEM, 1, params, new Interfaces.NetworkInterfaceListener() {
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
                    try {
                        final Issue issue = new Issue().parseFromJSON(obj, itemName, receiverName);
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(issue);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(context, "Something went wrong :(", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setAssetsSpinner() {
        App app = ((App) getApplication());
        assetsList.clear();
        String requestURl = Config.SEND_ASSETS;
        requestURl = requestURl.replace("[0]", App.userId);
        requestURl = requestURl.replace("[1]", App.projectId);

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
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        assetsList.add(new Assets().parseFromJSON(array.getJSONObject(i)));
                    }
                    assetsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setContractorSpinner() {
        App app = ((App) getApplication());
        IssueVendorList.clear();
        String requestURl = Config.SEND_ISSUE_VENDORS;
        requestURl = requestURl.replace("[0]", App.userId);
        requestURl = requestURl.replace("[1]", App.projectId);
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
                        IssueVendorList.add(new IssueVendor().parseFromJSON(array.getJSONObject(i)));
                    }
                    IssueVendorAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}