package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Worker;
import buildnlive.com.buildlive.utils.Utils;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class AttendanceAdapter extends RealmRecyclerViewAdapter<Worker, AttendanceAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Worker worker, int pos, View view);
    }

    public static final int RESET_ATTENDANCE_LIMIT_MIN = 900;
    public static final int CHECKOUT_TIME_GAP_MIN = 30;
    private final List<Worker> items;
    private Context context;
    private final OnItemClickListener listener;

    public AttendanceAdapter(Context context, OrderedRealmCollection<Worker> workers, OnItemClickListener listener) {
        super(workers, true);
        this.items = workers;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_attendance, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(context, items.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public static HashMap<String, Boolean> changedUsers = new HashMap<>();
        private TextView name, role;
        private CheckBox check_in, check_out;
        private boolean reset = false;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            role = view.findViewById(R.id.role);
            check_in = view.findViewById(R.id.check_in);
            check_out = view.findViewById(R.id.check_out);
        }

        public void bind(final Context context, final Worker item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getName());
            role.setText(item.getRoleAssigned() + "(" + item.getType() + ")");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, pos, itemView);
                }
            });
            check_in.setOnCheckedChangeListener(null);
            check_out.setOnCheckedChangeListener(null);

            int time_gap = Utils.differenceInMin(item.getCheckInTime(), System.currentTimeMillis());

            if (time_gap >= RESET_ATTENDANCE_LIMIT_MIN) {
                check_in.setEnabled(true);
                check_out.setEnabled(false);
                check_in.setChecked(false);
                check_out.setChecked(false);
                reset = true;
            } else if (item.isCheckedIn() && time_gap >= CHECKOUT_TIME_GAP_MIN) {
                check_out.setEnabled(true);
            } else {
                check_out.setEnabled(false);
            }

            if (!reset && item.isCheckedIn() && item.getCheckInTime() > 0) {
                check_in.setChecked(true);
                check_in.setEnabled(false);
            }

            if (!reset && item.isCheckedOut() && item.getCheckOutTime() > 0) {
                check_out.setChecked(true);
                check_out.setEnabled(false);
            }

            check_in.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        changedUsers.put(item.getId(), false);
                    else
                        changedUsers.remove(item.getId());
                }
            });

            check_out.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        changedUsers.put(item.getId(), true);
                    else
                        changedUsers.remove(item.getId());
                }
            });
        }
    }
}