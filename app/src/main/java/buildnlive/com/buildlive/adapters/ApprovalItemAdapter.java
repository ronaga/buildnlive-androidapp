package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.Server.Response.ApprovalResponseData;


public class ApprovalItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ApprovalResponseData> modelFeedArrayList;


    public interface OnItemInteractListener {
        void onItemClick(String type);
    }

    private OnItemInteractListener listener;


    public ApprovalItemAdapter(Context context, ArrayList<ApprovalResponseData> modelFeedArrayList, OnItemInteractListener listener) {

        this.context = context;
        this.modelFeedArrayList = modelFeedArrayList;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return modelFeedArrayList.size();
    }


    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_approval_item, parent, false);
        return new ApprovalHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final ApprovalResponseData data = modelFeedArrayList.get(i);
        final ApprovalHolder holder = (ApprovalHolder) viewHolder;

        holder.approve.setText(data.getHeading());
        holder.count.setText(String.format("%s/%s", data.getPending(), data.getTotal()));
        holder.container.setOnClickListener(view ->
                listener.onItemClick(data.getType())
        );
    }


    public class ApprovalHolder extends RecyclerView.ViewHolder {


        TextView approve, count;
        CardView container;

        ApprovalHolder(View itemView) {
            super(itemView);
            approve = itemView.findViewById(R.id.approve);
            count = itemView.findViewById(R.id.count);
            container = itemView.findViewById(R.id.container);
        }
    }

}