package buildnlive.com.buildlive.Approvals

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import buildnlive.com.buildlive.App
import buildnlive.com.buildlive.Interfaces.NetworkInterfaceListener
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.Server.HTTPResponseError
import buildnlive.com.buildlive.Server.Request.SendStoreRequest
import buildnlive.com.buildlive.Server.Response.DefaultResponse
import buildnlive.com.buildlive.Server.Response.GetStoreResponse
import buildnlive.com.buildlive.Server.RetrofitApiAuthSingleTon
import buildnlive.com.buildlive.Server.TCApi
import buildnlive.com.buildlive.console
import buildnlive.com.buildlive.elements.Item
import buildnlive.com.buildlive.utils.Config
import buildnlive.com.buildlive.utils.UtilityofActivity
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_edit_store_details.*
import org.json.JSONException
import retrofit2.Response
import java.util.*
import java.util.logging.Logger

class EditStoreDetails : AppCompatActivity() {
    private var selectedItem: String? = null
    private var store_request_id: String? = null
    private var builder: AlertDialog.Builder? = null
    private var context: Context? = null
    private var itemList: Item? = null
    private var utilityofActivity: UtilityofActivity? = null
    private val appCompatActivity: AppCompatActivity = this
    private var tcApi: TCApi? = null
    private var disposable = CompositeDisposable()


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
        setContentView(R.layout.activity_edit_store_details)
        context = this
        utilityofActivity = UtilityofActivity(this)

        store_request_id = intent.getStringExtra("store_request_id")

        tcApi = RetrofitApiAuthSingleTon.createService(TCApi::class.java)



        utilityofActivity!!.configureToolbar(appCompatActivity)
        val toolbar_title = findViewById<TextView>(R.id.toolbar_title)
        val toolbar_subtitle = findViewById<TextView>(R.id.toolbar_subtitle)
        toolbar_title.text = "Stock Request"
        toolbar_subtitle.text = App.projectName


        val bundle = intent.extras
        if (bundle != null) {
            itemList = bundle.getSerializable("Items") as Item?
        }
        app = application as App


        builder = AlertDialog.Builder(context!!)

        submit.text = "Update & Approve"

        submit!!.setOnClickListener {
            builder!!.setMessage("Do you want to Submit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                        if (quantity!!.text.toString().isNotEmpty()) {
                            try {
                                if (quantity!!.text.toString().toFloat() > max_quantity!!.text.toString().toFloat()) {
                                    Toast.makeText(context, "Check Quantity!", Toast.LENGTH_SHORT).show()
                                    return@OnClickListener
                                }
                                sendIssue()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            Toast.makeText(context, "Fill Data Properly!", Toast.LENGTH_LONG).show()
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

        disposable.add(tcApi!!.callEditStoreDetails(SendStoreRequest(App.userId, App.projectId, store_request_id!!))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { utilityofActivity!!.showProgressDialog() }
                .doOnError { utilityofActivity!!.dismissProgressDialog() }
                .doOnSuccess { utilityofActivity!!.dismissProgressDialog() }
                .subscribeWith(object : DisposableSingleObserver<Response<GetStoreResponse>>() {
                    override fun onSuccess(t: Response<GetStoreResponse>) {
                        if (t.isSuccessful) {
                            val response = t.body()
                            if (response!!.isSuccess) {
                                try {

                                    val data = response.data[0]
                                    if (data != null) {
                                        item.text = "Item Name: " + data.itemName
                                        slip_no.text = "Slip No: " + data.slip_no
                                        quantity.setText(data.qtyRequested)
                                        max_quantity.text = data.current_stock
                                        unit2.text = data.units
                                        receiver_edittext.text = "Receiver: " + data.staffName
                                        issueType.text = "Issue Type: " + data.issue_type
                                        member.text = "Issue to: " + data.issueTo
                                        asset.text = "Issue to: " + data.assetReg
                                        comment.setText(data.remarks)
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
    private fun sendIssue() {
        progress!!.visibility = View.VISIBLE
        hider!!.visibility = View.VISIBLE

        val params = HashMap<String, String>()
        params["user_id"] = App.userId
        params["detail_id"] = App.projectId
        params["store_request_id"] = store_request_id!!
        params["qty_requested"] = quantity.text.toString()

        console.log("ISSUE DATA:$params")
        app!!.sendNetworkRequest(Config.UpdateStoreRequestDetail, 1, params, object : NetworkInterfaceListener {
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