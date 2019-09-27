package buildnlive.com.buildlive.activities

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.android.volley.Request

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.HashMap

import buildnlive.com.buildlive.App
import buildnlive.com.buildlive.Interfaces
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.adapters.NotificationsAdapter
import buildnlive.com.buildlive.console
import buildnlive.com.buildlive.elements.Notification
import buildnlive.com.buildlive.utils.Config
import buildnlive.com.buildlive.utils.UtilityofActivity
import io.realm.Realm

class FcmNotificationActivity : AppCompatActivity() {
    private var app: App? = null
    private var realm: Realm? = null
    private var progressBar: ProgressBar? = null
    private val notificationList = ArrayList<Notification>()
    private var recyclerView: RecyclerView? = null
    private val back: ImageButton? = null
    internal var adapter: NotificationsAdapter?=null
    internal var builder: AlertDialog.Builder?=null
    private var context: Context? = null
    private var utilityofActivity: UtilityofActivity? = null
    private val appCompatActivity = this


    var listener: NotificationsAdapter.OnItemClickListener = NotificationsAdapter.OnItemClickListener { notification, pos, view ->
        if (view.id == R.id.image) {
            val intent = Intent(this@FcmNotificationActivity, BillImageView::class.java)
            val bundle = Bundle()
            bundle.putString("Link", notification.image)
            intent.putExtras(bundle)
            startActivity(intent)
        } else if (view.id == R.id.review) {

            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.alert_dialog_review_notification, null)
            val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context!!, R.style.PinDialog)
            val alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create()
            alertDialog.show()
            val alert_message = dialogView.findViewById<EditText>(R.id.alert_message)
            val close = dialogView.findViewById<Button>(R.id.negative)
            val approve = dialogView.findViewById<Button>(R.id.positive)


            close.setOnClickListener { alertDialog.dismiss() }

