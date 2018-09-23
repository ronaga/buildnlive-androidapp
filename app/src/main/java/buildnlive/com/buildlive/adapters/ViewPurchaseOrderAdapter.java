package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.activities.PurchaseOrder;
import buildnlive.com.buildlive.elements.Order;
import buildnlive.com.buildlive.elements.Worker;
import buildnlive.com.buildlive.utils.Utils;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class ViewPurchaseOrderAdapter extends RecyclerView.Adapter<ViewPurchaseOrderAdapter.ViewHolder> {
    private final List<Order> items;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Order item, int pos, View view);
    }

    public ViewPurchaseOrderAdapter(Context context, List<Order> workers, OnItemClickListener listener) {
        this.items = workers;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_purchase_order, parent, false);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, date, status;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            date = view.findViewById(R.id.date);
            status = view.findViewById(R.id.status);
        }

        public void bind(final Context context, final Order item, final int pos, final OnItemClickListener listener) {
            name.setText("Purchase Order-" + item.getSerialNo());
            date.setText(item.getDeliveryDate());
            status.setText(item.getStatus());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, pos, itemView);
                }
            });
        }
    }
}