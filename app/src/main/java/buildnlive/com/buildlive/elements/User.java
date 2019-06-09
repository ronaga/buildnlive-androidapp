package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String user_id;
    private String user_name;
//    private String project_id;
//    private String project_name;

    public User(){}
//    ,String project_id,String project_name

    public User(String user_id,String user_name){
        this.user_id=user_id;
        this.user_name=user_name;
//        this.project_id=project_id;
//        this.project_name=project_name;
    }

    public User parseFromJSON(JSONObject obj) throws JSONException {
        setUser_name(obj.getString("user_name"));
//        setProject_name(obj.getString("project_name"));
        setUser_id(obj.getString("user_id"));
//        setProject_id(obj.getString("project_id"));
        return this;
    }

//    public void setProject_name(String project_name) {
//        this.project_name = project_name;
//    }

//    public void setProject_id(String project_id) {
//        this.project_id = project_id;
//    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

//    public String getProject_name() {
//        return project_name;
//    }

//    public String getProject_id() {
//        return project_id;
//    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }
}
