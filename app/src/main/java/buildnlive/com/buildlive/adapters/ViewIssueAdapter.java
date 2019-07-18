package buildnlive.com.buildlive.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.ViewIssue;

public class ViewIssueAdapter extends RecyclerView.Adapter<ViewIssueAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ViewIssue items, int pos, View view);
    }

    private final List<ViewIssue> items;
    private Context context;
    private final OnItemClickListener listener;

    public ViewIssueAdapter(Context context, List<ViewIssue> users,OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_issue, parent, false);
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

        private TextView name, worker, quantity,status,return_item,date;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            worker = view.findViewById(R.id.worker);
            quantity = view.findViewById(R.id.quantity);
            status= view.findViewById(R.id.status);
            return_item= view.findViewById(R.id.return_item);
            date= view.findViewById(R.id.date);
        }

        public void bind(final Context context, final ViewIssue item, final int pos,final OnItemClickListener listener) {
            name.setText(item.getItemName());
            worker.setText(item.getReciever());
            quantity.setText(item.getQuantity() + " " + item.getUnit());

            date.setText(Html.fromHtml("<b>Date: </b>"+item.getDate()));

            if(item.getStatus().equals("Yes")){
                status.setText("Received");
            }
            else status.setText("Not Received");

            if(item.getButtonStatus().equals("1")){
                return_item.setVisibility(View.VISIBLE);
            }
            else {
                return_item.setVisibility(View.GONE);
            }
            return_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        listener.onItemClick(item,pos,return_item);
                }
            });
        }
    }
}