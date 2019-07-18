package buildnlive.com.buildlive.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.LabourModel;

public class BreakUpAdapter extends RecyclerView.Adapter<BreakUpAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(LabourModel items, int pos, View view);
        void onItemCheck(boolean checked);
        void onItemInteract(int pos, int flag);

    }

    private final List<LabourModel> items;
    private Context context;
    private final OnItemClickListener listener;

    public BreakUpAdapter(Context context, ArrayList<LabourModel> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
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
    public BreakUpAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_labour_report_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(context, items.get(position), position, listener);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView quantity;

        public ViewHolder(View view) {
            super(view);
            name= view.findViewById(R.id.name);
            quantity= view.findViewById(R.id.quantity);

        }

        public void bind(final Context context, final LabourModel item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getName());
            quantity.setText(item.getQuantity());
        }
    }

}