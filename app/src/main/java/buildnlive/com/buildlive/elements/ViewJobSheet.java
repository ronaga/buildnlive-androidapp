package buildnlive.com.buildlive.elements;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.UUID;

import buildnlive.com.buildlive.App;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class ViewJobSheet implements Serializable {




    private String inventory_asset_jobsheet_id;
    private String inventory_item_rent_id;
    private String inventory_assets_id;
    private String name;
    private String log_in_time;
    private String log_out_time;
    private String service_time;
    private String work_description;
    private String log_out_meter;
    private String log_in_meter;

    public ViewJobSheet(String inventory_asset_jobsheet_id, String inventory_item_rent_id, String inventory_assets_id, String name, String log_in_time, String log_out_time, String service_time, String work_description, String log_out_meter, String log_in_meter) {
        this.inventory_asset_jobsheet_id = inventory_asset_jobsheet_id;
        this.inventory_item_rent_id = inventory_item_rent_id;
        this.inventory_assets_id = inventory_assets_id;
        this.name = name;
        this.log_in_time = log_in_time;
        this.log_out_time = log_out_time;
        this.service_time = service_time;
        this.work_description = work_description;
        this.log_out_meter = log_out_meter;
        this.log_in_meter = log_in_meter;
    }


    public ViewJobSheet() {
    }

    public ViewJobSheet parseFromJSON(JSONObject obj) throws JSONException {
//        setId(obj.getString("id"));
        setInventoryJobsheetId(obj.getString("inventory_asset_jobsheet_id"));
        setInventoryRentId(obj.getString("inventory_item_rent_id"));
        setInventoryAssetId(obj.getString("inventory_assets_id"));
        setName(obj.getString("name"));
        setLoginTime(obj.getString("log_in_time"));
        setLogoutTime(obj.getString("log_out_time"));
        setServiceTime(obj.getString("service_time"));
        setWorkDescription(obj.getString("work_description"));
        setLog_out_meter(obj.getString("log_out_meter"));
        setLog_in_meter(obj.getString("log_in_meter"));
        return this;
    }

    public String getLog_out_meter() {
        return log_out_meter;
    }

    public void setLog_out_meter(String log_out_meter) {
        this.log_out_meter = log_out_meter;
    }

    public String getLog_in_meter() {
        return log_in_meter;
    }

    public void setLog_in_meter(String log_in_meter) {
        this.log_in_meter = log_in_meter;
    }

    private void setWorkDescription(String work_description) {
        this.work_description=work_description;
    }

    private void setServiceTime(String service_time) {
        this.service_time=service_time;
    }

    private void setLogoutTime(String log_out_time) {
        this.log_out_time=log_out_time;
    }

    private void setLoginTime(String log_in_time) {
        this.log_in_time=log_in_time;
    }

    private void setName(String name) {
        this.name=name;
    }

    private void setInventoryAssetId(String inventory_assets_id) {
    this.inventory_assets_id=inventory_assets_id;
    }

    private void setInventoryRentId(String inventory_item_rent_id) {
    this.inventory_item_rent_id=inventory_item_rent_id;
    }

    private void setInventoryJobsheetId(String inventory_asset_jobsheet_id) {
    this.inventory_asset_jobsheet_id=inventory_asset_jobsheet_id;
    }

    public String getInventory_item_rent_id() {
        return inventory_item_rent_id;
    }

    public String getName() {
        return name;
    }

    public String getInventory_asset_jobsheet_id() {
        return inventory_asset_jobsheet_id;
    }

    public String getInventory_assets_id() {
        return inventory_assets_id;
    }

    public String getLog_in_time() {
        return log_in_time;
    }

    public String getLog_out_time() {
        return log_out_time;
    }

    public String getService_time() {
        return service_time;
    }

    public String getWork_description() {
        return work_description;
    }

}
