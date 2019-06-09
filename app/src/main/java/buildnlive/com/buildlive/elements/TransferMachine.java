package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

public class TransferMachine {
    private String item_name;
    private String transfer_id;
    private String item_type;
    private String project_name;
    private String qty;
    private String date;
    private String status;

    public TransferMachine( String item_name,String transfer_id,String item_type,String project_name,String qty,String date,String status){
        this.item_name=item_name;
        this.transfer_id=transfer_id;
        this.item_type=item_type;
        this.project_name=project_name;
        this.qty=qty;
        this.date=date;
        this.status=status;
    }

    public TransferMachine(){}

    public TransferMachine parseFromJSON(JSONObject obj) throws JSONException {
        setItem_name(obj.getString("item_name"));
        setTransfer_id(obj.getString("transfer_id"));
        setItem_type(obj.getString("item_type"));
        setProject_name(obj.getString("project_name"));
        setQty(obj.getString("qty"));
        setDate(obj.getString("date"));
        setStatus(obj.getString("status"));
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getDate() {
        return date;
    }

    public String getItem_type() {
        return item_type;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getQty() {
        return qty;
    }

    public String getTransfer_id() {
        return transfer_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setTransfer_id(String transfer_id) {
        this.transfer_id = transfer_id;
    }

}
