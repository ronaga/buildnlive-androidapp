package buildnlive.com.buildlive.Server.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetStoreResponse implements Parcelable {
    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ArrayList<GetStoreResponseData> data;

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

    public ArrayList<GetStoreResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<GetStoreResponseData> data) {
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

    public GetStoreResponse() {
    }

    private GetStoreResponse(Parcel in) {
        this.success = in.readByte() != 0;
        this.message = in.readString();
        this.data = new ArrayList<GetStoreResponseData>();
        in.readList(this.data, GetStoreResponseData.class.getClassLoader());
    }

    public static final Parcelable.Creator<GetStoreResponse> CREATOR = new Parcelable.Creator<GetStoreResponse>() {
        @Override
        public GetStoreResponse createFromParcel(Parcel source) {
            return new GetStoreResponse(source);
        }

        @Override
        public GetStoreResponse[] newArray(int size) {
            return new GetStoreResponse[size];
        }
    };
}
