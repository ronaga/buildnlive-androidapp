package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import buildnlive.com.buildlive.App;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject {
    @Index
    @PrimaryKey
    String id;
    private String name;
    private String unit;
    private String bigUnit;
    private String type;
    private String quantity;
    private boolean isUpdated;
    private String belongsTo;

    public Item() {
    }

    public Item(String id, String name, String unit, String bigUnit, String type) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.bigUnit = bigUnit;
        this.type = type;
    }

    public Item parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("stock_id"));
        setName(obj.getString("name"));
        setType(obj.getString("item_code"));
        setQuantity(obj.getString("qty_left"));
        setUnit(obj.getString("units"));
        setBelongsTo(App.belongsTo);
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

    public boolean isUpdated() {
        return isUpdated;
    }

    public String getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(String belongsTo) {
        this.belongsTo = belongsTo;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBigUnit() {
        return bigUnit;
    }

    public void setBigUnit(String bigUnit) {
        this.bigUnit = bigUnit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
