package buildnlive.com.buildlive.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
import buildnlive.com.buildlive.activities.CreateTransfer
import buildnlive.com.buildlive.adapters.TransferOutAdapter
import buildnlive.com.buildlive.console
import buildnlive.com.buildlive.elements.Item
import buildnlive.com.buildlive.elements.TransferMachine
import buildnlive.com.buildlive.utils.Config
import buildnlive.com.buildlive.utils.Helper
import buildnlive.com.buildlive.utils.UtilityofActivity
import kotlinx.android.synthetic.main.fragment_transfer_out.*

class TransferOutFragment : Fragment() {
    private var items: RecyclerView? = null

    private var submit: Button? = null
    private var progress: ProgressBar? = null
    private var hider: TextView? = null
    private var no_content: TextView? = null

    private var adapter: TransferOutAdapter? = null
    private var searchView: SearchView? = null
    internal lateinit var builder: AlertDialog.Builder

    var utilityofActivity: UtilityofActivity? = null
    var appCompatActivity: AppCompatActivity? = null
    var mContext: Context? = null
    var helper: Helper? = null

    val listener: TransferOutAdapter.OnItemClickedListener= object : TransferOutAdapter.OnItemClickedListener{
        override fun onButtonClicked(transferMachine: TransferMachine?, pos: Int) {


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
        return inflater.inflate(R.layout.fragment_transfer_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        utilityofActivity= UtilityofActivity(appCompatActivity!!)

        builder = AlertDialog.Builder(context)
        no_content = view.findViewById(R.id.no_content)


        items = view.findViewById(R.id.items)
        submit = view.findViewById(R.id.submit)

        progress = view.findViewById(R.id.progress)
        hider = view.findViewById(R.id.hider)

        adapter = TransferOutAdapter(context, itemsList, listener)
        items!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        items!!.adapter = adapter


        fab.setOnClickListener{
            val intent=Intent(appCompatActivity!!, CreateTransfer::class.java)
            startActivity(intent)
        }

        refresh()
    }

    private fun refresh() {
        val app = activity!!.application as App
        itemsList.clear()
        var requestUrl = Config.TRANSFER_OUT
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
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show()
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
                    adapter = TransferOutAdapter(context, itemsList, listener)
                    items!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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