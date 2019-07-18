package buildnlive.com.buildlive.elements;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Order implements Parcelable {

    private String orderId;
    private String type_id;
    private String createdOn;
    private String status;
    private String deliveryDate;
    private String serial_no;

    public Order(String orderId,String type_id, String createdOn, String status, String deliveryDate,String serial_no) {
        this.orderId = orderId;
        this.type_id = type_id;
        this.createdOn = createdOn;
        this.status = status;
        this.deliveryDate = deliveryDate;
        this.serial_no= serial_no;
    }

    public Order() {
    }

    public Order parseFromJSON(JSONObject object) throws JSONException {
        setOrderId(object.getString("purchase_order_id"));
        setTypeId(object.getString("type_id"));
        setCreatedOn(object.getString("date_created"));
        setStatus(object.getString("status"));
        setDeliveryDate(object.getString("delivery_dat"));
        setSerialNo(object.getString("serial_no"));
        return this;
    }

    private void setTypeId(String type_id) {
        this.type_id=type_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
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

    public void setSerialNo(String serial_no) {
        this.serial_no = serial_no;
    }
    public String getSerialNo(){return  serial_no;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderId);
        dest.writeString(this.type_id);
        dest.writeString(this.createdOn);
        dest.writeString(this.status);
        dest.writeString(this.deliveryDate);
        dest.writeString(this.serial_no);
    }

    protected Order(Parcel in) {
        this.orderId = in.readString();
        this.type_id = in.readString();
        this.createdOn = in.readString();
        this.status = in.readString();
        this.deliveryDate = in.readString();
        this.serial_no = in.readString();
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
