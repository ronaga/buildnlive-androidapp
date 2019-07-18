package buildnlive.com.buildlive.elements.Approval;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ApproveLabourItem implements Parcelable {
    @SerializedName("overtime")
    private String overtime;
    @SerializedName("out_time")
    private String outTime;
    @SerializedName("in_time")
    private String inTime;
    @SerializedName("labour_name")
    private String labourName;
    @SerializedName("attendence_id")
    private String attendenceId;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOvertime() {
        return overtime;
    }

    public void setOvertime(String overtime) {
        this.overtime = overtime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getLabourName() {
        return labourName;
    }

    public void setLabourName(String labourName) {
        this.labourName = labourName;
    }

    public String getAttendenceId() {
        return attendenceId;
    }

    public void setAttendenceId(String attendenceId) {
        this.attendenceId = attendenceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.overtime);
        dest.writeString(this.outTime);
        dest.writeString(this.inTime);
        dest.writeString(this.labourName);
        dest.writeString(this.attendenceId);
    }

    public ApproveLabourItem() {
    }

    protected ApproveLabourItem(Parcel in) {
        this.overtime = in.readString();
        this.outTime = in.readString();
        this.inTime = in.readString();
        this.labourName = in.readString();
        this.attendenceId = in.readString();
    }

    public static final Parcelable.Creator<ApproveLabourItem> CREATOR = new Parcelable.Creator<ApproveLabourItem>() {
        @Override
        public ApproveLabourItem createFromParcel(Parcel source) {
            return new ApproveLabourItem(source);
        }

        @Override
        public ApproveLabourItem[] newArray(int size) {
            return new ApproveLabourItem[size];
        }
    };
}
