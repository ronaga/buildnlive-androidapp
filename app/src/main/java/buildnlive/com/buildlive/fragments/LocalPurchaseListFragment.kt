package buildnlive.com.buildlive.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import buildnlive.com.buildlive.App
import buildnlive.com.buildlive.Interfaces.NetworkInterfaceListener
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.activities.LocalMultiPurchaseForm
import buildnlive.com.buildlive.activities.LocalPurchaseForm
import buildnlive.com.buildlive.adapters.LocalPurchaseListAdapter
import buildnlive.com.buildlive.console
import buildnlive.com.buildlive.elements.Item
import buildnlive.com.buildlive.utils.Config
import buildnlive.com.buildlive.utils.UtilityofActivity
import com.android.volley.Request
import kotlinx.android.synthetic.main.fragment_issue_item_list.*
import kotlinx.android.synthetic.main.layout_local_purchase_dialog.view.*
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class LocalPurchaseListFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var inventory: ArrayList<Item>? = null
    private val finalList: ArrayList<Item> = ArrayList<Item>()
    private var progress: ProgressBar? = null
    private var hider: TextView? = null
    private var localPurchaseListAdapter: LocalPurchaseListAdapter? = null
    private var search_view: LinearLayout? = null
    private var search_text: EditText? = null
    private var search_close: ImageButton? = null
    private var search: ImageButton? = null
    private var mContext: Context? = null
    private var utilityofActivity: UtilityofActivity? = null
    private var appCompatActivity: AppCompatActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        appCompatActivity = activity as AppCompatActivity
    }

    var listener: LocalPurchaseListAdapter.OnItemClickListener = object : LocalPurchaseListAdapter.OnItemClickListener {
        override fun onItemClick(packet: Item, pos: Int, view: View) {
            /*val intent = Intent(activity, LocalPurchaseForm::class.java)
            val bundle = Bundle()
            bundle.putParcelable("Items", packet)
            intent.putExtras(bundle)
            startActivity(intent)*/
        }

        override fun onItemCheck(checked: Boolean, item: Item, view: View) {
            if (checked) {

                val inflater = layoutInflater
                val dialogView = inflater.inflate(R.layout.layout_local_purchase_dialog, null)
                val dialogBuilder = AlertDialog.Builder(context!!, R.style.PinDialog)
                val alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create()
                alertDialog.show()

                dialogView.alert_title.text = item.name
                dialogView.unit.text = item.unit

                val submit = dialogView.findViewById<TextView>(R.id.positive)
                val close = dialogView.findViewById<TextView>(R.id.negative)

                submit.setOnClickListener {
                    item.quantity = dialogView.quant.text.toString()
                    item.rate = dialogView.rate.text.toString()
                    when(dialogView.tax.selectedItem.toString())
                    {
                        "0%"->{
                            item.tax = "0"
                        }
                        "5%"->{
                            item.tax = "0.05"
                        }
                        "12%"->{
                            item.tax = "0.12"
                        }
                        "18%"->{
                            item.tax = "0.18"
                        }
                        "28%"->{
                            item.tax = "0.28"
                        }
                    }

                    view.setBackgroundColor(ContextCompat.getColor(context!!,R.color.razorgreen))
                    alertDialog.dismiss()
                }

                close.setOnClickListener {
                    alertDialog.dismiss()
                }

                item.isUpdated = true
                finalList.add(item)
            } else {
                item.isUpdated = false
                view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.transparent))
                finalList.remove(item)
            }
            for (i in finalList)
                console.log(i.name)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_issue_item_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        utilityofActivity = UtilityofActivity(appCompatActivity!!)
        recyclerView = view.findViewById(R.id.items)
        progress = view.findViewById(R.id.progress)
        hider = view.findViewById(R.id.hider)
        search_view = view.findViewById(R.id.search_view)
        search_text = view.findViewById(R.id.search_text)
        search_close = view.findViewById(R.id.search_close)
        search = view.findViewById(R.id.search)
        search!!.setOnClickListener(View.OnClickListener { v: View? ->
            search_text!!.visibility = View.VISIBLE
            search_close!!.visibility = View.VISIBLE
            search_text!!.requestFocus()
            if (search_text!!.hasFocus()) {
                val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                //                imm.hideSoftInputFromWindow(search_text.windowToken,InputMethodManager.SHOW_IMPLICIT )
                imm.showSoftInput(search_text, InputMethodManager.SHOW_IMPLICIT)
            }
        })
        search_text!!.setOnEditorActionListener(OnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(search_text!!.getText().toString())
                val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(search_text!!.getWindowToken(), 0)
                return@OnEditorActionListener true
            } else {
                return@OnEditorActionListener false
            }
        })
        search_close!!.setOnClickListener(View.OnClickListener { v: View? ->
            search_text!!.setText("")
            search_text!!.visibility = View.INVISIBLE
            search_close!!.visibility = View.INVISIBLE
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            refresh()
        })
        inventory = ArrayList()
        localPurchaseListAdapter = LocalPurchaseListAdapter(mContext, inventory, listener)
        val linearLayoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL)
        recyclerView!!.adapter = localPurchaseListAdapter
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.addItemDecoration(dividerItemDecoration)
        refresh()

        submit.setOnClickListener {
            if(finalList.size>0) {
                val intent = Intent(activity, LocalMultiPurchaseForm::class.java)
                val bundle = Bundle()
                bundle.putParcelableArrayList("Items", finalList)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            else
                utilityofActivity!!.toast("Select atleast one item.")
        }
    }

    private fun refresh() {
        inventory!!.clear()
        var requestUrl = Config.REQ_GET_ITEM_INVENTORY
        requestUrl = requestUrl.replace("[0]", App.userId)
        requestUrl = requestUrl.replace("[1]", App.projectId)
        app!!.sendNetworkRequest(requestUrl, Request.Method.GET, null, object : NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                progress!!.visibility = View.VISIBLE
                hider!!.visibility = View.VISIBLE
                utilityofActivity!!.showProgressDialog()
            }

            override fun onNetworkRequestError(error: String) {
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                utilityofActivity!!.dismissProgressDialog()
                console.error("Network request failed with error :$error")
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
                console.log(response)
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                utilityofActivity!!.dismissProgressDialog()
                try {
                    val array = JSONArray(response)
                    for (i in 0 until array.length()) {
                        inventory!!.add(Item().parseFromJSON(array.getJSONObject(i)))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                //
//                for (Item i : inventory) {
//                    items.add(i.getName());
//                    console.log("Name:"+i.getName());
//                }
                localPurchaseListAdapter!!.notifyDataSetChanged()
            }
        })
    }

    private fun search(keyword: String) {
        inventory!!.clear()
        var requestUrl = Config.INVENTORY_SEARCH
        requestUrl = requestUrl.replace("[0]", App.userId)
        requestUrl = requestUrl.replace("[1]", App.projectId)
        requestUrl = requestUrl.replace("[2]", keyword)
        console.log(requestUrl)
        app!!.sendNetworkRequest(requestUrl, Request.Method.GET, null, object : NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                progress!!.visibility = View.VISIBLE
                hider!!.visibility = View.VISIBLE
                utilityofActivity!!.showProgressDialog()
            }

            override fun onNetworkRequestError(error: String) {
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                utilityofActivity!!.dismissProgressDialog()
                console.error("Network request failed with error :$error")
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
//                console.log(response);
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                utilityofActivity!!.dismissProgressDialog()
                try {
                    val array = JSONArray(response)
                    for (i in 0 until array.length()) {
                        inventory!!.add(Item().parseFromJSON(array.getJSONObject(i)))
                    }
                    console.log("data set changed")
                    localPurchaseListAdapter!!.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }

    companion object {
        private var app: App? = null

        @JvmStatic
        fun newInstance(a: App?): LocalPurchaseListFragment {
            app = a
            return LocalPurchaseListFragment()
        }
    }
}