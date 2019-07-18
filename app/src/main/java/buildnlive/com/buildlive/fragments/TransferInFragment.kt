package buildnlive.com.buildlive.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.android.volley.Request

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.HashMap

import buildnlive.com.buildlive.App
import buildnlive.com.buildlive.Interfaces
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.adapters.TransferInAdapter
import buildnlive.com.buildlive.console
import buildnlive.com.buildlive.elements.TransferMachine
import buildnlive.com.buildlive.utils.Config
import buildnlive.com.buildlive.utils.Helper
import buildnlive.com.buildlive.utils.UtilityofActivity

class TransferInFragment : Fragment() {
    private var items: RecyclerView? = null

    private var submit: Button? = null
    private var progress: ProgressBar? = null
    private var hider: TextView? = null
    private var no_content: TextView? = null

    private var adapter: TransferInAdapter? = null
    private var searchView: SearchView? = null
    internal lateinit var builder: AlertDialog.Builder

    var utilityofActivity: UtilityofActivity? = null
    var appCompatActivity: AppCompatActivity? = null
    var mContext: Context? = null
    var helper: Helper? = null
    var statusText: String? = null



    val listener: TransferInAdapter.OnItemClickedListener= object : TransferInAdapter.OnItemClickedListener{
        override fun onButtonClicked(transferMachine: TransferMachine?, pos: Int) {

            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_receive_transfer, null)
            val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context!!, R.style.PinDialog)
            val alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create()
            alertDialog.show()

            val status = dialogView.findViewById<Spinner>(R.id.status)
            val commentText = dialogView.findViewById<EditText>(R.id.comment)

            status.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    statusText=parent!!.selectedItem.toString()
                }
            }



            val positive = dialogView.findViewById<Button>(R.id.positive)
            positive.setOnClickListener {
                if(!statusText.equals("Select Status")){
                    sendRequest(transferMachine,commentText.text.toString(),statusText!!)
                    alertDialog.dismiss()
                }
                else
                    Toast.makeText(context!!,"Select Status",Toast.LENGTH_LONG).show();
            }

            val negative = dialogView.findViewById<Button>(R.id.negative)
            negative.setOnClickListener { alertDialog.dismiss() }

        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        this.appCompatActivity = activity as AppCompatActivity?
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_transfer_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        utilityofActivity= UtilityofActivity(appCompatActivity!!)

        builder = AlertDialog.Builder(mContext)

        items = view.findViewById(R.id.items)
        submit = view.findViewById(R.id.submit)
        no_content = view.findViewById(R.id.no_content)

        progress = view.findViewById(R.id.progress)
        hider = view.findViewById(R.id.hider)

        adapter = TransferInAdapter(mContext, itemsList, listener)
        items!!.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        items!!.adapter = adapter

        refresh()
    }

    @Throws(JSONException::class)
    private fun sendRequest(items: TransferMachine?,comment:String,status:String) {
        val app = activity!!.application as App
        val params = HashMap<String, String>()
        params["user_id"] = App.userId
        params["transfer_id"] = items!!.transfer_id
        params["comment"] = comment
        params["status"] = status

        console.log("Res:$params")
        app.sendNetworkRequest(Config.RECEIVE_TRANSFER, 1, params, object : Interfaces.NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                progress!!.visibility = View.VISIBLE
                hider!!.visibility = View.VISIBLE
                utilityofActivity!!.showProgressDialog()
            }

            override fun onNetworkRequestError(error: String) {
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                utilityofActivity!!.dismissProgressDialog()
                Toast.makeText(mContext, "Error:$error", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                utilityofActivity!!.dismissProgressDialog()
                if (response == "1") {
                    Toast.makeText(mContext, "Request Generated", Toast.LENGTH_SHORT).show()
                    activity!!.finish()
                }
            }
        })
    }

    private fun refresh() {
        val app = activity!!.application as App
        itemsList.clear()
        var requestUrl = Config.TRANSFER_IN
        requestUrl = requestUrl.replace("[0]", App.userId)
        requestUrl = requestUrl.replace("[1]", App.projectId)
        console.log(requestUrl)

        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, object : Interfaces.NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                progress!!.visibility = View.VISIBLE
                hider!!.visibility = View.VISIBLE
//                utilityofActivity!!.showProgressDialog()
            }

            override fun onNetworkRequestError(error: String) {
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
//                utilityofActivity!!.dismissProgressDialog()
                console.error("Network request failed with error :$error")
                Toast.makeText(mContext, "Check Network, Something went wrong", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
                //                console.log(response);
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
//                utilityofActivity!!.dismissProgressDialog()
                try {
                    val array = JSONArray(response)
                    for (i in 0 until array.length()) {
                        itemsList.add(TransferMachine().parseFromJSON(array.getJSONObject(i)))
                    }
                    if(itemsList.isEmpty())
                    {
                        no_content!!.visibility= View.VISIBLE
                    }
                    else
                    {
                        no_content!!.visibility= View.GONE
                    }
                    console.log("data set changed")
                    adapter = TransferInAdapter(mContext, itemsList, listener)
                    items!!.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
                    items!!.adapter = adapter
                    adapter!!.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }


    companion object {
        val itemsList = ArrayList<TransferMachine>()
    }

}