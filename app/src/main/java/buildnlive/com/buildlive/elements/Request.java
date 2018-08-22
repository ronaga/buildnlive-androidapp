package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import buildnlive.com.buildlive.App;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Request extends RealmObject {
    private String id;
    private String name;
    private String description;
    private String type;
    private String typeId;
    private String quantity;
    private String material;
    private String status;
    @PrimaryKey
    private String createdOn;
    private String completedOn;
    @Index
    private String belongsTo;

    public Request() {

    }

    public Request(String name, String description, String type, String material) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.material = material;
    }

    public Request parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("request_form_id"));
        setName(obj.getString("request_form_label"));
        setDescription(obj.getString("request_form_specs"));
        setTypeId(obj.getString("request_form_type_id"));
        setType(obj.getString("request_form_type"));
        setQuantity(obj.getString("request_form_quantity"));
        setStatus(obj.getString("request_form_status"));
        setCreatedOn(obj.getString("request_form_creation_date"));
        setCompletedOn(obj.getString("request_form_completed_date"));
        setMaterial(obj.getString("name"));
        setBelongsTo(App.belongsTo);
        return this;
    }

    public Request parseFromString(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        setName(obj.getString("label"));
        setDescription(obj.getString("specs"));
        setTypeId(obj.getString("form_type_id"));
        setType(obj.getString("form_type"));
        setQuantity(obj.getString("quantity"));
        setStatus("Pending");
        setMaterial(obj.getString("material"));
        setCreatedOn(obj.getString("date"));

        setBelongsTo(App.belongsTo);
        return this;
    }

    public String getName() {
        return name;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(String completedOn) {
        this.completedOn = completedOn;
    }

    public String getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(String belongsTo) {
        this.belongsTo = belongsTo;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
