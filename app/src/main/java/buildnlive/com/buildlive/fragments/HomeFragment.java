package buildnlive.com.buildlive.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.activities.AddItem;
import buildnlive.com.buildlive.activities.Approval;
import buildnlive.com.buildlive.activities.IndentItems;
import buildnlive.com.buildlive.activities.IssuedItems;
import buildnlive.com.buildlive.activities.LabourActivity;
import buildnlive.com.buildlive.activities.LabourDeployment;
import buildnlive.com.buildlive.activities.LabourReportActivity;
import buildnlive.com.buildlive.activities.LocalPurchase;
import buildnlive.com.buildlive.activities.MachineList;
import buildnlive.com.buildlive.activities.MarkAttendance;
import buildnlive.com.buildlive.activities.MarkEmployeeAttendance;
import buildnlive.com.buildlive.activities.Planning;
import buildnlive.com.buildlive.activities.PurchaseOrder;
import buildnlive.com.buildlive.activities.RepairRequest;
import buildnlive.com.buildlive.activities.RequestItems;
import buildnlive.com.buildlive.activities.SitePayment;
import buildnlive.com.buildlive.activities.StoreRequest;
import buildnlive.com.buildlive.activities.TransferRequest;
import buildnlive.com.buildlive.activities.WorkProgress;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Project;
import buildnlive.com.buildlive.elements.ProjectMember;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.PrefernceFile;
import buildnlive.com.buildlive.utils.UtilityofActivity;
import io.realm.Realm;
import io.realm.RealmResults;

import static buildnlive.com.buildlive.utils.Config.PREF_NAME;

public class HomeFragment extends Fragment implements View.OnClickListener {
    //    private TextView title;
    private LinearLayout labour_deployment,sitePayment, approval, repairRequest, transferRequest, markAttendance, markEmployeeAttendance, manageInventory, issuedItems, requestItems, workProgress, purchaseOrder, siteRequest, localPurchase, labour, labourReport, planning, machine, storeRequest;
    private SharedPreferences pref;
    private Spinner projects;
    private static App app;
    private TextView badge;
    private Context context;
    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity;


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


        utilityofActivity = new UtilityofActivity(appCompatActivity);

        pref = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        markAttendance = view.findViewById(R.id.mark_attendance);
        markEmployeeAttendance = view.findViewById(R.id.mark_emp_attendance);
        manageInventory = view.findViewById(R.id.manage_inventory);
        issuedItems = view.findViewById(R.id.issued_items);
        requestItems = view.findViewById(R.id.request_items);
        projects = view.findViewById(R.id.projects);
        siteRequest = view.findViewById(R.id.request_list);
        localPurchase = view.findViewById(R.id.local_purchase);
        sitePayment = view.findViewById(R.id.site_payment);
        labour = view.findViewById(R.id.labour);
        labourReport = view.findViewById(R.id.manage_labour);
        planning = view.findViewById(R.id.planning);
        machine = view.findViewById(R.id.machine);
        transferRequest = view.findViewById(R.id.transfer_request);
        repairRequest = view.findViewById(R.id.repair);
        approval = view.findViewById(R.id.approve);
        storeRequest = view.findViewById(R.id.storeRequest);
        labour_deployment = view.findViewById(R.id.labour_deployment);

        badge = getActivity().findViewById(R.id.badge_notification);

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
                App.projectName = projects.get(position).getName();
                syncProject();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        title = view.findViewById(R.id.title);
        workProgress = view.findViewById(R.id.work_progress);
        purchaseOrder = view.findViewById(R.id.purchase);

        markAttendance.setOnClickListener(this);
        markEmployeeAttendance.setOnClickListener(this);
        manageInventory.setOnClickListener(this);
        issuedItems.setOnClickListener(this);
        requestItems.setOnClickListener(this);
        workProgress.setOnClickListener(this);
        purchaseOrder.setOnClickListener(this);
        siteRequest.setOnClickListener(this);
        localPurchase.setOnClickListener(this);
        labour.setOnClickListener(this);
        labourReport.setOnClickListener(this);
        planning.setOnClickListener(this);
        machine.setOnClickListener(this);
        transferRequest.setOnClickListener(this);
        repairRequest.setOnClickListener(this);
        approval.setOnClickListener(this);
        storeRequest.setOnClickListener(this);
        sitePayment.setOnClickListener(this);
        labour_deployment.setOnClickListener(this);


        ArrayList<String> permissionList = PrefernceFile.Companion.getInstance(context).getArrayList("Perm");

