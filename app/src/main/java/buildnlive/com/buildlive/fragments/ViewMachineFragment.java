package buildnlive.com.buildlive.fragments;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.ViewJobSheetAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.ViewJobSheet;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.PrefernceFile;
import buildnlive.com.buildlive.utils.UtilityofActivity;
import io.realm.Realm;

public class ViewMachineFragment extends Fragment {
    private static List<ViewJobSheet> itemsList=new ArrayList<>();
    private RecyclerView items;
    private Realm realm;
    private ProgressBar progress;
    private static App app;
    private TextView hider;
    private Context context;
    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity;
    private String logoutTimeText="";

    private int mHour, mMinute;
    private String mYear, mMonth, mDay, sHour, sMinute;
    private final Calendar c = Calendar.getInstance();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.appCompatActivity = (AppCompatActivity) activity;
    }

    public static ViewMachineFragment newInstance(App a) {
        app=a;
        return new ViewMachineFragment();
    }

    private ViewJobSheetAdapter.OnItemClickListener listner=new ViewJobSheetAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(ViewJobSheet items, int pos, View view) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.alert_dialog_job_sheet, null);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.PinDialog);

            final AlertDialog outDutyDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
            outDutyDialog.show();

            TextView title = dialogView.findViewById(R.id.alert_title);
            EditText endKms = dialogView.findViewById(R.id.endKms);
            TextView logoutTime = dialogView.findViewById(R.id.logoutTime);
            EditText reason = dialogView.findViewById(R.id.reason);
            title.setText("Update");

            logoutTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get Current Time

                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    logoutTime.setText(hourOfDay + ":" + minute);

                                    if (c.get(Calendar.MONTH) < 10) {
                                        mMonth = "0" + c.get(Calendar.MONTH);
                                    } else mMonth = "" + c.get(Calendar.MONTH);

                                    if (c.get(Calendar.DATE) < 10) {
                                        mDay = "0" + c.get(Calendar.DATE);
                                    } else mDay = "" + c.get(Calendar.DATE);

                                    if (hourOfDay < 10) {
                                        sHour = "0" + hourOfDay;
                                    } else sHour = "" + hourOfDay;

                                    if (minute < 10) {
                                        sMinute = "0" + minute;
                                    } else sMinute = "" + minute;

                                    logoutTimeText = c.get(Calendar.YEAR) + "-" + mMonth + "-" + mDay + " " + sHour + ":" + sMinute + ":" + "00";
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();

                }
            });

            Button positive = dialogView.findViewById(R.id.positive);
            Button negative = dialogView.findViewById(R.id.negative);

            positive.setText("Done");
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateJobSheet(items.getInventory_asset_jobsheet_id(),endKms.getText().toString(),logoutTimeText,reason.getText().toString(),outDutyDialog);
                    outDutyDialog.dismiss();
                }
            });
            negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    outDutyDialog.dismiss();
                }
            });

        }

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_jobsheet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        realm = Realm.getDefaultInstance();
//        itemsList = realm.where(Issue.class).equalTo("belongsTo", App.belongsTo).findAll();
        items = view.findViewById(R.id.items);
        utilityofActivity= new UtilityofActivity(appCompatActivity);
        progress=view.findViewById(R.id.progress);
        hider=view.findViewById(R.id.hider);
        ViewJobSheetAdapter adapter = new ViewJobSheetAdapter(getContext(), itemsList,listner);
        items.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        items.setAdapter(adapter);
        refresh();
    }


    private void refresh() {
        itemsList.clear();
        String url = Config.VIEW_MACHINE_LIST;
        url = url.replace("[0]", App.userId);
        url = url.replace("[1]", App.projectId);
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
                utilityofActivity.dismissProgressDialog();
                hider.setVisibility(View.GONE);
//                Realm realm = Realm.getDefaultInstance();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        itemsList.add(new ViewJobSheet().parseFromJSON(array.getJSONObject(i)));


//                        realm.executeTransaction(new Realm.Transaction() {
//                            @Override
//                            public void execute(Realm realm) {
//                                try {
//                                    RequestList request = new RequestList().parseFromJSONPlan(obj);
//                                    realm.copyToRealmOrUpdate(request);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
                    }
                    console.log("Size Of JobSheet: "+itemsList.size());
//                      realm.close();

                } catch (JSONException e) {
                }

                ViewJobSheetAdapter adapter = new ViewJobSheetAdapter(getContext(),itemsList,listner);
                items.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                items.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });
    }

    private void updateJobSheet(String inventory_asset_jobsheet_id, String endKms,String logoutTime, String reason, AlertDialog dialog) {
        String requestUrl = Config.UpdateJobSheet;

        HashMap<String, String> params = new HashMap<>();
        params.put("inventory_asset_jobsheet_id", inventory_asset_jobsheet_id);
        params.put("log_out_time", logoutTime);
        params.put("log_out_meter",endKms);
        params.put("work_description",reason);

        console.log("params"+params.toString());
        app.sendNetworkRequest(requestUrl, Request.Method.POST, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                utilityofActivity.dismissProgressDialog();
                console.error("Network request failed with error :" + error);
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();

                try {
                    if(response.equals("1"))
                    {
                        utilityofActivity.toast("Successfully Updated");
                        refresh();
                        dialog.dismiss();
                    }
                    else
                    {
                        utilityofActivity.toast("Something went wrong");
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}