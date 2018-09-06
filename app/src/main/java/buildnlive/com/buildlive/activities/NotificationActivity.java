package buildnlive.com.buildlive.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import io.realm.RealmResults;

public class NotificationActivity extends AppCompatActivity {
    private App app;
    private Realm realm;
    private ArrayList<Notification> notificationList=new ArrayList<>();
    private RecyclerView recyclerView;
    NotificationsAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        app = (App) getApplication();
        realm = Realm.getDefaultInstance();
        refresh();
        final RealmResults<Notification> notifications = realm.where(Notification.class).findAllAsync();
        recyclerView = (RecyclerView) findViewById(R.id.notifications);

            adapter = new NotificationsAdapter(this, notifications, new NotificationsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Notification notification, int pos, View view) {
                    switch (view.getId()){
                        case R.id.receive:
                            try {
                                sendRequest(notification.getId(),"Received");
                                adapter.notifyItemRemoved(pos);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Toast.makeText(getApplicationContext(),"Receive"+pos,Toast.LENGTH_LONG).show();
                            break;
                        case R.id.not_receive:
                            try {
                                sendRequest(notification.getId(),"Not Received");
                                adapter.notifyItemRemoved(pos);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Toast.makeText(getApplicationContext(),"Not Receive"+pos,Toast.LENGTH_LONG).show();
                            break;
                        case R.id.approve:
                            try {
                                sendRequest(notification.getId(),"Approved");
                                adapter.notifyItemRemoved(pos);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Toast.makeText(getApplicationContext(),"Not Receive"+pos,Toast.LENGTH_LONG).show();
                            break;
                        case R.id.reject:
                            try {
                                sendRequest(notification.getId(),"Rejected");
                                adapter.notifyItemRemoved(pos);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Toast.makeText(getApplicationContext(),"Not Receive"+pos,Toast.LENGTH_LONG).show();
                            break;
                        case R.id.review:
                            try {
                                sendRequest(notification.getId(),"Revision");
                                adapter.notifyItemRemoved(pos);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Toast.makeText(getApplicationContext(),"Not Receive"+pos,Toast.LENGTH_LONG).show();
                            break;
                    }

                    }

            });
//        final String adapter=new ArrayAdapter<String>(this,mobileArray);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
        }

    private void getNotifications(){

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
            }

            @Override
            public void onNetworkRequestError(String error) {

                console.error("Network request failed with error :" + error);
                Toast.makeText(getApplicationContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject obj = array.getJSONObject(i);
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                try {
                                    Notification notification = new Notification().parseFromJSON(obj);
                                    realm.copyToRealmOrUpdate(notification);
                                    console.log("Realm"+notification);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    realm.close();
                } catch (JSONException e) {

                }
            }
        });
    }


    private void sendRequest(String id,String answer) throws JSONException {
        App app= ((App)getApplication());
        HashMap<String, String> params = new HashMap<>();
//        params.put("notification", App.userId);
//        JSONArray array = new JSONArray();
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
