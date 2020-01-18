package buildnlive.com.buildlive.Server.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ApprovalDataResponseData implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("status")
    private String status;
    @SerializedName("description")
    private String description;
    @SerializedName("title")
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ApprovalDataResponseData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.status);
        dest.writeString(this.description);
        dest.writeString(this.title);
    }

    protected ApprovalDataResponseData(Parcel in) {
        this.id = in.readString();
        this.status = in.readString();
        this.description = in.readString();
        this.title = in.readString();
    }

    public static final Creator<ApprovalDataResponseData> CREATOR = new Creator<ApprovalDataResponseData>() {
        @Override
        public ApprovalDataResponseData createFromParcel(Parcel source) {
            return new ApprovalDataResponseData(source);
        }

        @Override
        public ApprovalDataResponseData[] newArray(int size) {
            return new ApprovalDataResponseData[size];
        }
    };
}
