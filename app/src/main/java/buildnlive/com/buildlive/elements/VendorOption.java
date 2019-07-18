package buildnlive.com.buildlive.elements;

import android.os.Parcel;
import android.os.Parcelable;

public class VendorOption implements Parcelable {
    private String option_id;
    private String option_label;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.option_id);
        dest.writeString(this.option_label);
    }

    public VendorOption() {
    }

    public String getOption_id() {
        return option_id;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }

    public String getOption_label() {
        return option_label;
    }

    public void setOption_label(String option_label) {
        this.option_label = option_label;
    }

    public static Creator<VendorOption> getCREATOR() {
        return CREATOR;
    }

    protected VendorOption(Parcel in) {
        this.option_id = in.readString();
        this.option_label = in.readString();
    }

    public static final Parcelable.Creator<VendorOption> CREATOR = new Parcelable.Creator<VendorOption>() {
        @Override
        public VendorOption createFromParcel(Parcel source) {
            return new VendorOption(source);
        }

        @Override
        public VendorOption[] newArray(int size) {
            return new VendorOption[size];
        }
    };
}
