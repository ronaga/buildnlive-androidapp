package buildnlive.com.buildlive.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.ViewIssueAdapter;
import buildnlive.com.buildlive.elements.Issue;
import io.realm.Realm;

public class ViewIssueFragment extends Fragment {
    private static List<Issue> itemsList;
    private RecyclerView items;
    private Realm realm;

    public static ViewIssueFragment newInstance() {
        return new ViewIssueFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_issues, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        realm = Realm.getDefaultInstance();
        itemsList = realm.where(Issue.class).equalTo("belongsTo", App.belongsTo).findAll();
        items = view.findViewById(R.id.items);
        final ViewIssueAdapter adapter = new ViewIssueAdapter(getContext(), itemsList);
        items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        items.setAdapter(adapter);
    }
}