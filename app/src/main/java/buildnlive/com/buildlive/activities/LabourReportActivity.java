package buildnlive.com.buildlive.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.fragments.ManageLabourFragment;
import buildnlive.com.buildlive.fragments.ViewLabourReportFragment;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class LabourReportActivity extends AppCompatActivity {
    private App app;
    private TextView edit, view;
    private Fragment fragment;

    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity=this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_activity);

        utilityofActivity=new UtilityofActivity(appCompatActivity);
        utilityofActivity.configureToolbar(appCompatActivity);

        TextView toolbar_title=findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle=findViewById(R.id.toolbar_subtitle);
        toolbar_subtitle.setText(App.projectName);
        toolbar_title.setText("Manage Labour");



        app = (App) getApplication();
        edit = findViewById(R.id.edit);
        view = findViewById(R.id.view);
//
        fragment = ManageLabourFragment.newInstance();
        changeScreen();
        edit = findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEdit();
                disableView();
                fragment = ManageLabourFragment.newInstance();
                changeScreen();
            }
        });
        view = findViewById(R.id.view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableView();
                disableEdit();
                fragment = ViewLabourReportFragment.newInstance();
                changeScreen();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void changeScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
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
