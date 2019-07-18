package buildnlive.com.buildlive.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
import buildnlive.com.buildlive.fragments.AddItemFragment
import buildnlive.com.buildlive.fragments.InventoryFragment
import buildnlive.com.buildlive.fragments.IssueItemFragment
import buildnlive.com.buildlive.fragments.ViewItemFragment
import buildnlive.com.buildlive.utils.Config
import buildnlive.com.buildlive.utils.Helper

class Inventory : AppCompatActivity() {
    private val itemsList = ArrayList<Item>()
    var appCompatActivity:AppCompatActivity?=this
    private var app: App? = null
    private var edit: TextView? = null
    private var view: TextView? = null
    private var fragment: Fragment? = null
    private val back: ImageButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        app = application as App
        edit = findViewById(R.id.edit)
        view = findViewById(R.id.view)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val helper=Helper.instance
        helper.moveFragment(InventoryFragment(),"Global", R.id.attendance_content, appCompatActivity!!)

        val toolbar_title = findViewById<TextView>(R.id.toolbar_title)
        toolbar_title.text = "Inventory"

//        fragment = AddItemFragment.newInstance()
//        changeScreen()
        edit = findViewById(R.id.edit)
        edit!!.setOnClickListener {
            enableEdit()
            disableView()
//            fragment = AddItemFragment.newInstance()
//            changeScreen()
            helper.moveFragment(InventoryFragment(),"Global", R.id.attendance_content, appCompatActivity!!)

        }
        view = findViewById(R.id.view)
        view!!.setOnClickListener {
            enableView()
            disableEdit()
            fragment = ViewItemFragment.newInstance(app)
            changeScreen()
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
