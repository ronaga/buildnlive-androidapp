package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Packet;
import buildnlive.com.buildlive.utils.Utils;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Packet packet, int pos, View view);
    }

    private final List<Packet> items;
    private Context context;
    private final OnItemClickListener listener;

    public ListAdapter(Context context, List<Packet> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list, parent, false);
        return new ViewHolder(v);
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

        private TextView name, extra;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            extra = view.findViewById(R.id.extra);
        }

        public void bind(final Context context, final Packet item, final int pos, final OnItemClickListener listener) {
            if (item.getType() == 7190) {
                if (item.getName() == null)
                    name.setText("NONE");
                else {
                    Date start = Utils.fromISODateFormat(item.getName());
                    if (start == null)
                        name.setText("");
                    else
                        name.setText(start.getDate() + " " + Utils.parseMonth(start.getMonth()) + ", " + Utils.parseYear(start.getYear()));
                }
                if (item.getExtra() == null)
                    extra.setText("NONE");
                else {
                    Date end = Utils.fromISODateFormat(item.getExtra());
                    if (end == null)
                        extra.setText("NONE");
                    else
                        extra.setText(end.getDate() + " " + Utils.parseMonth(end.getMonth()) + ", " + Utils.parseYear(end.getYear()));
                }
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, pos, itemView);
                }
            });
        }
    }
}