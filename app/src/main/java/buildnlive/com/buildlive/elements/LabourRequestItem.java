package buildnlive.com.buildlive.elements;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LabourRequestItem implements Parcelable {

    @SerializedName("date_request")
    private String dateRequest;
    @SerializedName("labour_qty_request")
    private String labourQtyRequest;
    @SerializedName("work_name")
    private String workName;
    @SerializedName("user_requested")
    private String userRequested;
    @SerializedName("labour_request_id")
    private String labourRequestId;
    @SerializedName("title")
    private String title;
    @SerializedName("heading")
    private String heading;

    public String getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(String dateRequest) {
        this.dateRequest = dateRequest;
    }

    public String getLabourQtyRequest() {
        return labourQtyRequest;
    }

    public void setLabourQtyRequest(String labourQtyRequest) {
        this.labourQtyRequest = labourQtyRequest;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getUserRequested() {
        return userRequested;
    }

    public void setUserRequested(String userRequested) {
        this.userRequested = userRequested;
    }

    public String getLabourRequestId() {
        return labourRequestId;
    }

    public void setLabourRequestId(String labourRequestId) {
        this.labourRequestId = labourRequestId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dateRequest);
        dest.writeString(this.labourQtyRequest);
        dest.writeString(this.workName);
        dest.writeString(this.userRequested);
        dest.writeString(this.labourRequestId);
        dest.writeString(this.title);
        dest.writeString(this.heading);
    }

    public LabourRequestItem() {
    }

    protected LabourRequestItem(Parcel in) {
        this.dateRequest = in.readString();
        this.labourQtyRequest = in.readString();
        this.workName = in.readString();
        this.userRequested = in.readString();
        this.labourRequestId = in.readString();
        this.title = in.readString();
        this.heading = in.readString();
    }

    public static final Parcelable.Creator<LabourRequestItem> CREATOR = new Parcelable.Creator<LabourRequestItem>() {
        @Override
        public LabourRequestItem createFromParcel(Parcel source) {
            return new LabourRequestItem(source);
        }

        @Override
        public LabourRequestItem[] newArray(int size) {
            return new LabourRequestItem[size];
        }
    };
}
