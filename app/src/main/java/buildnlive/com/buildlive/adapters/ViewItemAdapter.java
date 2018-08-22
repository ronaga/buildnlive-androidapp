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

public class ViewItemAdapter extends RecyclerView.Adapter<ViewItemAdapter.ViewHolder> {
    private final List<Item> items;
    private Context context;

    public ViewItemAdapter(Context context, List<Item> users) {
        this.items = users;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(context, items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, quantity;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            quantity = view.findViewById(R.id.quantity);
        }

        public void bind(final Context context, final Item item, final int pos) {
            name.setText(item.getName());
            quantity.setText(item.getQuantity() + " " + item.getUnit());
        }
    }
}