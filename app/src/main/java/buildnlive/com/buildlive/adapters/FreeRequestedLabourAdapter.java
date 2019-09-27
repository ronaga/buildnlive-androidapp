package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.LabourRequestFormItem;

public class FreeRequestedLabourAdapter extends RecyclerView.Adapter<buildnlive.com.buildlive.adapters.FreeRequestedLabourAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(LabourRequestFormItem.LabourList items, int pos, View view);

        void onItemCheck(boolean checked);

        void onItemInteract(int pos, int flag);

    }

    private final List<LabourRequestFormItem.LabourList> items;
    private Context context;
    private final buildnlive.com.buildlive.adapters.FreeRequestedLabourAdapter.OnItemClickListener listener;

    public FreeRequestedLabourAdapter(Context context, ArrayList<LabourRequestFormItem.LabourList> users, buildnlive.com.buildlive.adapters.FreeRequestedLabourAdapter.OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public buildnlive.com.buildlive.adapters.FreeRequestedLabourAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_labour_approve, parent, false);
        return new buildnlive.com.buildlive.adapters.FreeRequestedLabourAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull buildnlive.com.buildlive.adapters.FreeRequestedLabourAdapter.ViewHolder holder, int position) {
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
        private TextView name, labourRequested;
        private EditText labourApproved;
        private CheckBox check;
        public static boolean CHECKOUT = false;
        private static int checkCount = 0;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            labourRequested = view.findViewById(R.id.labourRequested);
            labourApproved = view.findViewById(R.id.labourApproved);
            check = view.findViewById(R.id.check);

        }

        public void bind(final Context context, final LabourRequestFormItem.LabourList item, final int pos, final buildnlive.com.buildlive.adapters.FreeRequestedLabourAdapter.OnItemClickListener listener) {
            name.setText(item.getTypeName());
            labourRequested.setText(item.getLabourRequested());

            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        if (labourApproved.getText().toString().length() > 0 && Integer.parseInt(labourApproved.getText().toString()) < Integer.parseInt(labourRequested.getText().toString())) {
                            checkCount++;
                            listener.onItemCheck(true);
                            item.setUpdated(true);
                            item.setLabour_selected(labourApproved.getText().toString());
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
                    listener.onItemClick(item, pos, itemView);
                }
            });
        }
    }

}