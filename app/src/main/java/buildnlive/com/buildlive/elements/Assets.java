package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Assets extends RealmObject {
    @Index
    @PrimaryKey
    private String assets_id;
    private String item_rent_id;
    private String name;
    private String reg_no;

    public Assets(String item_rent_id,String assets_id, String name,String reg_no) {
        this.assets_id=assets_id;
        this.name = name;
        this.reg_no=reg_no;
        this.item_rent_id=item_rent_id;
    }

    public Assets() {
    }

    public Assets parseFromJSON(JSONObject obj) throws JSONException {
        setAssetsId(obj.getString("asset_id"));
        setItemRentId(obj.getString("item_rent_id"));
        setAssetsName(obj.getString("name"));
        setRegNo(obj.getString("reg_no"));
        return this;
    }

    private void setRegNo(String reg_no) { this.reg_no=reg_no;
    }

    private void setItemRentId(String item_rent_id) { this.item_rent_id=item_rent_id;
    }

    private void setAssetsId(String assets_id) { this.assets_id=assets_id;
    }

    private void setAssetsName(String name) { this.name=name;
    }


    public String getAssets_id() {
        return assets_id;
    }

    public String getName() {
        return name;
    }

    public String getItem_rent_id() {
        return item_rent_id;
    }

    public String getReg_no() {
        return reg_no;
    }
}
