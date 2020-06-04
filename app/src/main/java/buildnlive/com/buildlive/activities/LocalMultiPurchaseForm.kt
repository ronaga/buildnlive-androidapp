package buildnlive.com.buildlive.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import buildnlive.com.buildlive.App
import buildnlive.com.buildlive.BuildConfig
import buildnlive.com.buildlive.Interfaces.NetworkInterfaceListener
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.adapters.SingleImageAdapter
import buildnlive.com.buildlive.console
import buildnlive.com.buildlive.elements.Item
import buildnlive.com.buildlive.elements.Packet
import buildnlive.com.buildlive.utils.AdvancedRecyclerView
import buildnlive.com.buildlive.utils.Config
import buildnlive.com.buildlive.utils.UtilityofActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class LocalMultiPurchaseForm : AppCompatActivity() {
    private var submit: Button? = null
    private val upload: Button? = null
    private var progress: ProgressBar? = null
    private var `val` = true
    private var hider: TextView? = null
    private var item: TextView? = null
    private var unitsText: TextView? = null
    private var total_edit: EditText? = null
    private var overheads_edit: EditText? = null
    private var vendor_details_edit: EditText? = null
    private var ship_no_edit: EditText? = null
    private var details_edit: EditText? = null
    private var vendor_gst: EditText? = null
    private val LOADING = false
    private var tax_paid: Spinner? = null
    private var builder: AlertDialog.Builder? = null
    private var imagePath: String? = null
    private var images: ArrayList<Packet>? = null
    private var finalList = ArrayList<Item>()
    private var imagesAdapter: SingleImageAdapter? = null
    private var context: Context? = null
    private val selectedItem: Item? = null
    private var utilityofActivity: UtilityofActivity? = null
    private val appCompatActivity: AppCompatActivity = this

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
        setContentView(R.layout.fragment_multi_purchase_form)
        utilityofActivity = UtilityofActivity(appCompatActivity)
        utilityofActivity!!.configureToolbar(appCompatActivity)
        val toolbar_title = findViewById<TextView>(R.id.toolbar_title)
        val toolbar_subtitle = findViewById<TextView>(R.id.toolbar_subtitle)
        toolbar_subtitle.text = App.projectName
        toolbar_title.text = "Purchase"
        val bundle = intent.extras
        if (bundle != null) {
            finalList = bundle.getParcelableArrayList<Item>("Items")!!
        }
        item = findViewById(R.id.item)
        progress = findViewById(R.id.progress)
        submit = findViewById(R.id.submit)
        context = this
        //        name_edit =  findViewById(R.id.name);
        total_edit = findViewById(R.id.total)
        overheads_edit = findViewById(R.id.overheads)
        vendor_details_edit = findViewById(R.id.vendor_details)
        ship_no_edit = findViewById(R.id.ship_no)
        vendor_gst = findViewById(R.id.vendor_gst)
        details_edit = findViewById(R.id.details)
        builder = AlertDialog.Builder(context)
        tax_paid = findViewById(R.id.tax_paid)
        tax_paid!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                tax_paid_text = tax_paid!!.getSelectedItem().toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        progress = findViewById(R.id.progress)
        hider = findViewById(R.id.hider)
        if (LOADING) {
            progress!!.visibility = View.VISIBLE
            hider!!.visibility = View.VISIBLE
        } else {
            progress!!.setVisibility(View.GONE)
            hider!!.setVisibility(View.GONE)
        }
        val list = findViewById<AdvancedRecyclerView>(R.id.images)
        images = ArrayList()
        images!!.add(Packet())
        imagesAdapter = SingleImageAdapter(context, images, SingleImageAdapter.OnItemClickListener { packet, pos, view ->
            if (pos == 0) {
                val inflater = layoutInflater
                val dialogView = inflater.inflate(R.layout.image_chooser, null)
                val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context!!, R.style.PinDialog)
                val alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create()
                alertDialog.show()
                val gallery = dialogView.findViewById<TextView>(R.id.gallery)
                val camera = dialogView.findViewById<TextView>(R.id.camera)
                gallery.setOnClickListener {
                    alertDialog.dismiss()
                    val pictureIntent = Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pictureIntent, REQUEST_GALLERY_IMAGE)
                }
                camera.setOnClickListener {
                    alertDialog.dismiss()
                    val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (pictureIntent.resolveActivity(packageManager) != null) {
                        var photoFile: File? = null
                        try {
                            photoFile = createImageFile()
                        } catch (ex: IOException) {
                        }
                        if (photoFile != null) {
                            val photoURI = FileProvider.getUriForFile(context!!, BuildConfig.APPLICATION_ID + ".provider", photoFile)
                            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            imagePath = photoFile.absolutePath
                            startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE)
                        }
                    }
                }
                val negative = dialogView.findViewById<Button>(R.id.negative)
                negative.setOnClickListener { alertDialog.dismiss() }
            } else {
                images!!.removeAt(pos)
                imagesAdapter!!.notifyItemRemoved(pos)
                imagesAdapter!!.notifyDataSetChanged()
            }
        })
        list.adapter = imagesAdapter
        list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        list.setmMaxHeight(350)
        submit!!.setOnClickListener(View.OnClickListener { //                name=name_edit.getText().toString();

            val imm= appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(submit!!.getWindowToken(), 0)

            total = total_edit!!.getText().toString()
            overheads = overheads_edit!!.getText().toString()
            vendor_details = vendor_details_edit!!.getText().toString()
            ship_no = ship_no_edit!!.getText().toString()
            details = details_edit!!.getText().toString()
            gst = vendor_gst!!.getText().toString()


            //Setting message manually and performing action on button click
            builder!!.setMessage("Do you want to Submit?")
                    .setCancelable(false)
                    .setPositiveButton("Submit") { dialog, id ->
                        if (validate(total, vendor_details)) {
                            console.log("From Validate")
                            try {
                                sendRequest(total!!, overheads, vendor_details, ship_no, details, images!!, gst, tax_paid_text)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, id -> //  Action for 'NO' Button
                        dialog.cancel()
                    }.setNeutralButton("Submit & Add Payment") { dialogInterface, i ->
                        if (validate(total!!, vendor_details)) {
                            console.log("From Validate")
                            try {
                                sendRequestForPayments(total, overheads, vendor_details, ship_no, details, images!!, gst, tax_paid_text)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }
            //Creating dialog box
            val alert = builder!!.create()
            //Setting the title manually
            alert.setTitle("Purchase")
            alert.show()
        })
    }

    private fun validate(total: String?, vendor_details: String?): Boolean {
        `val` = true
        if (TextUtils.isEmpty(total)) {
            total_edit!!.error = "Enter Total"
            `val` = false
        }
        if (TextUtils.isEmpty(vendor_details)) {
            vendor_details_edit!!.error = "Enter Vendor Details"
            `val` = false
        }
        return `val`
    }

    @Throws(JSONException::class)
    private fun sendRequest(total: String?,
                            overheads: String?, vendor_details: String?, ship_no: String?, details: String?, images: ArrayList<Packet>, vendor_gst_no: String?, tax_paid_text: String?) {
        val app = application as App
        val params = HashMap<String, String>()

        val dataArray = JSONArray()
        for (item in finalList) {
            val jsonObject = JSONObject()
            jsonObject.put("stock_id", item.id)
                    .put("quantity", item.quantity)
                    .put("rate", item.rate)
                    .put("tax", item.tax)
            dataArray.put(jsonObject)
        }

        params["local_purchase_list"] = dataArray.toString()

        //        JSONArray array = new JSONArray();
        val jsonObject = JSONObject()
        jsonObject.put("project_id", App.projectId).put("user_id", App.userId)
                .put("total_amount", total)
                .put("overheads", overheads).put("vendor_details", vendor_details).put("slip_no", ship_no)
                .put("details", details).put("vendor_gst_no", vendor_gst_no).put("tax_paid", tax_paid_text)
        params["local_purchase"] = jsonObject.toString()
        console.log("Local Purchase$params")
        val array = JSONArray()
        for (p in images) {
            if (p.name != null) {
                val bm = BitmapFactory.decodeFile(p.name)
                val baos = ByteArrayOutputStream()
                bm.compress(Bitmap.CompressFormat.JPEG, QUALITY, baos)
                val b = baos.toByteArray()
                val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
                array.put(encodedImage)
            }
        }
        params["images"] = array.toString()
        console.log("Image$params")
        app.sendNetworkRequest(Config.SEND_LOCAL_PURCHASE_MULTI, 1, params, object : NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                progress!!.visibility = View.VISIBLE
                hider!!.visibility = View.VISIBLE
                utilityofActivity!!.showProgressDialog()
            }

            override fun onNetworkRequestError(error: String) {
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                utilityofActivity!!.dismissProgressDialog()
                Toast.makeText(context, "Error$error", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
                console.log(response)
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                utilityofActivity!!.dismissProgressDialog()
                if (response == "1") {
                    Toast.makeText(context, "Request Generated", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(context, "Check Your Network", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    @Throws(JSONException::class)
    private fun sendRequestForPayments(total: String?,
                                       overheads: String?, vendor_details: String?, ship_no: String?, details: String?, images: ArrayList<Packet>, vendor_gst_no: String?, tax_paid_text: String?) {
        val app = application as App
        val params = HashMap<String, String>()

        val dataArray = JSONArray()
        for (item in finalList) {
            val jsonObject = JSONObject()
            jsonObject.put("stock_id", item.id)
                    .put("quantity", item.quantity)
                    .put("rate", item.rate)
                    .put("tax", item.tax)
            dataArray.put(jsonObject)
        }
        params["local_purchase_list"] = dataArray.toString()

        //        JSONArray array = new JSONArray();
        val jsonObject = JSONObject()
        jsonObject.put("project_id", App.projectId).put("user_id", App.userId)
                .put("total_amount", total)
                .put("overheads", overheads).put("vendor_details", vendor_details).put("slip_no", ship_no)
                .put("details", details).put("vendor_gst_no", vendor_gst_no).put("tax_paid", tax_paid_text)
        params["local_purchase"] = jsonObject.toString()
        console.log("Local Purchase$params")
        val array = JSONArray()
        for (p in images) {
            if (p.name != null) {
                val bm = BitmapFactory.decodeFile(p.name)
                val baos = ByteArrayOutputStream()
                bm.compress(Bitmap.CompressFormat.JPEG, QUALITY, baos)
                val b = baos.toByteArray()
                val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
                array.put(encodedImage)
            }
        }
        params["images"] = array.toString()
        console.log("Image$params")
        app.sendNetworkRequest(Config.SEND_LOCAL_PURCHASE_MULTI_CONT, 1, params, object : NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                progress!!.visibility = View.VISIBLE
                hider!!.visibility = View.VISIBLE
                utilityofActivity!!.showProgressDialog()
            }

            override fun onNetworkRequestError(error: String) {
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                utilityofActivity!!.dismissProgressDialog()
                Toast.makeText(context, "Error$error", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
                console.log(response)
                progress!!.visibility = View.GONE
                hider!!.visibility = View.GONE
                utilityofActivity!!.dismissProgressDialog()
                console.log("RESPONSE PAY: $response")
                try {
                    val jsonObject = JSONObject(response)
                    val bundle = Bundle()
                    bundle.putString("local_purchase_id", jsonObject.getString("local_purchase_id"))
                    bundle.putString("type", jsonObject.getString("type"))
                    bundle.putString("display", jsonObject.getString("display"))
                    val intent = Intent(context, LocalPayment::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    finish()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val packet = images!!.removeAt(0)
                packet.name = imagePath
                //                Uri uri=data.getData();
//                packet.setName(getRealPathFromURI(uri));
                console.log("Image Path " + packet.name + "EXTRAS " + packet.extra)
                images!!.add(0, Packet())
                images!!.add(packet)
                imagesAdapter!!.notifyDataSetChanged()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                console.log("Canceled")
            }
        } else if (requestCode == REQUEST_GALLERY_IMAGE) {
            val packet = images!!.removeAt(0)
            //            packet.setName(imagePath);
            val uri = data!!.data
            packet.name = getRealPathFromURI(uri)
            console.log("Image Path " + packet.name + "EXTRAS " + packet.extra)
            images!!.add(0, Packet())
            images!!.add(packet)
            imagesAdapter!!.notifyDataSetChanged()
        }
    }

    // And to convert the image URI to the direct file system path of the image file
    fun getRealPathFromURI(contentUri: Uri?): String? {

        // can post image
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context!!.contentResolver.query(contentUri!!,
                proj,  // Which columns to return
                null,  // WHERE clause; which rows to return (all rows)
                null,  // WHERE clause selection arguments (none)
                null) // Order-by clause (ascending by name)
        if (cursor!!.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            results = cursor.getString(column_index)
        }
        //                managedQuery( );
        cursor.moveToFirst()
        cursor.close()
        return results
    }

    companion object {
        //    name_edit name,
        private var total: String? = null
        private var overheads: String? = null
        private var vendor_details: String? = null
        private var ship_no: String? = null
        private var details: String? = null
        private var results: String? = null
        private var gst: String? = null
        private var tax_paid_text: String? = null
        const val QUALITY = 10
        const val REQUEST_CAPTURE_IMAGE = 7190
        const val REQUEST_GALLERY_IMAGE = 7191
    }
}