package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Machine extends RealmObject implements Serializable {
    @Index
    @PrimaryKey
    private String inventory_item_rent_id;
    private String asset_id;
    private String name;

    public Machine(String inventory_item_rent_id, String asset_id,String name) {
        this.inventory_item_rent_id=inventory_item_rent_id;
        this.asset_id = asset_id;
        this.name=name;
    }

    public Machine() {
    }

    public Machine parseFromJSON(JSONObject obj) throws JSONException {
        setAssetsId(obj.getString("asset_id"));
        setItemRentId(obj.getString("inventory_item_rent_id"));
        setAssetsName(obj.getString("name"));

        return this;
    }

    private void setAssetsName(String name) {
        this.name=name;
    }

    private void setItemRentId(String inventory_item_rent_id) {
        this.inventory_item_rent_id=inventory_item_rent_id;
    }

    private void setAssetsId(String asset_id) {
        this.asset_id=asset_id;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public String getInventory_item_rent_id() {
        return inventory_item_rent_id;
    }

    public String getName() {
        return name;
    }
}
