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
import buildnlive.com.buildlive.elements.LabourReport;

public class ViewLabourReportAdapter extends RecyclerView.Adapter<ViewLabourReportAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(LabourReport project, int pos, View view);
    }

    private final List<LabourReport> items;
    private Context context;
    private final OnItemClickListener listener;

    public ViewLabourReportAdapter(Context context, ArrayList<LabourReport> users, OnItemClickListener listener) {
        this.items = users;
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
    public ViewLabourReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_labour_report_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(context, items.get(position), position, listener);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,quantity,date;

        public ViewHolder(View view) {
            super(view);
            name= view.findViewById(R.id.contractor_name);
            quantity= view.findViewById(R.id.quantity);
            date= view.findViewById(R.id.date);

        }

        public void bind(final Context context, final LabourReport item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getContractor_name());
            quantity.setText(item.getLabour_count());
            date.setText(item.getDate());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item,pos,itemView);
                }
            });
        }
    }

}