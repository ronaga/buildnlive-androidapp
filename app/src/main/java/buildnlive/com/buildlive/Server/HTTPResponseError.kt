package buildnlive.com.buildlive.Server

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import buildnlive.com.buildlive.R
import kotlinx.android.synthetic.main.layout_response500.*

class HTTPResponseError : AppCompatActivity() {
    var errorCode: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_response500)

        if (intent != null) {
            errorCode = intent.getIntExtra("errorCode", 0)
        }

        when (errorCode) {
            400->{
                errorTitle.text = String.format(getString(R.string.http_error),errorCode.toString(),"Bad Request")
            }
            401->{
                errorTitle.text = String.format(getString(R.string.http_error),errorCode.toString(),"Unauthorized")
            }
            403->{
                errorTitle.text = String.format(getString(R.string.http_error),errorCode.toString(),"Forbidden")
            }
            404 -> {
               errorTitle.text = String.format(getString(R.string.http_error),errorCode.toString(),"Page Not Found")
            }
            500 -> {
                errorTitle.text = String.format(getString(R.string.http_error),errorCode.toString(),"Internal Server Error")
            }
            502 -> {
                errorTitle.text = String.format(getString(R.string.http_error),errorCode.toString(),"Bad Gateway")
            }
            503 -> {
                errorTitle.text = String.format(getString(R.string.http_error),errorCode.toString(),"Service Unavailable")
            }
            504 -> {
                errorTitle.text = String.format(getString(R.string.http_error),errorCode.toString(),"Gateway Timeout")
            }
            else ->
                errorTitle.text = getString(R.string.unknown_error)

        }
        connectionError.setAnimation("servererror.json")
        connectionError.playAnimation()
    }
}