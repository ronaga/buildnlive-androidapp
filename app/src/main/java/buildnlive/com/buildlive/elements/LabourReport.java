package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class LabourReport extends RealmObject {
    @Index
    @PrimaryKey
    private String daily_labour_report_id;
    private String contractor_name;
    private String date;
    private String labour_count;


    public LabourReport() {
    }

    public LabourReport(String daily_labour_report_id, String contractor_name, String date, String labour_count) {
        this.daily_labour_report_id = daily_labour_report_id;
        this.contractor_name=contractor_name;
        this.date=date;
        this.labour_count=labour_count;
    }
    public LabourReport parseFromJSON(JSONObject obj) throws JSONException {
        setDaily_labour_report_id(obj.getString("daily_labour_report_id"));
        setContractor_name(obj.getString("contractor_name"));
        setDate(obj.getString("date"));
        setLabour_count(obj.getString("labour_count"));
        return this;
    }

    public String getDaily_labour_report_id() {
        return daily_labour_report_id;
    }

    public void setDaily_labour_report_id(String daily_labour_report_id) {
        this.daily_labour_report_id = daily_labour_report_id;
    }

    public String getContractor_name() {
        return contractor_name;
    }

    public void setContractor_name(String name) {
        this.contractor_name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLabour_count() {
        return labour_count;
    }

    public void setLabour_count(String labour_count) {
        this.labour_count = labour_count;
    }
}
