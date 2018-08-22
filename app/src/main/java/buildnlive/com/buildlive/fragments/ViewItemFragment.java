package buildnlive.com.buildlive.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.AddItemAdapter;
import buildnlive.com.buildlive.adapters.ViewItemAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Item;
import buildnlive.com.buildlive.utils.Config;

public class ViewItemFragment extends Fragment {
    private static List<Item> itemsList;
    private RecyclerView items;

    public static ViewItemFragment newInstance(List<Item> u) {
        itemsList = u;
        return new ViewItemFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        items = view.findViewById(R.id.items);
        final ViewItemAdapter adapter = new ViewItemAdapter(getContext(), itemsList);
        items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        items.setAdapter(adapter);
    }
}