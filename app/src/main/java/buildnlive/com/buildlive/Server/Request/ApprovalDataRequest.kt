package buildnlive.com.buildlive.Server.Request

data class ApprovalDataRequest(
        var userId:String,
        var projectId:String,
        var type:String
)