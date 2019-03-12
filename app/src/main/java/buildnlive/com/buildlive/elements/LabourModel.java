package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;

public class LabourModel extends RealmObject {

    private String name;
    private String quantity;
    private boolean isUpdated;


    public LabourModel() {
    }

    public LabourModel(String name, String quantity) {
        this.name = name;
        this.quantity=quantity;
    }
    public LabourModel parseFromJSON(JSONObject obj) throws JSONException {
        setName(obj.getString("labour_type"));
        setQuantity(obj.getString("labour_count"));
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
