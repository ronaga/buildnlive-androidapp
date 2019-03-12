package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Item;
import buildnlive.com.buildlive.elements.Work;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class DailyWorkAdapter extends RecyclerView.Adapter<DailyWorkAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int pos, View view);
    }
    public interface OnButtonClickListener {
        void onButtonClick(int pos,View view);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    private final List<Work> items;
    private Context context;
    private final OnItemClickListener listener;
    private final OnButtonClickListener buttonClickListener;
    private String identifier;

//    public DailyWorkAdapter(Context context, OrderedRealmCollection<Work> works, OnItemClickListener listener,OnButtonClickListener buttonClickListener) {

    public DailyWorkAdapter(Context context, ArrayList<Work> works,String identifier,OnItemClickListener listener, OnButtonClickListener buttonClickListener) {
        this.items = works;
        this.context = context;
        this.listener = listener;
        this.identifier=identifier;
        this.buttonClickListener=buttonClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_daily_work_new, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(context, items.get(position), position, listener,buttonClickListener,identifier);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, status,duration,quantity,start,end;
        private TextView update,activity;
        private ProgressBar progressBar;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            quantity = view.findViewById(R.id.quantity);
            start = view.findViewById(R.id.start);
            end= view.findViewById(R.id.end);
            status = view.findViewById(R.id.status);
            update = view.findViewById(R.id.update_progress_button);
            duration =view.findViewById(R.id.duration);
            update = view.findViewById(R.id.update_progress_button);
            activity = view.findViewById(R.id.update_activity_button);
            duration =view.findViewById(R.id.duration);
            progressBar =view.findViewById(R.id.progressBarHorizontal);
        }

        public void bind(final Context context, final Work item, final int pos, final OnItemClickListener listener,final OnButtonClickListener buttonClickListener,String identifier) {
            if(identifier.equals("Work"))
            {
                name.setText(" " + item.getWorkName());
            status.setText(item.getStatus());
            quantity.setText("Quantity: "+item.getQuantity());
            duration.setText("Duration: "+item.getDuration());
            start.setText("Start Date: "+item.getStart());
            end.setText("End Date: "+item.getEnd());
            int progress= Integer.valueOf(item.getPercent_compl());
            progressBar.setProgress(progress);

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonClickListener.onButtonClick(pos,view);
                }
            });
                activity.setVisibility(View.VISIBLE);
            activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(pos,itemView);
                }
            });
            }else if(identifier.equals("Plan")){
                activity.setVisibility(View.GONE);
                name.setText(" "+item.getWorkName());
                status.setText(item.getStatus());
                quantity.setText("Quantity: "+item.getQuantity());
                duration.setText("Duration: "+item.getDuration());
                start.setText("Start Date: "+item.getStart());
                end.setText("End Date: "+item.getEnd());
                int progress= Integer.valueOf(item.getPercent_compl());
                progressBar.setProgress(progress);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buttonClickListener.onButtonClick(pos,view);
                    }
                });
            }
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onItemClick(pos, itemView);
//                }
//            });
//            float total = Float.parseFloat(item.getQuantity());
//            float com = Float.parseFloat(item.getQty_completed());
//            status.setText(item.getStatus());
//            status.setText((com / total * 100) + "% Completed");
        }
    }
}