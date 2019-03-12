package buildnlive.com.buildlive.utils

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
//import com.webpulse.crm.R


class Helper private constructor() {

    fun moveFragment(fragment: Fragment?, backStack: String?, containerId: Int, activity: AppCompatActivity) {

        if (fragment != null) {

            val fragmentManager = activity.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(containerId, fragment)
            if (backStack != null)
                fragmentTransaction.addToBackStack(backStack)
            fragmentTransaction.commit()
        }
    }

    companion object {

        val instance = Helper()
    }


}

