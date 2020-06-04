package buildnlive.com.buildlive.Server.Response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ViewPurchasingResponse(
        @SerializedName("success")
        var success: Boolean,
        @SerializedName("message")
        val message: String,
        @SerializedName("data")
        var data: ArrayList<ViewPurchasingResponseData>
) : Parcelable
