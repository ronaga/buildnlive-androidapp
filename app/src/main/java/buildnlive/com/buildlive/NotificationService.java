package buildnlive.com.buildlive;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import buildnlive.com.buildlive.elements.Notification;
import buildnlive.com.buildlive.utils.Config;
import io.realm.Realm;

public class NotificationService extends IntentService {

    private RequestQueue queue;

    public NotificationService() {
        super("Notification Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        console.log("Running From Service");
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, Config.SEND_NOTIFICATIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            console.log(response);
                            Realm realm=Realm.getDefaultInstance();
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                final JSONObject obj = array.getJSONObject(i);
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        try {
                                            Notification notification = new Notification().parseFromJSON(obj);
                                            realm.copyToRealmOrUpdate(notification);
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
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                console.error("Network request failed with error :" + error);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Config.REQ_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueueInstance(getApplicationContext()).add(stringRequest);
    }

    private RequestQueue getRequestQueueInstance(Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
            return queue;
        }
        return queue;
    }


}
