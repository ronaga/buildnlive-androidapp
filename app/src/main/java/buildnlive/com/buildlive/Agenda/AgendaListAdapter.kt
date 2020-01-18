/*
package buildnlive.com.buildlive.Agenda

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.elements.AgendaItem
import buildnlive.com.buildlive.utils.GlideApp
import java.util.*


class AgendaListAdapter(
        private val context: Context,
        private val modelFeedArrayList: ArrayList<AgendaItem>
//        private val longClickedListener: OnLongClickedInterface,
//        private val clickListener: OnItemClickedInterface
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    interface OnItemClickedInterface {
        fun onClicked()
    }

    interface OnLongClickedInterface {
        fun onLongAdd(id: String)
        fun onLongRemove(id: String)
    }

    companion object {
        var listener: OnItemClickedInterface? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_agenda_item, parent, false)

        return MyViewHolder(view)

    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {

        val holder = viewHolder as MyViewHolder

        val item = modelFeedArrayList[viewHolder.getAdapterPosition()]
//        GlideApp.with(context).load(item.image).centerCrop().into(holder.cover)
        holder.topic.text = item.topic


        if (item.status == "active") {
            GlideApp.with(context).load(R.drawable.active_circle).centerCrop().into(holder.statusIndicator)
        } else {
            GlideApp.with(context).load(R.drawable.inactive_circle).centerCrop().into(holder.statusIndicator)
        }

        holder.priority.text = item.priority
        holder.date.text = String.format(context.getString(R.string.dateHolder), item.date)

//        holder.edit.setOnClickListener {
//            clickListener.onClicked()
//        }

        holder.viewDescription.setOnClickListener {

        }


   */
/*     holder.navItem.setOnLongClickListener {

            if (!item.isSelectedStatus) {
                item.isSelectedStatus = true
                holder.selected.visibility = View.VISIBLE
                longClickedListener.onLongAdd(item.productid)
            } else {
                item.isSelectedStatus = false
                holder.selected.visibility = View.GONE
                longClickedListener.onLongRemove(item.productid)
            }

            return@setOnLongClickListener true
        }*//*



    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return modelFeedArrayList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    inner class MyViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var topic: TextView = itemView.findViewById(R.id.topic)
        internal var cover: ImageView = itemView.findViewById(R.id.cover)
        internal var edit: ImageView = itemView.findViewById(R.id.edit)
        internal var statusIndicator: ImageView = itemView.findViewById(R.id.statusIndicator)
        internal var priority: TextView = itemView.findViewById(R.id.priority)
        internal var date: TextView = itemView.findViewById(R.id.date)
        internal var viewDescription: TextView = itemView.findViewById(R.id.viewDetails)
        internal var navItem: CardView = itemView.findViewById(R.id.navItem)
        internal var selected: TextView = itemView.findViewById(R.id.selected)


    }

}
*/
