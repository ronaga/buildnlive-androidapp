package buildnlive.com.buildlive.Approvals

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import buildnlive.com.buildlive.App
import buildnlive.com.buildlive.Interfaces.NetworkInterfaceListener
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.Server.HTTPResponseError
import buildnlive.com.buildlive.Server.Request.EditLabourReportRequest
import buildnlive.com.buildlive.Server.Response.DefaultResponse
import buildnlive.com.buildlive.Server.Response.EditLabourReportResponse
import buildnlive.com.buildlive.Server.Response.EditLabourReportResponseData
import buildnlive.com.buildlive.Server.RetrofitApiAuthSingleTon
import buildnlive.com.buildlive.Server.TCApi
import buildnlive.com.buildlive.console
import buildnlive.com.buildlive.fragments.ManageLabourFragment
import buildnlive.com.buildlive.utils.Config
import buildnlive.com.buildlive.utils.UtilityofActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.util.*

class EditSubContractorReport : AppCompatActivity() {
    private var tcApi: TCApi? = null
    private var disposable = CompositeDisposable()

    private var name: TextView? = null
    private var items: RecyclerView? = null
    private var labourAdapter: EditLabourReportAdapter? = null
    private var submit: Button? = null
    private var builder: AlertDialog.Builder? = null

    private var sub_contract_labour_id: String? = null

    private var context: Context? = null
    private var progress: ProgressBar? = null
    private var hider: TextView? = null
    private var no_content: TextView? = null
    private var coordinatorLayout: CoordinatorLayout? = null
    private var utilityofActivity: UtilityofActivity? = null
    private val appCompatActivity: AppCompatActivity = this

    var listener: EditLabourReportAdapter.OnItemClickListener = object : EditLabourReportAdapter.OnItemClickListener {
        override fun onItemCheck(checked: Boolean) {
            if (checked) {
                submit!!.visibility = View.VISIBLE
            } else {
                submit!!.visibility = View.GONE
            }
        }

        override fun onItemInteract(pos: Int, flag: Int) {}
        override fun onItemClick(items: EditLabourReportResponseData?, pos: Int, view: View?) {}
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        data.clear()
        newItems.clear()
        disposable.dispose()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_labour)
        context = this

        sub_contract_labour_id = intent.getStringExtra("sub_contract_labour_id")

        tcApi = RetrofitApiAuthSingleTon.createService(TCApi::class.java)

        utilityofActivity = UtilityofActivity(this)
        progress = findViewById(R.id.progress)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        hider = findViewById(R.id.hider)
        name = findViewById(R.id.name)
        builder = AlertDialog.Builder(this)
        utilityofActivity = UtilityofActivity(appCompatActivity)
        utilityofActivity!!.configureToolbar(appCompatActivity)
        val toolbar_title = findViewById<TextView>(R.id.toolbar_title)
        val toolbar_subtitle = findViewById<TextView>(R.id.toolbar_subtitle)
        toolbar_subtitle.text = App.projectName
        toolbar_title.text = "Request Labour"
        no_content = findViewById(R.id.no_content)
        items = findViewById(R.id.item)
        submit = findViewById(R.id.submit)

        submit!!.text = "Update & Approve"

        name!!.isEnabled = false
        name!!.visibility = View.GONE


        items!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        items!!.addItemDecoration(dividerItemDecoration)
        labourAdapter = EditLabourReportAdapter(context, data, listener)
        items!!.adapter = labourAdapter


        submit!!.setOnClickListener(View.OnClickListener { view: View? ->
            newItems.clear()
            for (i in data.indices) {
                if (data[i].isUpdated) {
                    newItems.add(data[i])
                }
            }
            builder!!.setMessage("Are you sure?").setTitle("Submit")
            //Setting message manually and performing action on button click
            builder!!.setMessage("Do you want to Submit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog: DialogInterface?, id: Int ->
                        try {
                            sendRequest(newItems)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                    .setNegativeButton("No") { dialog: DialogInterface, id: Int ->
                        //  Action for 'NO' Button
                        dialog.cancel()
                    }
            //Creating dialog box
            val alert = builder!!.create()
            //Setting the title manually
            alert.setTitle("Submit")
            alert.show()
        })

        refresh()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun sendRequest1(items: List<EditLabourReportResponseData>) {

    }

    @Throws(JSONException::class)
    private fun sendRequest(list: List<EditLabourReportResponseData>) {
        val app = application as App
        val params = HashMap<String, String>()
        params["user_id"] = App.userId
        params["detail_id"] = App.projectId

        val array = JSONArray()
        for (i in list) {
            array.put(JSONObject().put("sub_contract_labour_detail_id", i.subContractLabourDetailId).put("count", i.count))
        }
        params["labour_list"] = array.toString()
        console.log("Res:$params")

        app.sendNetworkRequest(Config.UpdateSubLabourReport, 1, params, object : NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                progress!!.visibility = View.VISIBLE
                hider!!.visibility = View.VISIBLE
                utilityofActivity!!.showProgressDialog()
            }

            override fun onNetworkRequestError(error: String) {
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                utilityofActivity!!.dismissProgressDialog()
                //                Toast.makeText(getApplicationContext(),"Error:"+error,Toast.LENGTH_LONG).show();
                val snackbar = Snackbar.make(coordinatorLayout!!, "Something went wrong, Try again later", Snackbar.LENGTH_LONG)
                snackbar.show()
            }

            override fun onNetworkRequestComplete(response: String) {
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                utilityofActivity!!.dismissProgressDialog()
                console.log(response)
                val sresponse = Gson().fromJson(response, DefaultResponse::class.java)
                if (sresponse.success) {
                    utilityofActivity!!.toast(sresponse.message)
                    finish()
                } else {
                    utilityofActivity!!.toast(sresponse.message)
                }
            }
        })
    }

    private fun refresh() {


        disposable.add(tcApi!!.callEditLabourReport(EditLabourReportRequest(App.userId, App.projectId, sub_contract_labour_id!!))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { utilityofActivity!!.showProgressDialog() }
                .doOnError { utilityofActivity!!.dismissProgressDialog() }
                .doOnSuccess { utilityofActivity!!.dismissProgressDialog() }
                .subscribeWith(object : DisposableSingleObserver<Response<EditLabourReportResponse>>() {
                    override fun onSuccess(t: Response<EditLabourReportResponse>) {
                        if (t.isSuccessful) {
                            val response = t.body()
                            if (response!!.isSuccess) {
                                try {
                                    data = response.data

                                    progress!!.visibility = View.GONE
                                    hider!!.visibility = View.GONE

                                    console.log("data set changed")
                                    if (data.isEmpty()) {
                                        no_content!!.visibility = View.VISIBLE
                                        no_content!!.text = "No Labour"
                                    } else {
                                        no_content!!.visibility = View.GONE
                                    }
                                    items!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                                    val dividerItemDecoration = DividerItemDecoration(items!!.context, LinearLayoutManager.VERTICAL)
                                    items!!.addItemDecoration(dividerItemDecoration)

                                    labourAdapter = EditLabourReportAdapter(context, data, listener)
                                    items!!.adapter = labourAdapter

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            } else {
                                utilityofActivity!!.toast(response.message)
                            }
                        } else {
                            val intent = Intent(context, HTTPResponseError::class.java)
                            intent.putExtra("errorCode", t.code())
                            startActivity(intent)
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        utilityofActivity!!.toast(getString(R.string.error))
                    }

                }))

    }


    companion object {
        private var data = ArrayList<EditLabourReportResponseData>()
        private val newItems = ArrayList<EditLabourReportResponseData>()
    }


}