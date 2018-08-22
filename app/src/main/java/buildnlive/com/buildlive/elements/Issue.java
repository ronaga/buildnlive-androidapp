package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import buildnlive.com.buildlive.App;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Issue extends RealmObject {

    @Index
    @PrimaryKey
    private String id;
    private String item_id;
    private String labour_id;
    private long createdOn;
    private String belongsTo;
    private String quantity;
    private String itemName;
    private String labourName;

    public Issue(String id, String item_id, String labour_id, long createdOn, String quantity, String belongsTo) {
        this.id = id;
        this.item_id = item_id;
        this.labour_id = labour_id;
        this.createdOn = createdOn;
        this.quantity = quantity;
        this.belongsTo = belongsTo;
    }

    public Issue() {
    }

    public Issue parseFromJSON(JSONObject obj, String itemName, String labourName) throws JSONException {
        setId(UUID.randomUUID().toString());
        setItem_id(obj.getString("stock_id"));
        setLabour_id(obj.getString("receiver_id"));
        setQuantity(obj.getString("quantity"));
        setCreatedOn(System.currentTimeMillis());
        setItemName(itemName);
        setLabourName(labourName);
        setBelongsTo(App.belongsTo);
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getLabour_id() {
        return labour_id;
    }

    public String getId() {
        return id;
    }

    public String getLabourName() {
        return labourName;
    }

    public void setLabourName(String labourName) {
        this.labourName = labourName;
    }

    public String getItemName() {

        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLabour_id(String labour_id) {
        this.labour_id = labour_id;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public String getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(String belongsTo) {
        this.belongsTo = belongsTo;
    }
}
