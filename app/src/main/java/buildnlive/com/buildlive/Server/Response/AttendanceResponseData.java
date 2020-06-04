package buildnlive.com.buildlive.Server.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AttendanceResponseData implements Parcelable {

    @SerializedName("daily_attendence_id")
    private String daily_attendence_id;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("status")
    private String status;
    @SerializedName("day")
    private String day;
    @SerializedName("month")
    private String month;
    @SerializedName("year")
    private String year;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getDaily_attendence_id() {
        return daily_attendence_id;
    }

    public void setDaily_attendence_id(String daily_attendence_id) {
        this.daily_attendence_id = daily_attendence_id;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public AttendanceResponseData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.daily_attendence_id);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.status);
        dest.writeString(this.day);
        dest.writeString(this.month);
        dest.writeString(this.year);
    }

    protected AttendanceResponseData(Parcel in) {
        this.daily_attendence_id = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.status = in.readString();
        this.day = in.readString();
        this.month = in.readString();
        this.year = in.readString();
    }

    public static final Creator<AttendanceResponseData> CREATOR = new Creator<AttendanceResponseData>() {
        @Override
        public AttendanceResponseData createFromParcel(Parcel source) {
            return new AttendanceResponseData(source);
        }

        @Override
        public AttendanceResponseData[] newArray(int size) {
            return new AttendanceResponseData[size];
        }
    };
}
