package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;

public class LabourModel extends RealmObject {

    private String name;
    private String quantity;
    private String labour_type_id;
    private boolean isUpdated;


    public LabourModel() {
    }

    public String getLabour_type_id() {
        return labour_type_id;
    }

    public void setLabour_type_id(String labour_type_id) {
        this.labour_type_id = labour_type_id;
    }

    public LabourModel parseFromJSON(JSONObject obj) throws JSONException {
        setName(obj.getString("labour_type"));
        setQuantity(obj.getString("labour_count"));
        setLabour_type_id(obj.getString("labour_type_id"));
        return this;
    }


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }
}
