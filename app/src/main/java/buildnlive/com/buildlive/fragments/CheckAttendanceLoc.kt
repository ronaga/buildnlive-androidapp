package buildnlive.com.buildlive.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.provider.Settings
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import buildnlive.com.buildlive.*
import buildnlive.com.buildlive.activities.LoginActivity
import buildnlive.com.buildlive.utils.Config
import buildnlive.com.buildlive.utils.PrefernceFile
import buildnlive.com.buildlive.utils.UtilityofActivity
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.an.biometric.BiometricCallback
import com.an.biometric.BiometricManager
import com.an.biometric.BiometricUtils
import com.android.volley.Request
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.content_check_attendance.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.activities.CalendarViewActivity
import com.applikeysolutions.cosmocalendar.settings.lists.connected_days.ConnectedDays


class CheckAttendanceLoc : Fragment() {

    var attendenceId: String? = null
    private var pref: SharedPreferences? = null
    private val generator = ColorGenerator.MATERIAL
    private var mContext: Context? = null
    private var appCompatActivity: AppCompatActivity? = null
    var utilityofActivity: UtilityofActivity? = null

    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    var mBiometricManager: BiometricManager? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var outputLocation: String? = null


    private var lastLocation: Location? = null
    private lateinit var resultReceiver: AddressResultReceiver
    var locationRequest: LocationRequest? = null
    private lateinit var locationCallback: LocationCallback


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onAttach(activity: Activity) {
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

/*
        val weekendDays = HashSet<Int>()
        weekendDays.add(Calendar.SATURDAY)
        weekendDays.add(Calendar.SUNDAY)
        calendarView.weekendDays = weekendDays
*/

        calendar.setOnClickListener {
            startActivity(Intent(context, CalendarViewActivity::class.java))
        }

        mBiometricManager = BiometricManager.BiometricBuilder(mContext!!)
                .setTitle(getString(R.string.biometric_title))
                .setSubtitle(getString(R.string.biometric_subtitle))
                .setDescription(getString(R.string.biometric_description))
                .setNegativeButtonText(getString(R.string.biometric_negative_button_text))
                .build()

        if (!BiometricUtils.isHardwareSupported(mContext!!) && !BiometricUtils.isSdkVersionSupported()) {
            if (mBiometricManager != null)
                mBiometricManager!!.cancelAuthentication()
        }




        checkin.setOnClickListener {
            if (BiometricUtils.isHardwareSupported(mContext!!) && BiometricUtils.isSdkVersionSupported()) {
                mBiometricManager!!.authenticate(object : BiometricCallback {
                    override fun onSdkVersionNotSupported() {
                        if (lastLocation != null) {
                            Checkin(lastLocation!!.latitude.toString(), lastLocation!!.longitude.toString())
                        } else {
                            utilityofActivity!!.toast("Error fetching location please try again")
                        }
                        Toast.makeText(context, getString(R.string.biometric_error_sdk_not_supported), Toast.LENGTH_LONG).show()
                    }

                    override fun onBiometricAuthenticationNotSupported() {
                        if (lastLocation != null) {
                            Checkin(lastLocation!!.latitude.toString(), lastLocation!!.longitude.toString())
                        } else {
                            utilityofActivity!!.toast("Error fetching location please try again")
                        }
                        Toast.makeText(context, getString(R.string.biometric_error_hardware_not_supported), Toast.LENGTH_LONG).show()
                    }

                    override fun onBiometricAuthenticationNotAvailable() {
                        Toast.makeText(context, getString(R.string.biometric_error_fingerprint_not_available), Toast.LENGTH_LONG).show()
                    }

                    override fun onBiometricAuthenticationPermissionNotGranted() {
                        Toast.makeText(context, getString(R.string.biometric_error_permission_not_granted), Toast.LENGTH_LONG).show()
                    }

                    override fun onBiometricAuthenticationInternalError(error: String?) {
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    }

                    override fun onAuthenticationFailed() { //        Toast.makeText(getApplicationContext(), getString(R.string.biometric_failure), Toast.LENGTH_LONG).show();
                    }

                    override fun onAuthenticationCancelled() {
                        if (mBiometricManager != null)
                            mBiometricManager!!.cancelAuthentication()
                        Toast.makeText(context, getString(R.string.biometric_cancelled), Toast.LENGTH_LONG).show()
                    }

                    override fun onAuthenticationSuccessful() {
                        if (lastLocation != null) {
                            Checkin(lastLocation!!.latitude.toString(), lastLocation!!.longitude.toString())
                        } else {
                            utilityofActivity!!.toast("Error fetching location please try again")
                        }
                    }

                    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) { //        Toast.makeText(getApplicationContext(), helpString, Toast.LENGTH_LONG).show();
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) { //        Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
                    }

                })

            } else {
                if (mBiometricManager != null)
                    mBiometricManager!!.cancelAuthentication()
                if (lastLocation != null) {
                    Checkin(lastLocation!!.latitude.toString(), lastLocation!!.longitude.toString())
                } else {
                    utilityofActivity!!.toast("Error fetching location please try again")
                }
            }
        }

