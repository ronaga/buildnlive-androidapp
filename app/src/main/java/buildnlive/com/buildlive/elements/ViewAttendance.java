package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;


public class ViewAttendance {
    private String start_time;
    private String end_time;
    private String labour_id;
    private String labour_name;
    private String labour_contact;
    private String labour_type;
    private String labour_role;


    public ViewAttendance parseFromJSON(JSONObject obj) throws JSONException {
        setLabour_id(obj.getString("labour_id"));
        setLabour_name(obj.getString("labour_name"));
        setLabour_contact(obj.getString("labour_contact"));
        setLabour_role(obj.getString("labour_role"));
        setLabour_type(obj.getString("labour_type"));
        setStart_time(obj.getString("start_time"));
        setEnd_time(obj.getString("end_time"));
        return this;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setLabour_contact(String labour_contact) {
        this.labour_contact = labour_contact;
    }

    public void setLabour_id(String labour_id) {
        this.labour_id = labour_id;
    }

    public void setLabour_name(String labour_name) {
        this.labour_name = labour_name;
    }

    public void setLabour_role(String labour_role) {
        this.labour_role = labour_role;
    }

    public void setLabour_type(String labour_type) {
        this.labour_type = labour_type;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getLabour_contact() {
        return labour_contact;
    }

    public String getLabour_id() {
        return labour_id;
    }

    public String getLabour_name() {
        return labour_name;
    }

    public String getLabour_role() {
        return labour_role;
    }

    public String getLabour_type() {
        return labour_type;
    }

    public String getStart_time() {
        return start_time;
    }
}
