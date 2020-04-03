package buildnlive.com.buildlive.Server


import buildnlive.com.buildlive.Server.Request.ApprovalDataRequest
import buildnlive.com.buildlive.Server.Request.ApprovalRequest
import buildnlive.com.buildlive.Server.Request.EditLabourReportRequest
import buildnlive.com.buildlive.Server.Request.SendStoreRequest
import buildnlive.com.buildlive.Server.Response.ApprovalDataResponse
import buildnlive.com.buildlive.Server.Response.ApprovalResponse
import buildnlive.com.buildlive.Server.Response.EditLabourReportResponse
import buildnlive.com.buildlive.Server.Response.GetStoreResponse
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

}