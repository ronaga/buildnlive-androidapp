package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Category extends RealmObject {
    @Index
    @PrimaryKey
    String id;
    private String name;
    public Category() {
    }

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("id"));
        setName(obj.getString("name"));
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
}
