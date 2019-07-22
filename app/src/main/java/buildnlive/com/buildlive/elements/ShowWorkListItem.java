package buildnlive.com.buildlive.elements;

import android.os.Parcel;
import android.os.Parcelable;

public class ShowWorkListItem implements Parcelable {
    private String project_work_id;
    private String work_list_name;

    public String getProject_work_id() {
        return project_work_id;
    }

    public void setProject_work_id(String project_work_id) {
        this.project_work_id = project_work_id;
    }

    public String getWork_list_name() {
        return work_list_name;
    }

    public void setWork_list_name(String work_list_name) {
        this.work_list_name = work_list_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.project_work_id);
        dest.writeString(this.work_list_name);
    }

    public ShowWorkListItem() {
    }

    protected ShowWorkListItem(Parcel in) {
        this.project_work_id = in.readString();
        this.work_list_name = in.readString();
    }

    public static final Parcelable.Creator<ShowWorkListItem> CREATOR = new Parcelable.Creator<ShowWorkListItem>() {
        @Override
        public ShowWorkListItem createFromParcel(Parcel source) {
            return new ShowWorkListItem(source);
        }

        @Override
        public ShowWorkListItem[] newArray(int size) {
            return new ShowWorkListItem[size];
        }
    };
}
