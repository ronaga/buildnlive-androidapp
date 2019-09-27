package buildnlive.com.buildlive.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import buildnlive.com.buildlive.App
import buildnlive.com.buildlive.Interfaces
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.adapters.TakeLeaveAdapter
import buildnlive.com.buildlive.console
import buildnlive.com.buildlive.elements.LeaveHistoryItem
import buildnlive.com.buildlive.utils.Config
import buildnlive.com.buildlive.utils.UtilityofActivity
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.android.volley.Request
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_take_leave.*
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class TakeLeave : Fragment() {

    var attendenceId: String? = null
    private var pref: SharedPreferences? = null
    private val generator = ColorGenerator.MATERIAL
    private var mContext: Context? = null
    private var appCompatActivity: AppCompatActivity? = null
    var utilityofActivity: UtilityofActivity? = null
    private var mYear: Int? = 0
    private var mMonth: Int? = 0
    private var mDay: Int? = 0
    private var mHour: Int? = 0
    private var mMinute: Int? = 0
    private var pastList: ArrayList<LeaveHistoryItem>? = ArrayList()
    private var leaveAdapter: TakeLeaveAdapter? = null

    private val listener = TakeLeaveAdapter.OnItemClickListener { item, pos, view ->
        showLeaves(item)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        this.appCompatActivity = activity as AppCompatActivity?
    }


    override fun onStart() {
        super.onStart()
        getPastHistory()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_take_leave, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        utilityofActivity = UtilityofActivity(appCompatActivity!!)

        reasonForLeave.movementMethod = ScrollingMovementMethod()

        startDate.setOnClickListener(View.OnClickListener {
            // Get Current Date
            val c = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog = DatePickerDialog(context!!,
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        startDate.text = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                    }, mYear!!, mMonth!!, mDay!!)
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        })
        endDate.setOnClickListener(View.OnClickListener {
            // Get Current Date
            val c = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog = DatePickerDialog(context!!,
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        endDate.text = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                    }, mYear!!, mMonth!!, mDay!!)
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        })

        items.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(items.context, LinearLayoutManager.VERTICAL)
        items.addItemDecoration(dividerItemDecoration)

        leaveAdapter = TakeLeaveAdapter(context, pastList!!, listener)
        items.adapter = leaveAdapter

        requestLeave.setOnClickListener {

            val builder: AlertDialog.Builder? = AlertDialog.Builder(context!!)
            builder!!.setTitle("Request Leaves")

            builder.setMessage("Do you want to Submit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                        if ((startDate.text.toString() != "Start Date") && (endDate.text.toString() != "End Date")) {
                            submit(startDate.text.toString(), endDate.text.toString(), reasonForLeave.text.toString())
                        } else {
                            utilityofActivity!!.toast("Select Start Date and End Date")
                        }
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })

            val alert = builder.create()
            alert.show()


        }


    }


    private fun submit(startDate: String, endDate: String, comment: String) {
        val requestUrl = Config.SaveLeaveRequest

        val params = HashMap<String, String>()
        params["user_id"] = App.userId
        params["start_date"] = startDate
        params["end_date"] = endDate
        params["comments"] = comment


        console.log("submit: $params")
        app!!.sendNetworkRequest(requestUrl, Request.Method.POST, params, object : Interfaces.NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                utilityofActivity!!.showProgressDialog()
            }

            override fun onNetworkRequestError(error: String) {
                utilityofActivity!!.dismissProgressDialog()
                console.error("Network request failed with error :$error")
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
                console.log(response)
                utilityofActivity!!.dismissProgressDialog()
                try {

                    if (response == "1") {
                        getPastHistory()
                        Toast.makeText(context!!, "Request Generated", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context!!, "Request Failed, Please Try Again Later", Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }


    private fun getPastHistory() {
        var requestUrl = Config.ViewLeaveRequest
        requestUrl = requestUrl.replace("[0]", App.userId)

        console.log("Past History: " + requestUrl)
        app!!.sendNetworkRequest(requestUrl, Request.Method.GET, null, object : Interfaces.NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                utilityofActivity!!.showProgressDialog()
            }

            override fun onNetworkRequestError(error: String) {

                utilityofActivity!!.dismissProgressDialog()
                console.error("Network request failed with error :$error")
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
                console.log(response)

                utilityofActivity!!.dismissProgressDialog()
                try {
                    pastList!!.clear()

                    val vendorType = object : TypeToken<ArrayList<LeaveHistoryItem>>() {

                    }.type
                    pastList = Gson().fromJson<ArrayList<LeaveHistoryItem>>(response, vendorType)

                    leaveAdapter = TakeLeaveAdapter(context, pastList!!, listener)
                    items.adapter = leaveAdapter

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }

    private fun showLeaves(worker: LeaveHistoryItem) {
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.alert_cancel_leave, null)
        val dialogBuilder = AlertDialog.Builder(activity!!, R.style.PinDialog)
        val alertDialog = dialogBuilder.setCancelable(true).setView(dialogView).create()
        alertDialog.show()

        val title = dialogView.findViewById<TextView>(R.id.alert_title)
        title.text = "Leaves"
        val message = dialogView.findViewById<TextView>(R.id.alert_message)
        message.text = worker.comments

        val positive = dialogView.findViewById<Button>(R.id.positive)
        positive.setOnClickListener {
            alertDialog.dismiss()
            cancelLeave(worker.leaveId,alertDialog)
        }
        val negative = dialogView.findViewById<Button>(R.id.negative)
        negative.setOnClickListener {
            alertDialog.dismiss()
        }

        if (worker.status == "Pending") {
            positive.visibility = View.VISIBLE
        } else {
            positive.visibility = View.GONE
        }

    }


    private fun cancelLeave(id:String,dialog: AlertDialog) {

        var requestUrl = Config.cancelLeaveRequest
        requestUrl = requestUrl.replace("[0]", id)

        console.log("Past History: " + requestUrl)
        app!!.sendNetworkRequest(requestUrl, Request.Method.GET, null, object : Interfaces.NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                utilityofActivity!!.showProgressDialog()
            }

            override fun onNetworkRequestError(error: String) {

                utilityofActivity!!.dismissProgressDialog()
                console.error("Network request failed with error :$error")
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
                console.log(response)

                utilityofActivity!!.dismissProgressDialog()
                try {
                    if(response=="1")
                    {
                        dialog.dismiss()
                        utilityofActivity!!.toast("Cancellation successful")
                        getPastHistory()
                    }
                    else
                    {
                        utilityofActivity!!.toast("Something went wrong please try again later")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }


    companion object {
        private var app: App? = null
        fun newInstance(a: App): TakeLeave {
            app = a
            return TakeLeave()
        }
    }
}
