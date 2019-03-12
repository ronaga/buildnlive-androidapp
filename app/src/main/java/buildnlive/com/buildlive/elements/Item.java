package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import buildnlive.com.buildlive.App;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject implements Serializable {
    @Index
    @PrimaryKey
    String id;
    private String name;
    private String unit;
    private String bigUnit;
    private String code;
    private String quantity;
    private String item_type;
    private boolean isUpdated;
    private String belongsTo;

    public Item() {
    }

    public Item(String id, String name, String unit, String bigUnit, String code,String item_type) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.bigUnit = bigUnit;
        this.code = code;
        this.item_type=item_type;
    }

    public Item parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("stock_id"));
        setName(obj.getString("name"));
        setCode(obj.getString("item_code"));
        setQuantity(obj.getString("qty_left"));
        setUnit(obj.getString("units"));
        setItemType(obj.getString("type"));
        setBelongsTo(App.belongsTo);
        return this;
    }

    private void setItemType(String item_type) {this.item_type=item_type;
    }

    public String getItem_type() {
        return item_type;
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

    public String getCode() {
        return code;
    }

    public void setCode(String type) {
        this.code = type;
    }
}
