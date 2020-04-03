package buildnlive.com.buildlive.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import buildnlive.com.buildlive.App
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.Server.HTTPResponseError
import buildnlive.com.buildlive.Server.Request.ApprovalRequest
import buildnlive.com.buildlive.Server.Response.ApprovalResponse
import buildnlive.com.buildlive.Server.RetrofitApiAuthSingleTon
import buildnlive.com.buildlive.adapters.ApprovalItemAdapter
import buildnlive.com.buildlive.utils.UtilityofActivity
import buildnlive.com.buildlive.Server.TCApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class Approval : AppCompatActivity() {

    private var tcApi: TCApi? = null
    private var disposable = CompositeDisposable()
    private var utilityofActivity: UtilityofActivity? = null
    private val appCompatActivity = this
    private var context: Context? = null
    private var app: App? = null
    private var layoutManager: LinearLayoutManager? = null
    private var approvalItemAdapter: ApprovalItemAdapter? = null
    private var items: RecyclerView? = null
    private val listener = ApprovalItemAdapter.OnItemInteractListener { type ->
        val intent = Intent(context, ApproveLabourStatus::class.java)
        intent.putExtra("type", type!!)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approvals)

        context = this
        app = application as App
        utilityofActivity = UtilityofActivity(appCompatActivity)
        utilityofActivity!!.configureToolbar(appCompatActivity)

        tcApi = RetrofitApiAuthSingleTon.createService(TCApi::class.java)

        val toolbar_title = findViewById<TextView>(R.id.toolbar_title)
        val toolbar_subtitle = findViewById<TextView>(R.id.toolbar_subtitle)
        toolbar_title.text = getString(R.string.approvals)
        toolbar_subtitle.text = App.projectName

        items = findViewById(R.id.items)


        refresh()

    }

    private fun refresh() {


        disposable.add(tcApi!!.callApprovalsList(ApprovalRequest(App.userId, App.projectId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { utilityofActivity!!.showProgressDialog() }
                .doOnError { utilityofActivity!!.dismissProgressDialog() }
                .doOnSuccess { utilityofActivity!!.dismissProgressDialog() }
                .subscribeWith(object : DisposableSingleObserver<Response<ApprovalResponse>>() {
                    override fun onSuccess(t: Response<ApprovalResponse>) {
                        if (t.isSuccessful) {
                            val response = t.body()
                            if (response!!.isSuccess) {
                                try {
                                    val approvalList = response.data
                                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                                    approvalItemAdapter = ApprovalItemAdapter(context, approvalList, listener)
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

        /*    var requestUrl = Config.APPROVALS
            requestUrl = requestUrl.replace("[0]", App.userId)
            requestUrl = requestUrl.replace("[1]", App.projectId)

            console.log(requestUrl)

            app!!.sendNetworkRequest(requestUrl, Request.Method.POST, null, object : Interfaces.NetworkInterfaceListener {
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

                }
            })*/
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
