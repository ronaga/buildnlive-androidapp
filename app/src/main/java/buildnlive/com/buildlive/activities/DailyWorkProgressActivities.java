package buildnlive.com.buildlive.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.BuildConfig;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.ActivityImagesAdapter;
import buildnlive.com.buildlive.adapters.DailyWorkActivityAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Activity;
import buildnlive.com.buildlive.elements.Packet;
import buildnlive.com.buildlive.elements.Work;
import buildnlive.com.buildlive.fragments.DailyWorkProgressFragment;
import buildnlive.com.buildlive.utils.AdvancedRecyclerView;
import buildnlive.com.buildlive.utils.Config;
import io.realm.Realm;

public class DailyWorkProgressActivities extends AppCompatActivity {
    private App app;
    private RecyclerView items;
    private DailyWorkActivityAdapter adapter;
    private List<Activity> activities;
    private String id;
    public static final int QUALITY = 10;
    private TextView no_content;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_work_activities);
        app = (App) getApplication();
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        items = findViewById(R.id.items);
        no_content=findViewById(R.id.no_content);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        activities = new ArrayList<>();
        adapter = new DailyWorkActivityAdapter(getApplicationContext(), activities, new DailyWorkActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {

                menuUpdate(activities.get(pos));
            }
        });
        items.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        items.setAdapter(adapter);
//        if(adapter.getItemCount()==0)
//        {
//            Toast.makeText(this,"Nothing to Display",Toast.LENGTH_LONG).show();
//        }
        fetchActivities(id);
    }

    public static final int REQUEST_CAPTURE_IMAGE = 7190;
    private ArrayList<Packet> images;
    private ActivityImagesAdapter imagesAdapter;
    private String imagePath;

    private void menuUpdate(final Activity activity) {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_activity, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.PinDialog);
        final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
        alertDialog.show();
        final TextView disable = dialogView.findViewById(R.id.disableView);
        final TextView max = dialogView.findViewById(R.id.max);
        max.setText("Total: " + activity.getQuantity() + " " + activity.getUnits());
        final ProgressBar progress = dialogView.findViewById(R.id.progress);
        final EditText message = dialogView.findViewById(R.id.message);
        final EditText quantity = dialogView.findViewById(R.id.quantity);
        final TextView completed = dialogView.findViewById(R.id.completed);
        final AdvancedRecyclerView list = dialogView.findViewById(R.id.images);
        images = new ArrayList<>();
        images.add(new Packet());
        imagesAdapter = new ActivityImagesAdapter(getApplicationContext(), images, new ActivityImagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Packet packet, int pos, View view) {
                if (pos == 0) {
                    Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (pictureIntent.resolveActivity(getPackageManager()) != null) {

                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                        }
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            imagePath = photoFile.getAbsolutePath();
                            startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
                        }
                    }
                }
            }
        });
        list.setAdapter(imagesAdapter);
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        list.setmMaxHeight(400);
        completed.setText("Completed: " + activity.getQty_completed() + " " + activity.getUnits());
        TextView title = dialogView.findViewById(R.id.alert_title);
        title.setText("Activity Status");
        final TextView alert_message = dialogView.findViewById(R.id.alert_message);
        alert_message.setText("Please fill work progress.");
        Button positive = dialogView.findViewById(R.id.positive);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (message.getText().toString() != null || quantity.getText().toString() != null)
                        submit(activity, message.getText().toString(), quantity.getText().toString(), images, alertDialog);
                    else
                        Toast.makeText(getApplicationContext(), "Fill data properly!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Fill data properly!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button negative = dialogView.findViewById(R.id.negative);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            if (resultCode == android.app.Activity.RESULT_OK) {
                Packet packet = images.remove(0);
                packet.setName(imagePath);
                images.add(0, new Packet());
                images.add(packet);
                imagesAdapter.notifyDataSetChanged();
            } else if (resultCode == android.app.Activity.RESULT_CANCELED) {
                console.log("Canceled");
            }
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }

    private void submit(Activity activity, String message, String quantity, ArrayList<Packet> images, final AlertDialog alertDialog) throws JSONException {
        float q = Float.parseFloat(quantity);
        float c = Float.parseFloat(activity.getQty_completed());
        float qo = Float.parseFloat(activity.getQuantity());
        if (q <= (qo - c)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("work_update", new JSONObject()
                    .put("activity_list_id", activity.getActivityListId())
                    .put("type", "activity")
                    .put("project_comment", message)
                    .put("quantity_done", quantity)
                    .put("units", activity.getUnits())
                    .put("user_id", App.userId)
                    .put("project_id", App.projectId)
                    .put("percentage_work", q / qo).toString());
            JSONArray array = new JSONArray();
            for (Packet p : images) {
                if (p.getName() != null) {
                    Bitmap bm = BitmapFactory.decodeFile(p.getName());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, QUALITY, baos);
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    array.put(encodedImage);
                }
            }
            params.put("images",array.toString());

            app.sendNetworkRequest(Config.REQ_DAILY_WORK_ACTIVITY_UPDATE, 1, params, new Interfaces.NetworkInterfaceListener() {
                @Override
                public void onNetworkRequestStart() {

                }

                @Override
                public void onNetworkRequestError(String error) {

                }

                @Override
                public void onNetworkRequestComplete(String response) {
                    if (response.equals("1")) {
                        Toast.makeText(getApplicationContext(), "Status Updated", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        finish();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Put right quantity", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchActivities(String id) {
        String url = Config.REQ_DAILY_WORK_ACTIVITY;
        url = url.replace("[0]", App.userId);
        url = url.replace("[1]", id);
        console.log(url);
        app.sendNetworkRequest(url, 0, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
            }

            @Override
            public void onNetworkRequestError(String error) {

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log("Response:" + response);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject par = array.getJSONObject(i);
                        JSONObject sch = par.getJSONObject("work_schedule");
                        final Activity activity = new Activity().parseFromJSON(sch.getJSONObject("work_details"), par.getString("activity_list_id"),sch.getString("work_duration"),
                                 sch.getString("qty"), sch.getString("units"),
                                sch.getString("schedule_start_date"), sch.getString("schedule_finish_date"),
                                sch.getString("current_status"), sch.getString("qty_completed"));
                        activities.add(activity);

                        console.log(""+activities.get(i));
                    }
                    if(activities.isEmpty())
                    {
                          no_content.setVisibility(View.VISIBLE);
//                        LayoutInflater inflater = getLayoutInflater();
//                        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
//                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getApplicationContext(), R.style.PinDialog);
//                        final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
//                        alertDialog.show();
//                        final EditText alert_message=dialogView.findViewById(R.id.alert_message);
//                        final Button close=dialogView.findViewById(R.id.negative);
//                        final Button approve=dialogView.findViewById(R.id.positive);
//
//                        approve.setVisibility(View.GONE);
//                        close.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                alertDialog.dismiss();
//                            }
//                        });
//
//                        alert_message.setText("Nothing to Show");
//                        Toast.makeText(getApplicationContext(),"Nothing to Display",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}