package buildnlive.com.buildlive.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.activities.ReceiveImpressItem;
import buildnlive.com.buildlive.adapters.ImpressItemAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.ImpressItem;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;
import io.realm.Realm;

public class ImpressFragment extends Fragment {
    private static List<ImpressItem> itemsList = new ArrayList<>();
    private RecyclerView items;
    private Realm realm;
    private ProgressBar progress;
    private static App app;
    private TextView hider;
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

    public static ImpressFragment newInstance(App a) {
        app = a;
        return new ImpressFragment();
    }

    private ImpressItemAdapter.OnItemClickListener listner = new ImpressItemAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(ImpressItem items, int pos, View view) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("Item", items);
            Intent intent = new Intent(getActivity(), ReceiveImpressItem.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_impress_items, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        utilityofActivity = new UtilityofActivity(appCompatActivity);
//        realm = Realm.getDefaultInstance();
//        itemsList = realm.where(Issue.class).equalTo("belongsTo", App.belongsTo).findAll();
        items = view.findViewById(R.id.items);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
        ImpressItemAdapter adapter = new ImpressItemAdapter(getContext(), itemsList, listner);
        items.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        items.setAdapter(adapter);
        refresh();
    }


    private void refresh() {
        itemsList.clear();
        String url = Config.RECEIVE_IMPRESS;
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


                        Type vendorType = new TypeToken<ArrayList<ImpressItem>>() {
                        }.getType();
                        itemsList = new Gson().fromJson(response, vendorType);

//                        realm.executeTransaction(new Realm.Transaction() {
//                            @Override
//                            public void execute(Realm realm) {
//                                try {
//                                    RequestList request = new RequestList().parseFromJSON(obj);
//                                    realm.copyToRealmOrUpdate(request);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
                ImpressItemAdapter adapter = new ImpressItemAdapter(getContext(), itemsList, listner);
                items.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                items.setAdapter(adapter);
                adapter.notifyDataSetChanged();
//                for(ImpressItem i: itemsList){
//                    console.log(i.getItemName());
//                }
            }
        });
    }
}