package buildnlive.com.buildlive.Server.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ApprovalResponseData implements Parcelable {

    @SerializedName("total")
    private String total;
    @SerializedName("pending")
    private String pending;
    @SerializedName("type")
    private String type;
    @SerializedName("heading")
    private String heading;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        dest.writeString(this.total);
        dest.writeString(this.pending);
        dest.writeString(this.type);
        dest.writeString(this.heading);
    }

    public ApprovalResponseData() {
    }

    protected ApprovalResponseData(Parcel in) {
        this.total = in.readString();
        this.pending = in.readString();
        this.type = in.readString();
        this.heading = in.readString();
    }

    public static final Parcelable.Creator<ApprovalResponseData> CREATOR = new Parcelable.Creator<ApprovalResponseData>() {
        @Override
        public ApprovalResponseData createFromParcel(Parcel source) {
            return new ApprovalResponseData(source);
        }

        @Override
        public ApprovalResponseData[] newArray(int size) {
            return new ApprovalResponseData[size];
        }
    };
}
