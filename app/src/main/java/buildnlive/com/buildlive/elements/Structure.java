package buildnlive.com.buildlive.elements;

import android.os.Parcel;
import android.os.Parcelable;

public class Structure implements Parcelable {
    private String structure_id;
    private String structure_details;
    private String structure_area;



    public String getStructure_id() {
        return structure_id;
    }

    public void setStructure_id(String structure_id) {
        this.structure_id = structure_id;
    }

    public String getStructure_details() {
        return structure_details;
    }

    public void setStructure_details(String structure_details) {
        this.structure_details = structure_details;
    }

    public String getStructure_area() {
        return structure_area;
    }

    public void setStructure_area(String structure_area) {
        this.structure_area = structure_area;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.structure_id);
        dest.writeString(this.structure_details);
        dest.writeString(this.structure_area);
    }

    public Structure() {
    }

    protected Structure(Parcel in) {
        this.structure_id = in.readString();
        this.structure_details = in.readString();
        this.structure_area = in.readString();
    }

    public static final Parcelable.Creator<Structure> CREATOR = new Parcelable.Creator<Structure>() {
        @Override
        public Structure createFromParcel(Parcel source) {
            return new Structure(source);
        }

        @Override
        public Structure[] newArray(int size) {
            return new Structure[size];
        }
    };
}
