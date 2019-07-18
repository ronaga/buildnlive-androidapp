package buildnlive.com.buildlive.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Approval.ApprovalItem;
import buildnlive.com.buildlive.elements.Approval.ApproveIndentItem;
import buildnlive.com.buildlive.elements.Approval.ApproveIssueItem;
import buildnlive.com.buildlive.elements.Approval.ApproveLabourItem;
import buildnlive.com.buildlive.elements.Approval.ApproveLabourReportItem;
import buildnlive.com.buildlive.elements.Approval.ApproveWorkProgressItem;


public class ApprovalItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ApprovalItem> modelFeedArrayList;


    public interface OnItemInteractListener {
        void OnAttendanceItemClick(ArrayList<ApproveLabourItem> labourItem);

        void OnIssueItemClick(ArrayList<ApproveIssueItem> issue);

        void OnLabourReportItemClick(ArrayList<ApproveLabourReportItem> labourReport);

        void OnIndentItemClick(ArrayList<ApproveIndentItem> indentItem);

        void OnWorkProgressItemClick(ArrayList<ApproveWorkProgressItem> workProgressItem);
    }

    private OnItemInteractListener listener;


    public ApprovalItemAdapter(Context context, ArrayList<ApprovalItem> modelFeedArrayList,OnItemInteractListener listener) {

        this.context = context;
        this.modelFeedArrayList = modelFeedArrayList;
        this.listener=listener;

//        this.listener = listener;

    }

    @Override
    public int getItemViewType(int position) {
        switch (modelFeedArrayList.get(position).getType()) {
            case "1":
                return 1;
            case "2":
                return 2;
            case "3":
                return 3;
            case "4":
                return 4;
            case "5":
                return 5;
            default:
                return -1;
        }
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

        View view;

        switch (viewType) {
            case 1: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_approve_labour, parent, false);
                return new ApproveLabourHolder(view);
            }
            case 2: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_approve_issueitem, parent, false);
                return new ApproveIssueItemHolder(view);
            }
            case 3: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_approve_labourreport, parent, false);
                return new ApproveLabourReportHolder(view);
            }
            case 4: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_approve_indentitem, parent, false);
                return new ApproveIndentItemHolder(view);
            }
            case 5: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_approve_workprogress, parent, false);
                return new ApproveWorkProgressHolder(view);
            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        final ApprovalItem data = modelFeedArrayList.get(i);

        switch (data.getType()) {
            case "1": {
                final ApproveLabourHolder holder = (ApproveLabourHolder) viewHolder;
                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.OnAttendanceItemClick(data.getLabour());
                    }
                });
                break;
            }
            case "2": {
                final ApproveIssueItemHolder holder = (ApproveIssueItemHolder) viewHolder;
                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.OnIssueItemClick(data.getIssue_item());
                    }
                });
                break;
            }
            case "3": {
                final ApproveLabourReportHolder holder = (ApproveLabourReportHolder) viewHolder;
                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.OnLabourReportItemClick(data.getLabour_report());
                    }
                });
                break;
            }
            case "4": {
                final ApproveIndentItemHolder holder = (ApproveIndentItemHolder) viewHolder;
                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.OnIndentItemClick(data.getIndent_item());
                    }
                });
                break;
            }
            case "5": {
                final ApproveWorkProgressHolder holder = (ApproveWorkProgressHolder) viewHolder;
                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.OnWorkProgressItemClick(data.getWork_progress());
                    }
                });
                break;
            }
        }

    }


    public class ApproveLabourHolder extends RecyclerView.ViewHolder {


        TextView date,inTime,outTime,overtime;
        LinearLayout container;

        ApproveLabourHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            container = itemView.findViewById(R.id.container);
            /*inTime = itemView.findViewById(R.id.inTime);
            outTime = itemView.findViewById(R.id.outTime);
            overtime = itemView.findViewById(R.id.overtime);*/
        }
    }

    public class ApproveIssueItemHolder extends RecyclerView.ViewHolder {


        TextView date;
        LinearLayout container;

        ApproveIssueItemHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            container = itemView.findViewById(R.id.container);

        }
    }

    public class ApproveLabourReportHolder extends RecyclerView.ViewHolder {

        TextView date;
        LinearLayout container;

        ApproveLabourReportHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            container = itemView.findViewById(R.id.container);
        }
    }

  public class ApproveIndentItemHolder extends RecyclerView.ViewHolder {

        TextView date;
        LinearLayout container;

      ApproveIndentItemHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            container = itemView.findViewById(R.id.container);
        }
    }
  public class ApproveWorkProgressHolder extends RecyclerView.ViewHolder {

        TextView date;
        LinearLayout container;

      ApproveWorkProgressHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            container = itemView.findViewById(R.id.container);
        }
    }


}