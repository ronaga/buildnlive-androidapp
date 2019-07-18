package buildnlive.com.buildlive.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import buildnlive.com.buildlive.*
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.activities.LoginActivity
import buildnlive.com.buildlive.utils.Config
import buildnlive.com.buildlive.utils.PrefernceFile
import buildnlive.com.buildlive.utils.UtilityofActivity
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.android.volley.Request
import com.google.android.gms.location.*
import org.json.JSONException
import org.json.JSONObject
import kotlinx.android.synthetic.main.content_check_attendance.*
import kotlinx.android.synthetic.main.content_labour.view.*
import kotlin.math.abs

class CheckAttendanceLoc : Fragment() {

    var attendenceId: String? = null
    private var pref: SharedPreferences? = null
    private val generator = ColorGenerator.MATERIAL
    private var mContext: Context? = null
    private var appCompatActivity: AppCompatActivity? = null
    var utilityofActivity: UtilityofActivity? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var outputLocation: String? = null


    private var lastLocation: Location? = null
    private lateinit var resultReceiver: AddressResultReceiver
    var locationRequest: LocationRequest? = null
    private lateinit var locationCallback: LocationCallback


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        this.appCompatActivity = activity as AppCompatActivity?
    }

    object Constants {
        const val SUCCESS_RESULT = 0
        const val FAILURE_RESULT = 1
        const val PACKAGE_NAME = "com.google.android.gms.location.sample.locationaddress"
        const val RECEIVER = "$PACKAGE_NAME.RECEIVER"
        const val RESULT_DATA_KEY = "$PACKAGE_NAME.RESULT_DATA_KEY"
        const val LOCATION_DATA_EXTRA = "$PACKAGE_NAME.LOCATION_DATA_EXTRA"
    }


    internal inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {

            // Display the address string
            // or an error message sent from the intent service.
            outputLocation = resultData?.getString(Constants.RESULT_DATA_KEY) ?: ""


            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
//                utilityofActivity!!.toast(getString(R.string.address_found))
//                utilityofActivity!!.toast(outputLocation!!)
                utilityofActivity!!.dismissProgressDialog()
//                dialogView!!.findViewById<TextView>(R.id.address).text = outputLocation
//                next()
                console.log("Location: " + outputLocation)

            } else {
                utilityofActivity!!.toast("Error")
                utilityofActivity!!.dismissProgressDialog()
            }

        }
    }


    private fun startIntentService() {

        val intent = Intent(mContext, FetchAddressIntentService::class.java).apply {
            putExtra(Constants.RECEIVER, resultReceiver)
            putExtra(Constants.LOCATION_DATA_EXTRA, lastLocation)
        }
        mContext!!.startService(intent)
    }


    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private val requestingLocationUpdates: Boolean = true

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null /* Looper */
        )
    }


    override fun onStart() {
        super.onStart()
        checkAttendanceFun()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_check_attendance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (PrefernceFile.getInstance(context!!).getString("Lat") != null) {
            console.log("Fragment: " + PrefernceFile.getInstance(context!!).getString("Lat")!!)
        }

        resultReceiver = AddressResultReceiver(Handler())

        utilityofActivity = UtilityofActivity(appCompatActivity!!)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(appCompatActivity!!)


        locationRequest = LocationRequest.create()?.apply {
            interval = 1000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest!!)

        builder.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    if (location != null)
                        lastLocation = location
                    if (lastLocation != null) {
                        PrefernceFile.getInstance(mContext!!).setString("Lat", lastLocation!!.latitude.toString())
                        PrefernceFile.getInstance(mContext!!).setString("Long", lastLocation!!.longitude.toString())
//                        fetchAddressButtonHander()
                    }
                    console.log("Location LatLang: $lastLocation")
                }
            }
        }



        if (((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && (ContextCompat.checkSelfPermission(
                        appCompatActivity!!,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(appCompatActivity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 102)
        } else {
            //TODO add not dismissable dialog to enable permissions

//            startLocationUpdates()

            val lm = mContext!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var gps_enabled = false
            var network_enabled = false

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (ex: Exception) {
            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (ex: Exception) {
            }

            if (!gps_enabled && !network_enabled) {
                // notify user

                val builder = androidx.appcompat.app.AlertDialog.Builder(mContext!!)

                // Set the alert dialog title
                builder.setTitle("Location Settings")

                builder.setMessage("Location services are required for posting please switch them on to continue.")
                // Set a positive button and its click listener on alert dialog
                builder.setPositiveButton("Open Settings") { dialog, which ->
                    // Do something when user press the positive button
                    mContext!!.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                }


                // Display a negative button on alert dialog
                builder.setNegativeButton("Dismiss") { dialog, which ->
                    Toast.makeText(
                            mContext,
                            "Location Services are necessary for posting",
                            Toast.LENGTH_SHORT
                    ).show()
                }


                // Finally, make the alert dialog using builder
                val dialog: androidx.appcompat.app.AlertDialog = builder.create()

                // Display the alert dialog on app interface
                dialog.show()
            } else {

//            utilityofActivity!!.showProgressDialog()
                startLocationUpdates()
            }


        }



        pref = app!!.pref
        val n = pref!!.getString(LoginActivity.PREF_KEY_NAME, "Dummy")
        val e = pref!!.getString(LoginActivity.PREF_KEY_EMAIL, "abc@xyz.com")

        cover!!.setImageDrawable(TextDrawable.builder().buildRound("" + n!![0], generator.getColor(e!!)))

        name.text = n
        email.text = e

        checkin.setOnClickListener {
            Checkin()
        }

        checkout.setOnClickListener {
            CheckOut()
        }

        absent.setOnClickListener {

            val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogView = inflater.inflate(R.layout.alert_dialog_absent, null)
            val dialogBuilder = AlertDialog.Builder(context!!, R.style.PinDialog)

            val alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create()
            alertDialog.show()

            val title = dialogView.findViewById<TextView>(R.id.alert_title)
            val status = dialogView.findViewById<Spinner>(R.id.status)
            title.text = "Status"

            val positive = dialogView.findViewById<Button>(R.id.positive)
            val negative = dialogView.findViewById<Button>(R.id.negative)

            positive.text = "Done"
            positive.setOnClickListener {
                MarkAbsent(status.selectedItem.toString())
                alertDialog.dismiss()
            }
            negative.setOnClickListener {
                alertDialog.dismiss()
            }
        }


    }

    fun fetchAddressButtonHander() {


        startIntentService()


    }


    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>,
            grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty()) {
            when (requestCode) {
                102 -> {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        startLocationUpdates()


                    }

                }
            }
        }
    }


    private fun Checkin() {
        var requestUrl = Config.CHECK_IN
        requestUrl = requestUrl.replace("[0]", App.userId)
        requestUrl = requestUrl.replace("[1]", lastLocation!!.latitude.toString())
        requestUrl = requestUrl.replace("[2]", lastLocation!!.longitude.toString())

        console.log("CheckIn: " + requestUrl)
        app!!.sendNetworkRequest(requestUrl, Request.Method.GET, null, object : Interfaces.NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                progress.visibility = View.VISIBLE
                hider.visibility = View.VISIBLE
            }

            override fun onNetworkRequestError(error: String) {
                progress.visibility = View.GONE
                hider.visibility = View.GONE
                console.error("Network request failed with error :$error")
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
                console.log(response)
                progress.visibility = View.GONE
                hider.visibility = View.GONE
                try {

                    if (response == "0") {
                        Toast.makeText(context!!, "Checkout Failure Please try again", Toast.LENGTH_LONG).show()
                    } else if (response == "-1") {
                        Toast.makeText(context!!, "Invalid User, Please contact admin", Toast.LENGTH_LONG).show()
                    } else {
                        val jsonObject = JSONObject(response)
                        status.text = "You checked in at : " + jsonObject.get("start_time")
                        attendenceId = jsonObject.getString("attendence_id")
                        PrefernceFile.getInstance(mContext!!).setString("attendence_id", attendenceId!!)

                        checkout.visibility = View.VISIBLE
                        absent.visibility=View.GONE
                        checkin.visibility = View.GONE

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }

    private fun CheckOut() {
        var requestUrl = Config.CHECK_OUT
        try {
            requestUrl = requestUrl.replace("[0]", App.userId)
//        attendenceId=PrefernceFile.getInstance(mContext!!).getString("attendence_id")
            requestUrl = requestUrl.replace("[1]", PrefernceFile.getInstance(mContext!!).getString("attendence_id")!!)
            requestUrl = requestUrl.replace("[2]", lastLocation!!.latitude.toString())
            requestUrl = requestUrl.replace("[3]", lastLocation!!.longitude.toString())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        console.log("CheckOut: " + requestUrl)
        app!!.sendNetworkRequest(requestUrl, Request.Method.GET, null, object : Interfaces.NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                progress.visibility = View.VISIBLE
                hider.visibility = View.VISIBLE
            }

            override fun onNetworkRequestError(error: String) {
                progress.visibility = View.GONE
                hider.visibility = View.GONE
                console.error("Network request failed with error :$error")
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
                console.log(response)
                progress.visibility = View.GONE
                hider.visibility = View.GONE
                try {
//                    if(response == "1")
//                    {

//                    }
//                    else if(response == "0")
                    if (response == "0") {
                        Toast.makeText(context!!, "Checkout Failure Please try again", Toast.LENGTH_LONG).show()
                    } else if (response == "-1") {
                        Toast.makeText(context!!, "Invalid User, Please contact admin", Toast.LENGTH_LONG).show()
                    } else {
                        val jsonObject = JSONObject(response)

                        Toast.makeText(context!!, "Checkout Successful", Toast.LENGTH_LONG).show()
                        status.text = "You checked in at : " + jsonObject.get("start_time")
                        checkout.background = ContextCompat.getDrawable(context!!, R.drawable.inactive_home_button)
                        checkout.isEnabled = false
                        absent.visibility=View.GONE
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }


    private fun MarkAbsent(leaveStatus:String) {
        var requestUrl = Config.MARK_ABSENT_USER
        try {
            requestUrl = requestUrl.replace("[0]", App.userId)
//        attendenceId=PrefernceFile.getInstance(mContext!!).getString("attendence_id")
            requestUrl = requestUrl.replace("[1]", lastLocation!!.latitude.toString())
            requestUrl = requestUrl.replace("[2]", lastLocation!!.longitude.toString())
            requestUrl = requestUrl.replace("[3]", leaveStatus)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        console.log("CheckOut: " + requestUrl)
        app!!.sendNetworkRequest(requestUrl, Request.Method.GET, null, object : Interfaces.NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                progress.visibility = View.VISIBLE
                hider.visibility = View.VISIBLE
            }

            override fun onNetworkRequestError(error: String) {
                progress.visibility = View.GONE
                hider.visibility = View.GONE
                console.error("Network request failed with error :$error")
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
                console.log(response)
                progress.visibility = View.GONE
                hider.visibility = View.GONE
                try {
//                    if(response == "1")
//                    {

//                    }
//                    else if(response == "0")
                    if (response == "0") {
                        Toast.makeText(context!!, "Leave Failure Please try again", Toast.LENGTH_LONG).show()
                    } else if (response == "-1") {
                        Toast.makeText(context!!, "Invalid User, Please contact admin", Toast.LENGTH_LONG).show()
                    } else {
                        val jsonObject = JSONObject(response)

                        Toast.makeText(context!!, "Checkout Successful", Toast.LENGTH_LONG).show()
                        status.text = "You marked a leave at : " + jsonObject.get("start_time")
                        checkin.background = ContextCompat.getDrawable(context!!, R.drawable.inactive_home_button)
                        checkin.isEnabled = false
                        absent.background = ContextCompat.getDrawable(context!!, R.drawable.inactive_home_button)
                        absent.isEnabled = false
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }


    private fun checkAttendanceFun() {
        var requestUrl = Config.GET_ATTENDANCE
        requestUrl = requestUrl.replace("[0]", App.userId)

        console.log("CheckOut: " + requestUrl)
        app!!.sendNetworkRequest(requestUrl, Request.Method.GET, null, object : Interfaces.NetworkInterfaceListener {
            override fun onNetworkRequestStart() {
                progress.visibility = View.VISIBLE
                hider.visibility = View.VISIBLE
            }

            override fun onNetworkRequestError(error: String) {
                progress.visibility = View.GONE
                hider.visibility = View.GONE
                console.error("Network request failed with error :$error")
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show()
            }

            override fun onNetworkRequestComplete(response: String) {
                console.log(response)
                progress.visibility = View.GONE
                hider.visibility = View.GONE
                try {
//                    if(response == "1")
//                    {

//                    }
//                    else if(response == "0")
                    if (response == "-1") {
                        Toast.makeText(context!!, "Invalid User, Please contact admin", Toast.LENGTH_LONG).show()
                    } else {
                        val jsonObject = JSONObject(response)

                        PrefernceFile.getInstance(mContext!!).setString("attendence_id", jsonObject.getString("attendence_id"))

                        if ((jsonObject.getString("start_time") == "0" && jsonObject.getString("end_time") == "0")) {
                            checkin.visibility = View.VISIBLE
                            absent.visibility = View.VISIBLE
                            checkin.isEnabled = true
                            checkin.background = ContextCompat.getDrawable(context!!, R.drawable.home_button)
                            checkout.visibility = View.GONE
                        } else if ((jsonObject.getString("start_time").isNotEmpty() && jsonObject.getString("end_time") == "null")) {
                            checkin.visibility = View.GONE
                            absent.visibility = View.GONE
                            checkout.isEnabled = true
                            checkout.background = ContextCompat.getDrawable(context!!, R.drawable.home_button)
                            checkout.visibility = View.VISIBLE
                        }else if((jsonObject.getString("start_time") == "-1" && jsonObject.getString("end_time") == "-1"))
                        {
                            absent.visibility=View.VISIBLE
                            checkin.visibility = View.GONE
                            checkout.visibility = View.GONE
                            absent.background = ContextCompat.getDrawable(context!!, R.drawable.inactive_home_button)
                            absent.isEnabled = false
                        }
                        else {
                            checkin.visibility = View.GONE
                            absent.visibility = View.GONE
                            checkout.visibility = View.VISIBLE
                            checkout.background = ContextCompat.getDrawable(context!!, R.drawable.inactive_home_button)
                            checkout.isEnabled = false
                        }

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }

    companion object {
        private var app: App? = null
        fun newInstance(a: App): CheckAttendanceLoc {
            app = a
            return CheckAttendanceLoc()
        }
    }
}
