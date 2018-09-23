package buildnlive.com.buildlive.activities;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.elements.Work;
import buildnlive.com.buildlive.elements.Worker;
import buildnlive.com.buildlive.fragments.MarkAttendanceFragment;
import buildnlive.com.buildlive.fragments.ViewAttendanceFragment;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.console;
import io.realm.Realm;
import io.realm.RealmResults;

public class MarkAttendance extends AppCompatActivity {
    private App app;
    private Realm realm;
    private TextView edit, view;
    private Fragment fragment;
    private Interfaces.SyncListener listener;
    private RealmResults<Worker> workers;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);
        back =findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        app = (App) getApplication();
        realm = Realm.getDefaultInstance();
        workers = realm.where(Worker.class).equalTo("belongsTo", App.belongsTo).findAllAsync();
        fragment = MarkAttendanceFragment.newInstance(app, workers);
        listener = (Interfaces.SyncListener) fragment;
        changeScreen();
        edit = findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEdit();
                disableView();
                fragment = MarkAttendanceFragment.newInstance(app, workers);
                listener = (Interfaces.SyncListener) fragment;
                changeScreen();
            }
        });
        view = findViewById(R.id.view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableView();
                disableEdit();
                fragment = ViewAttendanceFragment.newInstance(workers);
                listener = null;
                changeScreen();
            }
        });
        refresh();
    }

    private void disableView() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.round_left, null));
        } else {
            view.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_left));
        }
        view.setTextColor(getResources().getColor(R.color.color2));
    }

    private void enableView() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.round_left_selected, null));
        } else {
            view.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_left_selected));
        }
        view.setTextColor(getResources().getColor(R.color.white));
    }

    private void disableEdit() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            edit.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.round_right, null));
        } else {
            edit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_right));
        }
        edit.setTextColor(getResources().getColor(R.color.color2));
    }

    private void enableEdit() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            edit.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.round_right_selected, null));
        } else {
            edit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_right_selected));
        }
        edit.setTextColor(getResources().getColor(R.color.white));
    }

    private void refresh() {
        String requestUrl = Config.REQ_GET_LABOUR;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", App.projectId);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                if (listener != null) {
                    listener.onSyncStart();
                }
            }

            @Override
            public void onNetworkRequestError(String error) {
                if (listener != null) {
                    listener.onSyncError(error);
                }
                console.error("Network request failed with error :" + error);
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNetworkRequestComplete(final String response) {
                if (listener != null) {
                    listener.onSyncFinish();
                }
                console.log("Response:" + response);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        final Worker worker = new Worker().parseFromJSON(array.getJSONObject(i));
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Worker u = realm.where(Worker.class).equalTo("id", worker.getId()).findFirst();
                                if (u == null) {
                                    realm.copyToRealm(worker);
                                }
                            }
                        });
                    }
                    if (listener != null) {
                        listener.onSync(workers);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void changeScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.attendance_content, fragment)
                .commit();
    }
}
