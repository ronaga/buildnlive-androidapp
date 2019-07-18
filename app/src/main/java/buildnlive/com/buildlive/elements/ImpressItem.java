package buildnlive.com.buildlive.elements;

/*
*   "impress_id": "1",
    "impress_details": "Impress Cash",
    "impress_amount": "Rs. 45000/-",
    "impress_date": "10/01/2019"*/

import android.os.Parcel;
import android.os.Parcelable;

public class ImpressItem implements Parcelable {
    private String impress_id;
    private String impress_details;
    private String impress_amount;
    private String impress_date;
    private String impress_status;


    public String getImpress_status() {
        return impress_status;
    }

    public void setImpress_status(String impress_status) {
        this.impress_status = impress_status;
    }

    public String getImpress_id() {
        return impress_id;
    }

    public void setImpress_id(String impress_id) {
        this.impress_id = impress_id;
    }

    public String getImpress_details() {
        return impress_details;
    }

    public void setImpress_details(String impress_details) {
        this.impress_details = impress_details;
    }

    public String getImpress_amount() {
        return impress_amount;
    }

    public void setImpress_amount(String impress_amount) {
        this.impress_amount = impress_amount;
    }

    public String getImpress_date() {
        return impress_date;
    }

    public void setImpress_date(String impress_date) {
        this.impress_date = impress_date;
    }

    public ImpressItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.impress_id);
        dest.writeString(this.impress_details);
        dest.writeString(this.impress_amount);
        dest.writeString(this.impress_date);
        dest.writeString(this.impress_status);
    }

    protected ImpressItem(Parcel in) {
        this.impress_id = in.readString();
        this.impress_details = in.readString();
        this.impress_amount = in.readString();
        this.impress_date = in.readString();
        this.impress_status = in.readString();
    }

    public static final Creator<ImpressItem> CREATOR = new Creator<ImpressItem>() {
        @Override
        public ImpressItem createFromParcel(Parcel source) {
            return new ImpressItem(source);
        }

        @Override
        public ImpressItem[] newArray(int size) {
            return new ImpressItem[size];
        }
    };
}
