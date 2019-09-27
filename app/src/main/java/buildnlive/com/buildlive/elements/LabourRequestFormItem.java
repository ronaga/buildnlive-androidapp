package buildnlive.com.buildlive.elements;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LabourRequestFormItem {

    @SerializedName("list")
    private ArrayList<LabourList> labourList;
    @SerializedName("work_name")
    private String workName;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("date")
    private String date;
    @SerializedName("name")
    private String name;

    public ArrayList<LabourList> getLabourList() {
        return labourList;
    }

    public void setLabourList(ArrayList<LabourList> labourList) {
        this.labourList = labourList;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public class LabourList {
        @SerializedName("labour_requested")
        private String labourRequested;
        @SerializedName("type_name")
        private String typeName;
        @SerializedName("labour_request_detail_id")
        private String labourRequestDetailId;

        private String labour_selected;
        private boolean updated;



        public String getLabour_selected() {
            return labour_selected;
        }

        public void setLabour_selected(String labour_selected) {
            this.labour_selected = labour_selected;
        }

        public boolean isUpdated() {
            return updated;
        }

        public void setUpdated(boolean updated) {
            this.updated = updated;
        }

        public String getLabourRequested() {
            return labourRequested;
        }

        public void setLabourRequested(String labourRequested) {
            this.labourRequested = labourRequested;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getLabourRequestDetailId() {
            return labourRequestDetailId;
        }

        public void setLabourRequestDetailId(String labourRequestDetailId) {
            this.labourRequestDetailId = labourRequestDetailId;
        }
    }
}
