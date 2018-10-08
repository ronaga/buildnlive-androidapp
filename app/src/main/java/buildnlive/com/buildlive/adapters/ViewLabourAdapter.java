package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Item;
import buildnlive.com.buildlive.elements.LabourInfo;
import buildnlive.com.buildlive.elements.Notification;
import buildnlive.com.buildlive.elements.ViewLabour;

public class ViewLabourAdapter extends RecyclerView.Adapter<ViewLabourAdapter.ViewHolder> {
    private final List<ViewLabour> items;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(ViewLabour ViewLabour, int pos, View view);
    }

    private final  OnItemClickListener listener;
    public ViewLabourAdapter(Context context, List<ViewLabour> users,OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener=listener;
    }

    @Override
    public ViewLabourAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_labour, parent, false);
        return new ViewLabourAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewLabourAdapter.ViewHolder holder, int position) {
        holder.bind(context, items.get(position), position,listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, quantity,type;
        private Button returnButton;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            type= view.findViewById(R.id.type);
            quantity = view.findViewById(R.id.quantity);
            returnButton= view.findViewById(R.id.returnbutton);
        }

        public void bind(final Context context, final ViewLabour item, final int pos,final OnItemClickListener listener) {
            name.setText(item.getName());
            quantity.setText(item.getQuantity());
            type.setText(item.getType());
            returnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item,pos,view);
                }
            });
        }
    }
}
