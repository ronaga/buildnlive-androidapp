package buildnlive.com.buildlive.elements;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import buildnlive.com.buildlive.App;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject implements Parcelable {
    @Index
    @PrimaryKey
    String id;
    private String name;
    private String unit;
    private String bigUnit;
    private String code;
    private String quantity;
    private String rate;
    private String tax;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.unit);
        dest.writeString(this.bigUnit);
        dest.writeString(this.code);
        dest.writeString(this.quantity);
        dest.writeString(this.rate);
        dest.writeString(this.tax);
        dest.writeString(this.item_type);
        dest.writeByte(this.isUpdated ? (byte) 1 : (byte) 0);
        dest.writeString(this.belongsTo);
    }

    protected Item(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.unit = in.readString();
        this.bigUnit = in.readString();
        this.code = in.readString();
        this.quantity = in.readString();
        this.rate = in.readString();
        this.tax = in.readString();
        this.item_type = in.readString();
        this.isUpdated = in.readByte() != 0;
        this.belongsTo = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
