package buildnlive.com.buildlive.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.OrderItem;

public class PurchaseOrderListingAdapter extends RecyclerView.Adapter<PurchaseOrderListingAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int pos, View view);
    }

    private final List<OrderItem> items;
    private Context context;
    private final OnItemClickListener listener;

    public PurchaseOrderListingAdapter(Context context, List<OrderItem> items, OnItemClickListener listener) {
        this.items = items;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_purchase_order_item, parent, false);
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

        private CheckBox check;
        private TextView name, quantity, unit, max;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            quantity = view.findViewById(R.id.quantity);
            unit = view.findViewById(R.id.unit);
            max = view.findViewById(R.id.max_quantity);
            check = view.findViewById(R.id.check);
        }

        public void bind(final Context context, final OrderItem item, final int pos, final OnItemClickListener listener) {

//            final String max_qty=item.getQuantity();
            item.setMax_qty(item.getQuantity());

            name.setText(item.getName());
            quantity.setText(item.getQuantity());
            quantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
//                    if(Integer.parseInt(item.getQuantity())<=Integer.parseInt(max_qty))
//                    {
                        item.setQuantity(s.toString());
//                    }
//                    else
//                    {
//                        Toast.makeText(context,"Enter Valid Quantity",Toast.LENGTH_LONG).show();
//                        item.setIncluded(false);
//                        check.setChecked(false);
//                    }
                }
            });
            unit.setText(item.getUnit());
            max.setText(item.getQuantity() + " " + item.getUnit());
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.setIncluded(isChecked);
                }
            });
            item.setIncluded(true);
        }
    }
}