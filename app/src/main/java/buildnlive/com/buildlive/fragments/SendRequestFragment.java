package buildnlive.com.buildlive.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.AddItemAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Item;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.Utils;
import io.realm.Realm;

public class SendRequestFragment extends Fragment {
    private Button submit;
    private Spinner type, material;
    private ArrayList<String> type_data, material_data, material_units;
    private ArrayList<String> ids = new ArrayList<>();
    private ArrayAdapter type_adapter, material_adapter;
    private static App app;
    private ProgressBar progress;
    private TextView hider;
    private TextView unit;
    private boolean isSelected;
    private int itemSelected = 0;
    private AlertDialog.Builder builder;
    private EditText name, description, quantity;
    Snackbar snackbar;
    public static SendRequestFragment newInstance(App a) {
        app = a;
        return new SendRequestFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send_request, container, false);
    }

    private String data;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.name);
        builder=new AlertDialog.Builder(getContext());
        description = view.findViewById(R.id.message);
        type = view.findViewById(R.id.type);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
        material = view.findViewById(R.id.material);
        quantity = view.findViewById(R.id.quantity);
        unit = view.findViewById(R.id.unit);
        submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (isSelected) {
                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("user_id", App.userId);
                                    params.put("project_id", App.projectId);
                                    try {
                                        JSONObject req = new JSONObject()
                                                .put("label", name.getText().toString())
                                                .put("specs", description.getText().toString())
                                                .put("form_type_id", ids.get(material.getSelectedItemPosition()))
                                                .put("form_type", type_data.get(type.getSelectedItemPosition()).toLowerCase())
                                                .put("quantity", quantity.getText().toString())
                                                .put("units", unit.getText().toString())
                                                .put("material", material_data.get(itemSelected))
                                                .put("date", Utils.fromTimeStampToDate(System.currentTimeMillis()));
                                        console.log("Req:" + req.toString());
                                        params.put("request", req.toString());
                                        data = req.toString();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    app.sendNetworkRequest(Config.SEND_REQUEST_ITEM, Request.Method.POST, params, new Interfaces.NetworkInterfaceListener() {
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
                                            progress.setVisibility(View.GONE);
                                            hider.setVisibility(View.GONE);
                                            if (response.equals("1")) {
                                                Toast.makeText(getContext(), "Item Requested", Toast.LENGTH_LONG).show();

                                                getActivity().finish();
                                                Realm realm = Realm.getDefaultInstance();
                                                realm.executeTransaction(new Realm.Transaction() {
                                                    @Override
                                                    public void execute(Realm realm) {
                                                        try {
                                                            buildnlive.com.buildlive.elements.Request request = new buildnlive.com.buildlive.elements.Request().parseFromString(data);
                                                            realm.copyToRealmOrUpdate(request);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "Select Something Please", Toast.LENGTH_LONG).show();
//                                    snackbar = Snackbar.make(view,"Select Something",Snackbar.LENGTH_LONG);
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
                alert.setTitle("Request Item");
                alert.show();

            }
        });

        type_data = new ArrayList<>();
        material_data = new ArrayList<>();
        material_units = new ArrayList<>();
        type_data.add("Select Type");
        type_data.add("Machinery");
        type_data.add("Item");
        type_data.add("Others");
        material_data.add("Select Material");

        type_adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, type_data);
        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        material_adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, material_data);
        material_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(type_adapter);
        material.setAdapter(material_adapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                if (position > 0) {
                    String url = Config.REQ_GET_REQUEST_TYPE;
                    url = url.replace("[0]", type_data.get(position).toLowerCase());
                    url = url.replace("[1]", App.userId);
                    app.sendNetworkRequest(url, Request.Method.POST, null, new Interfaces.NetworkInterfaceListener() {
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
                            console.log("res:" + response);
                            progress.setVisibility(View.GONE);
                            hider.setVisibility(View.GONE);
                            try {
                                isSelected = false;
                                handleMaterial(response, type_data.get(position));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    isSelected = true;
                }
                try {
                    unit.setText(material_units.get(position));
                    itemSelected = position;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void handleMaterial(String res, String type) throws JSONException {
        JSONArray array = new JSONArray(res);
        material_data.clear();
        ids.clear();
        for (int i = 0; i < array.length(); i++) {
            material_data.add(array.getJSONObject(i).getString("name"));
            material_units.add(array.getJSONObject(i).getString("units"));
            ids.add(array.getJSONObject(i).getString("id"));
        }
        material_data.add(0, "Select " + type);
        material_units.add(0, "Units");
        ids.add(0, "0");
        material.setSelection(0);
        material_adapter.notifyDataSetChanged();
    }
}