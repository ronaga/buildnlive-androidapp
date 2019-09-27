package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.LeaveHistoryItem;

public class TakeLeaveAdapter extends RecyclerView.Adapter<TakeLeaveAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(LeaveHistoryItem item, int pos, View view);
    }


    private final ArrayList<LeaveHistoryItem> items;
    private Context context;
    private OnItemClickListener listener;

    public TakeLeaveAdapter(Context context, ArrayList<LeaveHistoryItem> items, OnItemClickListener listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_take_leave_item, parent, false);
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

        private TextView status, date;

        public ViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            status = view.findViewById(R.id.status);
        }

        public void bind(final Context context, final LeaveHistoryItem item, final int pos, OnItemClickListener listener) {
            date.setText(item.getLeaveDates());

            switch (item.getStatus()) {
                case "Approved": {
                    status.setTextColor(ContextCompat.getColor(context, R.color.material_green));
                    break;
                }
                case "Cancelled": {
                    status.setTextColor(ContextCompat.getColor(context, R.color.material_red));
                    break;
                }
                case "On Hold": {
                    status.setTextColor(ContextCompat.getColor(context, R.color.c4));
                    break;
                }
                case "Pending": {
                    status.setTextColor(ContextCompat.getColor(context, R.color.c2));
                    break;
                }
                default: {
                    status.setTextColor(ContextCompat.getColor(context, R.color.black));
                    break;
                }
            }

            status.setText(item.getStatus());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item, pos, itemView);
                }
            });

        }
    }
}