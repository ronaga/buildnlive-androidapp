package buildnlive.com.buildlive.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.LabourBreakup;

public class DeployLabourAdapter extends RecyclerView.Adapter<DeployLabourAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(LabourBreakup items, int pos, View view, EditText quantity);
    }

    private final List<LabourBreakup> items;
    private Context context;
    private final OnItemClickListener listener;

    public DeployLabourAdapter(Context context, List<LabourBreakup> users,OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_deploy_labour, parent, false);
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
        holder.bind(context, items.get(position), position,listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name,status,return_item;
        private EditText quantity;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            status= view.findViewById(R.id.status);
            return_item= view.findViewById(R.id.return_item);
            quantity = view.findViewById(R.id.quantity);

        }

        public void bind(final Context context, final LabourBreakup item, final int pos,final OnItemClickListener listener) {
            name.setText(item.getType_name());

            status.setText(item.getLabour_no());

            /*if(item.getImpress_status().equals("1")){
                return_item.setVisibility(View.VISIBLE);
            }
            else {
                return_item.setVisibility(View.GONE);
            }
            */
            return_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item,pos,return_item,quantity);
                }
            });
        }
    }
}