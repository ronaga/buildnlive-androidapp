package buildnlive.com.buildlive.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import buildnlive.com.buildlive.adapters.NotificationsAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Notification;
import buildnlive.com.buildlive.utils.Config;
import io.realm.Realm;

public class NotificationActivity extends AppCompatActivity {
    private App app;
    private Realm realm;
    private ProgressBar progressBar;
    private ArrayList<Notification> notificationList=new ArrayList<>();
    private RecyclerView recyclerView;
    private ImageButton back;
    NotificationsAdapter adapter;
    AlertDialog.Builder builder;

    public NotificationsAdapter.OnItemClickListener listener = new NotificationsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(final Notification notification, final int pos, final View view) {
            builder.setMessage("Do you want to Submit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            switch (view.getId()){
                                case R.id.receive:
                                    try {
                                        sendRequest(notification.getId(),"Received");
                                        notificationList.remove(pos);
                                        adapter.notifyItemRemoved(pos);
                                        adapter.notifyItemRangeChanged(pos,notificationList.size());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case R.id.not_receive:
                                    try {
                                        sendRequest(notification.getId(),"Not Received");
                                        notificationList.remove(pos);
                                        adapter.notifyItemRemoved(pos);
                                        adapter.notifyItemRangeChanged(pos,notificationList.size());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case R.id.approve:
                                    try {
                                        sendRequest(notification.getId(),"Approved");
                                        notificationList.remove(pos);
                                        adapter.notifyItemRemoved(pos);
                                        adapter.notifyItemRangeChanged(pos,notificationList.size());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case R.id.reject:
                                    try {
                                        sendRequest(notification.getId(),"Rejected");
                                        notificationList.remove(pos);
                                        adapter.notifyItemRemoved(pos);
                                        adapter.notifyItemRangeChanged(pos,notificationList.size());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case R.id.review:
                                    try {
                                        sendRequest(notification.getId(),"Revision");
                                        notificationList.remove(pos);
                                        adapter.notifyItemRemoved(pos);
                                        adapter.notifyItemRangeChanged(pos,notificationList.size());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case R.id.read_notification:
                                    try {
                                        sendRequest(notification.getId(),"Read");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    notificationList.remove(pos);
                                    adapter.notifyItemRemoved(pos);
                                    adapter.notifyItemRangeChanged(pos,notificationList.size());
                                    break;

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
            alert.setTitle("Submit");
            alert.show();


        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        back= findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        app = (App) getApplication();
        progressBar= findViewById(R.id.progress);
        realm = Realm.getDefaultInstance();
        refresh();
        builder= new AlertDialog.Builder(this);
        recyclerView = (RecyclerView) findViewById(R.id.notifications);


//        final String adapter=new ArrayAdapter<String>(this,mobileArray);
        }


    private void refresh() {
        String requestUrl = Config.SEND_NOTIFICATIONS;
        notificationList.clear();
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", App.projectId);
        console.log(requestUrl);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progressBar.setVisibility(View.GONE);
                console.error("Network request failed with error :" + error);
                Toast.makeText(getApplicationContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                console.log(response);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject obj = array.getJSONObject(i);
                        notificationList.add(new Notification().parseFromJSON(obj));

                    }
                    adapter = new NotificationsAdapter(getApplicationContext(), notificationList,listener);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.hasFixedSize();
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {

                }
            }
        });
    }


    private void sendRequest(String id,String answer) throws JSONException {
        App app= ((App)getApplication());
        HashMap<String, String> params = new HashMap<>();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("id", id).put("response",answer).put("user_id", App.userId);
        params.put("notification", jsonObject.toString());
        console.log("Res:" + params);
        app.sendNetworkRequest(Config.GET_NOTIFICATIONS, 1, params, new Interfaces.NetworkInterfaceListener() {
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
                    Toast.makeText(getApplicationContext(), "Request Generated", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


}
