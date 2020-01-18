package buildnlive.com.buildlive.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.Server.Response.ApprovalDataResponseData
import buildnlive.com.buildlive.utils.ExpandableTextView
import java.util.*

class LabourStatusAdapter(private val context: Context,
                          users: ArrayList<ApprovalDataResponseData>,
                          private val listener: OnItemClickListener,
                          private val type: String) : RecyclerView.Adapter<LabourStatusAdapter.ViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(project: ApprovalDataResponseData, pos: Int)
        fun onItemUnchecked(project: ApprovalDataResponseData, pos: Int)
        fun onEditClicked(type: String)
    }

    private val items: List<ApprovalDataResponseData>

    init {
        this.items = users
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_approve, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, items[position], position, listener, type)
    }


    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var name: TextView = itemView.findViewById(R.id.name)
        internal var description: ExpandableTextView = itemView.findViewById(R.id.description)
        internal var container: CardView = itemView.findViewById(R.id.container)
        internal var approve: RadioButton = itemView.findViewById(R.id.approve)
        internal var disapprove: RadioButton = itemView.findViewById(R.id.disapprove)
        internal var check: CheckBox = itemView.findViewById(R.id.check)
        internal var edit: ImageView = itemView.findViewById(R.id.edit)

        fun bind(context: Context, item: ApprovalDataResponseData, pos: Int, listener: OnItemClickListener, type: String) {
            name.text = item.title

            description.text = item.description

            var status: String

            if (type == "staff" || type == "dept_labour") {
                edit.visibility = View.GONE
            }
            else
            {
                edit.visibility = View.VISIBLE
                edit.setOnClickListener {
                    listener.onEditClicked(item.id)
                }
            }

            check.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if ((!(approve.isChecked) && (!disapprove.isChecked))) {
                        Toast.makeText(context, "Choose approve/disapprove", Toast.LENGTH_SHORT).show()
                        check.isChecked = false

                    } else {
                        item.status = if (approve.isChecked) {
                            "approve"
                        } else {
                            "disapprove"
                        }


                        listener.onItemClick(item, pos)
                    }
                } else {
                    listener.onItemUnchecked(item, pos)
                }
            }

        }
    }

}