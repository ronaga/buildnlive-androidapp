package buildnlive.com.buildlive.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.fragments.DailyWorkProgressFragment;
import buildnlive.com.buildlive.fragments.SendRequestFragment;
import buildnlive.com.buildlive.fragments.ViewRequestsFragment;

public class WorkProgress extends AppCompatActivity {
    private App app;
    private TextView edit, filter,view;
    private Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_progress);
        app = (App) getApplication();
        edit = findViewById(R.id.edit);
        view = findViewById(R.id.view);
//        filter = findViewById(R.id.filter);
        fragment = DailyWorkProgressFragment.newInstance(app);
        changeScreen();
//        filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LayoutInflater inflater = getLayoutInflater();
//                View dialogView = inflater.inflate(R.layout.alert_filter, null);
//                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getApplicationContext(), R.style.PinDialog);
//                final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
//                alertDialog.show();
//            }
//        });

//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                enableEdit();
//                disableView();
//                fragment = DailyWorkProgressFragment.newInstance(app);
//                changeScreen();
//            }
//        });

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                enableView();
//                disableEdit();
//                fragment = DailyWorkProgressFragment.newInstance(app);
//                changeScreen();
//            }
//        });
    }

    private void changeScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.attendance_content, fragment)
                .commit();
    }

    private void disableView() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.round_left, null));
        } else {
            view.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_left));
        }
        view.setTextColor(getResources().getColor(R.color.color2));
    }

    private void enableView() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.round_left_selected, null));
        } else {
            view.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_left_selected));
        }
        view.setTextColor(getResources().getColor(R.color.white));
    }

    private void disableEdit() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            edit.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.round_right, null));
        } else {
            edit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_right));
        }
        edit.setTextColor(getResources().getColor(R.color.color2));
    }

    private void enableEdit() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            edit.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.round_right_selected, null));
        } else {
            edit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_right_selected));
        }
        edit.setTextColor(getResources().getColor(R.color.white));
    }
}
