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
import buildnlive.com.buildlive.elements.ImpressItem;

public class ImpressItemAdapter extends RecyclerView.Adapter<ImpressItemAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ImpressItem items, int pos, View view);
    }

    private final List<ImpressItem> items;
    private Context context;
    private final OnItemClickListener listener;

    public ImpressItemAdapter(Context context, List<ImpressItem> users,OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_impress, parent, false);
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

        private TextView name,status,return_item,date;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            status= view.findViewById(R.id.status);
            return_item= view.findViewById(R.id.return_item);
            date= view.findViewById(R.id.date);
        }

        public void bind(final Context context, final ImpressItem item, final int pos,final OnItemClickListener listener) {
            name.setText(item.getImpress_details());
            date.setText(Html.fromHtml("<b>Date: </b>"+item.getImpress_date()));

                status.setText(item.getImpress_amount());

            if(item.getImpress_status().equals("1")){
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