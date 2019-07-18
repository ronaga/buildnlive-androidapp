package buildnlive.com.buildlive.adapters


import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.elements.Approval.ApproveIssueItem
import buildnlive.com.buildlive.elements.Approval.ApproveLabourReportItem
import java.util.*

class LabourReportStatusAdapter
(private val context: Context, users: ArrayList<ApproveLabourReportItem>,private val listener: OnItemClickListener) : RecyclerView.Adapter<LabourReportStatusAdapter.ViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(project: ApproveLabourReportItem, pos: Int)
        fun onItemUnchecked(project: ApproveLabourReportItem, pos: Int)
    }

    private val items: List<ApproveLabourReportItem>

    init {
        this.items = users
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabourReportStatusAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_approve_labourreport_item, parent, false)
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
        internal var container: LinearLayout
        internal var approve: RadioButton
        internal var disapprove: RadioButton
        internal var check: CheckBox

        init {
            name = itemView.findViewById(R.id.name)
            container = itemView.findViewById(R.id.container)
            inTime = itemView.findViewById(R.id.inTime)
            approve = itemView.findViewById(R.id.approve)
            disapprove = itemView.findViewById(R.id.disapprove)
            check = itemView.findViewById(R.id.check)
        }

        fun bind(context: Context, item: ApproveLabourReportItem, pos: Int, listener: OnItemClickListener) {
            name.text = item.contractorName
            inTime.text = "Total Labour: "+item.totalLabour


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