package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import buildnlive.com.buildlive.App;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Activity implements Serializable {

    @PrimaryKey
    @Index
    private String id;
    private String activityId;
    private String activityListId;
    private String activityName;
    private String duration;
    private String quantity;
    private String units;
    private String start;
    private String end;
    private String status;
    private String belongsTo;
    private String qty_completed;

    public Activity(String id, String activityId, String activityListId, String activityName, String units, String duration, String quantity, String start, String end, String status,String qty_completed) {
        this.id = id;
        this.activityId = activityId;
        this.activityListId = activityListId;
        this.activityName = activityName;
        this.duration = duration;
        this.quantity = quantity;
        this.start = start;
        this.end = end;
        this.units = units;
        this.status = status;
        this.qty_completed=qty_completed;
    }

    public Activity() {
    }

    public Activity parseFromJSON(JSONObject obj, String activityListId, String duration, String quantity, String units, String start, String end, String status,String qty_completed) throws JSONException {
        setActivityListId(activityListId);
        setDuration(duration);
        setQuantity(quantity);
        setQuantityCompleted(qty_completed);
        setStart(start);
        setEnd(end);
        setStatus(status);
        setUnits(units);
        setActivityId(obj.getString("work_activity_id"));
        setActivityName(obj.getString("work_activity_name"));
        setId(getActivityId() + App.belongsTo);
        setBelongsTo(App.belongsTo);
        return this;
    }

    private void setQuantityCompleted(String qty_completed) {
        this.qty_completed=qty_completed;
    }

    public String getQty_completed() {
        return qty_completed;
    }
//
//    public String getCompleted() {
//        return completed;
//    }
//
//    public void setCompleted(String completed) {
//        this.completed = completed;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getBelongsTo() {
        return belongsTo;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public void setBelongsTo(String belongsTo) {
        this.belongsTo = belongsTo;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityListId() {
        return activityListId;
    }

    public void setActivityListId(String activityListId) {
        this.activityListId = activityListId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
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
