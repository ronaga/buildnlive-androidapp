package buildnlive.com.buildlive.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.activities.CreateLabour;
import buildnlive.com.buildlive.adapters.AttendanceAdapter;
import buildnlive.com.buildlive.adapters.ListAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Packet;
import buildnlive.com.buildlive.elements.Worker;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.PrefernceFile;
import buildnlive.com.buildlive.utils.UtilityofActivity;


public class MarkAttendanceNew extends Fragment {
    private Button submit;
    private RecyclerView attendees;
    private ProgressBar progress;
    private TextView hider;
    private ArrayList<Worker> workers = new ArrayList();
    private boolean LOADING;
    private static App app;
    private AttendanceAdapter adapter;
    private FloatingActionButton fab;
    private Context context;
    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity;


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

    public static MarkAttendanceNew newInstance(App a) {
        app = a;
        return new MarkAttendanceNew();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mark_attendance, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        utilityofActivity = new UtilityofActivity(appCompatActivity);
        attendees = view.findViewById(R.id.attendees);
        submit = view.findViewById(R.id.submit);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateLabour.class));
            }
        });

        attendees.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_attendance_ref, null);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.PinDialog);
                final AlertDialog alertDialog = dialogBuilder.setCancelable(true).setView(dialogView).create();
                alertDialog.show();
                final EditText refNo = dialogView.findViewById(R.id.alert_message);
                Button positive = dialogView.findViewById(R.id.positive);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

