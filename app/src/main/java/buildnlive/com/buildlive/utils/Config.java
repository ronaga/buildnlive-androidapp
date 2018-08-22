package buildnlive.com.buildlive.utils;

public class Config {

    public static final int REQ_TIME_OUT = 45000; //45 seconds
    public static final String REQ_GET_LABOUR = "http://buildnlive.com/app/mobileapp/index.php?r=site/SendLabourList&user_id=[0]&project_id=[1]";
    public static final String REQ_GET_INVENTORY = "http://buildnlive.com/app/mobileapp/index.php?r=site/SendInventory&user_id=[0]&project_id=[1]";
    public static final String REQ_SYNC_PROJECT = "http://buildnlive.com/app/mobileapp/index.php?r=site/SyncProject&user_id=[0]&project_id=[1]";
    public static final String REQ_POST_INVENTORY_UPDATES = "http://buildnlive.com/app/mobileapp/index.php?r=site/updateInventory";
    public static final String REQ_POST_CHECK_IN = "http://buildnlive.com/app/mobileapp/index.php?r=site/UpdateAttendenceCheckIn";
    public static final String REQ_POST_CHECK_OUT = "http://buildnlive.com/app/mobileapp/index.php?r=site/UpdateAttendenceCheckOut";
    public static final String REQ_GET_REQUEST_TYPE = "http://buildnlive.com/app/mobileapp/index.php?r=site/GetRequestTypeList&type=[0]&user_id=[1]";
    public static final String REQ_GET_USER_ATTENDANCE = "http://buildnlive.com/app/mobileapp/index.php?r=site/SendLabourAttendence&user_id=[0]&labour_id=[1]&project_id=[2]";
    public static final String SEND_REQUEST_ITEM = "http://buildnlive.com/app/mobileapp/index.php?r=site/GetRequestForm";
    public static final String GET_REQUEST_LIST = "http://buildnlive.com/app/mobileapp/index.php?r=site/SendRequestList&project_id=[0]&user_id=[1]";
    public static final String GET_PROJECT_PLANS = "http://buildnlive.com/app/mobileapp/index.php?r=site/SendProjectPlans&project_id=[0]&user_id=[1]";
    public static final String REQ_LOGIN = "http://buildnlive.com/app/mobileapp/index.php?r=site/UserLogin";
    public static final String SEND_ISSUED_ITEM = "http://buildnlive.com/app/mobileapp/index.php?r=site/GetIssueItem";
    public static final String REQ_DAILY_WORK = "http://buildnlive.com/app/mobileapp/index.php?r=site/WorkActivityDaily&user_id=[0]&project_id=[1]&type=Work";
    public static final String REQ_DAILY_WORK_ACTIVITY = "http://buildnlive.com/app/mobileapp/index.php?r=site/ActivityByWork&user_id=[0]&project_work_list_id=[1]";
    public static final String INVENTORY_ITEM_REQUEST = "http://buildnlive.com/app/mobileapp/index.php?r=site/InventoryItemRequest";
    public static final String REQ_DAILY_WORK_ACTIVITY_UPDATE = "http://buildnlive.com/app/mobileapp/index.php?r=site/GetWorkActivityUpdate";
    public static final String REQ_PLAN_MARKUP = "http://buildnlive.com/app/mobileapp/index.php?r=site/ProjectPlansMarkup";
    public static final String REQ_PURCHASE_ORDER = "http://buildnlive.com/app/mobileapp/index.php?r=site/SitePurchaseOrder&user_id=[0]&project_id=[1]";
    public static final String REQ_PURCHASE_ORDER_LISTING = "http://buildnlive.com/app/mobileapp/index.php?r=site/PurchaseOrderList&user_id=[0]&purchase_order_id=[1]";
    public static final String REQ_PURCHASE_ORDER_UPDATE = "http://buildnlive.com/app/mobileapp/index.php?r=site/GetPurchaseOrderList";

    public static final String PREF_NAME = "OGIL";
}
