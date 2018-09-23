package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Issue;
import buildnlive.com.buildlive.elements.ViewIssue;

public class ViewIssueAdapter extends RecyclerView.Adapter<ViewIssueAdapter.ViewHolder> {
    private final List<ViewIssue> items;
    private Context context;

    public ViewIssueAdapter(Context context, List<ViewIssue> users) {
        this.items = users;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_issue, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(context, items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, worker, quantity,status;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            worker = view.findViewById(R.id.worker);
            quantity = view.findViewById(R.id.quantity);
            status= view.findViewById(R.id.status);
        }

        public void bind(final Context context, final ViewIssue item, final int pos) {
            name.setText(item.getItemName());
            worker.setText(item.getReciever());
            quantity.setText(item.getQuantity() + " " + item.getUnit());
            if(item.getStatus().equals("Yes")){
                status.setText("Received");
            }
            else status.setText("Not Received");
        }
    }
}