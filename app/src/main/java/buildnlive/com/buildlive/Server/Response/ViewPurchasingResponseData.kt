package buildnlive.com.buildlive.Server.Response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ViewPurchasingResponseData(
        @SerializedName("purchase_id")
        val purchaseId: String,
        @SerializedName("item_name")
        val itemName: String,
        @SerializedName("date")
        val date: String,
        @SerializedName("qty")
        val qty: String,
        @SerializedName("user")
        val user: String,
        @SerializedName("button")
        val button: String,
        @SerializedName("vendor_name")
        val vendorName: String,
        @SerializedName("rate")
        val rate: String,
        @SerializedName("tax")
        val tax: String
) : Parcelable
