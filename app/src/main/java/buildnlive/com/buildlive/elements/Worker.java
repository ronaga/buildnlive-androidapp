package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import buildnlive.com.buildlive.App;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Worker extends RealmObject {
    @Index
    @PrimaryKey
    String id;
    private String name;
    @Index
    private String workerId;
    private String extra;
    private String roleAssigned;
    private String type;
    private long checkInTime;
    private String checkInTimeSelected;
    private String checkOutTimeSelected;
    private long checkOutTime;
    private String projectAssigned;
    private boolean checkedIn;
    private boolean checkedOut;
    private String attendanceId;
    private String labour_present;
    private String start_time;
    private String end_time;
    private String fix_time_in;
    private String fix_time_out;
    @Index
    private String belongsTo;

    public Worker() {

    }

    public Worker(String id, String name, String roleAssigned, String projectAssigned) {
        this.id = id;
        this.name = name;
        this.roleAssigned = roleAssigned;
        this.projectAssigned = projectAssigned;
    }

    public Worker parseFromJSON(JSONObject obj) throws JSONException {
        setWorkerId(obj.getString("labour_id"));
        setId(obj.getString("labour_id") + App.belongsTo);
        setName(obj.getString("labour_name"));
        setExtra(obj.getString("labour_contact"));
        setRoleAssigned(obj.getString("labour_role"));
        setType(obj.getString("labour_type"));
        setPresent(obj.getString("labour_present"));
        setStart_time(obj.getString("start_time"));
        setEnd_time(obj.getString("end_time"));
        setFix_time_in(obj.getString("fix_time_in"));
        setFix_time_out(obj.getString("fix_time_out"));
        setBelongsTo(App.belongsTo);
        return this;
    }

    public String getFix_time_in() {
        return fix_time_in;
    }

    public String getFix_time_out() {
        return fix_time_out;
    }

    public void setFix_time_in(String fix_time_in) {
        this.fix_time_in = fix_time_in;
    }

    public void setFix_time_out(String fix_time_out) {
        this.fix_time_out = fix_time_out;
    }

    public void setLabour_present(String labour_present) {
        this.labour_present = labour_present;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setCheckOutTimeSelected(String checkOutTimeSelected) {
        this.checkOutTimeSelected = checkOutTimeSelected;
    }

    public void setCheckInTimeSelected(String checkInTimeSelected) {
        this.checkInTimeSelected = checkInTimeSelected;
    }

    public String getCheckInTimeSelected() {
        return checkInTimeSelected;
    }

    public String getCheckOutTimeSelected() {
        return checkOutTimeSelected;
    }

    private void setPresent(String labour_present) {this.labour_present=labour_present;
    }

    public String getLabour_present() {
        return labour_present;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getRoleAssigned() {
        return roleAssigned;
    }

    public void setRoleAssigned(String roleAssigned) {
        this.roleAssigned = roleAssigned;
    }

    public long getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(long checkInTime) {
        this.checkInTime = checkInTime;
    }

    public long getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(long checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getProjectAssigned() {
        return projectAssigned;
    }

    public void setProjectAssigned(String projectAssigned) {
        this.projectAssigned = projectAssigned;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }

    public String getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(String belongsTo) {
        this.belongsTo = belongsTo;
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }
}
