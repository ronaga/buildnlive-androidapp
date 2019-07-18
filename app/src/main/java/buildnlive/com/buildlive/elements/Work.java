package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import buildnlive.com.buildlive.App;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Work extends RealmObject {
    @PrimaryKey
    @Index
    private String id;
    private String workId;
    private String workListId;
    private String masterWorkId;
    private String workName;
    private String units;
    private String workCode;
    private String duration;
    private String quantity;
    private String qty_completed;
    private String start;
    private String end;
    private String status;
    private String belongsTo;
    private String percent_compl;
    private String assign_qty;
    private String planned_detail_id;
    private String planned_id;
    private String type;
    private String status_color;

    public Work(String id, String workId, String workListId,String masterWorkId, String workName, String units, String workCode, String duration, String quantity, String start, String end, String status,String qty_completed, String percent_compl,String type) {
        this.id = id;
        this.workId = workId;
        this.workListId = workListId;
        this.masterWorkId=masterWorkId;
        this.workName = workName;
        this.units = units;
        this.workCode = workCode;
        this.duration = duration;
        this.quantity = quantity;
        this.start = start;
        this.end = end;
        this.status = status;
        this.qty_completed=qty_completed;
        this.percent_compl=percent_compl;
        this.type=type;
    }
    public Work(String planned_id,String planned_detail_id,String id, String workId, String workListId, String workName, String units, String workCode, String duration, String quantity, String start, String end, String status,String qty_completed, String percent_compl,String type) {
        this.planned_id=planned_id;
        this.planned_detail_id=planned_detail_id;
        this.id = id;
        this.workId = workId;
        this.workListId = workListId;
        this.workName = workName;
        this.units = units;
        this.workCode = workCode;
        this.duration = duration;
        this.quantity = quantity;
        this.start = start;
        this.end = end;
        this.status = status;
        this.qty_completed=qty_completed;
        this.percent_compl=percent_compl;
        this.type=type;
    }
    public Work(String planned_id,String planned_detail_id,String id, String workId, String workListId, String workName, String units, String workCode, String duration, String quantity, String start, String end, String status,String qty_completed, String percent_compl,String type,String status_color) {
        this.planned_id=planned_id;
        this.planned_detail_id=planned_detail_id;
        this.id = id;
        this.workId = workId;
        this.workListId = workListId;
        this.workName = workName;
        this.units = units;
        this.workCode = workCode;
        this.duration = duration;
        this.quantity = quantity;
        this.start = start;
        this.end = end;
        this.status = status;
        this.qty_completed=qty_completed;
        this.percent_compl=percent_compl;
        this.type=type;
        this.status_color=status_color;
    }

    public Work() {
    }

    public Work parseFromJSON(JSONObject obj, String workListId,String masterWorkId, String duration, String quantity, String start, String end, String status,String qty_completed,String percent_compl,String type,String status_color) throws JSONException {
        setWorkListId(workListId);
        setMasterWorkId(masterWorkId);
        setDuration(duration);
        setQuantity(quantity);
        setStart(start);
        setEnd(end);
        setStatus(status);
        setWorkId(obj.getString("work_activity_id"));
        setWorkName(obj.getString("work_activity_name"));
        setUnits(obj.getString("work_activity_units"));
        setWorkCode(obj.getString("work_activity_code"));
        setId(getWorkId() + App.belongsTo);
        setBelongsTo(App.belongsTo);
        setCompletedQty(qty_completed);
        setPercentCompleted(percent_compl);
        setType(type);
        setStatus_color(status_color);

        return this;
    }

    public String getStatus_color() {
        return status_color;
    }

    public void setStatus_color(String status_color) {
        this.status_color = status_color;
    }

    private void setMasterWorkId(String masterWorkId) {
        this.masterWorkId=masterWorkId;
    }

    public String getMasterWorkId() {
        return masterWorkId;
    }

    public Work parseFromJSON(JSONObject obj, String planned_id, String planned_detail_id, String duration, String quantity, String start, String end, String status, String qty_completed, String percent_compl, String assign_qty, String type,String status_color) throws JSONException {
        setPlannedId(planned_id);
        setPlannedDetailid(planned_detail_id);
        setAssignedQuantity(assign_qty);
        setDuration(duration);
        setQuantity(quantity);
        setStart(start);
        setEnd(end);
        setStatus(status);
        setWorkId(obj.getString("work_activity_id"));
        setWorkName(obj.getString("work_activity_name"));
//        setUnits(obj.getString("work_activity_units"));
//        setWorkCode(obj.getString("work_activity_code"));
        setId(getWorkId() + App.belongsTo);
        setBelongsTo(App.belongsTo);
        setCompletedQty(qty_completed);
        setPercentCompleted(percent_compl);
        setType(type);
        setStatus_color(status_color);
        return this;
    }


    private void setType(String type) {this.type=type;
    }

    public String getType() {
        return type;
    }

    private void setAssignedQuantity(String assign_qty) {this.assign_qty=assign_qty;
    }

    public String getAssign_qty() {
        return assign_qty;
    }

    private void setPlannedDetailid(String planned_detail_id) {this.planned_detail_id=planned_detail_id;
    }

    public String getPlanned_detail_id() {
        return planned_detail_id;
    }

    private void setPlannedId(String planned_id) { this.planned_id=planned_id;
    }

    public String getPlanned_id() {
        return planned_id;
    }

    private void setPercentCompleted(String percent_compl) {this.percent_compl=percent_compl;
    }

    public String getPercent_compl() {
        return percent_compl;
    }

    private void setCompletedQty(String qty_completed) {
        this.qty_completed=qty_completed;
    }

    public String getQty_completed() {
        return qty_completed;
    }

//    public String getCompleted() {
//        return completed;
//    }
//
//    public void setCompleted(String completed) {
//        this.completed = completed;
//    }
//
//    public String getTotal() {
//        return total;
//    }
//
//    public void setTotal(String total) {
//        this.total = total;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkId() {
        return workId;
    }

    public String getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(String belongsTo) {
        this.belongsTo = belongsTo;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getWorkListId() {
        return workListId;
    }

    public void setWorkListId(String workListId) {
        this.workListId = workListId;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
