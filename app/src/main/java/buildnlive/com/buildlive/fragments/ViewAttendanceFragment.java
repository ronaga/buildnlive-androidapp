package buildnlive.com.buildlive.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.ViewLiveAttendanceAdapter;
import buildnlive.com.buildlive.adapters.WorkerHistoryAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.ViewAttendance;
import buildnlive.com.buildlive.elements.WorkerHistory;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class ViewAttendanceFragment extends Fragment {
    private RecyclerView attendees;
    private ProgressBar progress;
    private TextView hider;
    private ArrayList<ViewAttendance> attendanceList = new ArrayList<>();
    private ViewLiveAttendanceAdapter adapter;
    private boolean LOADING;
    private Context context;
    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity;
    private static App app;

    private android.app.AlertDialog.Builder builder;

    public static ViewAttendanceFragment newInstance() {
        return new ViewAttendanceFragment();
    }

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_attendance, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        app = (App) appCompatActivity.getApplication();
        utilityofActivity = new UtilityofActivity(appCompatActivity);
        attendees = view.findViewById(R.id.attendees);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);


        builder = new android.app.AlertDialog.Builder(context);
        adapter = new ViewLiveAttendanceAdapter(getContext(), attendanceList, new ViewLiveAttendanceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewAttendance worker, int pos, View view) {
                showUser(worker);
            }
        });
        attendees.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        attendees.setAdapter(adapter);
        if (LOADING)
            progress.setVisibility(View.VISIBLE);
        else
            progress.setVisibility(View.GONE);


        refresh();
    }


    private void refresh() {
        String requestUrl = Config.SEND_LABOUR_ATTENDANCE_LIST;
        requestUrl = requestUrl.replace("[1]", App.projectId);
        requestUrl = requestUrl.replace("[0]", App.userId);
        console.log(requestUrl);
        attendanceList.clear();
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
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
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        attendanceList.add(new ViewAttendance().parseFromJSON(array.getJSONObject(i)));
                    }

                    adapter = new ViewLiveAttendanceAdapter(getContext(), attendanceList, new ViewLiveAttendanceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(ViewAttendance worker, int pos, View view) {
                            showUser(worker);
                        }
                    });
                    attendees.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                    attendees.setAdapter(adapter);

//                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void deleteAttedance(String id, AlertDialog alertDialog) {
        String requestUrl = Config.DeleteLabourAttendance;
        requestUrl = requestUrl.replace("[1]", App.userId);
        requestUrl = requestUrl.replace("[0]", id);
        console.log(requestUrl);
        attendanceList.clear();
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
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
                     utilityofActivity.toast("Successfully deleted");
                        alertDialog.dismiss();

                    }
                    else if(response.equals("-1"))
                    {
                        utilityofActivity.toast("You do not have permission to delete");
                        alertDialog.dismiss();
                    }
                    else
                    {
                        utilityofActivity.toast("Something went wrong");
                        alertDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void showUser(final ViewAttendance worker) {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_show_user, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(), R.style.PinDialog);
        final AlertDialog alertDialog = dialogBuilder.setCancelable(true).setView(dialogView).create();
        alertDialog.show();
        final TextView disable = dialogView.findViewById(R.id.disableView);
        final ProgressBar progress = dialogView.findViewById(R.id.progress);
        final RecyclerView list = dialogView.findViewById(R.id.list);
//        list.setmMaxHeight(400);
        TextView title = dialogView.findViewById(R.id.alert_title);
        title.setText(context.getString(R.string.worker_details));
        TextView message = dialogView.findViewById(R.id.alert_message);
        message.setText(String.format(context.getString(R.string.workerHolder), worker.getLabour_name(), worker.getLabour_role()));
        Button positive = dialogView.findViewById(R.id.positive);
        positive.setText(context.getString(R.string.close));
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
        requestUrl = requestUrl.replace("[1]", worker.getLabour_id());
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
                    List<WorkerHistory> packets = parseRequest(response);
                    WorkerHistoryAdapter adapter = new WorkerHistoryAdapter(getContext(), packets, new WorkerHistoryAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(WorkerHistory packet, int pos, View view) {
                            builder.setMessage("Are you sure you want to delete this entry?") .setTitle("Delete Attendance");

                            //Setting message manually and performing action on button click
                            builder.setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            deleteAttedance(packet.getDailyAttendenceId(),alertDialog);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //  Action for 'NO' Button
                                            dialog.cancel();

                                        }
                                    });
                            //Creating dialog box
                            android.app.AlertDialog alert = builder.create();
                            alert.show();
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

    private List<WorkerHistory> parseRequest(String response) throws JSONException {
        JSONArray array = new JSONArray(response);
        List<WorkerHistory> packets = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            packets.add(new WorkerHistory(obj.getString("daily_attendence_id"),obj.getString("end_time"), obj.getString("start_time"), obj.getString("start_date_time")));
        }
        return packets;
    }


}