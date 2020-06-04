package buildnlive.com.buildlive.Server.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EditPaymentResponse implements Parcelable {
    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ArrayList<EditPaymentResponseData> data;

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

    public ArrayList<EditPaymentResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<EditPaymentResponseData> data) {
        this.data = data;
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

    public EditPaymentResponse() {
    }

    protected EditPaymentResponse(Parcel in) {
        this.data = new ArrayList<EditPaymentResponseData>();
        in.readList(this.data, EditPaymentResponseData.class.getClassLoader());
        this.message = in.readString();
        this.success = in.readByte() != 0;
    }

    public static final Parcelable.Creator<EditPaymentResponse> CREATOR = new Parcelable.Creator<EditPaymentResponse>() {
        @Override
        public EditPaymentResponse createFromParcel(Parcel source) {
            return new EditPaymentResponse(source);
        }

        @Override
        public EditPaymentResponse[] newArray(int size) {
            return new EditPaymentResponse[size];
        }
    };
}
