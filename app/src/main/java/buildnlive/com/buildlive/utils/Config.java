package buildnlive.com.buildlive.utils;

public class Config {

    public static final int REQ_TIME_OUT = 45000; //45 seconds
    public static final String REQ_GET_LABOUR = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendLabourList&user_id=[0]&project_id=[1]";
    public static final String REQ_GET_EMPLOYEE = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendEmployees&user_id=[0]&project_id=[1]";
    public static final String REQ_GET_INVENTORY = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendInventory&user_id=[0]&project_id=[1]";
    public static final String REQ_GET_ITEM_INVENTORY = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendItemListing&user_id=[0]&project_id=[1]";
    public static final String REQ_SYNC_PROJECT = "https://buildnlive.com/app/mobileapp/index.php?r=site/SyncProject&user_id=[0]&project_id=[1]";
    public static final String REQ_POST_INVENTORY_UPDATES = "https://buildnlive.com/app/mobileapp/index.php?r=site/updateInventory";
    public static final String REQ_POST_CHECK_IN = "https://buildnlive.com/app/mobileapp/index.php?r=site/UpdateAttendenceCheckIn";
    public static final String REQ_POST_CHECK_OUT = "https://buildnlive.com/app/mobileapp/index.php?r=site/UpdateAttendencecheckOut";
    public static final String REQ_POST_EMP_CHECK_IN = "https://buildnlive.com/app/mobileapp/index.php?r=site/UpdateAttendenceCheckInUser";
    public static final String MARK_LEAVE_USER = "https://buildnlive.com/app/mobileapp/index.php?r=site/MarkLeaveUser";
    public static final String REQ_POST_EMP_CHECK_OUT = "https://buildnlive.com/app/mobileapp/index.php?r=site/UpdateAttendencecheckoutUser";
    public static final String REQ_GET_REQUEST_TYPE = "https://buildnlive.com/app/mobileapp/index.php?r=site/GetRequestTypeList&type=[0]&user_id=[1]";
    public static final String REQ_GET_USER_ATTENDANCE = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendLabourAttendence&user_id=[0]&labour_id=[1]&project_id=[2]";
    public static final String REQ_GET_STAFF_ATTENDANCE = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendUserAttendence&user_id=[0]&staff_id=[1]&project_id=[2]";
    public static final String SEND_REQUEST_ITEM = "https://buildnlive.com/app/mobileapp/index.php?r=site/GetRequestForm";
    public static final String GET_REQUEST_LIST = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendRequestList&project_id=[0]&user_id=[1]";
    public static final String GET_PROJECT_PLANS = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendProjectPlans&project_id=[0]&user_id=[1]";
    public static final String REQ_LOGIN = "https://buildnlive.com/app/mobileapp/index.php?r=site/UserLogin";
    public static final String SEND_ISSUED_ITEM = "https://buildnlive.com/app/mobileapp/index.php?r=site/GetIssueItem";
    public static final String UpdateStoreRequestDetail = "https://buildnlive.com/app/mobileapp/index.php?r=Approvals/UpdateStoreRequestDetail";
    public static final String UpdateSitePaymentDetail = "https://buildnlive.com/app/mobileapp/index.php?r=Approvals/UpdateSitePaymentDetail";
    public static final String SEND_STOCK_ITEM = "https://buildnlive.com/app/mobileapp/index.php?r=site/GetStockRequest";
    public static final String REQ_DAILY_WORK = "https://buildnlive.com/app/mobileapp/index.php?r=site/WorkActivityDaily&user_id=[0]&project_id=[1]&type=Work&project_work_list_id=[2]";
    public static final String ShowWorkList = "https://buildnlive.com/app/mobileapp/index.php?r=site/ShowWorkList&user_id=[0]&project_id=[1]&structure_id=[2]";
    public static final String ShowWorkListName = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendWorkListNames";
    public static final String Arealist = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendWorkListArea&pms_project_work_list=[0]";
    public static final String REQ_DAILY_WORK_ACTIVITY = "https://buildnlive.com/app/mobileapp/index.php?r=site/ActivityByWork&user_id=[0]&project_work_list_id=[1]";
    public static final String WorkActivityPlanning = "https://buildnlive.com/app/mobileapp/index.php?r=site/WorkActivityPlanning&user_id=[0]&project_id=[1]&type=[2]&project_work_list_id=[3]";
    public static final String INVENTORY_ITEM_REQUEST = "https://buildnlive.com/app/mobileapp/index.php?r=site/InventoryItemRequest";
    public static final String REQ_DAILY_WORK_ACTIVITY_UPDATE = "https://buildnlive.com/app/mobileapp/index.php?r=site/GetWorkActivityUpdate";
    public static final String REQ_PLAN_MARKUP = "https://buildnlive.com/app/mobileapp/index.php?r=site/ProjectPlansMarkup";
    public static final String REQ_PURCHASE_ORDER = "https://buildnlive.com/app/mobileapp/index.php?r=site/SitePurchaseOrder&user_id=[0]&project_id=[1]";
    public static final String REQ_PURCHASE_ORDER_LISTING = "https://buildnlive.com/app/mobileapp/index.php?r=site/PurchaseOrderList&user_id=[0]&purchase_order_id=[1]";
    public static final String REQ_PURCHASE_ORDER_UPDATE = "https://buildnlive.com/app/mobileapp/index.php?r=site/GetPurchaseOrderList";
    public static final String REQ_SENT_CATEGORIES = "https://buildnlive.com/app/mobileapp/index.php?r=site/SentCategories&user_id=[0]";
    public static final String GET_SITE_LIST = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendItemList&cat_id=[0]&user_id=[1]&project_id=[2]";
    public static final String SEND_REQUEST_SITE_LIST = "https://buildnlive.com/app/mobileapp/index.php?r=site/SiteRequestItem&user_id=[0]";
    public static final String SEND_REQUEST_STATUS = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendRequestStatus&user_id=[0] ";
    public static final String SEND_LOCAL_PURCHASE = "https://buildnlive.com/app/mobileapp/index.php?r=site/LocalPurchase";
    public static final String SEND_LOCAL_PURCHASE_MULTI = "https://buildnlive.com/app/mobileapp/index.php?r=site/LocalPurchaseMultiple";
    public static final String SEND_LOCAL_PURCHASE_CONT = "https://buildnlive.com/app/mobileapp/index.php?r=site/LocalPurchaseCont";
    public static final String SEND_LOCAL_PURCHASE_MULTI_CONT = "https://buildnlive.com/app/mobileapp/index.php?r=site/LocalPurchaseMultipleCont";
    public static final String SEND_SITE_PAYMENTS = "https://buildnlive.com/app/mobileapp/index.php?r=site/SitePayments";
    public static final String SEND_SITE_PAYMENTS_CONT = "https://buildnlive.com/app/mobileapp/index.php?r=site/SitePaymentsCont";
    public static final String SEND_NOTIFICATIONS = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendNotifications&user_id=[0]&project_id=[1]";
    public static final String GET_NOTIFICATIONS = "https://buildnlive.com/app/mobileapp/index.php?r=site/GetNotifications";
    public static final String GET_NOTIFICATIONS_COUNT = "https://buildnlive.com/app/mobileapp/index.php?r=site/GetNotificationCount";
    public static final String GET_ISSUE_STATUS = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendIssueStatus&user_id=[0]&project_id=[1]";
    public static final String GET_STOCK_REQUEST_STATUS = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendStockRequestStatus&user_id=[0]&project_id=[1]";
    public static final String GET_LABOUR_VENDOR_LIST = "https://buildnlive.com/app/mobileapp/index.php?r=site/GetContractorList&user_id=[0]&project_id=[1]";
    public static final String GET_LABOUR_LIST = "https://buildnlive.com/app/mobileapp/index.php?r=site/GetLabourCount&user_id=[0]&project_id=[1]&vendor_id=[2]";
    public static final String SEND_LABOUR_LIST = "https://buildnlive.com/app/mobileapp/index.php?r=site/SaveLabourTransfer";
    public static final String VIEW_LABOUR_LIST = "https://buildnlive.com/app/mobileapp/index.php?RFr=site/SendTransferDetails&user_id=[0]&project_id=[1]";
    public static final String SEND_COMMENTS = "https://buildnlive.com/app/mobileapp/index.php?r=site/UpdateLabourTransfer";
    public static final String FORGOT_PASSWORD = "https://buildnlive.com/app/mobileapp/index.php?r=site/ForgotPassword";
    public static final String RESET_PASSWORD = "https://buildnlive.com/app/mobileapp/index.php?r=site/ChangePassword";
    public static final String WORK_FILTERS = "https://buildnlive.com/app/mobileapp/index.php?r=site/WorkActivityFilters";
    public static final String PREF_NAME = "OGIL";
    public static final String GET_CONTRACTORS = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendVendors&user_id=[0]&project_id=[1]";
    public static final String VIEW_LABOUR_REPORT = "https://buildnlive.com/app/mobileapp/index.php?r=site/ViewLabourReport&user_id=[0]&project_id=[1]";
    public static final String GET_LABOUR_PROGRESS = "https://buildnlive.com/app/mobileapp/index.php?r=site/GetLabourProgress";
    public static final String UpdateSubLabourReport = "https://buildnlive.com/app/mobileapp/index.php?r=Approvals/UpdateSubLabourReport";
    public static final String VIEW_DETAILED_LABOUR_REPORT = "https://buildnlive.com/app/mobileapp/index.php?r=site/ViewLabourReportBreakup&user_id=[0]&daily_labour_report_id=[1]&project_id=[2]";
    public static final String LABOUR_TYPE_LIST = "https://buildnlive.com/app/mobileapp/index.php?r=site/GetLabourTypes&user_id=[0]";
    public static final String GET_PLANNED_LIST = "https://buildnlive.com/app/mobileapp/index.php?r=site/GetPlannedWork&user_id=[0]&project_id=[1]&structure_id=[2]";
    public static final String UPDATE_PLANNED_WORK = "https://buildnlive.com/app/mobileapp/index.php?r=site/GetPlannedWorkUpdate";
    public static final String SEND_ASSETS = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendAssets&user_id=[0]&project_id=[1]";
    public static final String SEND_ISSUE_VENDORS = "https://buildnlive.com/app/mobileapp/index.php?r=site/SendIssueVendors&user_id=[0]&project_id=[1]";
    public static final String RETURN_ISSUED_ITEM = "https://buildnlive.com/app/mobileapp/index.php?r=site/ReturnIssuedItem&issue_id=[0]&item_record_id=[1]&return_qty=[2]&return_type=[3]";
    public static final String INVENTORY_SEARCH = "https://buildnlive.com/app/mobileapp/index.php?r=site/searchItem&project=[1]&user_id=[0]&text=[2]";
    public static final String INVENTORY_ITEM_SEARCH = "https://buildnlive.com/app/mobileapp/index.php?r=site/searchItemStock&project=[1]&user_id=[0]&text=[2]";
    public static final String LOGOOUT = "https://buildnlive.com/app/mobileapp/index.php?r=site/UserLogout&user_id=[0]";
    public static final String CREATE_LABOUR = "https://buildnlive.com/app/mobileapp/index.php?r=Site/CreateLabour";
    public static final String CREATE_ACTIVITY = "https://buildnlive.com/app/mobileapp/index.php?r=Site/CreateActivity";
    public static final String GET_MACHINE_LIST = "https://buildnlive.com/app/mobileapp/index.php?r=Site/SendMachineList&user_id=[0]&project_id=[1]";
    public static final String VIEW_MACHINE_LIST = "https://buildnlive.com/app/mobileapp/index.php?r=Site/ViewJobSheet&user_id=[0]&project_id=[1]";
    public static final String SEND_MACHINE_LIST = "https://buildnlive.com/app/mobileapp/index.php?r=Site/AssetJobSheet";
    public static final String TRANSFER_IN = "https://buildnlive.com/app/mobileapp/index.php?r=Site/TransferIn&user_id=[0]&project_id=[1]";
    public static final String TRANSFER_OUT = "https://buildnlive.com/app/mobileapp/index.php?r=Site/TransferOut&user_id=[0]&project_id=[1]";
    public static final String RECEIVE_TRANSFER = "https://buildnlive.com/app/mobileapp/index.php?r=Site/ReceiveTransfer";
    public static final String SEND_TRANSFER_LIST = "https://buildnlive.com/app/mobileapp/index.php?r=Site/SendTransferList&user_id=[0]&project_id=[1]";
    public static final String GET_TRANSFER_REQUEST = "https://buildnlive.com/app/mobileapp/index.php?r=Site/GetTransferRequest";
    public static final String SEND_USERS = "https://buildnlive.com/app/mobileapp/index.php?r=Site/SendUsers&user_id=[0]";
    public static final String SEND_PROJECTS = "https://buildnlive.com/app/mobileapp/index.php?r=Site/SendProjects&user_id=[0]";
    public static final String SEARCH_TRANSFER = "https://buildnlive.com/app/mobileapp/index.php?r=site/SearchTransferList&project=[1]&user_id=[0]&text=[2]";
    public static final String SEND_REPAIR_ITEM = "https://buildnlive.com/app/mobileapp/index.php?r=Site/SendItemRepair&user_id=[0]&project_id=[1]";
    public static final String GET_REPAIR_ITEM = "https://buildnlive.com/app/mobileapp/index.php?r=Site/GetRepairItem";
    public static final String SEND_REPAIR_STATUS = "https://buildnlive.com/app/mobileapp/index.php?r=Site/SendRepairStatus&user_id=[0]&project_id=[1]";
    public static final String GET_RETURN_REPAIR_STATUS = "https://buildnlive.com/app/mobileapp/index.php?r=Site/GetReturnRepairStatus&user_id=[0]&project_id=[1]&service_id=[2]&status=[3]&return_qty=[4]";
    public static final String UPDATE_LABOUR_PROJECT = "https://buildnlive.com/app/mobileapp/index.php?r=Site/UpdateLabourProject&user_id=[0]&project_id=[1]&code=[2]";
    public static final String SEND_LABOUR_ATTENDANCE_LIST = "https://buildnlive.com/app/mobileapp/index.php?r=Site/SendLabourAttendenceList&user_id=[0]&project_id=[1]";
    public static final String SEND_USER_ATTENDANCE_LIST = "https://buildnlive.com/app/mobileapp/index.php?r=Site/SendUserAttendenceList&user_id=[0]&project_id=[1]";
    public static final String UPDATE_FCM_KEY = "https://buildnlive.com/app/mobileapp/index.php?r=Site/UpdateFCMKey";
    public static final String CheckUID = "https://buildnlive.com/app/mobileapp/index.php?r=Site/CheckUID";
    public static final String APPROVALS = "https://buildnlive.com/app/mobileapp/index.php?r=Site/SendApprovals&user_id=[0]&detail_id=[1]";
    public static final String GET_APPROVALS = "https://buildnlive.com/app/mobileapp/index.php?r=Site/GetApprovals";
    public static final String SUBMIT_APPROVALS = "https://buildnlive.com/app/mobileapp/index.php?r=Approval/SubmitApprovals";
    public static final String SITE_PAYMENT_OPTION = "https://buildnlive.com/app/mobileapp/index.php?r=Site/SitePaymentOptions&user_id=[0]&project_id=[1]";
    public static final String LABOUR_TRADE = "https://buildnlive.com/app/mobileapp/index.php?r=Site/LabourTrade&user_id=[0]&project_id=[1]";
    public static final String LABOUR_TYPE = "https://buildnlive.com/app/mobileapp/index.php?r=Site/LabourType&user_id=[0]&project_id=[1]";
    public static final String CHECK_IN = "https://buildnlive.com/app/mobileapp/index.php?r=Site/CheckIn&user_id=[0]&latitude=[1]&longitude=[2]";
    public static final String CHECK_OUT = "https://buildnlive.com/app/mobileapp/index.php?r=Site/CheckOut&user_id=[0]&attendence_id=[1]&latitude=[2]&longitude=[3]";
    public static final String MARK_ABSENT_USER = "https://buildnlive.com/app/mobileapp/index.php?r=Site/MarkAbsentUser&user_id=[0]&latitude=[1]&longitude=[2]&leave_status=[3]";
    public static final String GET_ATTENDANCE = "https://buildnlive.com/app/mobileapp/index.php?r=site/GetAttendence&user_id=[0]";
    public static final String RECEIVE_IMPRESS = "https://buildnlive.com/app/mobileapp/index.php?r=Site/ReceiveImpress&user_id=[0]&project_id=[1]";
    public static final String GetReceiveImpress = "https://buildnlive.com/app/mobileapp/index.php?r=Site/GetReceiveImpress&impress_id=[0]&user_id=[1]&status=[2]";
    public static final String SendStructures = "https://buildnlive.com/app/mobileapp/index.php?r=Site/SendStructures&user_id=[0]&project_id=[1]";
    public static final String SendLabourDeployment = "https://buildnlive.com/app/mobileapp/index.php?r=Site/SendLabourDeployement&user_id=[0]&project_id=[1]";
    public static final String GetLabourDeployment = "https://buildnlive.com/app/mobileapp/index.php?r=Site/GetLabourDeployement&user_id=[0]&project_id=[1]";
    public static final String SEND_LABOUR_STRUCTURE = "https://buildnlive.com/app/mobileapp/index.php?r=Site/SendLabourStructure&structure_labour_id=[0]";
    public static final String GetLabourOut = "https://buildnlive.com/app/mobileapp/index.php?r=Site/GetLabourOut&structure_labour_id=[0]&count=[1]&user_id=[2]";
    public static final String FreeLabour = "https://buildnlive.com/app/mobileapp/index.php?r=Site/FreeLabour&user_id=[0]&project_id=[1]";
    public static final String SendWorkList = "https://buildnlive.com/app/mobileapp/index.php?r=Site/SendWorkList&&user_id=[0]&project_id=[1]&structure_id=[2]";
    public static final String GetDailyPlanning = "https://buildnlive.com/app/mobileapp/index.php?r=Site/GetDailyPlanning";
    public static final String SendBalance = "https://buildnlive.com/app/mobileapp/index.php?r=Site/SendBalance&user_id=[0]&project_id=[1]&type=[2]";
    public static final String LabourDeployementRequestList = "https://buildnlive.com/app/mobileapp/index.php?r=Site/LabourDeployementRequestList&user_id=[0]&project_id=[1]";
    public static final String LabourDeployementRequestView = "https://buildnlive.com/app/mobileapp/index.php?r=Site/LabourDeployementRequestView&labour_request_id=[0]";
    public static final String SaveLabourRequest = "https://buildnlive.com/app/mobileapp/index.php?r=Site/SaveLabourRequest";
    public static final String ViewLeaveRequest = "https://buildnlive.com/app/mobileapp/index.php?r=site/ViewLeaveRequest&user_id=[0]";
    public static final String SaveLeaveRequest = "https://buildnlive.com/app/mobileapp/index.php?r=site/SaveLeaveRequest";
    public static final String cancelLeaveRequest = "https://buildnlive.com/app/mobileapp/index.php?r=site/cancelLeaveRequest&leave_id=[0]";
    public static final String MarkOutDuty = "https://buildnlive.com/app/mobileapp/index.php?r=site/MarkOutDuty";
    public static final String DeleteLabourAttendance = "https://buildnlive.com/app/mobileapp/index.php?r=site/DeleteLabourAttendance&daily_attendence_id=[0]&user_id=[1]";
    public static final String UpdateJobSheet = "https://buildnlive.com/app/mobileapp/index.php?r=site/UpdateJobSheet";

}
