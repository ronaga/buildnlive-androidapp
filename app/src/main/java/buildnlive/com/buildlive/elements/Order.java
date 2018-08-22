package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

public class Order {

    private String orderId;
    private String createdOn;
    private String status;
    private String deliveryDate;

    public Order(String orderId, String createdOn, String status, String deliveryDate) {
        this.orderId = orderId;
        this.createdOn = createdOn;
        this.status = status;
        this.deliveryDate = deliveryDate;
    }

    public Order() {
    }

    public Order parseFromJSON(JSONObject object) throws JSONException {
        setOrderId(object.getString("purchase_order_id"));
        setCreatedOn(object.getString("date_created"));
        setStatus(object.getString("status"));
        setDeliveryDate(object.getString("delivery_dat"));
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
