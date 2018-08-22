package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Request;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class ViewRequestAdapter extends RealmRecyclerViewAdapter<Request, ViewRequestAdapter.ViewHolder> {
    private final List<Request> items;
    private Context context;

    public ViewRequestAdapter(Context context, OrderedRealmCollection<Request> users) {
        super(users, true);
        this.items = users;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_request, parent, false);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, description, material, quantity, status;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            quantity = view.findViewById(R.id.quantity);
            material = view.findViewById(R.id.material);
            status = view.findViewById(R.id.status);
        }

        public void bind(final Context context, final Request item, final int pos) {
            name.setText(item.getName());
            description.setText(item.getDescription());
            material.setText(item.getMaterial());
            quantity.setText(item.getQuantity() + " Unit");
            status.setText("Pending");
        }
    }
}