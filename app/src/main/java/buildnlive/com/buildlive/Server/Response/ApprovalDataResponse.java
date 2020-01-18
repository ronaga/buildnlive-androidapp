package buildnlive.com.buildlive.Server.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApprovalDataResponse implements Parcelable {

    @SerializedName("data")
    private ArrayList<ApprovalDataResponseData> data;
    @SerializedName("message")
    private String message;
    @SerializedName("success")
    private boolean success;

    public ArrayList<ApprovalDataResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<ApprovalDataResponseData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.data);
        dest.writeString(this.message);
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
    }

    public ApprovalDataResponse() {
    }

    protected ApprovalDataResponse(Parcel in) {
        this.data = in.createTypedArrayList(ApprovalDataResponseData.CREATOR);
        this.message = in.readString();
        this.success = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ApprovalDataResponse> CREATOR = new Parcelable.Creator<ApprovalDataResponse>() {
        @Override
        public ApprovalDataResponse createFromParcel(Parcel source) {
            return new ApprovalDataResponse(source);
        }

        @Override
        public ApprovalDataResponse[] newArray(int size) {
            return new ApprovalDataResponse[size];
        }
    };
}
