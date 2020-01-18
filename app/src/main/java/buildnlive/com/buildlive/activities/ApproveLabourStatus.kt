package buildnlive.com.buildlive.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import buildnlive.com.buildlive.App
import buildnlive.com.buildlive.Interfaces
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.Server.HTTPResponseError
import buildnlive.com.buildlive.Server.Request.ApprovalDataRequest
import buildnlive.com.buildlive.Server.Response.ApprovalDataResponse
import buildnlive.com.buildlive.Server.Response.ApprovalDataResponseData
import buildnlive.com.buildlive.Server.RetrofitApiAuthSingleTon
import buildnlive.com.buildlive.adapters.LabourStatusAdapter
import buildnlive.com.buildlive.console
import buildnlive.com.buildlive.utils.Config
import buildnlive.com.buildlive.utils.UtilityofActivity
import com.webpulse.webpulseclients.Server.TCApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_approve_labour_status.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.util.*

class ApproveLabourStatus : AppCompatActivity() {

    private var tcApi: TCApi? = null
    private var disposable = CompositeDisposable()

    private var utilityofActivity: UtilityofActivity? = null
    private val appCompatActivity = this
    private var context: Context? = null
    private var app: App? = null
    private var layoutManager: LinearLayoutManager? = null
    private var approvalItemAdapter: LabourStatusAdapter? = null
    private var type: String? = null
    private val resultList = ArrayList<ApprovalDataResponseData>()
    private var submit: Button? = null
    private var builder: AlertDialog.Builder? = null

    private val listener = object : LabourStatusAdapter.OnItemClickListener {
        override fun onEditClicked(type: String) {
            when (type) {
                "staff" -> {
                }
                "dept_labour" -> {
                }
                "issue_item" -> {

                }
                "receive_item" -> {

                }
                "sub_labour_report" -> {

                }
                "indent_item" -> {

                }
                "work_progress" -> {

                }
                "work_plan" -> {

                }
                "asset_job" -> {

                }
                else -> {
                }
            }
        }

        override fun onItemUnchecked(project: ApprovalDataResponseData, pos: Int) {
            if (resultList.isNotEmpty()) {
                resultList.remove(project)
            }
        }

        override fun onItemClick(project: ApprovalDataResponseData, pos: Int) {
            resultList.add(project)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approve_labour_status)


        context = this
        app = application as App
        utilityofActivity = UtilityofActivity(appCompatActivity)
        utilityofActivity!!.configureToolbar(appCompatActivity)

        builder = AlertDialog.Builder(context)

        tcApi = RetrofitApiAuthSingleTon.createService(TCApi::class.java)

        type = intent!!.getStringExtra("type")

        val toolbar_title = findViewById<TextView>(R.id.toolbar_title)
        val toolbar_subtitle = findViewById<TextView>(R.id.toolbar_subtitle)
        submit = findViewById(R.id.submit)

        toolbar_title.text = getString(R.string.approvals)
        toolbar_subtitle.text = App.projectName

        getData()

        submit!!.setOnClickListener { view ->
            builder!!.setMessage("Are you sure?").setTitle("Submit Approvals?")

            //Setting message manually and performing action on button click
            builder!!.setMessage("Do you want to Submit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        try {
                            sendRequest(resultList)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                    .setNegativeButton("No") { dialog, id ->
                        //  Action for 'NO' Button
                        dialog.cancel()

                    }
            //Creating dialog box
            val alert = builder!!.create()
            //Setting the title manually
            alert.setTitle("Forgot Password?")
            alert.show()


        }


    }

    private fun getData() {
        disposable.add(tcApi!!.callApprovalsData(ApprovalDataRequest(App.userId, App.projectId, type!!))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { utilityofActivity!!.showProgressDialog() }
                .doOnError { utilityofActivity!!.dismissProgressDialog() }
                .doOnSuccess { utilityofActivity!!.dismissProgressDialog() }
                .subscribeWith(object : DisposableSingleObserver<Response<ApprovalDataResponse>>() {
                    override fun onSuccess(t: Response<ApprovalDataResponse>) {
                        if (t.isSuccessful) {
                            val response = t.body()
                            if (response!!.success) {
                                utilityofActivity!!.toast(response.message)
                                try {

                                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                                    approvalItemAdapter = LabourStatusAdapter(context!!, response.data, listener,type!!)
                                    items!!.layoutManager = layoutManager
                                    items!!.adapter = approvalItemAdapter


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

    @Throws(JSONException::class)
    private fun sendRequest(items: ArrayList<ApprovalDataResponseData>) {
        val params = HashMap<String, String>()
        params["user_id"] = App.userId
        params["type"] = type!!
        val array = JSONArray()
        for (i in items) {
            array.put(JSONObject().put("id", i.id).put("status", i.status))
        }
        params["approval_list"] = array.toString()
        console.log("Res:$params")
        app!!.sendNetworkRequest(Config.GET_APPROVALS, 1, params, object : Interfaces.NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                utilityofActivity!!.showProgressDialog()
            }

            override fun onNetworkRequestError(error: String) {
                utilityofActivity!!.dismissProgressDialog()
                Toast.makeText(context, "Error:$error", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
                utilityofActivity!!.dismissProgressDialog()
                finish()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}
