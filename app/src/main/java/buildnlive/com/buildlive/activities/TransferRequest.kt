package buildnlive.com.buildlive.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

import com.android.volley.Request

import org.json.JSONArray
import org.json.JSONException

import java.util.ArrayList

import buildnlive.com.buildlive.App
import buildnlive.com.buildlive.Interfaces
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.console
import buildnlive.com.buildlive.elements.Item
import buildnlive.com.buildlive.fragments.*
import buildnlive.com.buildlive.utils.Config
import buildnlive.com.buildlive.utils.Helper
import buildnlive.com.buildlive.utils.UtilityofActivity

class TransferRequest : AppCompatActivity() {

    var appCompatActivity:AppCompatActivity?=this
    private var app: App? = null
    private var edit: TextView? = null
    private var view: TextView? = null
    private var fragment: Fragment? = null
    private val back: ImageButton? = null
    private var utilityofActivity: UtilityofActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_request)
        app = application as App
        edit = findViewById(R.id.edit)
        view = findViewById(R.id.view)

        utilityofActivity = UtilityofActivity(appCompatActivity!!)
        utilityofActivity!!.configureToolbar(appCompatActivity!!)

        val toolbar_title = findViewById<TextView>(R.id.toolbar_title)
        val toolbar_subtitle = findViewById<TextView>(R.id.toolbar_subtitle)
        toolbar_subtitle.text = App.projectName
        val helper=Helper.instance
        helper.moveFragment(TransferInFragment(),"Global", R.id.attendance_content, appCompatActivity!!)

        toolbar_title.text = "Transfer Request"

        edit = findViewById(R.id.edit)
        edit!!.setOnClickListener {
            enableEdit()
            disableView()
            helper.moveFragment(TransferInFragment(),"Global", R.id.attendance_content, appCompatActivity!!)

        }

        view = findViewById(R.id.view)
        view!!.setOnClickListener {
            enableView()
            disableEdit()
            helper.moveFragment(TransferOutFragment(),"Global", R.id.attendance_content, appCompatActivity!!)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun changeScreen() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.attendance_content, fragment!!)
                .commit()
    }

    private fun disableView() {
        val sdk = android.os.Build.VERSION.SDK_INT
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view!!.setBackgroundDrawable(ResourcesCompat.getDrawable(resources, R.drawable.round_left, null))
        } else {
            view!!.background = ContextCompat.getDrawable(applicationContext, R.drawable.round_left)
        }
        view!!.setTextColor(resources.getColor(R.color.color2))
    }

    private fun enableView() {
        val sdk = android.os.Build.VERSION.SDK_INT
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view!!.setBackgroundDrawable(ResourcesCompat.getDrawable(resources, R.drawable.round_left_selected, null))
        } else {
            view!!.background = ContextCompat.getDrawable(applicationContext, R.drawable.round_left_selected)
        }
        view!!.setTextColor(resources.getColor(R.color.white))
    }

    private fun disableEdit() {
        val sdk = android.os.Build.VERSION.SDK_INT
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            edit!!.setBackgroundDrawable(ResourcesCompat.getDrawable(resources, R.drawable.round_right, null))
        } else {
            edit!!.background = ContextCompat.getDrawable(applicationContext, R.drawable.round_right)
        }
        edit!!.setTextColor(resources.getColor(R.color.color2))
    }

    private fun enableEdit() {
        val sdk = android.os.Build.VERSION.SDK_INT
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            edit!!.setBackgroundDrawable(ResourcesCompat.getDrawable(resources, R.drawable.round_right_selected, null))
        } else {
            edit!!.background = ContextCompat.getDrawable(applicationContext, R.drawable.round_right_selected)
        }
        edit!!.setTextColor(resources.getColor(R.color.white))
    }
}
