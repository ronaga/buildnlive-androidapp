package buildnlive.com.buildlive.Approvals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.Server.Response.EditLabourReportResponseData;

public class EditLabourReportAdapter extends RecyclerView.Adapter<EditLabourReportAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(EditLabourReportResponseData items, int pos, View view);

        void onItemCheck(boolean checked);

        void onItemInteract(int pos, int flag);

    }

    private final List<EditLabourReportResponseData> items;
    private Context context;
    private final OnItemClickListener listener;

    public EditLabourReportAdapter(Context context, ArrayList<EditLabourReportResponseData> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public EditLabourReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
            name = view.findViewById(R.id.name);
            quantity = view.findViewById(R.id.quantity);
            check = view.findViewById(R.id.check);

        }

        public void bind(final Context context, final EditLabourReportResponseData item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getLabourType());
            quantity.setText(item.getCount());

            check.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    if (quantity.getText().toString().length() > 0) {
                        checkCount++;
                        listener.onItemCheck(true);
                        item.setUpdated(true);
                        item.setCount(quantity.getText().toString());
                    } else {
                        Toast.makeText(context, "Please enter quantity", Toast.LENGTH_SHORT).show();
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
            });
            item.setCount(quantity.getText().toString());
            itemView.setOnClickListener(view -> listener.onItemClick(item, pos, itemView));
        }
    }

}
