package buildnlive.com.buildlive.elements.Approval;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ApproveIssueItem implements Parcelable {
    @SerializedName("receiver")
    private String receiver;
    @SerializedName("qty")
    private String qty;
    @SerializedName("item_name")
    private String itemName;
    @SerializedName("issue_id")
    private String issueId;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public ApproveIssueItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.receiver);
        dest.writeString(this.qty);
        dest.writeString(this.itemName);
        dest.writeString(this.issueId);
        dest.writeString(this.status);
    }

    protected ApproveIssueItem(Parcel in) {
        this.receiver = in.readString();
        this.qty = in.readString();
        this.itemName = in.readString();
        this.issueId = in.readString();
        this.status = in.readString();
    }

    public static final Creator<ApproveIssueItem> CREATOR = new Creator<ApproveIssueItem>() {
        @Override
        public ApproveIssueItem createFromParcel(Parcel source) {
            return new ApproveIssueItem(source);
        }

        @Override
        public ApproveIssueItem[] newArray(int size) {
            return new ApproveIssueItem[size];
        }
    };
}
