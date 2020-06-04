package buildnlive.com.buildlive.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.Server.Response.ViewPaymentResponseData
import buildnlive.com.buildlive.utils.GlideApp
import java.util.*

class PaymentItemAdapter(private val context: Context, private val modelFeedArrayList: ArrayList<ViewPaymentResponseData>, private val listener: OnItemInteractListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_view_payment_item, parent, false)
        return PaymentHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        val item = modelFeedArrayList[i]
        val holder = viewHolder as PaymentHolder
        when (item.status) {
            "Paid" -> {
                GlideApp.with(context).load(R.drawable.active_circle).centerCrop().into(holder.statusIndicator)
            }
            "Pending" -> {
                GlideApp.with(context).load(R.drawable.inactive_circle).centerCrop().into(holder.statusIndicator)
            }
            "Approved" -> {
                GlideApp.with(context).load(R.drawable.yellow_circle).centerCrop().into(holder.statusIndicator)
            }
        }


        holder.amount.text = String.format(context.getString(R.string.amount), item.amount)
        holder.name.text = String.format(context.getString(R.string.nameLabel), item.accountName)
        holder.payType.text = String.format(context.getString(R.string.payTypeLabel), item.type)
        holder.payee.text = String.format(context.getString(R.string.payeeLabel), item.payee)
        holder.comment.text = String.format(context.getString(R.string.descriptionLabel), item.desc)
        holder.date.text = String.format(context.getString(R.string.dateLabel), item.date)

        holder.delete.setOnClickListener { listener.onItemClick(item.accountDetailId) }
    }

    class PaymentHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name)
        var payee: TextView = itemView.findViewById(R.id.payee)
        var amount: TextView = itemView.findViewById(R.id.amount)
        var date: TextView = itemView.findViewById(R.id.date)
        var payType: TextView = itemView.findViewById(R.id.payType)
        var comment: TextView = itemView.findViewById(R.id.comment)
        var statusIndicator: ImageView = itemView.findViewById(R.id.statusIndicator)
        var delete: ImageView = itemView.findViewById(R.id.delete)

    }

}