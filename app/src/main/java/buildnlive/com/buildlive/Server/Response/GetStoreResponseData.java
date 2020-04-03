package buildnlive.com.buildlive.Server.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GetStoreResponseData implements Parcelable {

    @SerializedName("current_stock")
    private String current_stock;
    @SerializedName("slip_no")
    private String slip_no;
    @SerializedName("issue_type")
    private String issue_type;
    @SerializedName("remarks")
    private String remarks;
    @SerializedName("asset_reg")
    private String assetReg;
    @SerializedName("issue_to")
    private String issueTo;
    @SerializedName("staff_name")
    private String staffName;
    @SerializedName("qty_requested")
    private String qtyRequested;
    @SerializedName("item_name")
    private String itemName;
    @SerializedName("store_request_id")
    private String storeRequestId;
    @SerializedName("units")
    private String units;

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getCurrent_stock() {
        return current_stock;
    }

    public void setCurrent_stock(String current_stock) {
        this.current_stock = current_stock;
    }

    public String getSlip_no() {
        return slip_no;
    }

    public void setSlip_no(String slip_no) {
        this.slip_no = slip_no;
    }

    public String getIssue_type() {
        return issue_type;
    }

    public void setIssue_type(String issue_type) {
        this.issue_type = issue_type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAssetReg() {
        return assetReg;
    }

    public void setAssetReg(String assetReg) {
        this.assetReg = assetReg;
    }

    public String getIssueTo() {
        return issueTo;
    }

    public void setIssueTo(String issueTo) {
        this.issueTo = issueTo;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getQtyRequested() {
        return qtyRequested;
    }

    public void setQtyRequested(String qtyRequested) {
        this.qtyRequested = qtyRequested;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getStoreRequestId() {
        return storeRequestId;
    }

    public void setStoreRequestId(String storeRequestId) {
        this.storeRequestId = storeRequestId;
    }

    public GetStoreResponseData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.current_stock);
        dest.writeString(this.slip_no);
        dest.writeString(this.issue_type);
        dest.writeString(this.remarks);
        dest.writeString(this.assetReg);
        dest.writeString(this.issueTo);
        dest.writeString(this.staffName);
        dest.writeString(this.qtyRequested);
        dest.writeString(this.itemName);
        dest.writeString(this.storeRequestId);
        dest.writeString(this.units);
    }

    protected GetStoreResponseData(Parcel in) {
        this.current_stock = in.readString();
        this.slip_no = in.readString();
        this.issue_type = in.readString();
        this.remarks = in.readString();
        this.assetReg = in.readString();
        this.issueTo = in.readString();
        this.staffName = in.readString();
        this.qtyRequested = in.readString();
        this.itemName = in.readString();
        this.storeRequestId = in.readString();
        this.units = in.readString();
    }

    public static final Creator<GetStoreResponseData> CREATOR = new Creator<GetStoreResponseData>() {
        @Override
        public GetStoreResponseData createFromParcel(Parcel source) {
            return new GetStoreResponseData(source);
        }

        @Override
        public GetStoreResponseData[] newArray(int size) {
            return new GetStoreResponseData[size];
        }
    };
}
