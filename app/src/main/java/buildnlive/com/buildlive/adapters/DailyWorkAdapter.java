package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Work;

public class DailyWorkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int pos, View view);
    }

    public interface OnButtonClickListener {
        void onButtonClick(int pos, View view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        switch (items.get(position).getLayouttype()) {
            case "0":
                return 0;
            case "1":
                return 1;
            default:
                return -1;
        }
    }


    private final List<Work> items;
    private Context context;
    private final OnItemClickListener listener;
    private final OnButtonClickListener buttonClickListener;
    private String identifier;


    public DailyWorkAdapter(Context context, ArrayList<Work> works, String identifier, OnItemClickListener listener, OnButtonClickListener buttonClickListener) {
        this.items = works;
        this.context = context;
        this.listener = listener;
        this.identifier = identifier;
        this.buttonClickListener = buttonClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case 0: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_daily_work_new, parent, false);
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

        Work workdata = items.get(position);

        switch (workdata.getLayouttype()) {
            case "0": {
                ((DataViewHolder) (holder)).bind(context, workdata, position, listener, buttonClickListener, identifier);
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

        void bindTitle(final Context context, final Work item, final int pos) {
            title.setText(item.getWorkName());
        }

    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView name, status, duration, quantity, start, end;
        private TextView update, activity, progressPercentage;
        private ProgressBar progressBar;

        DataViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            quantity = view.findViewById(R.id.quantity);
            start = view.findViewById(R.id.start);
            end = view.findViewById(R.id.end);
            status = view.findViewById(R.id.status);
            update = view.findViewById(R.id.update_progress_button);
            duration = view.findViewById(R.id.duration);
            activity = view.findViewById(R.id.update_activity_button);
            duration = view.findViewById(R.id.duration);
            progressBar = view.findViewById(R.id.progressBarHorizontal);
            progressPercentage = view.findViewById(R.id.progressPercentage);
        }

        public void bind(final Context context, final Work item, final int pos, final OnItemClickListener listener, final OnButtonClickListener buttonClickListener, String identifier) {
            if (identifier.equals("Work")) {

                status.setBackgroundColor(Color.parseColor(item.getStatus_color()));
                name.setText(" " + item.getWorkName());
                status.setText(item.getStatus());
                quantity.setText("Quantity: " + item.getQuantity());
                duration.setText("Duration: " + item.getDuration());
                start.setText("Start Date: " + item.getStart());
                end.setText("End Date: " + item.getEnd());
                int progress = Integer.valueOf(item.getPercent_compl());
                progressBar.setProgress(progress);
                progressPercentage.setText(String.format(context.getString(R.string.percentComplete),item.getPercent_compl()));

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buttonClickListener.onButtonClick(pos, view);
                    }
                });

                if (!item.getPercent_compl().equals("100")) {
                    update.setEnabled(true);
                    update.setBackground(ContextCompat.getDrawable(context,R.drawable.home_button));
                } else {
                    update.setEnabled(false);
                    update.setBackground(ContextCompat.getDrawable(context,R.drawable.negative_button));
                }
                activity.setVisibility(View.VISIBLE);
                activity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemClick(pos, itemView);
                    }
                });
            } else if (identifier.equals("Plan")) {

                console.log(item.getStatus_color());
                status.setBackgroundColor(Color.parseColor(item.getStatus_color()));

                activity.setVisibility(View.GONE);
                name.setText(" " + item.getWorkName());
                status.setText(item.getStatus());
                quantity.setText("Quantity: " + item.getQuantity());
                duration.setText("Duration: " + item.getDuration());
                start.setText("Start Date: " + item.getStart());
                end.setText("End Date: " + item.getEnd());
                int progress = Integer.valueOf(item.getPercent_compl());
                progressBar.setProgress(progress);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buttonClickListener.onButtonClick(pos, view);
                    }
                });
            }

        }
    }
}
