package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Contractor extends RealmObject {
    @Index
    @PrimaryKey
    private String contractor_id;
    private String contractor_name;

    public Contractor(String contractor_id, String contractor_name) {
        this.contractor_id=contractor_id;
        this.contractor_name = contractor_name;
    }

    public Contractor() {
    }

    public Contractor parseFromJSON(JSONObject obj) throws JSONException {
        setContractorId(obj.getString("vendor_id"));
        setContractorName(obj.getString("name"));
        return this;
    }

    private void setContractorId(String contractor_id) { this.contractor_id=contractor_id;
    }

    private void setContractorName(String contractor_name) { this.contractor_name=contractor_name;
    }

    public String getContractor_name() {
        return contractor_name;
    }

    public String getContractor_id() {
        return contractor_id;
    }
}
