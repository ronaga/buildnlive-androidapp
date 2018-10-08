package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class ViewLabour extends RealmObject {
    @Index
    @PrimaryKey
    String id;
    private String name;
    private String type;
    private String quantity;
    public ViewLabour() {
    }

    public ViewLabour(String id, String name, String type, String quantity) {
        this.id = id;
        this.name = name;
        this.quantity=quantity;
        this.type=type;
    }

    public ViewLabour parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("transfer_id"));
        setName(obj.getString("vendor_name"));
        setQuantity(obj.getString("labour_qty"));
        setType(obj.getString("labour_type"));
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

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
