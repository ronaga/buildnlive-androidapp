package buildnlive.com.buildlive.Server.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EditLabourReportResponse implements Parcelable {
    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ArrayList<EditLabourReportResponseData> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<EditLabourReportResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<EditLabourReportResponseData> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeList(this.data);
    }

    public EditLabourReportResponse() {
    }

    protected EditLabourReportResponse(Parcel in) {
        this.success = in.readByte() != 0;
        this.message = in.readString();
        this.data = new ArrayList<EditLabourReportResponseData>();
        in.readList(this.data, EditLabourReportResponseData.class.getClassLoader());
    }

    public static final Parcelable.Creator<EditLabourReportResponse> CREATOR = new Parcelable.Creator<EditLabourReportResponse>() {
        @Override
        public EditLabourReportResponse createFromParcel(Parcel source) {
            return new EditLabourReportResponse(source);
        }

        @Override
        public EditLabourReportResponse[] newArray(int size) {
            return new EditLabourReportResponse[size];
        }
    };
}
