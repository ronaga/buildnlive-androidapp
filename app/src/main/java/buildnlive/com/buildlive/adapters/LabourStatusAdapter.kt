package buildnlive.com.buildlive.adapters


import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.elements.Approval.ApproveLabourItem
import java.util.*

class LabourStatusAdapter
(private val context: Context, users: ArrayList<ApproveLabourItem>,private val listener: OnItemClickListener) : RecyclerView.Adapter<LabourStatusAdapter.ViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(project: ApproveLabourItem, pos: Int)
        fun onItemUnchecked(project: ApproveLabourItem, pos: Int)
    }

    private val items: List<ApproveLabourItem>

    init {
        this.items = users
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabourStatusAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_approve, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, items[position], position,listener)
    }


    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var name: TextView
        internal var inTime: TextView
        internal var outTime: TextView
        internal var overtime: TextView
        internal var container: LinearLayout
        internal var approve: RadioButton
        internal var disapprove: RadioButton
        internal var check: CheckBox

        init {
            name = itemView.findViewById(R.id.name)
            container = itemView.findViewById(R.id.container)
            inTime = itemView.findViewById(R.id.inTime)
            outTime = itemView.findViewById(R.id.outTime)
            overtime = itemView.findViewById(R.id.overtime)
            approve = itemView.findViewById(R.id.approve)
            disapprove = itemView.findViewById(R.id.disapprove)
            check = itemView.findViewById(R.id.check)
        }

        fun bind(context: Context, item: ApproveLabourItem, pos: Int, listener: OnItemClickListener) {
            name.text = item.labourName
            inTime.text = "In Time: "+ item.inTime
            outTime.text ="Out Time: "+ item.outTime
            overtime.text = "Overtime: "+item.overtime

            var status:String

            check.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    if ((!(approve.isChecked) && (!disapprove.isChecked))) {
                        Toast.makeText(context, "Choose approve/disapprove", Toast.LENGTH_SHORT).show()
                        check.isChecked = false

                    } else {
                        item.status= if(approve.isChecked) {
                            "approve"
                        } else {
                            "disapprove"
                        }


                        listener.onItemClick(item,pos)
                    }
                }
                else
                {
                    listener.onItemUnchecked(item,pos)
                }
            }

        }
    }

}