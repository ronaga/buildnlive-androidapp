package buildnlive.com.buildlive.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.fragments.PaymentFragment;
import buildnlive.com.buildlive.utils.UtilityofActivity;
import io.realm.Realm;

public class AddSitePayment extends AppCompatActivity {
    private App app;
    private Realm realm;
    private Fragment fragment;
    private TextView edit, view;
    private ImageButton back;

    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity = this;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        console.log("Destroyed");
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_site_payments);

        app = (App) getApplication();
        realm = Realm.getDefaultInstance();
        fragment = PaymentFragment.newInstance();

        utilityofActivity = new UtilityofActivity(appCompatActivity);
        utilityofActivity.configureToolbar(appCompatActivity);


        TextView toolbar_title = findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle = findViewById(R.id.toolbar_subtitle);
        toolbar_subtitle.setText(App.projectName);
        toolbar_title.setText(R.string.add_payment);

        changeScreen();
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
                .replace(R.id.site_content, fragment)
                .commit();
    }

}
