package buildnlive.com.buildlive.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.ShowWorkListItem;

public class ShowWorkListAdapter extends RecyclerView.Adapter<ShowWorkListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String id);
    }

    private final List<ShowWorkListItem> items;
    private Context context;
    private final OnItemClickListener listener;

    public ShowWorkListAdapter(Context context, List<ShowWorkListItem> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_issue_item, parent, false);
        return new ViewHolder(v);
    }@Override
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

        private TextView name, extra;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            extra = view.findViewById(R.id.extra);
        }

        public void bind(final Context context, final ShowWorkListItem item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getWork_list_name());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item.getProject_work_id());
                }
            });
        }
    }
}