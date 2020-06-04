package buildnlive.com.buildlive.Server.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AttendanceResponse implements Parcelable {
    @SerializedName("data")
    private ArrayList<AttendanceResponseData> data;
    @SerializedName("message")
    private String message;
    @SerializedName("success")
    private boolean success;

    public ArrayList<AttendanceResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<AttendanceResponseData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
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
        dest.writeList(this.data);
        dest.writeString(this.message);
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
    }

    public AttendanceResponse() {
    }

    protected AttendanceResponse(Parcel in) {
        this.data = new ArrayList<AttendanceResponseData>();
        in.readList(this.data, AttendanceResponseData.class.getClassLoader());
        this.message = in.readString();
        this.success = in.readByte() != 0;
    }

    public static final Parcelable.Creator<AttendanceResponse> CREATOR = new Parcelable.Creator<AttendanceResponse>() {
        @Override
        public AttendanceResponse createFromParcel(Parcel source) {
            return new AttendanceResponse(source);
        }

        @Override
        public AttendanceResponse[] newArray(int size) {
            return new AttendanceResponse[size];
        }
    };
}
