package buildnlive.com.buildlive.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.FreeLabour;

public class FreeLabourAdapter extends RecyclerView.Adapter<FreeLabourAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(FreeLabour items, int pos, View view);
        void onItemCheck(boolean checked);
        void onItemInteract(int pos, int flag);

    }

    private final List<FreeLabour> items;
    private Context context;
    private final OnItemClickListener listener;

    public FreeLabourAdapter(Context context, ArrayList<FreeLabour> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public FreeLabourAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_labour, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(context, items.get(position), position, listener);
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
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private EditText quantity;
        private CheckBox check;
        public static boolean CHECKOUT = false;
        private static int checkCount = 0;

        public ViewHolder(View view) {
            super(view);
            name= view.findViewById(R.id.name);
            quantity= view.findViewById(R.id.quantity);
            check= view.findViewById(R.id.check);

        }

        public void bind(final Context context, final FreeLabour item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getLabour_type());

            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        if (quantity.getText().toString().length() > 0) {
                            checkCount++;
                            listener.onItemCheck(true);
                            item.setUpdated(true);
                            item.setLabour_selected(quantity.getText().toString());
                        } else {
                            Toast.makeText(context, "Please enter correct quantity", Toast.LENGTH_SHORT).show();
                            compoundButton.setChecked(false);
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
//            item.setLabour_selected(quantity.getText().toString());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item,pos,itemView);
                }
            });
        }
    }

}