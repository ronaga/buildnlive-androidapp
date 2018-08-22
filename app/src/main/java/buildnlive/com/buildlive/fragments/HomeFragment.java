package buildnlive.com.buildlive.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.activities.AddItem;
import buildnlive.com.buildlive.activities.IssuedItems;
import buildnlive.com.buildlive.activities.MarkAttendance;
import buildnlive.com.buildlive.activities.PurchaseOrder;
import buildnlive.com.buildlive.activities.RequestItems;
import buildnlive.com.buildlive.activities.WorkProgress;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Project;
import buildnlive.com.buildlive.elements.ProjectMember;
import buildnlive.com.buildlive.utils.Config;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static buildnlive.com.buildlive.activities.LoginActivity.PREF_KEY_NAME;
import static buildnlive.com.buildlive.utils.Config.PREF_NAME;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private TextView title;
    private LinearLayout markAttendance, manageInventory, issuedItems, requestItems, workProgress, purchaseOrder;
    private SharedPreferences pref;
    private Spinner projects;
    private static App app;

    public static HomeFragment newInstance(App a) {
        app = a;
        return new HomeFragment();
    }

    private ArrayList<String> userProjects = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");
        markAttendance = view.findViewById(R.id.mark_attendance);
        manageInventory = view.findViewById(R.id.manage_inventory);
        pref = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        issuedItems = view.findViewById(R.id.issued_items);
        requestItems = view.findViewById(R.id.request_items);
        projects = view.findViewById(R.id.projects);
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Project> projects = realm.where(Project.class).findAll();
        for (Project p : projects) {
            userProjects.add(p.getName());
        }
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, userProjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.projects.setAdapter(adapter);
        this.projects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                App.projectId = projects.get(position).getId();
                App.belongsTo = App.projectId + App.userId;
                syncProject();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        title = view.findViewById(R.id.title);
        workProgress = view.findViewById(R.id.work_progress);
        purchaseOrder = view.findViewById(R.id.purchase);
        markAttendance.setOnClickListener(this);
        manageInventory.setOnClickListener(this);
        issuedItems.setOnClickListener(this);
        requestItems.setOnClickListener(this);
        workProgress.setOnClickListener(this);
        purchaseOrder.setOnClickListener(this);
        title.setText("Welcome " + pref.getString(PREF_KEY_NAME, "Dummy").split(" ")[0]);
    }

    private void syncProject() {
        String url = Config.REQ_SYNC_PROJECT;
        url = url.replace("[0]", App.userId).replace("[1]", App.projectId);
        app.sendNetworkRequest(url, 0, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    final JSONArray array = obj.getJSONArray("project_members");
                    Realm realm = Realm.getDefaultInstance();
                    for (int i = 0; i < array.length(); i++) {
                        final ProjectMember member = new ProjectMember().parseFromJSON(array.getJSONObject(i));
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(member);
                            }
                        });
                    }
                    realm.close();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mark_attendance:
                startActivity(new Intent(getContext(), MarkAttendance.class));
                break;
            case R.id.manage_inventory:
                startActivity(new Intent(getContext(), AddItem.class));
                break;
            case R.id.issued_items:
                startActivity(new Intent(getContext(), IssuedItems.class));
                break;
            case R.id.request_items:
                startActivity(new Intent(getContext(), RequestItems.class));
                break;
            case R.id.work_progress:
                startActivity(new Intent(getContext(), WorkProgress.class));
                break;
            case R.id.purchase:
                startActivity(new Intent(getContext(), PurchaseOrder.class));
                break;
        }
    }
}