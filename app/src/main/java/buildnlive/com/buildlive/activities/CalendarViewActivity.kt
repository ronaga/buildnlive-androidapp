package buildnlive.com.buildlive.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import buildnlive.com.buildlive.App
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.Server.HTTPResponseError
import buildnlive.com.buildlive.Server.Request.AttendanceRequest
import buildnlive.com.buildlive.Server.Response.AttendanceResponse
import buildnlive.com.buildlive.Server.RetrofitApiAuthSingleTon
import buildnlive.com.buildlive.Server.TCApi
import buildnlive.com.buildlive.adapters.EmployeeAttendanceAdapter
import buildnlive.com.buildlive.console
import buildnlive.com.buildlive.utils.PrefernceFile.Companion.getInstance
import buildnlive.com.buildlive.utils.UtilityofActivity
import com.applikeysolutions.cosmocalendar.selection.OnDaySelectedListener
import com.applikeysolutions.cosmocalendar.selection.SingleSelectionManager
import com.applikeysolutions.cosmocalendar.settings.lists.connected_days.ConnectedDays
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_calendar_view.*
import kotlinx.android.synthetic.main.alert_dialog_calendar_view.*
import retrofit2.Response
import java.util.*

class CalendarViewActivity : AppCompatActivity() {
    private var utilityofActivity: UtilityofActivity? = null
    private val appCompatActivity: AppCompatActivity = this

    private var context: Context? = null
    private var tcApi: TCApi? = null
    private var disposable = CompositeDisposable()
    private var presentConnectedDays: ConnectedDays? = null
    private var absentConnectedDays: ConnectedDays? = null
    private var outdutyConnectedDays: ConnectedDays? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        context = this
        tcApi = RetrofitApiAuthSingleTon.createService(TCApi::class.java)


        utilityofActivity = UtilityofActivity(appCompatActivity)
        utilityofActivity!!.configureToolbar(appCompatActivity)


        val toolbar_title = findViewById<TextView>(R.id.toolbar_title)
        val toolbar_subtitle = findViewById<TextView>(R.id.toolbar_subtitle)
        toolbar_subtitle.text = App.projectName
        toolbar_title.setText(R.string.attendance)

/*
        val calendar = Calendar.getInstance()
        val days = TreeSet<Long>()
        calendar.set(2020, 6, 6)
        days.add(calendar.timeInMillis)
        calendar.set(2020, 6, 7)
        days.add(calendar.timeInMillis)
        calendar.set(2020, 6, 8)
        days.add(calendar.timeInMillis)

        val textColor = Color.parseColor("#ff0000");
        val selectedTextColor = Color.parseColor("#ff4000");
        val disabledTextColor = Color.parseColor("#ff8000");
        val connectedDays = ConnectedDays(days, textColor, selectedTextColor, disabledTextColor)

        calendarView.addConnectedDays(connectedDays)
*/

