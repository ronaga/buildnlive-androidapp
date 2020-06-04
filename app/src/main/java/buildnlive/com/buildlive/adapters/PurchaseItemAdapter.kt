package buildnlive.com.buildlive.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.Server.Response.ViewPurchasingResponseData
import java.util.*

class PurchaseItemAdapter(private val context: Context, private val modelFeedArrayList: ArrayList<ViewPurchasingResponseData>, private val listener: OnItemInteractListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemInteractListener {
        fun onItemClick(accountDetailId: String?)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return modelFeedArrayList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_view_purchase_item, parent, false)
        return PaymentHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        val item = modelFeedArrayList[i]
        val holder = viewHolder as PaymentHolder

        holder.quantity.text = String.format(context.getString(R.string.quantityLabel), item.qty)
        holder.name.text = String.format(context.getString(R.string.nameLabel), item.itemName)
        holder.vendorName.text = String.format(context.getString(R.string.vendorLabel), item.vendorName)
        holder.rate.text = String.format(context.getString(R.string.rateLabel), item.rate)
        holder.tax.text = String.format(context.getString(R.string.taxLabel), item.tax)
        holder.date.text = String.format(context.getString(R.string.dateLabel), item.date)

//        holder.delete.setOnClickListener { listener.onItemClick(item.purchaseId) }
    }

    class PaymentHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name)
        var vendorName: TextView = itemView.findViewById(R.id.vendorName)
        var rate: TextView = itemView.findViewById(R.id.rate)
        var quantity: TextView = itemView.findViewById(R.id.quantity)
        var date: TextView = itemView.findViewById(R.id.date)
        var tax: TextView = itemView.findViewById(R.id.tax)
//        var delete: ImageView = itemView.findViewById(R.id.delete)

    }

}