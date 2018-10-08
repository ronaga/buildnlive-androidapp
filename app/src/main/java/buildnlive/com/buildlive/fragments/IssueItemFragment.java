package buildnlive.com.buildlive.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Issue;
import buildnlive.com.buildlive.elements.Item;
import buildnlive.com.buildlive.elements.ProjectMember;
import buildnlive.com.buildlive.utils.Config;
import io.realm.Realm;
import io.realm.RealmResults;

public class IssueItemFragment extends Fragment {
    private ProgressBar progress;
    private TextView hider, max, unit, unit2;
    private Button submit;
    private EditText quantity;
    private Spinner item, receiver;
    private ArrayList<Item> inventory;
    private ArrayList<String> items, receivers;
    private static App app;
    private Realm realm;
    private ArrayAdapter item_adapter, receiver_adapter;
    private String selectedItem, selectedReceiver, itemName, receiverName;
    private AlertDialog.Builder builder;

    public static IssueItemFragment newInstance(App a) {
        app = a;
        return new IssueItemFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_issue_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
        builder = new AlertDialog.Builder(getContext());
        submit = view.findViewById(R.id.submit);
        quantity = view.findViewById(R.id.quantity);
        max = view.findViewById(R.id.max_quantity);
        item = view.findViewById(R.id.item);
        unit = view.findViewById(R.id.unit);
        unit2 = view.findViewById(R.id.unit2);
        receiver = view.findViewById(R.id.receiver);
        realm = Realm.getDefaultInstance();
        items = new ArrayList<>();
        receivers = new ArrayList<>();
        inventory = new ArrayList<>();
        items.add(0, "Select Item");
        item_adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        item_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        item.setAdapter(item_adapter);

        final RealmResults<ProjectMember> receivers1 = realm.where(ProjectMember.class).equalTo("belongsTo", App.belongsTo).findAll();
        for (ProjectMember w : receivers1) {
            receivers.add(w.getName());
        }
        receivers.add(0, "Select Member");

        receiver_adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, receivers);
        receiver_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        receiver.setAdapter(receiver_adapter);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (quantity.getText().toString().length() > 0 && selectedItem.length() > 0 && selectedReceiver.length() > 0) {
                                    try {
                                        if (Float.parseFloat(quantity.getText().toString()) > Float.parseFloat(max.getText().toString())) {
                                            Toast.makeText(getContext(), "Check Quantity!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        sendIssue();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Fill Data Properly!", Toast.LENGTH_LONG).show();
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
                alert.setTitle("Issue Item");
                alert.show();



            }
        });

        item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedItem = inventory.get(position - 1).getId();
                    itemName = inventory.get(position - 1).getName();
                    max.setText(inventory.get(position - 1).getQuantity());
                    unit.setText(inventory.get(position - 1).getUnit());
                    unit2.setText(inventory.get(position - 1).getUnit());
                } else {
                    selectedItem = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        receiver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedReceiver = receivers1.get(position - 1).getUserId();
                    receiverName = receivers1.get(position - 1).getName();
                } else {
                    selectedReceiver = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        refresh();
    }

    private void refresh() {
        String requestUrl = Config.REQ_GET_INVENTORY;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", App.projectId);

        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.error("Network request failed with error :" + error);
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        inventory.add(new Item().parseFromJSON(array.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (Item i : inventory) {
                    items.add(i.getName());
                }
                receiver_adapter.notifyDataSetChanged();
            }
        });
    }

    private void sendIssue() throws JSONException {
        progress.setVisibility(View.VISIBLE);
        hider.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", App.userId);
        final JSONObject obj = new JSONObject().put("stock_id", selectedItem).put("quantity", quantity.getText().toString()).put("receiver_id", selectedReceiver);
        params.put("issue_list", obj.toString());
        app.sendNetworkRequest(Config.SEND_ISSUED_ITEM, 1, params, new Interfaces.NetworkInterfaceListener() {
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
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}