package buildnlive.com.buildlive.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.NotificationService;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Notification;
import buildnlive.com.buildlive.fragments.HomeFragment;
import buildnlive.com.buildlive.fragments.PlansFragment;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

import static buildnlive.com.buildlive.activities.LoginActivity.PREF_KEY_EMAIL;
import static buildnlive.com.buildlive.activities.LoginActivity.PREF_KEY_NAME;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageButton imageButton;
    private ImageView imageView;
    private ColorGenerator generator = ColorGenerator.MATERIAL;
    private Fragment fragment;
    private SharedPreferences pref;
    public static final String PREF_KEY_LOGGED_IN = "is_logged_in";
    private App app;
    private AlarmManager alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Realm realm =Realm.getDefaultInstance();

        startService(new Intent(this, NotificationService.class));
        Intent intent = new Intent(this, NotificationService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);

       alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Start service every 4 hour

        alarm = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 14400*1000,
                14400*1000, pintent);
// 4 hours in miliSec 14400*1000

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_home);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        realm.where(Notification.class).findAllAsync().addChangeListener(new RealmChangeListener<RealmResults<Notification>>() {
            @Override
            public void onChange(RealmResults<Notification> notifications) {
                imageButton.setImageResource(R.drawable.ic_notifications_black_24dp);
            }
        });

        app = (App) getApplication();
        pref = app.getPref();
        fragment = HomeFragment.newInstance(app);
        if (!pref.getBoolean(PREF_KEY_LOGGED_IN, false)) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        imageButton = findViewById(R.id.notification);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButton.setImageResource(R.drawable.ic_notifications_none_black_24dp);
                startActivity(new Intent(getApplicationContext(),NotificationActivity2.class));

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(listener);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View navView = navigationView.inflateHeaderView(R.layout.nav_header_home);
        imageView = navView.findViewById(R.id.imageView);
        TextView name = navView.findViewById(R.id.name);
        TextView email = navView.findViewById(R.id.email);
        String n = pref.getString(PREF_KEY_NAME, "Dummy");
        String e = pref.getString(PREF_KEY_EMAIL, "abc@xyz.com");
        name.setText(n);
        email.setText(e);
        imageView.setImageDrawable(TextDrawable.builder().buildRound("" + n.charAt(0), generator.getColor(e)));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        changeFragment();
        getStoragePermission();
    }

    private void getStoragePermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 7190);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment = HomeFragment.newInstance(app);
                break;
            case R.id.nav_plans:
                fragment = PlansFragment.newInstance((App) getApplication());
                break;
            case R.id.nav_logout:
                logout();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        pref.edit().clear().commit();
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }

    private DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            if (fragment != null) {
                changeFragment();
                fragment = null;
            }
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    private void changeFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }
}
