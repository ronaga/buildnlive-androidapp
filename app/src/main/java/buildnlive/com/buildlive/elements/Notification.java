package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Notification extends RealmObject {
    @Index
    @PrimaryKey
    String id;
    private String label,type,user,date,level,text;
    public Notification() {
    }

    public Notification(String id, String label,String type,String level,String user,String date,String text) {
        this.id = id;
        this.label = label;
        this.date = date;
        this.type = type;
        this.level = level;
        this.user = user;
        this.text = text;
    }
    public Notification parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("id"));
        setLabel(obj.getString("label"));
        setType(obj.getString("type"));
        setLevel(obj.getString("level"));
        setUser(obj.getString("user"));
        setDate(obj.getString("date"));
        setText(obj.getString("text"));
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
