package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import buildnlive.com.buildlive.App;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Project extends RealmObject {
    @Index
    @PrimaryKey
    private String id;
    private String name;
    private String address;

    public Project(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Project() {
    }

    public Project parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("project_id"));
        setName(obj.getString("project_name"));
        setAddress(obj.getString("site_address"));
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
