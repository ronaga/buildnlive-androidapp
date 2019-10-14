package buildnlive.com.buildlive.elements;

import com.google.gson.annotations.SerializedName;

public class WorkerHistory {

    @SerializedName("daily_attendence_id")
    private String dailyAttendenceId;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("start_date_time")
    private String startDateTime;

/*
    public WorkerHistory(String endTime, String startTime, String startDateTime) {
        this.endTime = endTime;
        this.startTime = startTime;
        this.startDateTime = startDateTime;
    }*/

    public WorkerHistory(String dailyAttendenceId, String endTime, String startTime, String startDateTime) {
        this.dailyAttendenceId = dailyAttendenceId;
        this.endTime = endTime;
        this.startTime = startTime;
        this.startDateTime = startDateTime;
    }

    public String getDailyAttendenceId() {
        return dailyAttendenceId;
    }

    public void setDailyAttendenceId(String dailyAttendenceId) {
        this.dailyAttendenceId = dailyAttendenceId;
    }


    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }
}
