package buildnlive.com.buildlive.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Item;
import buildnlive.com.buildlive.fragments.AddItemFragment;
import buildnlive.com.buildlive.fragments.IssueItemFragment;
import buildnlive.com.buildlive.fragments.ViewItemFragment;
import buildnlive.com.buildlive.utils.Config;

public class AddItem extends AppCompatActivity {
    private List<Item> itemsList = new ArrayList<>();
    private App app;
    private TextView edit, view;
    private Fragment fragment;
    private Interfaces.SyncListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        app = (App) getApplication();
        edit = findViewById(R.id.edit);
        view = findViewById(R.id.view);
        fragment = AddItemFragment.newInstance(itemsList);
        listener = (Interfaces.SyncListener) fragment;
        changeScreen();
        edit = findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEdit();
                disableView();
                fragment = AddItemFragment.newInstance(itemsList);
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
                fragment = ViewItemFragment.newInstance(itemsList);
                listener = null;
                changeScreen();
            }
        });
        refresh();
    }

    private void refresh() {
        String requestUrl = Config.REQ_GET_INVENTORY;
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
                Toast.makeText(getApplicationContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                if (listener != null) {
                    listener.onSyncFinish();
                }
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        itemsList.add(new Item().parseFromJSON(array.getJSONObject(i)));
                    }
                    if (listener != null)
                        listener.onSync(itemsList);
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
}
