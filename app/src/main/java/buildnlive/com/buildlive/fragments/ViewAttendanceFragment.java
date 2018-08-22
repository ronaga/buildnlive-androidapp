package buildnlive.com.buildlive.fragments;

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

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.ViewAttendanceAdapter;
import buildnlive.com.buildlive.elements.Worker;
import io.realm.RealmResults;

public class ViewAttendanceFragment extends Fragment {
    private RecyclerView attendees;
    private ProgressBar progress;
    private TextView hider;
    private static RealmResults<Worker> workers;
    private ViewAttendanceAdapter adapter;
    private boolean LOADING;

    public static ViewAttendanceFragment newInstance(RealmResults<Worker> u) {
        workers = u;
        return new ViewAttendanceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_attendance, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attendees = view.findViewById(R.id.attendees);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
        adapter = new ViewAttendanceAdapter(getContext(), workers);
        attendees.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        attendees.setAdapter(adapter);
        if (LOADING)
            progress.setVisibility(View.VISIBLE);
        else
            progress.setVisibility(View.GONE);
    }
}