package buildnlive.com.buildlive.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.ViewRepairItem;

public class ViewRepairStatusAdapter extends RecyclerView.Adapter<ViewRepairStatusAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ViewRepairItem items, int pos, View view);
    }

    private final List<ViewRepairItem> items;
    private Context context;
    private final OnItemClickListener listener;

    public ViewRepairStatusAdapter(Context context, List<ViewRepairItem> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_repair, parent, false);
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

        private TextView name, date, quantity, status, return_item;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            date = view.findViewById(R.id.date);
            quantity = view.findViewById(R.id.quantity);
            status = view.findViewById(R.id.status);
            return_item = view.findViewById(R.id.return_item);
        }

        public void bind(final Context context, final ViewRepairItem item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getName());
            date.setText(item.getDate());
            quantity.setText(item.getQty());
            status.setText(item.getStatus());

            return_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item, pos, return_item);
                }
            });
        }
    }
}