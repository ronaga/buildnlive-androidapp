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

    public Work(String id, String workId, String workListId, String workName, String units, String workCode, String duration, String quantity, String start, String end, String status,String qty_completed) {
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
    }

    public Work() {
    }

    public Work parseFromJSON(JSONObject obj, String workListId, String duration, String quantity, String start, String end, String status,String qty_completed) throws JSONException {
        setWorkListId(workListId);
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
        return this;
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
