package buildnlive.com.buildlive.Server.Response;

import android.os.Parcel;
import android.os.Parcelable;

public class EditPaymentResponseData implements Parcelable {
    private String account_details_id;
    private String purpose;
    private String description;
    private String amount;
    private String details;
    private String payment_type;
    private String payment_mode;
    private String reason;
    private String payee;

    public String getAccount_details_id() {
        return account_details_id;
    }

    public void setAccount_details_id(String account_details_id) {
        this.account_details_id = account_details_id;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.account_details_id);
        dest.writeString(this.purpose);
        dest.writeString(this.description);
        dest.writeString(this.amount);
        dest.writeString(this.details);
        dest.writeString(this.payment_type);
        dest.writeString(this.payment_mode);
        dest.writeString(this.reason);
        dest.writeString(this.payee);
    }

    public EditPaymentResponseData() {
    }

    protected EditPaymentResponseData(Parcel in) {
        this.account_details_id = in.readString();
        this.purpose = in.readString();
        this.description = in.readString();
        this.amount = in.readString();
        this.details = in.readString();
        this.payment_type = in.readString();
        this.payment_mode = in.readString();
        this.reason = in.readString();
        this.payee = in.readString();
    }

    public static final Parcelable.Creator<EditPaymentResponseData> CREATOR = new Parcelable.Creator<EditPaymentResponseData>() {
        @Override
        public EditPaymentResponseData createFromParcel(Parcel source) {
            return new EditPaymentResponseData(source);
        }

        @Override
        public EditPaymentResponseData[] newArray(int size) {
            return new EditPaymentResponseData[size];
        }
    };
}