        checkout.setOnClickListener {
            if (BiometricUtils.isHardwareSupported(mContext!!) && BiometricUtils.isSdkVersionSupported()) {
                mBiometricManager!!.authenticate(object : BiometricCallback {
                    override fun onSdkVersionNotSupported() {
                        if (lastLocation != null) {
                            CheckOut(lastLocation!!.latitude.toString(), lastLocation!!.longitude.toString())
                        } else {
                            utilityofActivity!!.toast("Error fetching location please try again")
                        }
                        Toast.makeText(context, getString(R.string.biometric_error_sdk_not_supported), Toast.LENGTH_LONG).show()
                    }

                    override fun onBiometricAuthenticationNotSupported() {
                        if (lastLocation != null) {
                            CheckOut(lastLocation!!.latitude.toString(), lastLocation!!.longitude.toString())
                        } else {
                            utilityofActivity!!.toast("Error fetching location please try again")
                        }
                        Toast.makeText(context, getString(R.string.biometric_error_hardware_not_supported), Toast.LENGTH_LONG).show()
                    }

                    override fun onBiometricAuthenticationNotAvailable() {
                        Toast.makeText(context, getString(R.string.biometric_error_fingerprint_not_available), Toast.LENGTH_LONG).show()
                    }

                    override fun onBiometricAuthenticationPermissionNotGranted() {
                        Toast.makeText(context, getString(R.string.biometric_error_permission_not_granted), Toast.LENGTH_LONG).show()
                    }

                    override fun onBiometricAuthenticationInternalError(error: String?) {
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    }

                    override fun onAuthenticationFailed() { //        Toast.makeText(getApplicationContext(), getString(R.string.biometric_failure), Toast.LENGTH_LONG).show();
                    }

                    override fun onAuthenticationCancelled() {
                        if (mBiometricManager != null)
                            mBiometricManager!!.cancelAuthentication()
                        Toast.makeText(context, getString(R.string.biometric_cancelled), Toast.LENGTH_LONG).show()
                    }

                    override fun onAuthenticationSuccessful() {
                        if (lastLocation != null) {
                            CheckOut(lastLocation!!.latitude.toString(), lastLocation!!.longitude.toString())
                        } else {
                            utilityofActivity!!.toast("Error fetching location please try again")
                        }
                    }

                    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) { //        Toast.makeText(getApplicationContext(), helpString, Toast.LENGTH_LONG).show();
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) { //        Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
                    }

                })

            } else {
                if (mBiometricManager != null)
                    mBiometricManager!!.cancelAuthentication()
                if (lastLocation != null) {
                    CheckOut(lastLocation!!.latitude.toString(), lastLocation!!.longitude.toString())
                } else {
                    utilityofActivity!!.toast("Error fetching location please try again")
                }
            }


        }

        absent.setOnClickListener {

            val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogView = inflater.inflate(R.layout.alert_dialog_absent, null)
            val dialogBuilder = AlertDialog.Builder(context!!, R.style.PinDialog)

            val alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create()
            alertDialog.show()

            val title = dialogView.findViewById<TextView>(R.id.alert_title)
            val status = dialogView.findViewById<Spinner>(R.id.status)
            title.text = getString(R.string.status)

            val positive = dialogView.findViewById<Button>(R.id.positive)
            val negative = dialogView.findViewById<Button>(R.id.negative)

            positive.text = getString(R.string.done)
            positive.setOnClickListener {

                if (BiometricUtils.isHardwareSupported(mContext!!) && BiometricUtils.isSdkVersionSupported()) {

                    mBiometricManager!!.authenticate(object : BiometricCallback {
                        override fun onSdkVersionNotSupported() {
                            if (lastLocation != null) {
                                MarkAbsent(status.selectedItem.toString(), lastLocation!!.latitude.toString(), lastLocation!!.longitude.toString())
                                alertDialog.dismiss()
                            } else {
                                utilityofActivity!!.toast("Error fetching location please try again")
                            }
                            Toast.makeText(context, getString(R.string.biometric_error_sdk_not_supported), Toast.LENGTH_LONG).show()
                        }

                        override fun onBiometricAuthenticationNotSupported() {
                            if (lastLocation != null) {
                                MarkAbsent(status.selectedItem.toString(), lastLocation!!.latitude.toString(), lastLocation!!.longitude.toString())
                                alertDialog.dismiss()
                            } else {
                                utilityofActivity!!.toast("Error fetching location please try again")
                            }
                            Toast.makeText(context, getString(R.string.biometric_error_hardware_not_supported), Toast.LENGTH_LONG).show()
                        }

                        override fun onBiometricAuthenticationNotAvailable() {
                            Toast.makeText(context, getString(R.string.biometric_error_fingerprint_not_available), Toast.LENGTH_LONG).show()
                        }

                        override fun onBiometricAuthenticationPermissionNotGranted() {
                            Toast.makeText(context, getString(R.string.biometric_error_permission_not_granted), Toast.LENGTH_LONG).show()
                        }

                        override fun onBiometricAuthenticationInternalError(error: String?) {
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                        }

                        override fun onAuthenticationFailed() { //        Toast.makeText(getApplicationContext(), getString(R.string.biometric_failure), Toast.LENGTH_LONG).show();
                        }

                        override fun onAuthenticationCancelled() {
                            if (mBiometricManager != null)
                                mBiometricManager!!.cancelAuthentication()
                            Toast.makeText(context, getString(R.string.biometric_cancelled), Toast.LENGTH_LONG).show()
                        }

                        override fun onAuthenticationSuccessful() {
                            if (lastLocation != null) {
                                MarkAbsent(status.selectedItem.toString(), lastLocation!!.latitude.toString(), lastLocation!!.longitude.toString())
                                alertDialog.dismiss()
                            } else {
                                utilityofActivity!!.toast("Error fetching location please try again")
                            }
                        }

                        override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) { //        Toast.makeText(getApplicationContext(), helpString, Toast.LENGTH_LONG).show();
                        }

                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) { //        Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
                        }

                    })
                } else {
                    if (mBiometricManager != null)
                        mBiometricManager!!.cancelAuthentication()
                    if (lastLocation != null) {
                        MarkAbsent(status.selectedItem.toString(), lastLocation!!.latitude.toString(), lastLocation!!.longitude.toString())
                        alertDialog.dismiss()
                    } else {
                        utilityofActivity!!.toast("Error fetching location please try again")
                    }
                }
            }
            negative.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        outDuty.setOnClickListener {

            val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogView = inflater.inflate(R.layout.alert_dialog_outduty, null)
            val dialogBuilder = AlertDialog.Builder(context!!, R.style.PinDialog)

            val alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create()
            alertDialog.show()

            val title = dialogView.findViewById<TextView>(R.id.alert_title)
            title.text = getString(R.string.outduty)

            val positive = dialogView.findViewById<Button>(R.id.positive)
            val negative = dialogView.findViewById<Button>(R.id.negative)
            val date = dialogView.findViewById<TextView>(R.id.time)
            val reason = dialogView.findViewById<EditText>(R.id.reason)

            /*date.setOnClickListener {
                // Get Current Date
                val c = Calendar.getInstance()
                mYear = c.get(Calendar.YEAR)
                mMonth = c.get(Calendar.MONTH)
                mDay = c.get(Calendar.DAY_OF_MONTH)


                val datePickerDialog = DatePickerDialog(context,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> date.text = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year }, mYear!!, mMonth!!, mDay!!)
                datePickerDialog.show()
            }*/

            reason.movementMethod = ScrollingMovementMethod()

            positive.text = getString(R.string.done)
            positive.setOnClickListener {

                if (BiometricUtils.isHardwareSupported(mContext!!) && BiometricUtils.isSdkVersionSupported()) {
                    mBiometricManager!!.authenticate(object : BiometricCallback {
                        override fun onSdkVersionNotSupported() {
                            if (lastLocation != null) {
                                MarkOutDuty(date.text.toString(), reason.text.toString(), lastLocation!!.latitude.toString(), lastLocation!!.longitude.toString())
                                alertDialog.dismiss()
                            } else {
                                utilityofActivity!!.toast("Error fetching location please try again")
                            }
                            Toast.makeText(context, getString(R.string.biometric_error_sdk_not_supported), Toast.LENGTH_LONG).show()
                        }

                        override fun onBiometricAuthenticationNotSupported() {
                            if (lastLocation != null) {
                                MarkOutDuty(date.text.toString(), reason.text.toString(), lastLocation!!.latitude.toString(), lastLocation!!.longitude.toString())
                                alertDialog.dismiss()
                            } else {
                                utilityofActivity!!.toast("Error fetching location please try again")
                            }
                            Toast.makeText(context, getString(R.string.biometric_error_hardware_not_supported), Toast.LENGTH_LONG).show()
                        }

                        override fun onBiometricAuthenticationNotAvailable() {
                            Toast.makeText(context, getString(R.string.biometric_error_fingerprint_not_available), Toast.LENGTH_LONG).show()
                        }

                        override fun onBiometricAuthenticationPermissionNotGranted() {
                            Toast.makeText(context, getString(R.string.biometric_error_permission_not_granted), Toast.LENGTH_LONG).show()
                        }

                        override fun onBiometricAuthenticationInternalError(error: String?) {
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                        }

                        override fun onAuthenticationFailed() { //        Toast.makeText(getApplicationContext(), getString(R.string.biometric_failure), Toast.LENGTH_LONG).show();
                        }

                        override fun onAuthenticationCancelled() {
                            if (mBiometricManager != null)
                                mBiometricManager!!.cancelAuthentication()
                            Toast.makeText(context, getString(R.string.biometric_cancelled), Toast.LENGTH_LONG).show()
                        }

                        override fun onAuthenticationSuccessful() {
                            if (lastLocation != null) {
                                MarkOutDuty(date.text.toString(), reason.text.toString(), lastLocation!!.latitude.toString(), lastLocation!!.longitude.toString())
                                alertDialog.dismiss()
                            } else {
                                utilityofActivity!!.toast("Error fetching location please try again")
                            }
                        }

                        override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) { //        Toast.makeText(getApplicationContext(), helpString, Toast.LENGTH_LONG).show();
                        }

                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) { //        Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
                        }

                    })
                } else {
                    if (mBiometricManager != null)
                        mBiometricManager!!.cancelAuthentication()
                    if (lastLocation != null) {
                        MarkOutDuty(date.text.toString(), reason.text.toString(), lastLocation!!.latitude.toString(), lastLocation!!.longitude.toString())
                        alertDialog.dismiss()
                    } else {
                        utilityofActivity!!.toast("Error fetching location please try again")
                    }
                }

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


    private fun Checkin(latitude: String, longitude: String) {
        var requestUrl = Config.CHECK_IN
        requestUrl = requestUrl.replace("[0]", App.userId)
        requestUrl = requestUrl.replace("[1]", latitude)
        requestUrl = requestUrl.replace("[2]", longitude)

        console.log("CheckIn: " + requestUrl)
        app!!.sendNetworkRequest(requestUrl, Request.Method.GET, null, object : Interfaces.NetworkInterfaceListener {
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

                    if (response == "0") {
                        Toast.makeText(context!!, "Checkout Failure Please try again", Toast.LENGTH_LONG).show()
                    } else if (response == "-1") {
                        Toast.makeText(context!!, "Invalid User, Please contact admin", Toast.LENGTH_LONG).show()
                    } else {

                        utilityofActivity!!.toast("Checked In Successfully")
                        val jsonObject = JSONObject(response)
                        status.text = "You checked in at : " + jsonObject.get("start_time")
                        attendenceId = jsonObject.getString("attendence_id")
                        PrefernceFile.getInstance(mContext!!).setString("attendence_id", attendenceId!!)

                        checkout.visibility = View.VISIBLE
                        absent.visibility = View.GONE
                        checkin.visibility = View.GONE

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }

    private fun CheckOut(latitude: String, longitude: String) {
        var requestUrl = Config.CHECK_OUT
        try {
            requestUrl = requestUrl.replace("[0]", App.userId)
//        attendenceId=PrefernceFile.getInstance(mContext!!).getString("attendence_id")
            requestUrl = requestUrl.replace("[1]", PrefernceFile.getInstance(mContext!!).getString("attendence_id")!!)
            requestUrl = requestUrl.replace("[2]", latitude)
            requestUrl = requestUrl.replace("[3]", longitude)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        console.log("CheckOut: " + requestUrl)
        app!!.sendNetworkRequest(requestUrl, Request.Method.GET, null, object : Interfaces.NetworkInterfaceListener {
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
                    if (response == "0") {
                        Toast.makeText(context!!, "Checkout Failure Please try again", Toast.LENGTH_LONG).show()
                    } else if (response == "-1") {
                        Toast.makeText(context!!, "Invalid User, Please contact admin", Toast.LENGTH_LONG).show()
                    } else {
                        val jsonObject = JSONObject(response)


                        utilityofActivity!!.toast("Checked Out Successfully")
                        status.text = "You checked out at : " + jsonObject.get("end_time")
//                        checkout.background = ContextCompat.getDrawable(context!!, R.drawable.inactive_home_button)
                        checkout.visibility = View.GONE
                        absent.visibility = View.GONE
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }


    private fun MarkAbsent(leaveStatus: String, latitude: String, longitude: String) {
        var requestUrl = Config.MARK_ABSENT_USER
        try {
            requestUrl = requestUrl.replace("[0]", App.userId)
//        attendenceId=PrefernceFile.getInstance(mContext!!).getString("attendence_id")
            requestUrl = requestUrl.replace("[1]", latitude)
            requestUrl = requestUrl.replace("[2]", longitude)
            requestUrl = requestUrl.replace("[3]", leaveStatus)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        console.log("CheckOut: " + requestUrl)
        app!!.sendNetworkRequest(requestUrl, Request.Method.GET, null, object : Interfaces.NetworkInterfaceListener {
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

                        Toast.makeText(context!!, "Successfully Requested", Toast.LENGTH_LONG).show()

                        status.text = "Status: " + jsonObject.get("leave_status")
//                        checkin.background = ContextCompat.getDrawable(context!!, R.drawable.inactive_home_button)
                        checkin.visibility = View.GONE
//                        absent.background = ContextCompat.getDrawable(context!!, R.drawable.inactive_home_button)
                        absent.visibility = View.GONE
//                        absent.isEnabled = false
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }


    private fun MarkOutDuty(date: String, reason: String, latitude: String, longitude: String) {
        val requestUrl = Config.MarkOutDuty

        val params = HashMap<String, String>()
        params["user_id"] = App.userId
        params["latitude"] = latitude
        params["longitude"] = longitude
        params["comments"] = reason
        params["to_date"] = date

        console.log("MarkOutDuty: " + requestUrl)
        app!!.sendNetworkRequest(requestUrl, Request.Method.POST, params, object : Interfaces.NetworkInterfaceListener {
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
//                    if(response == "1")
//                    {

//                    }
//                    else if(response == "0")
                    if (response == "0") {
                        Toast.makeText(context!!, "Already On Duty", Toast.LENGTH_LONG).show()
                    } else if (response == "-1") {
                        Toast.makeText(context!!, "System Error, Please contact admin", Toast.LENGTH_LONG).show()
                    } else {
                        val jsonObject = JSONObject(response)

                        Toast.makeText(context!!, "Successfully Requested", Toast.LENGTH_LONG).show()
                        status.text = "Status: " + jsonObject.get("leave_status")
//                        checkin.background = ContextCompat.getDrawable(context!!, R.drawable.inactive_home_button)
                        checkin.visibility = View.GONE
//                        absent.background = ContextCompat.getDrawable(context!!, R.drawable.inactive_home_button)
                        absent.visibility = View.GONE
//                        absent.isEnabled = false
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
                            outDuty.visibility = View.VISIBLE
                            status.text = ""
//                            checkin.isEnabled = true
//                            checkin.background = ContextCompat.getDrawable(context!!, R.drawable.home_button)
                            checkout.visibility = View.GONE
                        } else if ((jsonObject.getString("start_time").isNotEmpty() && jsonObject.getString("end_time") == "null")) {
                            checkin.visibility = View.GONE
                            absent.visibility = View.GONE
                            outDuty.visibility = View.GONE
//                            checkout.isEnabled = true
//                            checkout.background = ContextCompat.getDrawable(context!!, R.drawable.home_button)
                            checkout.visibility = View.VISIBLE
                        } else if ((jsonObject.getString("start_time") == "-1" && jsonObject.getString("end_time") == "-1")) {
                            absent.visibility = View.VISIBLE
                            outDuty.visibility = View.VISIBLE
                            checkin.visibility = View.GONE
                            checkout.visibility = View.GONE
//                            absent.background = ContextCompat.getDrawable(context!!, R.drawable.inactive_home_button)
                            absent.isEnabled = false
                        } else {
                            checkin.visibility = View.GONE
                            absent.visibility = View.GONE
                            outDuty.visibility = View.GONE
                            checkout.visibility = View.VISIBLE
//                            checkout.background = ContextCompat.getDrawable(context!!, R.drawable.inactive_home_button)
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