//                        <-------------------------- Network Request------------------------------->

                        final ArrayList<Worker> checkingOutWorkers = new ArrayList<>();
                        final JSONArray checkIn = new JSONArray();
                        final JSONArray checkOut = new JSONArray();

                        Worker u = new Worker();

                        for (final String s : AttendanceAdapter.ViewHolder.changedUsers.keySet()) {


                            for (Worker i : workers) {
                                if (i.getId().equals(s)) {
                                    u = i;
                                    break;
                                }
                            }
//                            Worker u = realm.where(Worker.class).equalTo("id", s).findFirst();
                            try {
                                if (AttendanceAdapter.ViewHolder.changedUsers.get(s)) {
                                    checkingOutWorkers.add(u);
                                    if (!PrefernceFile.Companion.getInstance(context).getString("CheckoutTime" + u.getId()).equals("")) {
                                        checkOut.put(new JSONObject().put("starttime", u.getCheckInTimeSelected()).put("finishtime", PrefernceFile.Companion.getInstance(context).getString("CheckoutTime" + u.getId())).put("attendence_id", PrefernceFile.Companion.getInstance(context).getString("attendence_id" + u.getId()))
                                                .put("overtime_hours", PrefernceFile.Companion.getInstance(context).getString("OvertimeHours" + u.getId()))
                                                .put("overtime_work", PrefernceFile.Companion.getInstance(context).getString("OvertimeWork" + u.getId())));
                                    } else {
                                        Toast.makeText(context, "Please Enter Checkout Time", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    if (!PrefernceFile.Companion.getInstance(context).getString("CheckinTime" + u.getId()).equals("")) {
                                        checkIn.put(new JSONObject().put("starttime", PrefernceFile.Companion.getInstance(context).getString("CheckinTime" + u.getId())).put("labour_id", u.getWorkerId()).put("ref_no", refNo.getText().toString()));
                                    } else {
                                        Toast.makeText(context, "Please Enter Checkin Time", Toast.LENGTH_LONG).show();
                                    }

                                }
                            } catch (JSONException e) {

                            }
                        }
                        if (checkIn.length() > 0) {
                            progress.setVisibility(View.VISIBLE);
                            hider.setVisibility(View.VISIBLE);
                            HashMap<String, String> params = new HashMap<>();
                            params.put("user_id", App.userId);
                            params.put("project_id", App.projectId);
                            params.put("attendence", checkIn.toString()); // TODO PLEASE CHECK SPELLING MISTAKES
                            console.log("Params" + params.toString());
                            app.sendNetworkRequest(Config.REQ_POST_CHECK_IN, Request.Method.POST, params, new Interfaces.NetworkInterfaceListener() {
                                @Override
                                public void onNetworkRequestStart() {
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
                                    progress.setVisibility(View.GONE);
                                    hider.setVisibility(View.GONE);
                                    utilityofActivity.dismissProgressDialog();
                                    AttendanceAdapter.ViewHolder.changedUsers.clear();
                                    console.log("CheckIn Response:" + response);
                                    try {
                                        JSONArray array = new JSONArray(response);
                                        Worker u = new Worker();
                                        for (int i = 0; i < array.length(); i++) {
                                            final JSONObject obj = array.getJSONObject(i);

                                            for (Worker j : workers) {
                                                if (j.getId().equals(obj.getString("labour_id") + App.belongsTo)) {
                                                    u = j;
                                                    break;
                                                }
                                            }
                                            try {

                                                u.setStart_time(obj.getString("starttime"));
                                                u.setCheckInTimeSelected(PrefernceFile.Companion.getInstance(context).getString("CheckinTime" + u.getId()));
                                                u.setCheckInTime(System.currentTimeMillis());
                                                u.setAttendanceId(obj.getString("attendence_id"));
                                                PrefernceFile.Companion.getInstance(context).setString("attendence_id" + u.getId(), obj.getString("attendence_id"));

                                            } catch (JSONException e) {
                                            }
                                        }
                                        appCompatActivity.finish();
                                    } catch (JSONException e) {

                                    }
                                    Toast.makeText(getContext(), "Attendance Updated", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        if (checkOut.length() > 0) {
                            progress.setVisibility(View.VISIBLE);
                            hider.setVisibility(View.VISIBLE);
                            final HashMap<String, String> params = new HashMap<>();
                            params.put("user_id", App.userId);
                            params.put("project_id", App.projectId);
                            params.put("attendence", checkOut.toString()); // TODO PLEASE CHECK SPELLING MISTAKES
                            console.log("CheckOut: " + params);
                            app.sendNetworkRequest(Config.REQ_POST_CHECK_OUT, Request.Method.POST, params, new Interfaces.NetworkInterfaceListener() {
                                @Override
                                public void onNetworkRequestStart() {
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
                                    AttendanceAdapter.ViewHolder.changedUsers.clear();
                                    progress.setVisibility(View.GONE);
                                    utilityofActivity.dismissProgressDialog();
                                    hider.setVisibility(View.GONE);
                                    console.log("CheckOut Response:" + response);
                                    if (response.equals("1")) {
//                                        AttendanceAdapter.ViewHolder.check_out.setEnabled(false);
                                        for (Worker u : checkingOutWorkers) {
                                            u.setCheckOutTimeSelected(PrefernceFile.Companion.getInstance(context).getString("CheckoutTime" + u.getId()));
                                            u.setEnd_time("end_time");
                                        }
                                        appCompatActivity.finish();
                                        Toast.makeText(getContext(), "Attendance Updated", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }


                    }
                });
                Button negative = dialogView.findViewById(R.id.negative);
                negative.setText("Close");
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });


            }
        });

        if (LOADING) {
            progress.setVisibility(View.VISIBLE);
            hider.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
            hider.setVisibility(View.GONE);
        }
    }

    private void showUser(final Worker worker) {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(), R.style.PinDialog);
        final AlertDialog alertDialog = dialogBuilder.setCancelable(true).setView(dialogView).create();
        alertDialog.show();
        final TextView disable = dialogView.findViewById(R.id.disableView);
        final ProgressBar progress = dialogView.findViewById(R.id.progress);
        final RecyclerView list = dialogView.findViewById(R.id.list);
//        list.setmMaxHeight(400);
        TextView title = dialogView.findViewById(R.id.alert_title);
        title.setText("Worker Details");
        TextView message = dialogView.findViewById(R.id.alert_message);
        message.setText(worker.getName() + " (" + worker.getRoleAssigned() + ")");
        Button positive = dialogView.findViewById(R.id.positive);
        positive.setText("Close");
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        Button negative = dialogView.findViewById(R.id.negative);
        negative.setVisibility(View.GONE);
        String requestUrl = Config.REQ_GET_USER_ATTENDANCE;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", worker.getWorkerId());
        requestUrl = requestUrl.replace("[2]", App.projectId);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                utilityofActivity.dismissProgressDialog();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log("Request :" + response);
                utilityofActivity.dismissProgressDialog();
                try {
                    List<Packet> packets = parseRequest(response);
                    ListAdapter adapter = new ListAdapter(getContext(), packets, new ListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Packet packet, int pos, View view) {

                        }
                    });
                    disable.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    list.setLayoutManager(manager);
                    list.setVisibility(View.VISIBLE);
                    list.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<Packet> parseRequest(String response) throws JSONException {
        JSONArray array = new JSONArray(response);
        List<Packet> packets = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            packets.add(new Packet(obj.getString("start_date_time"), obj.getString("end_date_time"), 7190));
        }
        return packets;
    }


    private void refresh() {
        workers.clear();
        String requestUrl = Config.REQ_GET_LABOUR;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", App.projectId);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {

                utilityofActivity.dismissProgressDialog();
                console.error("Network request failed with error :" + error);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNetworkRequestComplete(final String response) {
                utilityofActivity.dismissProgressDialog();

                console.log("Response:" + response);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        workers.add(new Worker().parseFromJSON(array.getJSONObject(i)));
                    }


                    adapter = new AttendanceAdapter(getContext(), workers, new AttendanceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Worker worker, int pos, View view) {
//                showUser(worker);
                        }
                    });

                    attendees.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}