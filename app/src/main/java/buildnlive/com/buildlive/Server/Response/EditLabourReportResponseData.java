package buildnlive.com.buildlive.Server.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class EditLabourReportResponseData implements Parcelable {

    @SerializedName("count")
    private String count;
    @SerializedName("labour_type")
    private String labourType;
    @SerializedName("sub_contract_labour_detail_id")
    private String subContractLabourDetailId;
    private boolean isUpdated;

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getLabourType() {
        return labourType;
    }

    public void setLabourType(String labourType) {
        this.labourType = labourType;
    }

    public String getSubContractLabourDetailId() {
        return subContractLabourDetailId;
    }

    public void setSubContractLabourDetailId(String subContractLabourDetailId) {
        this.subContractLabourDetailId = subContractLabourDetailId;
    }

    public EditLabourReportResponseData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.count);
        dest.writeString(this.labourType);
        dest.writeString(this.subContractLabourDetailId);
        dest.writeByte(this.isUpdated ? (byte) 1 : (byte) 0);
    }

    protected EditLabourReportResponseData(Parcel in) {
        this.count = in.readString();
        this.labourType = in.readString();
        this.subContractLabourDetailId = in.readString();
        this.isUpdated = in.readByte() != 0;
    }

    public static final Creator<EditLabourReportResponseData> CREATOR = new Creator<EditLabourReportResponseData>() {
        @Override
        public EditLabourReportResponseData createFromParcel(Parcel source) {
            return new EditLabourReportResponseData(source);
        }

        @Override
        public EditLabourReportResponseData[] newArray(int size) {
            return new EditLabourReportResponseData[size];
        }
    };
}
