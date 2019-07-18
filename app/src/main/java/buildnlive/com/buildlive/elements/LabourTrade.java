package buildnlive.com.buildlive.elements;

import android.os.Parcel;
import android.os.Parcelable;

public class LabourTrade implements Parcelable {
//    {"trade_id":"0","trade_name":"Select Trade"}
    private String trade_id;
    private String trade_name;

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
    }

    public String getTrade_name() {
        return trade_name;
    }

    public void setTrade_name(String trade_name) {
        this.trade_name = trade_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.trade_id);
        dest.writeString(this.trade_name);
    }

    public LabourTrade() {
    }

    protected LabourTrade(Parcel in) {
        this.trade_id = in.readString();
        this.trade_name = in.readString();
    }

    public static final Parcelable.Creator<LabourTrade> CREATOR = new Parcelable.Creator<LabourTrade>() {
        @Override
        public LabourTrade createFromParcel(Parcel source) {
            return new LabourTrade(source);
        }

        @Override
        public LabourTrade[] newArray(int size) {
            return new LabourTrade[size];
        }
    };
}
