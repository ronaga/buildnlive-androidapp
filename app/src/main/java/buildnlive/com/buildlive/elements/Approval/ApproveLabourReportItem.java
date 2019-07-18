package buildnlive.com.buildlive.elements.Approval;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ApproveLabourReportItem implements Parcelable {
    @SerializedName("total_labour")
    private String totalLabour;
    @SerializedName("contractor_name")
    private String contractorName;
    @SerializedName("report_id")
    private String reportId;


    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalLabour() {
        return totalLabour;
    }

    public void setTotalLabour(String totalLabour) {
        this.totalLabour = totalLabour;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public ApproveLabourReportItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.totalLabour);
        dest.writeString(this.contractorName);
        dest.writeString(this.reportId);
        dest.writeString(this.status);
    }

    protected ApproveLabourReportItem(Parcel in) {
        this.totalLabour = in.readString();
        this.contractorName = in.readString();
        this.reportId = in.readString();
        this.status = in.readString();
    }

    public static final Creator<ApproveLabourReportItem> CREATOR = new Creator<ApproveLabourReportItem>() {
        @Override
        public ApproveLabourReportItem createFromParcel(Parcel source) {
            return new ApproveLabourReportItem(source);
        }

        @Override
        public ApproveLabourReportItem[] newArray(int size) {
            return new ApproveLabourReportItem[size];
        }
    };
}
