package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Activity;
import buildnlive.com.buildlive.elements.Work;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class DailyWorkActivityAdapter extends RecyclerView.Adapter<DailyWorkActivityAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int pos, View view);
    }

    private final List<Activity> items;
    private Context context;
    private final OnItemClickListener listener;

    public DailyWorkActivityAdapter(Context context, List<Activity> works, OnItemClickListener listener) {
        this.items = works;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_daily_work_activity, parent, false);
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

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, quantity, status, start, end, duration;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            quantity = view.findViewById(R.id.quantity);
            status = view.findViewById(R.id.status);
            start = view.findViewById(R.id.start);
            end = view.findViewById(R.id.end);
            duration = view.findViewById(R.id.duration);
        }

        public void bind(final Context context, final Activity item, final int pos, final OnItemClickListener listener) {
            name.setText(" " + item.getActivityName());
            quantity.setText(" " + item.getQuantity() + " " + item.getUnits());
            status.setText(" " + item.getStatus());
            start.setText(" " + item.getStart());
            end.setText(" " + item.getEnd());
            duration.setText(" " + item.getDuration());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(pos, itemView);
                }
            });
        }
    }
}