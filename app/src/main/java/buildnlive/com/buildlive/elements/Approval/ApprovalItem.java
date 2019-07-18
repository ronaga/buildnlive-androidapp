package buildnlive.com.buildlive.elements.Approval;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ApprovalItem implements Parcelable {
    @SerializedName("labour")
    private ArrayList<ApproveLabourItem> labour;
    @SerializedName("issue_item")
    private ArrayList<ApproveIssueItem> issue_item;
    @SerializedName("labour_report")
    private ArrayList<ApproveLabourReportItem> labour_report;
    @SerializedName("indent_item")
    private ArrayList<ApproveIndentItem> indent_item;
    @SerializedName("work_progress")
    private ArrayList<ApproveWorkProgressItem> work_progress;
    @SerializedName("type")
    private String type;


    public void setIssue_item(ArrayList<ApproveIssueItem> issue_item) {
        this.issue_item = issue_item;
    }

    public void setLabour(ArrayList<ApproveLabourItem> labour) {
        this.labour = labour;
    }

    public void setLabour_report(ArrayList<ApproveLabourReportItem> labour_report) {
        this.labour_report = labour_report;
    }

    public ArrayList<ApproveIssueItem> getIssue_item() {
        return issue_item;
    }

    public ArrayList<ApproveLabourItem> getLabour() {
        return labour;
    }

    public ArrayList<ApproveLabourReportItem> getLabour_report() {
        return labour_report;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public ArrayList<ApproveIndentItem> getIndent_item() {
        return indent_item;
    }

    public void setIndent_item(ArrayList<ApproveIndentItem> indent_item) {
        this.indent_item = indent_item;
    }

    public ArrayList<ApproveWorkProgressItem> getWork_progress() {
        return work_progress;
    }

    public void setWork_progress(ArrayList<ApproveWorkProgressItem> work_progress) {
        this.work_progress = work_progress;
    }

    public ApprovalItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.labour);
        dest.writeTypedList(this.issue_item);
        dest.writeTypedList(this.labour_report);
        dest.writeList(this.indent_item);
        dest.writeList(this.work_progress);
        dest.writeString(this.type);
    }

    protected ApprovalItem(Parcel in) {
        this.labour = in.createTypedArrayList(ApproveLabourItem.CREATOR);
        this.issue_item = in.createTypedArrayList(ApproveIssueItem.CREATOR);
        this.labour_report = in.createTypedArrayList(ApproveLabourReportItem.CREATOR);
        this.indent_item = new ArrayList<ApproveIndentItem>();
        in.readList(this.indent_item, ApproveIndentItem.class.getClassLoader());
        this.work_progress = new ArrayList<ApproveWorkProgressItem>();
        in.readList(this.work_progress, ApproveWorkProgressItem.class.getClassLoader());
        this.type = in.readString();
    }

    public static final Creator<ApprovalItem> CREATOR = new Creator<ApprovalItem>() {
        @Override
        public ApprovalItem createFromParcel(Parcel source) {
            return new ApprovalItem(source);
        }

        @Override
        public ApprovalItem[] newArray(int size) {
            return new ApprovalItem[size];
        }
    };
}


