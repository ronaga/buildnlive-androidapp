package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ViewRepairItem implements Serializable {
    private String name;
    private String service_id;
    private String qty;
    private String date;
    private String status;

    public ViewRepairItem(String service_id,String name, String qty,String date,String status) {
        this.name = name;
        this.qty=qty;
        this.service_id=service_id;
        this.date=date;
        this.status=status;
    }

    public ViewRepairItem() {
    }

    public ViewRepairItem parseFromJSON(JSONObject obj) throws JSONException {
        setService_id(obj.getString("service_id"));
        setName(obj.getString("item_name"));
        setQty(obj.getString("qty"));
        setDate(obj.getString("date"));
        setStatus(obj.getString("status"));
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public String getService_id() {
        return service_id;
    }

    public String getName() {
        return name;
    }
}
