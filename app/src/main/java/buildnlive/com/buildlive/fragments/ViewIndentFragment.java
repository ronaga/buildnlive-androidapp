package buildnlive.com.buildlive.fragments;

import android.app.Activity;
import android.content.Context;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.ViewIndentAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.RequestList;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class ViewIndentFragment extends Fragment {
    private RecyclerView items;
    private static App app;
    private ProgressBar progress;
    private TextView hider;
    private static List<RequestList> requestList =new ArrayList<>();
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

    public static ViewIndentFragment newInstance(App a) {
        app = a;
        return new ViewIndentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_indent_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        utilityofActivity=new UtilityofActivity(appCompatActivity);
        items = view.findViewById(R.id.requests);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);

        refresh();
    }

    private void refresh() {
        requestList.clear();
        String url = Config.SEND_REQUEST_STATUS;
        url = url.replace("[0]", App.userId);
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
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject obj = array.getJSONObject(i);
                        requestList.add(new RequestList().parseFromJSON(obj));

//                        realm.executeTransaction(new Realm.Transaction() {
//                            @Override
//                            public void execute(Realm realm) {
//                                try {
//                                    RequestList request = new RequestList().parseFromJSONPlan(obj);
//                                    realm.copyToRealmOrUpdate(request);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
                    }
                    ViewIndentAdapter adapter = new ViewIndentAdapter(getContext(), requestList);
                    items.setAdapter(adapter);
                    items.setLayoutManager(new LinearLayoutManager(getContext()));
//                    realm.close();

                } catch (JSONException e) {

                }
            }
        });
    }
}