            approve.setOnClickListener {
                try {
                    sendRequest(notification.id, alert_message.text.toString(), "Revision", userId)
                    notificationList.removeAt(pos)
                    adapter!!.notifyItemRemoved(pos)
                    adapter!!.notifyItemRangeChanged(pos, notificationList.size)
                    alertDialog.dismiss()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        } else {
            builder!!.setMessage("Do you want to Submit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        when (view.id) {
                            R.id.receive -> try {
                                sendRequest(notification.id, "", "Received", userId)
                                notificationList.removeAt(pos)
                                adapter!!.notifyItemRemoved(pos)
                                adapter!!.notifyItemRangeChanged(pos, notificationList.size)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                            R.id.not_receive -> try {
                                sendRequest(notification.id, "", "Not Received", userId)
                                notificationList.removeAt(pos)
                                adapter!!.notifyItemRemoved(pos)
                                adapter!!.notifyItemRangeChanged(pos, notificationList.size)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                            R.id.approve -> try {
                                sendRequest(notification.id, "", "Approved", userId)
                                notificationList.removeAt(pos)
                                adapter!!.notifyItemRemoved(pos)
                                adapter!!.notifyItemRangeChanged(pos, notificationList.size)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                            R.id.reject -> try {
                                sendRequest(notification.id, "", "Rejected", userId)
                                notificationList.removeAt(pos)
                                adapter!!.notifyItemRemoved(pos)
                                adapter!!.notifyItemRangeChanged(pos, notificationList.size)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                            R.id.read_notification -> {
                                try {
                                    sendRequest(notification.id, "", "Read", userId)
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }

                                notificationList.removeAt(pos)
                                adapter!!.notifyItemRemoved(pos)
                                adapter!!.notifyItemRangeChanged(pos, notificationList.size)
                            }
                        }
                    }
                    .setNegativeButton("No") { dialog, id ->
                        //  Action for 'NO' Button
                        dialog.cancel()
                    }
            //Creating dialog box
            val alert = builder!!.create()
            //Setting the title manually
            alert.setTitle("Submit")
            alert.show()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)


        context = this

        utilityofActivity = UtilityofActivity(appCompatActivity)
        utilityofActivity!!.configureToolbar(appCompatActivity)

        val toolbar_title = findViewById<TextView>(R.id.toolbar_title)
        val toolbar_subtitle = findViewById<TextView>(R.id.toolbar_subtitle)
        //        toolbar_subtitle.setText(App.projectName);
        toolbar_subtitle.visibility = View.GONE
        toolbar_title.text = "Notifications"

        app = application as App
        progressBar = findViewById(R.id.progress)
        realm = Realm.getDefaultInstance()


        val intent = intent

        if (intent != null) {
            refresh(intent.getStringExtra("user_id"), intent.getStringExtra("project_id"))
            userId = intent.getStringExtra("user_id")
        }

        builder = AlertDialog.Builder(this)
        recyclerView = findViewById<View>(R.id.notifications) as RecyclerView


        //        final String adapter=new ArrayAdapter<String>(this,mobileArray);
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

/*

    @RequiresApi(Build.VERSION_CODES.M)
    fun isRunning(ctx: Context): Boolean {
        val activityManager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = activityManager.appTasks

        for (task in tasks) {
            if (ctx.packageName.equals(task.taskInfo.baseActivity.packageName, ignoreCase = true))
                return true
        }

        return false
    }

    private fun isRunningBelowM(ctx: Context): Boolean {
        val activityManager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = activityManager.getRunningTasks(Integer.MAX_VALUE)

        for (task in tasks) {
            if (ctx.packageName.equals(task.baseActivity.packageName, ignoreCase = true))
                return true
        }

        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    !isRunning(this)
                } else {
                    isRunningBelowM(this)
                }
        ) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
 */
    private fun refresh(userId: String?, projectId: String?) {
        var requestUrl = Config.SEND_NOTIFICATIONS
        notificationList.clear()
        requestUrl = requestUrl.replace("[0]", userId!!)
        requestUrl = requestUrl.replace("[1]", projectId!!)
        console.log(requestUrl)
        app!!.sendNetworkRequest(requestUrl, Request.Method.GET, null, object : Interfaces.NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                progressBar!!.visibility = View.VISIBLE
            }

            override fun onNetworkRequestError(error: String) {
                progressBar!!.visibility = View.GONE
                console.error("Network request failed with error :$error")
                Toast.makeText(applicationContext, "Check Network, Something went wrong", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
                console.log(response)
                console.log(response)
                progressBar!!.visibility = View.GONE
                try {
                    val array = JSONArray(response)
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        notificationList.add(Notification().parseFromJSON(obj))
                    }
                    adapter = NotificationsAdapter(applicationContext, notificationList, listener)
                    val linearLayoutManager = LinearLayoutManager(applicationContext)
                    linearLayoutManager.orientation = RecyclerView.VERTICAL
                    recyclerView!!.layoutManager = linearLayoutManager
                    recyclerView!!.hasFixedSize()
                    recyclerView!!.adapter = adapter

                } catch (e: JSONException) {

                }

            }
        })
    }


    @Throws(JSONException::class)
    private fun sendRequest(id: String, comments: String, answer: String, userId: String?) {
        val app = application as App
        val params = HashMap<String, String>()
        val jsonObject = JSONObject()
        if (answer == "Revision") {
            jsonObject.put("id", id).put("response", answer).put("user_id", userId).put("response_comments", comments)
        } else {
            jsonObject.put("id", id).put("response", answer).put("user_id", userId).put("response_comments", "")
        }
        params["notification"] = jsonObject.toString()
        console.log("Res:$params")
        app.sendNetworkRequest(Config.GET_NOTIFICATIONS, 1, params, object : Interfaces.NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                utilityofActivity!!.showProgressDialog()

            }

            override fun onNetworkRequestError(error: String) {
                utilityofActivity!!.dismissProgressDialog()
            }

            override fun onNetworkRequestComplete(response: String) {
                console.log(response)
                utilityofActivity!!.dismissProgressDialog()
                if (response == "1") {
                    Toast.makeText(applicationContext, "Request Generated", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

    companion object {
        private var userId: String? = null
    }


}
