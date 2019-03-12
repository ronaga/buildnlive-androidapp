package buildnlive.com.buildlive.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*

import com.android.volley.Request

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.HashMap

import buildnlive.com.buildlive.App
import buildnlive.com.buildlive.Interfaces
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.adapters.AddItemAdapter
import buildnlive.com.buildlive.console
import buildnlive.com.buildlive.elements.Item
import buildnlive.com.buildlive.utils.Config
import buildnlive.com.buildlive.utils.Helper
import buildnlive.com.buildlive.utils.UtilityofActivity
import kotlinx.android.synthetic.main.fragment_inventory_new.*

class InventoryFragment : Fragment() {
    private var items: RecyclerView? = null
    private var next: Button? = null
    private var submit: Button? = null
    private var progress: ProgressBar? = null
    private var hider: TextView? = null
    private var checkout_text: TextView? = null
    private var search_textview: TextView? = null
    private val LOADING: Boolean = false
    private var close: ImageButton? = null
    private var adapter: AddItemAdapter? = null
    private var searchView: SearchView? = null
    internal lateinit var builder: AlertDialog.Builder
    internal val newItems: MutableList<Item> = ArrayList()

    var utilityofActivity: UtilityofActivity? = null
    var appCompatActivity: AppCompatActivity? = null
    var mContext: Context? = null
    var helper: Helper? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        this.appCompatActivity = activity as AppCompatActivity?
    }

    var listener: AddItemAdapter.OnItemSelectedListener = object : AddItemAdapter.OnItemSelectedListener {
        override fun onItemCheck(checked: Boolean) {
            if (checked) {
                next!!.visibility = View.VISIBLE
            } else {
                next!!.visibility = View.GONE
            }
        }

        override fun onItemInteract(pos: Int, flag: Int) {

        }

//        override fun onItemReturn(item: Item?, pos: Int) {
//            newItems.add(item!!)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        newItems.clear()
        itemsList.clear()
        AddItemAdapter.ViewHolder.CHECKOUT = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inventory_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        builder = AlertDialog.Builder(context)
        items = view.findViewById(R.id.items)
        submit = view.findViewById(R.id.submit)
        next = view.findViewById(R.id.next)
        close = view.findViewById(R.id.close_checkout)

        checkout_text = view.findViewById(R.id.checkout_text)

        next!!.setOnClickListener {
            searchView!!.visibility = View.GONE
            checkout_text!!.visibility = View.VISIBLE
            close!!.visibility = View.VISIBLE
            search_textview!!.visibility = View.GONE
            AddItemAdapter.ViewHolder.CHECKOUT = true
            for (i in itemsList.indices) {
                if (itemsList[i].isUpdated) {
                    newItems.add(itemsList[i])
                }
            }
            for (i in newItems){
                console.log(i.name)
            }

            adapter = AddItemAdapter(context, newItems, object : AddItemAdapter.OnItemSelectedListener {
                override fun onItemCheck(checked: Boolean) {

                }

                override fun onItemInteract(pos: Int, flag: Int) {
                    if (flag == 100) {
                        newItems.removeAt(pos)
                        adapter!!.notifyItemRemoved(pos)
                    }
                }
            })
//
            items!!.adapter = adapter
            submit!!.visibility = View.VISIBLE
            next!!.visibility = View.GONE
            submit!!.setOnClickListener {
                builder.setMessage("Are you sure?").setTitle("Indent Item")

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes") { dialog, id ->
                            try {
                                sendRequest(newItems)
                            } catch (e: JSONException) {

                            }
                        }
                        .setNegativeButton("No") { dialog, id ->
                            //  Action for 'NO' Button
                            dialog.cancel()
                        }
                //Creating dialog box
                val alert = builder.create()
                //Setting the title manually
                alert.setTitle("Indent Item")
                alert.show()
            }
        }
//        search_textview = view.findViewById(R.id.search_textview)
        progress = view.findViewById(R.id.progress)
        hider = view.findViewById(R.id.hider)
