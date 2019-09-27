package buildnlive.com.buildlive.elements;

import com.google.gson.annotations.SerializedName;

public class ActivityItem {

    public ActivityItem() {
    }

    @SerializedName("layouttype")
    private String layouttype;
    @SerializedName("assign_qty")
    private String assignQty;
    @SerializedName("work_schedule")
    private WorkSchedule workSchedule;
    @SerializedName("planned_detail_id")
    private String plannedDetailId;
    @SerializedName("planned_id")
    private String plannedId;

    public String getLayouttype() {
        return layouttype;
    }

    public void setLayouttype(String layouttype) {
        this.layouttype = layouttype;
    }

    public String getAssignQty() {
        return assignQty;
    }

    public void setAssignQty(String assignQty) {
        this.assignQty = assignQty;
    }

    public WorkSchedule getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(WorkSchedule workSchedule) {
        this.workSchedule = workSchedule;
    }

    public String getPlannedDetailId() {
        return plannedDetailId;
    }

    public void setPlannedDetailId(String plannedDetailId) {
        this.plannedDetailId = plannedDetailId;
    }

    public String getPlannedId() {
        return plannedId;
    }

    public void setPlannedId(String plannedId) {
        this.plannedId = plannedId;
    }

    public class WorkSchedule {
        @SerializedName("status_color")
        private String statusColor;
        @SerializedName("current_status")
        private String currentStatus;
        @SerializedName("schedule_finish_date")
        private String scheduleFinishDate;
        @SerializedName("schedule_start_date")
        private String scheduleStartDate;
        @SerializedName("units")
        private String units;
        @SerializedName("percent_compl")
        private String percentCompl;
        @SerializedName("qty_completed")
        private String qtyCompleted;
        @SerializedName("qty")
        private String qty;
        @SerializedName("work_duration")
        private String workDuration;
        @SerializedName("schedule_id")
        private String scheduleId;
        @SerializedName("work_details")
        private WorkDetails workDetails;

        public String getStatusColor() {
            return statusColor;
        }

        public void setStatusColor(String statusColor) {
            this.statusColor = statusColor;
        }

        public String getCurrentStatus() {
            return currentStatus;
        }

        public void setCurrentStatus(String currentStatus) {
            this.currentStatus = currentStatus;
        }

        public String getScheduleFinishDate() {
            return scheduleFinishDate;
        }

        public void setScheduleFinishDate(String scheduleFinishDate) {
            this.scheduleFinishDate = scheduleFinishDate;
        }

        public String getScheduleStartDate() {
            return scheduleStartDate;
        }

        public void setScheduleStartDate(String scheduleStartDate) {
            this.scheduleStartDate = scheduleStartDate;
        }

        public String getUnits() {
            return units;
        }

        public void setUnits(String units) {
            this.units = units;
        }

        public String getPercentCompl() {
            return percentCompl;
        }

        public void setPercentCompl(String percentCompl) {
            this.percentCompl = percentCompl;
        }

        public String getQtyCompleted() {
            return qtyCompleted;
        }

        public void setQtyCompleted(String qtyCompleted) {
            this.qtyCompleted = qtyCompleted;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getWorkDuration() {
            return workDuration;
        }

        public void setWorkDuration(String workDuration) {
            this.workDuration = workDuration;
        }

        public String getScheduleId() {
            return scheduleId;
        }

        public void setScheduleId(String scheduleId) {
            this.scheduleId = scheduleId;
        }

        public WorkDetails getWorkDetails() {
            return workDetails;
        }

        public void setWorkDetails(WorkDetails workDetails) {
            this.workDetails = workDetails;
        }
    }

    public class WorkDetails {
        @SerializedName("work_activity_code")
        private String workActivityCode;
        @SerializedName("work_activity_units")
        private String workActivityUnits;
        @SerializedName("work_activity_name")
        private String workActivityName;
        @SerializedName("work_activity_id")
        private String workActivityId;

        public String getWorkActivityCode() {
            return workActivityCode;
        }

        public void setWorkActivityCode(String workActivityCode) {
            this.workActivityCode = workActivityCode;
        }

        public String getWorkActivityUnits() {
            return workActivityUnits;
        }

        public void setWorkActivityUnits(String workActivityUnits) {
            this.workActivityUnits = workActivityUnits;
        }

        public String getWorkActivityName() {
            return workActivityName;
        }

        public void setWorkActivityName(String workActivityName) {
            this.workActivityName = workActivityName;
        }

        public String getWorkActivityId() {
            return workActivityId;
        }

        public void setWorkActivityId(String workActivityId) {
            this.workActivityId = workActivityId;
        }
    }
}
