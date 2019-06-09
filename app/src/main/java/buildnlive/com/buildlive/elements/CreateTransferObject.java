package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CreateTransferObject implements Serializable {

    private String item_name;
    private String item_id;
    private String item_type;
    private String qty;
    private String units;

    public CreateTransferObject( String item_name,String item_id,String item_type,String qty,String units){
        this.item_name=item_name;
        this.item_id=item_id;
        this.item_type=item_type;
        this.units=units;
        this.qty=qty;
    }

    public CreateTransferObject(){}

    public CreateTransferObject parseFromJSON(JSONObject obj) throws JSONException {
        setItem_name(obj.getString("item_name"));
        setItem_type(obj.getString("item_type"));
        setItem_id(obj.getString("item_id"));
        setUnits(obj.getString("units"));
        setQty(obj.getString("qty"));
        return this;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getUnits() {
        return units;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getItem_name() {
        return item_name;
    }


    public String getItem_type() {
        return item_type;
    }

    public String getQty() {
        return qty;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

}
