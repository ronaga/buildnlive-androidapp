package buildnlive.com.buildlive.Server.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ApprovalResponse implements Parcelable {
    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ArrayList<ApprovalResponseData> data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<ApprovalResponseData> getData() {
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeTypedList(this.data);
    }

    public ApprovalResponse() {
    }

    protected ApprovalResponse(Parcel in) {
        this.success = in.readByte() != 0;
        this.message = in.readString();
        this.data = in.createTypedArrayList(ApprovalResponseData.CREATOR);
    }

    public static final Parcelable.Creator<ApprovalResponse> CREATOR = new Parcelable.Creator<ApprovalResponse>() {
        @Override
        public ApprovalResponse createFromParcel(Parcel source) {
            return new ApprovalResponse(source);
        }

        @Override
        public ApprovalResponse[] newArray(int size) {
            return new ApprovalResponse[size];
        }
    };
}
