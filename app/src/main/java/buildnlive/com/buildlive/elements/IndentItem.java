package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class IndentItem extends RealmObject {
    @Index
    @PrimaryKey
    String id;
    private String name;
    private String unit;
    private String quantity;
    private boolean isUpdated;

    public IndentItem() {
    }

    public IndentItem(String id, String name, String unit, String quantity) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.quantity=quantity;
    }

    public IndentItem parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("stock_id"));
        setName(obj.getString("name"));
        setQuantity(obj.getString("qty_left"));
        setUnit(obj.getString("units"));
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

    public String getUnit() {
        return unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }
}
