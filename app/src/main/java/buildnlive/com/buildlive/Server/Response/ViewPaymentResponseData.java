package buildnlive.com.buildlive.Server.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ViewPaymentResponseData implements Parcelable {

    @SerializedName("type")
    private String type;
    @SerializedName("payee")
    private String payee;
    @SerializedName("desc")
    private String desc;
    @SerializedName("button")
    private String button;
    @SerializedName("user")
    private String user;
    @SerializedName("status")
    private String status;
    @SerializedName("amount")
    private String amount;
    @SerializedName("date")
    private String date;
    @SerializedName("account_name")
    private String accountName;
    @SerializedName("account_detail_id")
    private String accountDetailId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountDetailId() {
        return accountDetailId;
    }

    public void setAccountDetailId(String accountDetailId) {
        this.accountDetailId = accountDetailId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.payee);
        dest.writeString(this.desc);
        dest.writeString(this.button);
        dest.writeString(this.user);
        dest.writeString(this.status);
        dest.writeString(this.amount);
        dest.writeString(this.date);
        dest.writeString(this.accountName);
        dest.writeString(this.accountDetailId);
    }

    public ViewPaymentResponseData() {
    }

    protected ViewPaymentResponseData(Parcel in) {
        this.type = in.readString();
        this.payee = in.readString();
        this.desc = in.readString();
        this.button = in.readString();
        this.user = in.readString();
        this.status = in.readString();
        this.amount = in.readString();
        this.date = in.readString();
        this.accountName = in.readString();
        this.accountDetailId = in.readString();
    }

    public static final Parcelable.Creator<ViewPaymentResponseData> CREATOR = new Parcelable.Creator<ViewPaymentResponseData>() {
        @Override
        public ViewPaymentResponseData createFromParcel(Parcel source) {
            return new ViewPaymentResponseData(source);
        }

        @Override
        public ViewPaymentResponseData[] newArray(int size) {
            return new ViewPaymentResponseData[size];
        }
    };
}
