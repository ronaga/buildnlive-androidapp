package buildnlive.com.buildlive.elements;

import android.os.Parcel;
import android.os.Parcelable;

public class WorkList implements Parcelable {
    private String pms_project_work_list;
    private String name;
    private String qty_left;
    private String units;
    private String status;


    public String getPms_project_work_list() {
        return pms_project_work_list;
    }

    public void setPms_project_work_list(String pms_project_work_list) {
        this.pms_project_work_list = pms_project_work_list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty_left() {
        return qty_left;
    }

    public void setQty_left(String qty_left) {
        this.qty_left = qty_left;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pms_project_work_list);
        dest.writeString(this.name);
        dest.writeString(this.qty_left);
        dest.writeString(this.units);
        dest.writeString(this.status);
    }

    public WorkList() {
    }

    protected WorkList(Parcel in) {
        this.pms_project_work_list = in.readString();
        this.name = in.readString();
        this.qty_left = in.readString();
        this.units = in.readString();
        this.status = in.readString();
    }

    public static final Parcelable.Creator<WorkList> CREATOR = new Parcelable.Creator<WorkList>() {
        @Override
        public WorkList createFromParcel(Parcel source) {
            return new WorkList(source);
        }

        @Override
        public WorkList[] newArray(int size) {
            return new WorkList[size];
        }
    };
}
