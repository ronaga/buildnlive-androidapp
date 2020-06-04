package buildnlive.com.buildlive.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import buildnlive.com.buildlive.App
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.Server.Request.DeletePaymentRequest
import buildnlive.com.buildlive.Server.HTTPResponseError
import buildnlive.com.buildlive.Server.Request.ProjectIdRequest
import buildnlive.com.buildlive.Server.Response.DefaultResponse
import buildnlive.com.buildlive.Server.Response.ViewPaymentResponse
import buildnlive.com.buildlive.Server.RetrofitApiAuthSingleTon
import buildnlive.com.buildlive.Server.TCApi
import buildnlive.com.buildlive.adapters.PaymentItemAdapter
import buildnlive.com.buildlive.utils.UtilityofActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_view_payment.*
import retrofit2.Response

class ViewPaymentFragment : Fragment() {
    private var mContext: Context? = null
    private var utilityofActivity: UtilityofActivity? = null
    private var appCompatActivity: AppCompatActivity? = null
    private var disposable = CompositeDisposable()
    private var paymentItemAdapter: PaymentItemAdapter? = null

    private var builder: AlertDialog.Builder? = null

    private var tcApi: TCApi? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        appCompatActivity = activity as AppCompatActivity
    }

    override fun onStart() {
        super.onStart()
        getpaymentsList()
    }

    private var listener = object : PaymentItemAdapter.OnItemInteractListener {
        override fun onItemClick(accountDetailId: String?) {
            builder!!.setMessage("Are you sure?").setTitle("Delete")
            //Setting message manually and performing action on button click
            builder!!.setMessage("Do you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog: DialogInterface?, id: Int ->
                            deletePayment(accountDetailId!!)
                    }
                    .setNegativeButton("No") { dialog: DialogInterface, id: Int ->
                        //  Action for 'NO' Button
                        dialog.cancel()
                    }
            //Creating dialog box
            val alert = builder!!.create()
            //Setting the title manually
            alert.setTitle("Delete")
            alert.show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_payment, container, false)
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        utilityofActivity = UtilityofActivity(appCompatActivity!!)
        tcApi = RetrofitApiAuthSingleTon.createService(TCApi::class.java)


        builder = AlertDialog.Builder(context)
   /*     val toolbar_title = view.findViewById<TextView>(R.id.toolbar_title)
        val toolbar_subtitle = view.findViewById<TextView>(R.id.toolbar_subtitle)
        toolbar_title.text = getString(R.string.view_payments)
        toolbar_subtitle.text = App.projectName
*/
    }

    fun getpaymentsList() {
        disposable.add(tcApi!!.callSendPayments(ProjectIdRequest(App.projectId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { utilityofActivity!!.showProgressDialog() }
                .doOnError { utilityofActivity!!.dismissProgressDialog() }
                .doOnSuccess { utilityofActivity!!.dismissProgressDialog() }
                .subscribeWith(object : DisposableSingleObserver<Response<ViewPaymentResponse>>() {
                    override fun onSuccess(t: Response<ViewPaymentResponse>) {
                        if (t.isSuccessful) {
                            val response = t.body()
                            if (response!!.success) {
                                try {
                                    val paymentList = response.data
                                    val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                                    paymentItemAdapter = PaymentItemAdapter(context!!, paymentList, listener)
                                    items!!.layoutManager = layoutManager
                                    items!!.adapter = paymentItemAdapter

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

    fun deletePayment(accountDetailId: String) {
        disposable.add(tcApi!!.callSendDeletePayments(DeletePaymentRequest(accountDetailId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { utilityofActivity!!.showProgressDialog() }
                .doOnError { utilityofActivity!!.dismissProgressDialog() }
                .doOnSuccess { utilityofActivity!!.dismissProgressDialog() }
                .subscribeWith(object : DisposableSingleObserver<Response<DefaultResponse>>() {
                    override fun onSuccess(t: Response<DefaultResponse>) {
                        if (t.isSuccessful) {
                            val response = t.body()
                            if (response!!.success) {
                                utilityofActivity!!.toast(response.message)
                                getpaymentsList()
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
        @JvmStatic
        fun newInstance(): ViewPaymentFragment {
            return ViewPaymentFragment()
        }
    }
}