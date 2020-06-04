package buildnlive.com.buildlive.Server


import buildnlive.com.buildlive.Server.Request.*
import buildnlive.com.buildlive.Server.Response.*
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface TCApi {
//  <--------------------------------------------RXJAVA------------------------------------------------>

    @POST("index.php?r=Approvals/SendApprovals")
    fun callApprovalsList(@Body approvalRequest: ApprovalRequest): Single<Response<ApprovalResponse>>

    @POST("index.php?r=Approvals/SendApprovalsData")
    fun callApprovalsData(@Body approvalDataRequest: ApprovalDataRequest): Single<Response<ApprovalDataResponse>>

    @POST("index.php?r=Approvals/SendStoreRequestDetail")
    fun callEditStoreDetails(@Body approvalDataRequest: SendStoreRequest): Single<Response<GetStoreResponse>>

    @POST("index.php?r=Approvals/SendSubLabourReportDetail")
    fun callEditLabourReport(@Body approvalDataRequest: EditLabourReportRequest): Single<Response<EditLabourReportResponse>>

    @POST("index.php?r=Approvals/SendSitePaymentDetails")
    fun callEditPaymentDetails(@Body approvalDataRequest: EditPaymentRequest): Single<Response<EditPaymentResponse>>
/*
    @POST("index.php?r=Reports/UpdateSitePaymentDetail")
    fun callUpdatePayments(@Body projectIdRequest: ProjectIdRequest): Single<Response<ViewPaymentResponse>>*/

    @POST("index.php?r=Site/SendPayments")
    fun callSendPayments(@Body projectIdRequest: ProjectIdRequest): Single<Response<ViewPaymentResponse>>

    @POST("index.php?r=Site/SendPurchasing")
    fun callSendPurchasing(@Body projectIdRequest: ProjectIdRequest): Single<Response<ViewPurchasingResponse>>

   @POST("index.php?r=Site/DeletePayments")
    fun callSendDeletePayments(@Body deletePaymentRequest: DeletePaymentRequest): Single<Response<DefaultResponse>>

   @POST("index.php?r=Site/DeletePurchasing")
    fun callDeletePurchasing(@Body deletePurchaseRequest: DeletePurchaseRequest): Single<Response<DefaultResponse>>

@POST("index.php?r=Attendance/SendAttendanceUser")
    fun callSendAttendance(@Body attendanceRequest: AttendanceRequest): Single<Response<AttendanceResponse>>

}