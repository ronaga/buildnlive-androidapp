package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.WorkerHistory;

public class WorkerHistoryAdapter extends RecyclerView.Adapter<WorkerHistoryAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(WorkerHistory packet, int pos, View view);
    }

    private final List<WorkerHistory> items;
    private Context context;
    private final OnItemClickListener listener;

    public WorkerHistoryAdapter(Context context, List<WorkerHistory> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_worker_history, parent, false);
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

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView date, checkinTime, checkOutTime;
        private ImageView delete;

        public ViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            delete = view.findViewById(R.id.delete);
            checkinTime = view.findViewById(R.id.checkinTime);
            checkOutTime = view.findViewById(R.id.checkOutTime);
        }

        public void bind(final Context context, final WorkerHistory item, final int pos, final OnItemClickListener listener) {
            if (item.getStartDateTime() == null)
                date.setText("NONE");
            else {
                date.setText(item.getStartDateTime());
            }
            if (item.getStartTime() == null)
                checkinTime.setText("NONE");
            else {
                checkinTime.setText(item.getStartTime());
            }
            if (item.getEndTime() == null)
                checkOutTime.setText("NONE");
            else {
                checkOutTime.setText(item.getEndTime());
            }

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item,pos,delete);
                }
            });
        }

    }

}