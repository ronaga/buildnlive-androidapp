package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RepairItem implements Serializable {
    private String name;
    private String item_rent_id;
    private String qty;

    public RepairItem(String item_rent_id,String name, String qty) {
        this.name = name;
        this.qty=qty;
        this.item_rent_id=item_rent_id;
    }

    public RepairItem() {
    }

    public RepairItem parseFromJSON(JSONObject obj) throws JSONException {
        setItem_rent_id(obj.getString("item_rent_id"));
        setName(obj.getString("item_name"));
        setQty(obj.getString("qty"));
        return this;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setItem_rent_id(String item_rent_id) {
        this.item_rent_id = item_rent_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public String getItem_rent_id() {
        return item_rent_id;
    }

    public String getName() {
        return name;
    }
}
