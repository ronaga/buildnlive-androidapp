package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import buildnlive.com.buildlive.App;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class ProjectMember extends RealmObject {

    @Index
    @PrimaryKey
    private String id;
    private String userId;
    private String name;
    private String mobile;
    @Index
    private String belongsTo;

    public ProjectMember(String userId, String name, String mobile, String belongsTo) {
        this.userId = userId;
        this.name = name;
        this.mobile = mobile;
        this.belongsTo = belongsTo;
    }

    public ProjectMember() {
    }

    public ProjectMember parseFromJSON(JSONObject obj) throws JSONException {
        setUserId(obj.getString("user_id"));
        setId(getUserId() + App.belongsTo);
        setName(obj.getString("name"));
        setMobile(obj.getString("mobile"));
        setBelongsTo(App.belongsTo);
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(String belongsTo) {
        this.belongsTo = belongsTo;
    }
}
