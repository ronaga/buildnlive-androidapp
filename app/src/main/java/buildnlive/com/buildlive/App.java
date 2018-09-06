package buildnlive.com.buildlive;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

import buildnlive.com.buildlive.utils.Config;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static buildnlive.com.buildlive.activities.LoginActivity.PREF_KEY_PERMISSIONS;
import static buildnlive.com.buildlive.activities.LoginActivity.PREF_KEY_PROJECTS;
import static buildnlive.com.buildlive.activities.LoginActivity.PREF_KEY_USER_ID;
import static buildnlive.com.buildlive.utils.Config.PREF_NAME;

public class App extends Application {
    private RequestQueue queue;
    private SharedPreferences pref;
    public static String userId;
    public static String projectId;
    public static String belongsTo;
    public static String permissions;

    @Override
    public void onCreate() {
        super.onCreate();
        console.setContext(getApplicationContext());
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        userId = pref.getString(PREF_KEY_USER_ID, "dummy");
        projectId = pref.getString(PREF_KEY_PROJECTS, "{}");
        belongsTo = projectId + userId;
        permissions=pref.getString(PREF_KEY_PERMISSIONS,"");
    }

    public void sendNetworkRequest(final String URL, final int METHOD, final Map<String, String> params, final Interfaces.NetworkInterfaceListener listener) {
        listener.onNetworkRequestStart();
        StringRequest stringRequest = new StringRequest(METHOD, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onNetworkRequestComplete(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onNetworkRequestError("" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
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

    public SharedPreferences getPref() {
        return pref;
    }

    public void setPref(SharedPreferences pref) {
        this.pref = pref;
    }
}
