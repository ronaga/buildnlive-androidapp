package buildnlive.com.buildlive.elements;

import android.os.Parcel;
import android.os.Parcelable;

public class LabourBreakup implements Parcelable {
    private String structure_labour_id;
    private String type_name;
    private String labour_no;



    public String getLabour_no() {
        return labour_no;
    }

    public void setLabour_no(String labour_no) {
        this.labour_no = labour_no;
    }

    public String getStructure_labour_id() {
        return structure_labour_id;
    }

    public void setStructure_labour_id(String structure_labour_id) {
        this.structure_labour_id = structure_labour_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.structure_labour_id);
        dest.writeString(this.type_name);
        dest.writeString(this.labour_no);
    }

    public LabourBreakup() {
    }

    protected LabourBreakup(Parcel in) {
        this.structure_labour_id = in.readString();
        this.type_name = in.readString();
        this.labour_no = in.readString();
    }

    public static final Creator<LabourBreakup> CREATOR = new Creator<LabourBreakup>() {
        @Override
        public LabourBreakup createFromParcel(Parcel source) {
            return new LabourBreakup(source);
        }

        @Override
        public LabourBreakup[] newArray(int size) {
            return new LabourBreakup[size];
        }
    };
}