        for (String permission : permissionList) {
            switch (permission) {
                case "Work": {
                    workProgress.setVisibility(View.VISIBLE);
                    break;
                }
                case "Progress": {
                    planning.setVisibility(View.VISIBLE);
                    break;
                }
                case "Work Schedule": {
                    workProgress.setVisibility(View.VISIBLE);
                    break;
                }
                case "Labour Progress": {
                    labourReport.setVisibility(View.VISIBLE);
                    break;
                }
                case "Inventory": {
                    manageInventory.setVisibility(View.VISIBLE);
                    break;
                }
                case "Indent Item": {
                    manageInventory.setVisibility(View.VISIBLE);
                    break;
                }
                case "Issue Item": {
                    issuedItems.setVisibility(View.VISIBLE);
                    break;
                }
                case "Receive Item": {
                    purchaseOrder.setVisibility(View.VISIBLE);
                    break;
                }
                case "Local Purchase": {
                    localPurchase.setVisibility(View.VISIBLE);
                    break;
                }
                case "Site Payments": {
                    sitePayment.setVisibility(View.VISIBLE);
                    break;
                }
                case "Labour Mgmt": {
                    labour.setVisibility(View.VISIBLE);
                    break;
                }
                case "Assets": {
                    machine.setVisibility(View.VISIBLE);
                    break;
                }
                case "Request": {
                    requestItems.setVisibility(View.VISIBLE);
                    break;
                }
                case "Inventory Stock Request": {
                    siteRequest.setVisibility(View.VISIBLE);
                    break;
                }
                case "Repair": {
                    repairRequest.setVisibility(View.VISIBLE);
                    break;
                }
                case "Transfer": {
                    transferRequest.setVisibility(View.VISIBLE);
                    break;
                }
                case "Attendance": {
                    markAttendance.setVisibility(View.VISIBLE);
                    markEmployeeAttendance.setVisibility(View.VISIBLE);
                    break;
                }
                case "Approvals": {
                    approval.setVisibility(View.VISIBLE);
                    break;
                }
                case "Store Request": {
                    storeRequest.setVisibility(View.VISIBLE);
                    break;
                }
                case "Labour Deployment": {
                    labour_deployment.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }

//        switch (App.permissions) {
//            case "Storekeeper":
//                labour.setVisibility(View.GONE);
//                siteRequest.setVisibility(View.GONE);
//                workProgress.setVisibility(View.GONE);
//                break;
//            case "Siteengineer":
//                markAttendance.setVisibility(View.GONE);
//                manageInventory.setVisibility(View.GONE);
//                purchaseOrder.setVisibility(View.GONE);
//                issuedItems.setVisibility(View.GONE);
//                localPurchase.setVisibility(View.GONE);
//
//                break;
//            case "Siteadmin":
//                siteRequest.setVisibility(View.GONE);
//                issuedItems.setVisibility(View.GONE);
//                purchaseOrder.setVisibility(View.GONE);
//                manageInventory.setVisibility(View.GONE);
//                workProgress.setVisibility(View.GONE);
//                labour.setVisibility(View.GONE);
//
//                break;
//            case "Siteincharge":
//                siteRequest.setVisibility(View.GONE);
//                labour.setVisibility(View.GONE);
//                break;
//            case "labourmanager":
//                workProgress.setVisibility(View.GONE);
//                purchaseOrder.setVisibility(View.GONE);
//                markAttendance.setVisibility(View.GONE);
//                manageInventory.setVisibility(View.GONE);
//                issuedItems.setVisibility(View.GONE);
//                requestItems.setVisibility(View.GONE);
//                workProgress.setVisibility(View.GONE);
//                purchaseOrder.setVisibility(View.GONE);
//                siteRequest.setVisibility(View.GONE);
//                localPurchase.setVisibility(View.GONE);
//                labour.setVisibility(View.GONE);
//                break;
//
//        }

//            title.setText("Welcome " + pref.getString(PREF_KEY_NAME, "Dummy").split(" ")[0]);


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
            case R.id.mark_emp_attendance:
                startActivity(new Intent(getContext(), MarkEmployeeAttendance.class));
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
            case R.id.request_list:
                startActivity(new Intent(getContext(), IndentItems.class));
                break;
            case R.id.local_purchase:
                startActivity(new Intent(getContext(), LocalPurchase.class));
                break;
            case R.id.site_payment:
                startActivity(new Intent(getContext(), SitePayment.class));
                break;
            case R.id.labour:
                startActivity(new Intent(getContext(), LabourActivity.class));
                break;
            case R.id.manage_labour:
                startActivity(new Intent(getContext(), LabourReportActivity.class));
                break;
            case R.id.planning:
                startActivity(new Intent(getContext(), Planning.class));
                break;
            case R.id.machine:
                startActivity(new Intent(getContext(), MachineList.class));
                break;
            case R.id.transfer_request:
                startActivity(new Intent(getContext(), TransferRequest.class));
                break;
            case R.id.repair:
                startActivity(new Intent(getContext(), RepairRequest.class));
                break;
            case R.id.approve:
                startActivity(new Intent(getContext(), Approval.class));
                break;
            case R.id.storeRequest:
                startActivity(new Intent(getContext(), StoreRequest.class));
                break;
            case R.id.labour_deployment:
                startActivity(new Intent(getContext(), LabourDeployment.class));
                break;

        }
    }

    private void sendRequest() throws JSONException {
        App app = ((App) getActivity().getApplication());
        HashMap<String, String> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("project_id", App.projectId).put("user_id", App.userId);
        params.put("notification_count", jsonObject.toString());
        console.log("Res:" + params);
        app.sendNetworkRequest(Config.GET_NOTIFICATIONS_COUNT, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                if (response.equals("0")) {
                    badge.setVisibility(View.GONE);
                } else {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(response);
                }
            }
        });
    }
}