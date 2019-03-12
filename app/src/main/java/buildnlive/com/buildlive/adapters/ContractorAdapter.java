package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Contractor;

public class ContractorAdapter extends RecyclerView.Adapter<ContractorAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Contractor project, int pos, View view);
    }

    private final List<Contractor> items;
    private Context context;
    private final OnItemClickListener listener;

    public ContractorAdapter(Context context, ArrayList<Contractor> users, OnItemClickListener listener) {
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
    public ContractorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_labour_list, parent, false);
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
        private TextView name;

        public ViewHolder(View view) {
            super(view);
            name= view.findViewById(R.id.project_name);

        }

        public void bind(final Context context, final Contractor item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getContractor_name());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item,pos,itemView);
                }
            });
        }
    }

}