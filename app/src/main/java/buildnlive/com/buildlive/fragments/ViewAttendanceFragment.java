package buildnlive.com.buildlive.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.ListAdapter;
import buildnlive.com.buildlive.adapters.ViewLiveAttendanceAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Packet;
import buildnlive.com.buildlive.elements.ViewAttendance;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class ViewAttendanceFragment extends Fragment {
    private RecyclerView attendees;
    private ProgressBar progress;
    private TextView hider;
    private ArrayList<ViewAttendance> attendanceList= new ArrayList<>();
    private ViewLiveAttendanceAdapter adapter;
    private boolean LOADING;
    private Context context;
    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity;
    private static App app;


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

        adapter = new ViewLiveAttendanceAdapter(getContext(), attendanceList, new ViewLiveAttendanceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewAttendance worker, int pos, View view) {
                showUser(worker);
            }
        });
        attendees.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
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
                    attendees.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    attendees.setAdapter(adapter);

//                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    private void showUser(final ViewAttendance worker) {
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
        message.setText(worker.getLabour_name() + " (" + worker.getLabour_role() + ")");
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



}