package buildnlive.com.buildlive.adapters;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Item;

public class LocalPurchaseListAdapter extends RecyclerView.Adapter<LocalPurchaseListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Item packet, int pos, View view);

        void onItemCheck(boolean checked, Item item, View itemView);

    }

    private final List<Item> items;
    private Context context;
    private final OnItemClickListener listener;

    public LocalPurchaseListAdapter(Context context, List<Item> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_issue_item, parent, false);
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

        private TextView name, extra;
        private CheckBox check;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            extra = view.findViewById(R.id.extra);
            check = view.findViewById(R.id.checkItem);
        }

        public void bind(final Context context, final Item item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getName());
            if (item.isUpdated()) {
                check.setChecked(true);
                itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.razorgreen));
            }
            else {
                check.setChecked(false);
                itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, pos, itemView);
                }
            });

            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    listener.onItemCheck(isChecked, item, itemView);
                }
            });

        }
    }
}