        /* val present = TreeSet<Long>()
         calendar.set(2020, 6, 9)
         present.add(calendar.timeInMillis)
         calendar.set(2020, 6, 10)
         present.add(calendar.timeInMillis)
         calendar.set(2020, 6, 15)
         present.add(calendar.timeInMillis)

         val textColor1 = Color.parseColor("#00ff00");
         val selectedTextColor1 = Color.parseColor("#ff4000");
         val disabledTextColor1 = Color.parseColor("#ff8000");
         val connectedDays1 = ConnectedDays(present, textColor1, selectedTextColor1, disabledTextColor1)

         calendarView.addConnectedDays(connectedDays1)
 */
        refresh()
    }

    private fun refresh() {

        disposable.add(tcApi!!.callSendAttendance(AttendanceRequest(App.userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { utilityofActivity!!.showProgressDialog() }
                .doOnError { utilityofActivity!!.dismissProgressDialog() }
                .doOnSuccess { utilityofActivity!!.dismissProgressDialog() }
                .subscribeWith(object : DisposableSingleObserver<Response<AttendanceResponse>>() {
                    override fun onSuccess(t: Response<AttendanceResponse>) {
                        if (t.isSuccessful) {
                            val response = t.body()
                            if (response!!.isSuccess) {
                                try {
                                    val data = response.data
                                    val calendar = Calendar.getInstance()
                                    val presentDays = TreeSet<Long>()
                                    val absentDays = TreeSet<Long>()
                                    val outdutyDays = TreeSet<Long>()

                                    var count = 0
                                    if (data != null) {
                                        for (i in data) {
                                            calendar.set(i.year.toInt(), i.month.toInt() - 1, i.day.toInt())
                                            count++
                                            console.log("FOR LOOP CASE: " + count)
                                            when (i.status) {
                                                "present" -> {
                                                    presentDays.add(calendar.timeInMillis)
                                                    console.log("Present Case" + i.status)
                                                }
                                                "absent" -> {
                                                    absentDays.add(calendar.timeInMillis)
                                                    console.log("Absent Case" + i.status)
                                                }
                                                "leave" -> {
                                                    absentDays.add(calendar.timeInMillis)
                                                    console.log("Leave Case" + i.status)
                                                }
                                                "outduty" -> {
                                                    outdutyDays.add(calendar.timeInMillis)
                                                    console.log("Outduty Case" + i.status)
                                                }
                                            }
                                        }

                                        val textColor1 = Color.parseColor("#00ff00")
                                        val selectedTextColor1 = Color.parseColor("#ff4000")
                                        val disabledTextColor1 = Color.parseColor("#ff8000")
                                        presentConnectedDays = ConnectedDays(presentDays, textColor1, selectedTextColor1, disabledTextColor1)

                                        if (!presentDays.isNullOrEmpty()) {
                                            calendarView.addConnectedDays(presentConnectedDays)
                                        } else
                                            console.log("PRESENT EMPTY")

                                        val textColor2 = Color.parseColor("#ff0000")
                                        val selectedTextColor2 = Color.parseColor("#ff4000")
                                        val disabledTextColor2 = Color.parseColor("#ff8000")
                                        absentConnectedDays = ConnectedDays(absentDays, textColor2, selectedTextColor2, disabledTextColor2)


                                        if (!absentDays.isNullOrEmpty())
                                            calendarView.addConnectedDays(absentConnectedDays)
                                        else
                                            console.log("ABSENT EMPTY")

                                        val textColor3 = Color.parseColor("#cccc00")
                                        val selectedTextColor3 = Color.parseColor("#ff4000")
                                        val disabledTextColor3 = Color.parseColor("#ff8000")
                                        outdutyConnectedDays = ConnectedDays(outdutyDays, textColor3, selectedTextColor3, disabledTextColor3)


                                        if (!outdutyDays.isNullOrEmpty())
                                            calendarView.addConnectedDays(outdutyConnectedDays)
                                        else
                                            console.log("OUTDUTY EMPTY")

                                        calendarView.selectionManager = SingleSelectionManager(object : OnDaySelectedListener {
                                            override fun onDaySelected() {
                                                for (j in data) {
                                                    if (calendarView.selectedDates[0].get(Calendar.DAY_OF_MONTH) == j.day.toInt() &&
                                                            calendarView.selectedDates[0].get(Calendar.MONTH) == (j.month.toInt() - 1) &&
                                                            calendarView.selectedDates[0].get(Calendar.YEAR) == j.year.toInt()
                                                    ) {
                                                        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                                                        val dialogView = inflater.inflate(R.layout.alert_dialog_calendar_view, null)
                                                        val dialogBuilder = AlertDialog.Builder(context!!, R.style.PinDialog)

                                                        val outDutyDialog = dialogBuilder.setCancelable(false).setView(dialogView).create()
                                                        outDutyDialog.show()

                                                        val title = dialogView.findViewById<TextView>(R.id.alert_title)
                                                        val checkin = dialogView.findViewById<TextView>(R.id.checkinTime)
                                                        val checkout = dialogView.findViewById<TextView>(R.id.checkoutTime)

                                                        if (j.startTime != null) {
                                                            checkin.text = j.startTime
                                                        }
                                                        if (j.endTime != null) {
                                                            checkout.text = j.endTime
                                                        }

                                                        title.text = "Details"

                                                        val negative = dialogView.findViewById<Button>(R.id.negative)
                                                        negative.setOnClickListener { outDutyDialog.dismiss() }

                                                    } else {
                                                        console.log(calendarView.selectedDates[0].get(Calendar.DAY_OF_MONTH).toString() + ":" + calendarView.selectedDates[0].get(Calendar.MONTH).toString() + ":" + calendarView.selectedDates[0].get(Calendar.YEAR).toString())
                                                        console.log(j.day + ":" + j.month + ":" + j.year)
                                                    }
                                                }
                                            }
                                        })

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

    override fun onDestroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onDestroy()
    }
}