//                adapter = new AddItemAdapter(getContext(), itemsList, listener);
//                items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//                items.setAdapter(adapter);
        if (LOADING) {
            progress!!.visibility = View.VISIBLE
            hider!!.visibility = View.VISIBLE
        } else {
            progress!!.visibility = View.GONE
            hider!!.visibility = View.GONE
        }

        search.setOnClickListener {
            search_text.visibility=View.VISIBLE
            search_close.visibility=View.VISIBLE
            search_text.requestFocus()
            if(search_text.hasFocus()){
                val imm = appCompatActivity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(search_text.windowToken,InputMethodManager.SHOW_IMPLICIT )
                imm.showSoftInput(search_text,InputMethodManager.SHOW_IMPLICIT)
            }
        }

        search_text.setOnEditorActionListener() { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                search(search_text!!.text.toString())
                val imm = appCompatActivity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(search_text!!.windowToken, 0)
                true
            } else {
                false
            }
        }
        search_close.setOnClickListener {
            search_text.setText("")
            search_text.visibility=View.INVISIBLE
            search_close.visibility=View.INVISIBLE
            val imm = appCompatActivity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            refresh()
        }

//        searchView = view.findViewById(R.id.search_view)
//
//        searchView!!.setOnClickListener { search_textview!!.visibility = View.GONE }
//        // listening to search query text change
//        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                // filter recycler view when query submitted
//                adapter!!.filter.filter(query)
//                return false
//            }
//
//            override fun onQueryTextChange(query: String): Boolean {
//                // filter recycler view when text is changed
//                adapter!!.filter.filter(query)
//                return false
//            }
//        })
        refresh()
    }

    @Throws(JSONException::class)

    private fun sendRequest(items: List<Item>) {
        val app = activity!!.application as App
        val params = HashMap<String, String>()
        params["user_id"] = App.userId
        params["detail_id"] = App.projectId
        val array = JSONArray()
        for (i in items) {
            array.put(JSONObject().put("stock_id", i.id).put("qty", i.quantity))
        }
        params["item_list"] = array.toString()
        console.log("Res:$params")
        app.sendNetworkRequest(Config.INVENTORY_ITEM_REQUEST, 1, params, object : Interfaces.NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                progress!!.visibility = View.VISIBLE
                hider!!.visibility = View.VISIBLE
            }

            override fun onNetworkRequestError(error: String) {
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                Toast.makeText(context, "Error:$error", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                if (response == "1") {
                    Toast.makeText(context, "Request Generated", Toast.LENGTH_SHORT).show()
                    AddItemAdapter.ViewHolder.CHECKOUT = false
                    activity!!.finish()
                }
            }
        })
    }

    private fun refresh() {
        val app = activity!!.application as App
        itemsList.clear()
        var requestUrl = Config.REQ_GET_INVENTORY
        requestUrl = requestUrl.replace("[0]", App.userId)
        requestUrl = requestUrl.replace("[1]", App.projectId)
        console.log(requestUrl)
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, object : Interfaces.NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                progress!!.visibility = View.VISIBLE
                hider!!.visibility = View.VISIBLE
            }

            override fun onNetworkRequestError(error: String) {
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                console.error("Network request failed with error :$error")
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
                //                console.log(response);
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                try {
                    val array = JSONArray(response)
                    for (i in 0 until array.length()) {
                        itemsList.add(Item().parseFromJSON(array.getJSONObject(i)))
                    }
                    console.log("data set changed")
                    adapter = AddItemAdapter(context, itemsList, listener)
                    items!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    items!!.adapter = adapter
                    adapter!!.notifyDataSetChanged()
                    console.log("" + adapter!!.itemCount)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }

 private fun search(keyword:String) {
        val app = activity!!.application as App
        itemsList.clear()
        var requestUrl = Config.INVENTORY_SEARCH
        requestUrl = requestUrl.replace("[0]", App.userId)
        requestUrl = requestUrl.replace("[1]", App.projectId)
        requestUrl = requestUrl.replace("[2]", keyword)
        console.log(requestUrl)
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, object : Interfaces.NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                progress!!.visibility = View.VISIBLE
                hider!!.visibility = View.VISIBLE
            }

            override fun onNetworkRequestError(error: String) {
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                console.error("Network request failed with error :$error")
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
                //                console.log(response);
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                try {
                    val array = JSONArray(response)
                    for (i in 0 until array.length()) {
                        itemsList.add(Item().parseFromJSON(array.getJSONObject(i)))
                    }
                    console.log("data set changed")
                    adapter = AddItemAdapter(context, itemsList, listener)
                    items!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    items!!.adapter = adapter
                    adapter!!.notifyDataSetChanged()
                    console.log("" + adapter!!.itemCount)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }

    companion object {
        val itemsList = ArrayList<Item>()

        fun newInstance(): AddItemFragment {
            //        itemsList = u;
            return AddItemFragment()
        }
    }


}