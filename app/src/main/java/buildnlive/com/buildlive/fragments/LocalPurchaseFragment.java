package buildnlive.com.buildlive.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import buildnlive.com.buildlive.adapters.CategorySpinAdapter;
import buildnlive.com.buildlive.adapters.ItemSpinAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Category;
import buildnlive.com.buildlive.elements.IndentItem;
import buildnlive.com.buildlive.utils.Config;

public class LocalPurchaseFragment extends Fragment {
    private static ArrayList<IndentItem> itemList = new ArrayList<>();
    private static ArrayList<Category> categoryList=new ArrayList<>();
    private Button submit;
    private ProgressBar progress;
    private boolean val=true;
    private TextView hider, checkout_text;
    private EditText name_edit,quantity_edit,total_edit,overheads_edit,vendor_details_edit,ship_no_edit,details_edit;
    private static String name,quantity,total,overheads,unit,vendor_details,ship_no,details,category,item;
    private boolean LOADING;
    private Spinner categorySpinner,itemSpinner,unitspinner;
    private ItemSpinAdapter itemAdapter;
    private CategorySpinAdapter categoryAdapter;
    private static String stockid;
    private AlertDialog.Builder builder;


    public static LocalPurchaseFragment newInstance() {
        return new LocalPurchaseFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_local_purchase, container, false);
        setCategorySpinner();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        submit = view.findViewById(R.id.submit);
        name_edit = view.findViewById(R.id.name);
        quantity_edit = view.findViewById(R.id.quantity);
        total_edit = view.findViewById(R.id.total);
        overheads_edit = view.findViewById(R.id.overheads);
        vendor_details_edit = view.findViewById(R.id.vendor_details);
        ship_no_edit = view.findViewById(R.id.ship_no);
        details_edit = view.findViewById(R.id.details);
        builder = new AlertDialog.Builder(getContext());
        categorySpinner=view.findViewById(R.id.category_local);
        itemSpinner=view.findViewById(R.id.material);
        unitspinner=view.findViewById(R.id.unit);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String a = categoryAdapter.getID(i);
                category=categoryAdapter.getName(i);
                if(!a.equals("0")){
                    refresh(a);
                    }
                itemAdapter=new ItemSpinAdapter(getContext(), R.layout.custom_spinner,itemList);
                itemSpinner.setAdapter(itemAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item=itemAdapter.getName(i);
                stockid=itemAdapter.getID(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        unitspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unit=unitspinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        categoryAdapter=new CategorySpinAdapter(getContext(), R.layout.custom_spinner,categoryList);
        categorySpinner.setAdapter(categoryAdapter);


        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);

        if (LOADING) {
            progress.setVisibility(View.VISIBLE);
            hider.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
            hider.setVisibility(View.GONE);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=name_edit.getText().toString();
                quantity=quantity_edit.getText().toString();
                total=total_edit.getText().toString();
                overheads=overheads_edit.getText().toString();
                vendor_details=vendor_details_edit.getText().toString();
                ship_no=ship_no_edit.getText().toString();
                details=details_edit.getText().toString();

                builder.setMessage("Are you sure?") .setTitle("Payment");

                //Setting message manually and performing action on button click  
                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(validate(category,item,name,quantity,total,vendor_details,unit))
                                {
                                    try {
                                        sendRequest(stockid,name,quantity,unit,total,overheads,vendor_details,ship_no,details);
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
                alert.setTitle("Local Purchase");
                alert.show();


            }
        });


    }

    private boolean validate(String category,String item,String name,String quantity,String total,String vendor_details,String unit)
    {

        if(TextUtils.equals(category,"Select Category")){
            Toast.makeText(getContext(),"Please Select Category",Toast.LENGTH_LONG).show();
            val=false;
        }

        if((!TextUtils.isEmpty(name))&&(!TextUtils.equals(item,"Select Item"))){

            Toast.makeText(getContext(),"Either Choose Item from the list or Enter name",Toast.LENGTH_LONG).show();
            val=false;

        }
        if(TextUtils.isEmpty(name)&&TextUtils.equals(item,"Select Item")){

            Toast.makeText(getContext(),"Either Choose Item from the list or Enter name",Toast.LENGTH_LONG).show();
            val=false;

        }

        if(TextUtils.equals(unit,"Unit")){

            Toast.makeText(getContext(),"Select Unit",Toast.LENGTH_LONG).show();
            val=false;

        }

        if(TextUtils.isEmpty(quantity)){
            quantity_edit.setError("Enter Quantity");
            val=false;
        }

        if(TextUtils.isEmpty(total)){
            total_edit.setError("Enter Total");
            val=false;
        }
        if(TextUtils.isEmpty(vendor_details)){
            vendor_details_edit.setError("Enter Vendor Details");
            val=false;
        }
        return val;
    }

    private void sendRequest(String stockid,String name,String quantity,String units,String total,
                             String overheads,String vendor_details,String ship_no,String details) throws JSONException {
        App app= ((App)getActivity().getApplication());
        HashMap<String, String> params = new HashMap<>();
        params.put("local_purchase", App.userId);
//        JSONArray array = new JSONArray();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("stock_id", stockid).put("project_id", App.projectId).put("user_id", App.userId).put("item_name",name)
                .put("quantity",quantity).put("units",units).put("total_amount",total)
                .put("overheads",overheads).put("vendor_details",vendor_details).put("ship_no",ship_no)
                .put("details",details);
        params.put("local_purchase", jsonObject.toString());
        console.log("Res:" + params);
        app.sendNetworkRequest(Config.SEND_LOCAL_PURCHASE, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                    console.log(response);
                    if(response.equals("1")) {
                        Toast.makeText(getContext(), "Request Generated", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
            }
        });
    }




    private void setCategorySpinner() {
        App app= ((App)getActivity().getApplication());
        categoryList.clear();
        String requestURl= Config.REQ_SENT_CATEGORIES ;
        requestURl = requestURl.replace("[0]", App.userId);
        app.sendNetworkRequest(requestURl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {

                console.error("Network request failed with error :" + error);
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        categoryList.add(new Category().parseFromJSON(array.getJSONObject(i)));
                    }
                    categoryAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void refresh(String a) {
        App app= ((App)getActivity().getApplication());
        String requestUrl = Config.GET_SITE_LIST;
        requestUrl = requestUrl.replace("[1]", App.userId);
        requestUrl = requestUrl.replace("[0]", a);
        console.log(requestUrl);
        itemList.clear();
        IndentItem temp1=new IndentItem();
        temp1.setId("0");
        temp1.setName("Select Item");
        itemList.add(temp1);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                }

            @Override
            public void onNetworkRequestError(String error) {

                console.error("Network request failed with error :" + error);
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        itemList.add(new IndentItem().parseFromJSON(array.getJSONObject(i)));
                    }


                    itemAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
