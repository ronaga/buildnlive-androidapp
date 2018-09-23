package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.RequestList;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class ViewIndentAdapter extends RecyclerView.Adapter<ViewIndentAdapter.ViewHolder> {
    private final List<RequestList> items;
    private Context context;

    public ViewIndentAdapter(Context context, List<RequestList> users) {
        this.items = users;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_site_request, parent, false);
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

        private TextView name,unit,quantity, status;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            unit = view.findViewById(R.id.unit);
            quantity = view.findViewById(R.id.quantity);
            status = view.findViewById(R.id.status);
        }

        public void bind(final Context context, final RequestList item, final int pos) {
            name.setText(item.getName());
            unit.setText(item.getUnit());
            quantity.setText(item.getQuantity());
            status.setText(item.getStatus());
        }
    }
}