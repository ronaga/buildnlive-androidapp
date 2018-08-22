package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Item;
import buildnlive.com.buildlive.elements.Work;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class DailyWorkAdapter extends RealmRecyclerViewAdapter<Work, DailyWorkAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int pos, View view);
    }

    private final List<Work> items;
    private Context context;
    private final OnItemClickListener listener;

    public DailyWorkAdapter(Context context, OrderedRealmCollection<Work> works, OnItemClickListener listener) {
        super(works, true);
        this.items = works;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_daily_work, parent, false);
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
        private TextView name, completed, total, status;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            completed = view.findViewById(R.id.completed);
            total = view.findViewById(R.id.total);
            status = view.findViewById(R.id.status);
        }

        public void bind(final Context context, final Work item, final int pos, final OnItemClickListener listener) {
            name.setText(" " + item.getWorkName());
            completed.setText(item.getCompleted());
            total.setText(item.getTotal());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(pos, itemView);
                }
            });
            float total = Float.parseFloat(item.getTotal());
            float com = Float.parseFloat(item.getCompleted());
            status.setText((com / total * 100) + "% Completed");
        }
    }
}