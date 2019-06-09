package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Worker;
import buildnlive.com.buildlive.utils.Utils;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

import static buildnlive.com.buildlive.adapters.AttendanceAdapter.RESET_ATTENDANCE_LIMIT_MIN;

public class ViewAttendanceAdapter extends RealmRecyclerViewAdapter<Worker, ViewAttendanceAdapter.ViewHolder> {
    private final List<Worker> items;
    private Context context;
    public interface OnItemClickListener {
        void onItemClick(Worker worker, int pos, View view);
    }


    private final OnItemClickListener listener;


    public ViewAttendanceAdapter(Context context, OrderedRealmCollection<Worker> workers, OnItemClickListener listener) {
        super(workers, true);
        this.items = workers;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_attendance, parent, false);
        return new ViewHolder(v);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(context, items.get(position), position,listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public static HashMap<String, Boolean> changedUsers = new HashMap<>();
        private TextView name, role, present,checkIn,checkOut;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            role = view.findViewById(R.id.role);
//            present = view.findViewById(R.id.present);
            checkIn = view.findViewById(R.id.check_in);
            checkOut = view.findViewById(R.id.check_out);
        }

        public void bind(final Context context, final Worker item, final int pos, OnItemClickListener listener) {
            name.setText(item.getName());
            role.setText(item.getRoleAssigned() + "(" + item.getType() + ")");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, pos, itemView);
                }
            });

            checkIn.setText(item.getCheckInTimeSelected());
            checkOut.setText(item.getCheckOutTimeSelected());

            int time_gap = Utils.differenceInMin(item.getCheckInTime(), System.currentTimeMillis());
            if (time_gap >= RESET_ATTENDANCE_LIMIT_MIN) {
                present.setText("Absent");
            }
            else if (item.getCheckInTime() > 0) {
                present.setText("Present");
            } else {
                present.setText("Absent");
            }
        }
    }
}