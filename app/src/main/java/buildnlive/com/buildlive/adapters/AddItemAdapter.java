package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Item;

public class AddItemAdapter extends RecyclerView.Adapter<AddItemAdapter.ViewHolder> {

    public interface OnItemSelectedListener {
        void onItemCheck(boolean checked);

        void onItemInteract(int pos, int flag);
    }

    private List<Item> items;
    private Context context;
    private OnItemSelectedListener listener;

    public AddItemAdapter(Context context, List<Item> users, OnItemSelectedListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (ViewHolder.CHECKOUT) {
            if (items.get(position).isUpdated())
                holder.bind(context, items.get(position), position, listener);
        } else {
            holder.bind(context, items.get(position), position, listener);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private static int checkCount = 0;
        public static boolean CHECKOUT = false;
        private TextView name, unit;
        private EditText quantity;
        private CheckBox check;
        private ImageButton close;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            unit = view.findViewById(R.id.unit);
            check = view.findViewById(R.id.check);
            quantity = view.findViewById(R.id.quantity);
            close = view.findViewById(R.id.close);
        }

        public void bind(final Context context, final Item item, final int pos, final OnItemSelectedListener listener) {
            name.setText(item.getName());
            unit.setText(item.getUnit());
            if (CHECKOUT) {
                check.setChecked(true);
                check.setEnabled(false);
                quantity.setEnabled(false);
                quantity.setText(item.getQuantity());
                item.setUpdated(false);
                check.setVisibility(View.GONE);
                close.setVisibility(View.VISIBLE);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemInteract(pos, 100);
                    }
                });

            } else {
                check.setVisibility(View.VISIBLE);
                close.setVisibility(View.GONE);
                check.setChecked(false);
                check.setEnabled(true);
                quantity.setEnabled(true);
                check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (quantity.getText().toString().length() > 0) {
                                checkCount++;
                                listener.onItemCheck(true);
                                item.setUpdated(true);
                                item.setQuantity(quantity.getText().toString());
                            } else {
                                Toast.makeText(context, "Please enter quantity", Toast.LENGTH_SHORT).show();
                                buttonView.setChecked(false);
                            }
                        } else {
                            checkCount--;
                            item.setUpdated(false);
                            if (checkCount > 0) {
                                listener.onItemCheck(true);
                            } else {
                                listener.onItemCheck(false);
                            }
                        }
                    }
                });
            }
        }
    }
}