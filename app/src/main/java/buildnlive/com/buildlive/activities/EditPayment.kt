package buildnlive.com.buildlive.Approvals

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import buildnlive.com.buildlive.App
import buildnlive.com.buildlive.Interfaces
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.Server.HTTPResponseError
import buildnlive.com.buildlive.Server.Request.EditPaymentRequest
import buildnlive.com.buildlive.Server.Response.DefaultResponse
import buildnlive.com.buildlive.Server.Response.EditPaymentResponse
import buildnlive.com.buildlive.Server.RetrofitApiAuthSingleTon
import buildnlive.com.buildlive.Server.TCApi
import buildnlive.com.buildlive.console
import buildnlive.com.buildlive.utils.Config
import buildnlive.com.buildlive.utils.UtilityofActivity
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.acitivity_edit_payment.*
import kotlinx.android.synthetic.main.acitivity_edit_payment.hider
import kotlinx.android.synthetic.main.acitivity_edit_payment.progress
import kotlinx.android.synthetic.main.acitivity_edit_payment.receiver
import kotlinx.android.synthetic.main.acitivity_edit_payment.submit
import kotlinx.android.synthetic.main.fragment_issue_item.*
import org.json.JSONException
import retrofit2.Response

class EditPayment : AppCompatActivity() {
    private var account_details_id: String? = null
    private var builder: AlertDialog.Builder? = null
    private var context: Context? = null
    private var utilityofActivity: UtilityofActivity? = null
    private val appCompatActivity: AppCompatActivity = this
    private var tcApi: TCApi? = null
    private var disposable = CompositeDisposable()
    private var itemAdapter: ArrayAdapter<String>? = null

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_edit_payment)
        context = this
        utilityofActivity = UtilityofActivity(this)

        account_details_id = intent.getStringExtra("account_details_id")

        tcApi = RetrofitApiAuthSingleTon.createService(TCApi::class.java)



        utilityofActivity!!.configureToolbar(appCompatActivity)
        val toolbar_title = findViewById<TextView>(R.id.toolbar_title)
        val toolbar_subtitle = findViewById<TextView>(R.id.toolbar_subtitle)
        toolbar_title.text = getString(R.string.site_payment)
        toolbar_subtitle.text = App.projectName

        app = application as App

        itemAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.payment_mode))
        paymentMode.adapter = itemAdapter


        builder = AlertDialog.Builder(context!!)

        submit.text = "Update & Approve"

        submit!!.setOnClickListener {
            builder!!.setMessage("Do you want to Submit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                        try {
                            sendPayments()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    })
                    .setNegativeButton("No") { dialog, id ->
                        //  Action for 'NO' Button
                        dialog.cancel()
                    }
            //Creating dialog box
            val alert = builder!!.create()
            //Setting the title manually
            alert.setTitle("Stock Request")
            alert.show()
        }

        refresh()
    }


    private fun refresh() {

        disposable.add(tcApi!!.callEditPaymentDetails(EditPaymentRequest(App.userId, App.projectId, account_details_id!!))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { utilityofActivity!!.showProgressDialog() }
                .doOnError { utilityofActivity!!.dismissProgressDialog() }
                .doOnSuccess { utilityofActivity!!.dismissProgressDialog() }
                .subscribeWith(object : DisposableSingleObserver<Response<EditPaymentResponse>>() {
                    override fun onSuccess(t: Response<EditPaymentResponse>) {
                        if (t.isSuccessful) {
                            val response = t.body()
                            if (response!!.isSuccess) {
                                try {
                                    val data = response.data[0]
                                    if (data != null) {
                                        purpose.text = data.purpose

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            description.text = Html.fromHtml("<b>Description: </b>" + data.description, Html.FROM_HTML_MODE_LEGACY)
                                        } else description.text = Html.fromHtml("<b>Description: </b>" + data.description)

                                        amount.setText(data.amount)
                                        payment_details.setText(data.details)
                                        receiver.text = String.format("To: %s", data.payee)
                                        reason.text = String.format("Reason: %s", data.reason)
                                        paymentType.text = data.payment_type
                                        paymentMode.setSelection(itemAdapter!!.getPosition(data.payment_mode))

                                    } else {
                                        Log.e("DATA", "EMPTY")
                                    }
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
    private fun sendPayments() {
        progress!!.visibility = View.VISIBLE
        hider!!.visibility = View.VISIBLE

        val params = HashMap<String, String>()
        params["user_id"] = App.userId
        params["detail_id"] = App.projectId
        params["account_details_id"] = account_details_id!!
        params["amount"] = amount.text.toString()
        params["details"] = payment_details.text.toString()
        params["payment_mode"] = paymentMode.selectedItem.toString()

        console.log("PAYMENT DATA:$params")
        app!!.sendNetworkRequest(Config.UpdateSitePaymentDetail, 1, params, object : Interfaces.NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                progress!!.visibility = View.VISIBLE
                hider!!.visibility = View.VISIBLE
                utilityofActivity!!.showProgressDialog()
            }

            override fun onNetworkRequestError(error: String) {
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                utilityofActivity!!.dismissProgressDialog()
            }

            override fun onNetworkRequestComplete(response: String) {
                console.log("Response:$response")
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


    companion object {
        private var app: App? = null
    }
}