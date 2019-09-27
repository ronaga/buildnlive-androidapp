package buildnlive.com.buildlive.elements;

public class WorkNameItem {
    private String work_activity_id, work_activity_name, work_activity_units,work_qty;

    public WorkNameItem() {
    }

    public String getWork_qty() {
        return work_qty;
    }

    public void setWork_qty(String work_qty) {
        this.work_qty = work_qty;
    }

    public String getWork_activity_id() {
        return work_activity_id;
    }

    public void setWork_activity_id(String work_activity_id) {
        this.work_activity_id = work_activity_id;
    }

    public String getWork_activity_name() {
        return work_activity_name;
    }

    public void setWork_activity_name(String work_activity_name) {
        this.work_activity_name = work_activity_name;
    }

    public String getWork_activity_units() {
        return work_activity_units;
    }

    public void setWork_activity_units(String work_activity_units) {
        this.work_activity_units = work_activity_units;
    }
}
