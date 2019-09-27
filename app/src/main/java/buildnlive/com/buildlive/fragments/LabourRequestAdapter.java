package buildnlive.com.buildlive.fragments;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.LabourRequestItem;

public class LabourRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int pos, View view, String id);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        switch (items.get(position).getHeading()) {
            case "0":
                return 0;
            case "1":
                return 1;
            default:
                return -1;
        }
    }


    private final List<LabourRequestItem> items;
    private Context context;
    private final OnItemClickListener listener;


    public LabourRequestAdapter(Context context, List<LabourRequestItem> works, OnItemClickListener listener) {
        this.items = works;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case 0: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_request_item, parent, false);
                return new DataViewHolder(view);
            }
            case 1: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_title, parent, false);
                return new TitleViewHolder(view);
            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        LabourRequestItem workdata = items.get(position);

        switch (workdata.getHeading()) {
            case "0": {
                ((DataViewHolder) (holder)).bind(context, workdata, position, listener);
                break;
            }
            case "1": {
                ((TitleViewHolder) (holder)).bindTitle(context, workdata, position);
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        TitleViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.name);
        }

        void bindTitle(final Context context, final LabourRequestItem item, final int pos) {
            title.setText(item.getTitle());
        }

    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView name, workName, date, quantity;
        private LinearLayout container;

        DataViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            workName = view.findViewById(R.id.workName);
            quantity = view.findViewById(R.id.quantity);
            date = view.findViewById(R.id.date);
//            enter = view.findViewById(R.id.enter);
            container = view.findViewById(R.id.container);
        }

        public void bind(final Context context, final LabourRequestItem item, final int pos, final OnItemClickListener listener) {

            name.setText(item.getUserRequested());
            workName.setText(item.getWorkName());
            quantity.setText(item.getLabourQtyRequest());
            date.setText(item.getDateRequest());
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(pos, container, item.getLabourRequestId());
                }
            });

        }
    }
}
