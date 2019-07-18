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
import buildnlive.com.buildlive.elements.CreateTransferObject;

public class CreateTransferAdapter extends RecyclerView.Adapter<CreateTransferAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(CreateTransferObject packet, int pos, View view);
    }

    private final List<CreateTransferObject> items;
    private Context context;
    private final OnItemClickListener listener;

    public CreateTransferAdapter(Context context, ArrayList<CreateTransferObject> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_create_transfer, parent, false);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

        public void bind(final Context context, final CreateTransferObject item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getItem_name());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, pos, itemView);
                }
            });
        }
    }
}