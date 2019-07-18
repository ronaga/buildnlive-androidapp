package buildnlive.com.buildlive.elements.Approval;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ApproveIndentItem implements Parcelable{
        @SerializedName("qty_required")
        private String qtyRequired;
        @SerializedName("item_name")
        private String itemName;
        @SerializedName("item_id")
        private String itemId;
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }


        public String getQtyRequired() {
            return qtyRequired;
        }

        public void setQtyRequired(String qtyRequired) {
            this.qtyRequired = qtyRequired;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public ApproveIndentItem() {
        }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.qtyRequired);
        dest.writeString(this.itemName);
        dest.writeString(this.itemId);
        dest.writeString(this.status);
    }

    protected ApproveIndentItem(Parcel in) {
        this.qtyRequired = in.readString();
        this.itemName = in.readString();
        this.itemId = in.readString();
        this.status = in.readString();
    }

    public static final Creator<ApproveIndentItem> CREATOR = new Creator<ApproveIndentItem>() {
        @Override
        public ApproveIndentItem createFromParcel(Parcel source) {
            return new ApproveIndentItem(source);
        }

        @Override
        public ApproveIndentItem[] newArray(int size) {
            return new ApproveIndentItem[size];
        }
    };
}
