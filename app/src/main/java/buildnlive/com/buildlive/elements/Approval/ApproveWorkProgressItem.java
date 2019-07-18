package buildnlive.com.buildlive.elements.Approval;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ApproveWorkProgressItem implements Parcelable {
    @SerializedName("schedule")
    private String schedule;
    @SerializedName("qty_achieved")
    private String qtyAchieved;
    @SerializedName("qty_planned")
    private String qtyPlanned;
    @SerializedName("work_name")
    private String workName;
    @SerializedName("work_id")
    private String workId;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getQtyAchieved() {
        return qtyAchieved;
    }

    public void setQtyAchieved(String qtyAchieved) {
        this.qtyAchieved = qtyAchieved;
    }

    public String getQtyPlanned() {
        return qtyPlanned;
    }

    public void setQtyPlanned(String qtyPlanned) {
        this.qtyPlanned = qtyPlanned;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public ApproveWorkProgressItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.schedule);
        dest.writeString(this.qtyAchieved);
        dest.writeString(this.qtyPlanned);
        dest.writeString(this.workName);
        dest.writeString(this.workId);
        dest.writeString(this.status);
    }

    protected ApproveWorkProgressItem(Parcel in) {
        this.schedule = in.readString();
        this.qtyAchieved = in.readString();
        this.qtyPlanned = in.readString();
        this.workName = in.readString();
        this.workId = in.readString();
        this.status = in.readString();
    }

    public static final Creator<ApproveWorkProgressItem> CREATOR = new Creator<ApproveWorkProgressItem>() {
        @Override
        public ApproveWorkProgressItem createFromParcel(Parcel source) {
            return new ApproveWorkProgressItem(source);
        }

        @Override
        public ApproveWorkProgressItem[] newArray(int size) {
            return new ApproveWorkProgressItem[size];
        }
    };
}
