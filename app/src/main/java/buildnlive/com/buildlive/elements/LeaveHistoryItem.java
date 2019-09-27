package buildnlive.com.buildlive.elements;

import com.google.gson.annotations.SerializedName;

public class LeaveHistoryItem {

    @SerializedName("leave_id")
    private String leaveId;
    @SerializedName("status")
    private String status;
    @SerializedName("leave_dates")
    private String leaveDates;
    @SerializedName("comments")
    private String comments;

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLeaveDates() {
        return leaveDates;
    }

    public void setLeaveDates(String leaveDates) {
        this.leaveDates = leaveDates;
    }
}
