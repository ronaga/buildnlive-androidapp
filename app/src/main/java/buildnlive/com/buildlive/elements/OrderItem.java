package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderItem {
    private String name;
    private String quantity;
    private String unit;
    private String orderId;
    private String comments;
    private boolean isIncluded;

    public OrderItem(String name, String quantity, String unit, String orderId, String comments) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.orderId = orderId;
        this.comments = comments;
    }

    public OrderItem() {
    }

    public OrderItem parseFromJSON(JSONObject obj) throws JSONException{
        setName(obj.getString("item_name"));
        setOrderId(obj.getString("purchase_order_list_id"));
        setQuantity(obj.getString("item_qty"));
        setUnit(obj.getString("item_units"));
        return this;
    }

    public boolean isIncluded() {
        return isIncluded;
    }

    public void setIncluded(boolean included) {
        isIncluded = included;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
