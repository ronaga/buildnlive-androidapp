package buildnlive.com.buildlive.elements;

import android.os.Parcel;
import android.os.Parcelable;

public class FreeLabour implements Parcelable {
    private String labour_type_id;
    private String labour_type;
    private String labour_available;
    private String labour_selected;
    private boolean isUpdated;


    public String getLabour_selected() {
        return labour_selected;
    }

    public void setLabour_selected(String labour_selected) {
        this.labour_selected = labour_selected;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public String getLabour_type_id() {
        return labour_type_id;
    }

    public void setLabour_type_id(String labour_type_id) {
        this.labour_type_id = labour_type_id;
    }

    public String getLabour_type() {
        return labour_type;
    }

    public void setLabour_type(String labour_type) {
        this.labour_type = labour_type;
    }

    public String getLabour_available() {
        return labour_available;
    }

    public void setLabour_available(String labour_available) {
        this.labour_available = labour_available;
    }


    public FreeLabour() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.labour_type_id);
        dest.writeString(this.labour_type);
        dest.writeString(this.labour_available);
        dest.writeString(this.labour_selected);
        dest.writeByte(this.isUpdated ? (byte) 1 : (byte) 0);
    }

    protected FreeLabour(Parcel in) {
        this.labour_type_id = in.readString();
        this.labour_type = in.readString();
        this.labour_available = in.readString();
        this.labour_selected = in.readString();
        this.isUpdated = in.readByte() != 0;
    }

    public static final Creator<FreeLabour> CREATOR = new Creator<FreeLabour>() {
        @Override
        public FreeLabour createFromParcel(Parcel source) {
            return new FreeLabour(source);
        }

        @Override
        public FreeLabour[] newArray(int size) {
            return new FreeLabour[size];
        }
    };
}
