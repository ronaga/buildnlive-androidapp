package buildnlive.com.buildlive.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.ViewAttendance;

public class ViewLiveAttendanceAdapter extends RecyclerView.Adapter<ViewLiveAttendanceAdapter.ViewHolder> {
    private final List<ViewAttendance> items;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(ViewAttendance worker, int pos, View view);
    }


    private final OnItemClickListener listener;


    public ViewLiveAttendanceAdapter(Context context, ArrayList<ViewAttendance> workers, OnItemClickListener listener) {
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
        holder.bind(context, items.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public static HashMap<String, Boolean> changedUsers = new HashMap<>();
        private TextView name, role, present, checkIn, checkOut;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            role = view.findViewById(R.id.role);
//            present = view.findViewById(R.id.present);
            checkIn = view.findViewById(R.id.check_in);
            checkOut = view.findViewById(R.id.check_out);
        }

        public void bind(final Context context, final ViewAttendance item, final int pos, OnItemClickListener listener) {
            name.setText(item.getLabour_name());
            role.setText(String.format(context.getString(R.string.workerHolder),item.getLabour_role(),item.getLabour_type()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, pos, itemView);
                }
            });

            checkIn.setText(item.getStart_time());
            checkOut.setText(item.getEnd_time());

            if(item.getStart_time().equals("0"))
            {
                checkIn.setText("X");
            }
            if (item.getEnd_time().equals("0")) {
                checkOut.setText("X");
            }
        }
    }
}