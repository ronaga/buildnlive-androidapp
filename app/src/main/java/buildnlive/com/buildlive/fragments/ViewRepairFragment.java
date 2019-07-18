package buildnlive.com.buildlive.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.activities.ReturnRepairItem;
import buildnlive.com.buildlive.adapters.ViewRepairStatusAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.ViewRepairItem;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;
import io.realm.Realm;

public class ViewRepairFragment extends Fragment {
    private static List<ViewRepairItem> itemsList=new ArrayList<>();
    private RecyclerView items;
    private Realm realm;
    private ProgressBar progress;
    private static App app;
    private TextView hider;
    private ViewRepairStatusAdapter adapter;
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

    public static ViewRepairFragment newInstance(App a) {
        app=a;
        return new ViewRepairFragment();
    }

    private ViewRepairStatusAdapter.OnItemClickListener listner=new ViewRepairStatusAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(ViewRepairItem items, int pos, View view) {
            Bundle bundle= new Bundle();
            bundle.putSerializable("Item",items);
            Intent intent= new Intent(getActivity(), ReturnRepairItem.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_repairs, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        realm = Realm.getDefaultInstance();
//        itemsList = realm.where(Issue.class).equalTo("belongsTo", App.belongsTo).findAll();
        items = view.findViewById(R.id.items);
        utilityofActivity= new UtilityofActivity(appCompatActivity);
        progress=view.findViewById(R.id.progress);
        hider=view.findViewById(R.id.hider);
        adapter = new ViewRepairStatusAdapter(getContext(), itemsList,listner);
        items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        items.setAdapter(adapter);
        refresh();
    }


    private void refresh() {
        itemsList.clear();
        String url = Config.SEND_REPAIR_STATUS;
        url = url.replace("[0]", App.userId);
        url = url.replace("[1]", App.projectId);
        app.sendNetworkRequest(url, 0, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log("Response:" + response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
//                Realm realm = Realm.getDefaultInstance();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        itemsList.add(new ViewRepairItem().parseFromJSON(array.getJSONObject(i)));
                    }

                } catch (JSONException e) {
                }
                adapter = new ViewRepairStatusAdapter(getContext(), itemsList,listner);
                items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                items.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }
}