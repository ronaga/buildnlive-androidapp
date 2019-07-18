package buildnlive.com.buildlive.elements;

import android.os.Parcel;
import android.os.Parcelable;

public class LabourDeploy implements Parcelable {
    private String structure_labour_id;
    private String structure_details;
    private String labour_no;


    public String getLabour_no() {
        return labour_no;
    }

    public void setLabour_no(String labour_no) {
        this.labour_no = labour_no;
    }

    public String getStructure_id() {
        return structure_labour_id;
    }

    public void setStructure_id(String structure_id) {
        this.structure_labour_id = structure_id;
    }

    public String getStructure_details() {
        return structure_details;
    }

    public void setStructure_details(String structure_details) {
        this.structure_details = structure_details;
    }


    public LabourDeploy() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.structure_labour_id);
        dest.writeString(this.structure_details);
        dest.writeString(this.labour_no);
    }

    protected LabourDeploy(Parcel in) {
        this.structure_labour_id = in.readString();
        this.structure_details = in.readString();
        this.labour_no = in.readString();
    }

    public static final Creator<LabourDeploy> CREATOR = new Creator<LabourDeploy>() {
        @Override
        public LabourDeploy createFromParcel(Parcel source) {
            return new LabourDeploy(source);
        }

        @Override
        public LabourDeploy[] newArray(int size) {
            return new LabourDeploy[size];
        }
    };
}
