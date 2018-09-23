package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import buildnlive.com.buildlive.App;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class ViewIssue extends RealmObject {

    @Index
    @PrimaryKey
    private String issue_id;
//    private String id;
    private String item_name;
    private String quantity;
    private String units;
    private String reciever;
    private String checkout_Date;
    private String status;

    public ViewIssue(String item_name, String issue_id, String quantity, String units, String reciever, String checkout_Date,String status) {
//        this.id = id;
        this.item_name= item_name;
        this.issue_id = issue_id;
        this.quantity = quantity;
        this.units = units;
        this.reciever = reciever;
        this.checkout_Date = checkout_Date;
        this.status= status;
    }

    public ViewIssue() {
    }

    public ViewIssue parseFromJSON(JSONObject obj) throws JSONException {
//        setId(obj.getString("id"));
        setItemName(obj.getString("item_name"));
        setIssue_id(obj.getString("issue_id"));
        setQuantity(obj.getString("qty"));
        setUnit(obj.getString("units"));
        setReciever(obj.getString("receiver"));
        setCheckOutDate(obj.getString("check_out_date"));
        setStatus(obj.getString("status"));
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


//    public void setId(String id) {
//        this.id = id;
//    }
//    public String getId() {
//        return id;
//    }


    public void setIssue_id(String issue_id) {
        this.issue_id = issue_id;
    }
    public String getIssueId() {
        return issue_id;
    }


    public void setUnit(String unit) {
        this.units = unit;
    }
    public String getUnit() {
        return units;
    }


    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getReciever() {
        return reciever;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkout_Date = checkOutDate;
    }
    public String getCheckOutDate() {
        return checkout_Date;
    }


    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }


    public void setItemName(String itemName) {
        this.item_name = itemName;
    }
    public String getItemName() {
        return item_name;
    }

}
