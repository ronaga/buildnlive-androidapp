/*
package buildnlive.com.buildlive.Agenda

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import buildnlive.com.buildlive.App
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.utils.UtilityofActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_agenda.*
import kotlinx.android.synthetic.main.toolbar.*

class Agenda : AppCompatActivity() {

    private var context: Context? = null
    private var appCompatActivity: AppCompatActivity? = this
    private var utilityofActivity: UtilityofActivity? = null
    private var app: App? = null


    override fun onStart() {
        super.onStart()
//        getItems()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agenda)

        context = this


        app = application as App

        utilityofActivity = UtilityofActivity(appCompatActivity!!)
        utilityofActivity!!.configureToolbar(appCompatActivity!!)

        val toolbar_title = findViewById<TextView>(R.id.toolbar_title)
        val toolbar_subtitle = findViewById<TextView>(R.id.toolbar_subtitle)
        toolbar_subtitle.text = App.projectName
        toolbar_title.text = getString(R.string.agenda)

        notification.visibility = View.GONE
        addItem.visibility = View.VISIBLE


        addItem.setOnClickListener {
            startActivity(Intent(context, AddAgenda::class.java))
        }


        */
/*  val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)

          items!!.addItemDecoration(dividerItemDecoration)
           items!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

           listAdapter = AMCListAdapter(context, ArrayList<AMCItem>(), listener)
           items!!.adapter = listAdapter*//*

        configureRecyclerView()
    }

    private fun configureRecyclerView() {


        val json = Gson()

        */
/*
   {
  "success": true,
  "message": "Success",
  "data": [
  {
  "agendaId": "2",
  "topic": "Building material required in RCC Level 1",
  "priority": "Urgent",
  "status": "active",
  "description": "Test",
  "date": "29-09-2019 : 30-09-2019"
}
  ]
}


  *//*


        val data = "{\n" +
                "  \"success\": true,\n" +
                "  \"message\": \"Success\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"agendaId\": \"2\",\n" +
                "      \"topic\": \"Building material required in RCC Level 1\",\n" +
                "      \"priority\": \"Urgent\",\n" +
                "      \"status\": \"active\",\n" +
                "      \"description\": \"Test\",\n" +
                "      \"date\": \"29-09-2019 : 30-09-2019\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"

        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
        )

        items.layoutManager = layoutManager


        val adapter = AgendaListAdapter(context!!, json.fromJson(data, AgendaResponseData::class.java).data)
        items.adapter = adapter


    }


*/
/*    private fun getItems() {
        var requestUrl = Config.ShowAMC

        requestUrl = requestUrl.replace("[0]", App.userId)

        itemList.clear()

        console.log("Services:  $requestUrl")

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


                try {
                    val vendorType = object : TypeToken<ArrayList<AMCItem>>() {

                    }.type
                    itemList = Gson().fromJson<ArrayList<AMCItem>>(response, vendorType)

                    listAdapter = AMCListAdapter(context!!, itemList, listener)
                    items!!.adapter = listAdapter


                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }*//*



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}*/
