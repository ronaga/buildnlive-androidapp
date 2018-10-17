package buildnlive.com.buildlive.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Project;
import buildnlive.com.buildlive.utils.Config;
import io.realm.Realm;

import static buildnlive.com.buildlive.activities.HomeActivity.PREF_KEY_LOGGED_IN;

public class LoginActivity extends AppCompatActivity {
    private App app;
    private EditText mobile, pass;
    private Button login;
    private ProgressBar progress;
    private TextView hider,unable_to_login;
    private SharedPreferences pref;
    public static final String PREF_KEY_EMAIL = "user_email";
    public static final String PREF_KEY_NAME = "user_name";
    public static final String PREF_KEY_CONTACT = "user_contact";
    public static final String PREF_KEY_USER_ID = "user_id";
    public static final String PREF_KEY_PROJECTS = "user_projects";
    public static final String PREF_KEY_PERMISSIONS = "user_permissions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        mobile = findViewById(R.id.mobile);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.login);
        unable_to_login = findViewById(R.id.unable_login);
        app = (App) getApplication();
        pref = app.getPref();
        progress = findViewById(R.id.progress);
        hider = findViewById(R.id.hider);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = mobile.getText().toString();
                String p = pass.getText().toString();
                if (e.length() > 3 && p.length() > 3) {
                    checkLogin(e, p);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter valid details", Toast.LENGTH_LONG).show();
                }
            }
        });
        unable_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ResetActivity.class));

            }
        });
    }

    private void checkLogin(String e, String p) {
        progress.setVisibility(View.VISIBLE);
        hider.setVisibility(View.VISIBLE);
        String login = Config.REQ_LOGIN;
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile_no", e);
        params.put("password", p);
        app.sendNetworkRequest(login, Request.Method.POST, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log("Response:" + response);
                if (response.equals("Fail")) {
                    Toast.makeText(getApplicationContext(), "Wrong mobile or password", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String id = obj.getString("id");
                        String perm= obj.getString("role");
                        pref.edit().putBoolean(PREF_KEY_LOGGED_IN, true).apply();
                        pref.edit().putString(PREF_KEY_USER_ID, id).apply();
                        pref.edit().putString(PREF_KEY_EMAIL, obj.getString("login_name")).apply();
                        pref.edit().putString(PREF_KEY_NAME, obj.getString("first_name") + " " + obj.getString("last_name")).apply();
                        pref.edit().putString(PREF_KEY_CONTACT, obj.getString("contact_mobile")).apply();
                        pref.edit().putString(PREF_KEY_PERMISSIONS,perm).apply();
                        App.userId = id;
                        App.permissions=perm;
                        JSONArray array = obj.getJSONArray("project_list");
                        Realm realm = Realm.getDefaultInstance();
                        for (int i = 0; i < array.length(); i++) {
                            final Project project = new Project().parseFromJSON(array.getJSONObject(i));
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealmOrUpdate(project);
                                }
                            });
                        }
                        realm.close();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
            }
        });
    }
}
