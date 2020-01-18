package com.webpulse.webpulseclients.Server


import buildnlive.com.buildlive.Server.Request.ApprovalDataRequest
import buildnlive.com.buildlive.Server.Request.ApprovalRequest
import buildnlive.com.buildlive.Server.Response.ApprovalDataResponse
import buildnlive.com.buildlive.Server.Response.ApprovalResponse
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

}