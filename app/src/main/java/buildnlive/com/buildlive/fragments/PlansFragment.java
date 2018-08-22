package buildnlive.com.buildlive.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.activities.PreviewImage;
import buildnlive.com.buildlive.adapters.PlansAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Plans;
import buildnlive.com.buildlive.utils.Config;
import io.realm.Realm;
import io.realm.RealmResults;

public class PlansFragment extends Fragment {
    private RecyclerView recyclerView;
    private Realm realm;
    private static App app;
    private ProgressBar progress;
    private TextView hider;

    public static PlansFragment newInstance(App a) {
        app = a;
        return new PlansFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plans, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.plans);
        realm = Realm.getDefaultInstance();
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
        RealmResults<Plans> plans = realm.where(Plans.class).equalTo("belongsTo", App.belongsTo).findAllAsync();
        final PlansAdapter adapter = new PlansAdapter(getContext(), plans, new PlansAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Plans item, int pos, View view) {
                Intent preview = new Intent(getContext(), PreviewImage.class);
                preview.putExtra("image", item.getName());
                preview.putExtra("id", item.getId());
                startActivity(preview);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refresh();
    }

    int flag = 0;

    private void refresh() {
        if (app != null) {
            String requestUrl = Config.GET_PROJECT_PLANS;
            requestUrl = requestUrl.replace("[0]", App.projectId);
            requestUrl = requestUrl.replace("[1]", App.userId);
            app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
                @Override
                public void onNetworkRequestStart() {
                    progress.setVisibility(View.VISIBLE);
                    hider.setVisibility(View.VISIBLE);
                }

                @Override
                public void onNetworkRequestError(String error) {
                    progress.setVisibility(View.GONE);
                    hider.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNetworkRequestComplete(String response) {
                    console.log("Res:" + response);
                    progress.setVisibility(View.GONE);
                    hider.setVisibility(View.GONE);
                    try {
                        flag = 0;
                        JSONObject obj = new JSONObject(response);
                        final String path = obj.getString("folder_path");
                        for (int i = 0; ; i++) {
                            if (flag == 1)
                                break;
                            final JSONObject o = obj.getJSONObject("" + i);
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    try {
                                        Plans plans = new Plans(o.getString("id"), o.getString("image_name"), o.getString("description"), path + o.getString("image_name"));
                                        realm.copyToRealmOrUpdate(plans);
                                    } catch (JSONException e) {
                                        flag = 1;
                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {

                    }
                }
            });
        }
    }
}