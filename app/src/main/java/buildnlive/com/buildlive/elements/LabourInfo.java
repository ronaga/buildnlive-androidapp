package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class LabourInfo extends RealmObject {
    @Index
    @PrimaryKey
    String id;
    private String name;
    private String unit;
    private String quantity;
    private boolean isUpdated;

    public LabourInfo() {
    }

    public LabourInfo(String id, String name, String unit, String quantity) {
        this.id = id;
        this.name = name;
        this.quantity=quantity;
    }

    public LabourInfo parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("daily_labour_id"));
        setName(obj.getString("type"));
        setQuantity(obj.getString("qty"));
        return this;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }
}
