package buildnlive.com.buildlive.Server.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ViewPaymentResponse implements Parcelable {
    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ArrayList<ViewPaymentResponseData> data;

    public boolean getSuccess() {
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

    public ArrayList<ViewPaymentResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<ViewPaymentResponseData> data) {
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

    public ViewPaymentResponse() {
    }

    private ViewPaymentResponse(Parcel in) {
        this.success = in.readByte() != 0;
        this.message = in.readString();
        this.data = new ArrayList<ViewPaymentResponseData>();
        in.readList(this.data, ViewPaymentResponseData.class.getClassLoader());
    }

    public static final Parcelable.Creator<ViewPaymentResponse> CREATOR = new Parcelable.Creator<ViewPaymentResponse>() {
        @Override
        public ViewPaymentResponse createFromParcel(Parcel source) {
            return new ViewPaymentResponse(source);
        }

        @Override
        public ViewPaymentResponse[] newArray(int size) {
            return new ViewPaymentResponse[size];
        }
    };
}
