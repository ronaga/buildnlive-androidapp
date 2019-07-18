package buildnlive.com.buildlive.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.LabourDeploy;

public class StructureListAdapter extends RecyclerView.Adapter<StructureListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(LabourDeploy packet, int pos, View view);
    }

    private final List<LabourDeploy> items;
    private Context context;
    private final OnItemClickListener listener;

    public StructureListAdapter(Context context, List<LabourDeploy> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_structure_item, parent, false);
        return new ViewHolder(v);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(context, items.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView details, area;

        public ViewHolder(View view) {
            super(view);
            details = view.findViewById(R.id.details);
            area = view.findViewById(R.id.area);
        }

        public void bind(final Context context, final LabourDeploy item, final int pos, final OnItemClickListener listener) {
            details.setText(item.getStructure_details());
            area.setText(item.getLabour_no());


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, pos, itemView);
                }
            });
        }
    }
}