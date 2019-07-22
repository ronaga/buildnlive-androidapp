package buildnlive.com.buildlive.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.BuildConfig;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.ActivityImagesAdapter;
import buildnlive.com.buildlive.adapters.DailyWorkAdapter;
import buildnlive.com.buildlive.adapters.StructureSpinAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Packet;
import buildnlive.com.buildlive.elements.Structure;
import buildnlive.com.buildlive.elements.Work;
import buildnlive.com.buildlive.utils.AdvancedRecyclerView;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;
import io.realm.Realm;
import io.realm.RealmResults;

public class Planning extends AppCompatActivity {
    private App app;
    private TextView edit, filter, view, reset;
    private Fragment fragment;
    private String status_text, category_text, results;
    private RecyclerView items;
    private ProgressBar progress;
    private TextView hider, no_content;
    private DailyWorkAdapter adapter;
    private static RealmResults<Work> works;
    private static ArrayList<Work> workslist = new ArrayList<>();
    private Realm realm;
    private boolean LOADING = true;
    public static final int QUALITY = 10;
    private ImageButton back;
    private Context context;
    private StructureSpinAdapter structureSpinAdapter;
    private ArrayList<Structure> structureList = new ArrayList<>();
    private Spinner structureSpinner;
    private String structureId;

    public static final int REQUEST_GALLERY_IMAGE = 7191;

    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity = this;

    @Override
    protected void onStart() {
        super.onStart();
        setStructureSpinner();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_planning);
        context = this;
        utilityofActivity = new UtilityofActivity(appCompatActivity);
        utilityofActivity.configureToolbar(appCompatActivity);

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle = findViewById(R.id.toolbar_subtitle);
        toolbar_subtitle.setText(App.projectName);

        app = (App) getApplication();


        FloatingActionButton fab = appCompatActivity.findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreatePlan.class);
                startActivity(intent);
            }
        });


        toolbar_title.setText("Work Planning");
        items = findViewById(R.id.items);
        progress = findViewById(R.id.progress);
        hider = findViewById(R.id.hider);
        no_content = findViewById(R.id.no_content);

        if (LOADING) {
            progress.setVisibility(View.VISIBLE);
            hider.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
            hider.setVisibility(View.GONE);
        }


        structureSpinner = findViewById(R.id.structure);
        structureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                structureId = structureSpinAdapter.getStructureId(i);
                loadWorks(structureId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        structureSpinAdapter = new StructureSpinAdapter(context, R.layout.custom_spinner, structureList);
        structureSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        structureSpinner.setAdapter(structureSpinAdapter);



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

    private void loadWorks(String structureId) {
        workslist.clear();
        String url = Config.GET_PLANNED_LIST;
        url = url.replace("[0]", App.userId);
        url = url.replace("[1]", App.projectId);
        url = url.replace("[2]", structureId);
        console.log("URL:" + url);
        app.sendNetworkRequest(url, 0, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                utilityofActivity.dismissProgressDialog();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                workslist.clear();
                utilityofActivity.dismissProgressDialog();
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.log("Response:" + response);
                try {

/*
                    Type approvalListType = new TypeToken<ArrayList<Work>>() {
                    }.getType();
                    workslist = new Gson().fromJson(response, approvalListType);
*/


                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject par = array.getJSONObject(i);
                        JSONObject sch = par.getJSONObject("work_schedule");
                        final Work work = new Work().parseFromJSONPlan(sch.getJSONObject("work_details"), par.getString("planned_id"), par.getString("planned_detail_id"),
                                sch.getString("work_duration"), sch.getString("qty"), sch.getString("schedule_start_date"), sch.getString("schedule_finish_date")
                                , sch.getString("current_status"), sch.getString("qty_completed"), sch.getString("percent_compl"), par.getString("assign_qty"), "Plan",sch.getString("status_color"));
                        workslist.add(work);

//                        , par.getString("completed_activities"), par.getString("total_activities")
                        //                        realm.executeTransaction(new Realm.Transaction() {
//                            @Override
//                            public void execute(Realm realm) {
//                                realm.copyToRealmOrUpdate(work);
//                            }
//                        });
                        console.log("Plan" + work.getPlanned_id());
                    }
                    if (workslist.isEmpty()) {
                        no_content.setVisibility(View.VISIBLE);
                    } else no_content.setVisibility(View.GONE);
                    adapter = new DailyWorkAdapter(context, workslist, "Plan", new DailyWorkAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos, View view) {
                            Intent intent = new Intent(context, DailyWorkProgressActivities.class);
                            intent.putExtra("id", workslist.get(pos).getWorkListId());
                            startActivity(intent);
                        }
                    }, new DailyWorkAdapter.OnButtonClickListener() {
                        @Override
                        public void onButtonClick(int pos, View view) {
                            menuUpdate(workslist.get(pos));
                        }
                    });
                    items.setLayoutManager(new LinearLayoutManager(context));
                    items.setAdapter(adapter);

                } catch (JSONException e) {

                }
            }
        });
    }

    public static final int REQUEST_CAPTURE_IMAGE = 7190;
    private ArrayList<Packet> images;
    private ActivityImagesAdapter imagesAdapter;
    private String imagePath;


    private void menuUpdate(final Work activity) {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_activity, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.PinDialog);
        final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
        alertDialog.show();
        final TextView disable = dialogView.findViewById(R.id.disableView);
        final TextView max = dialogView.findViewById(R.id.max);
        max.setText("Assigned Quantity: " + activity.getAssign_qty());
        final ProgressBar progress = dialogView.findViewById(R.id.progress);
        final EditText message = dialogView.findViewById(R.id.message);
        final EditText quantity = dialogView.findViewById(R.id.quantity);
        final TextView completed = dialogView.findViewById(R.id.completed);
        completed.setVisibility(View.GONE);
        final AdvancedRecyclerView list = dialogView.findViewById(R.id.images);
        images = new ArrayList<>();
        images.add(new Packet());
        imagesAdapter = new ActivityImagesAdapter(context, images, new ActivityImagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Packet packet, int pos, View view) {
                if (pos == 0) {

                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.image_chooser, null);
                    androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.PinDialog);
                    final androidx.appcompat.app.AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
                    alertDialog.show();
                    final TextView gallery = dialogView.findViewById(R.id.gallery);
                    final TextView camera = dialogView.findViewById(R.id.camera);

                    gallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            Intent pictureIntent = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pictureIntent, REQUEST_GALLERY_IMAGE);

                        }
                    });

                    camera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (pictureIntent.resolveActivity(getPackageManager()) != null) {

                                File photoFile = null;
                                try {
                                    photoFile = createImageFile();
                                } catch (IOException ex) {
                                }
                                if (photoFile != null) {
                                    Uri photoURI = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    imagePath = photoFile.getAbsolutePath();
                                    startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
                                }
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

                } else {
                    images.remove(pos);
                    imagesAdapter.notifyItemRemoved(pos);
                    imagesAdapter.notifyDataSetChanged();
                }
            }
        });
        list.setAdapter(imagesAdapter);
        list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        list.setmMaxHeight(400);
//        completed.setText("Completed: " + activity.getQty_completed() + " " + activity.getUnits());
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
                        Toast.makeText(context, "Fill data properly!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(context, "Fill data properly!", Toast.LENGTH_SHORT).show();
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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CAPTURE_IMAGE) {
//            if (resultCode == android.app.Activity.RESULT_OK) {
//                Packet packet = images.remove(0);
//                packet.setName(imagePath);
//                images.add(0, new Packet());
//                images.add(packet);
//                imagesAdapter.notifyDataSetChanged();
//            } else if (resultCode == android.app.Activity.RESULT_CANCELED) {
//                console.log("Canceled");
//            }
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            if (resultCode == android.app.Activity.RESULT_OK) {
                Packet packet = images.remove(0);
                packet.setName(imagePath);
//                Uri uri=data.getData();
//                packet.setName(getRealPathFromURI(uri));
                console.log("Image Path " + packet.getName() + "EXTRAS " + packet.getExtra());
                images.add(0, new Packet());
                images.add(packet);
                imagesAdapter.notifyDataSetChanged();
            } else if (resultCode == android.app.Activity.RESULT_CANCELED) {
                console.log("Canceled");
            }
        } else if (requestCode == REQUEST_GALLERY_IMAGE) {
            Packet packet = images.remove(0);
//            packet.setName(imagePath);
            Uri uri = data.getData();
            packet.setName(getRealPathFromURI(uri));
            console.log("Image Path " + packet.getName() + "EXTRAS " + packet.getExtra());
            images.add(0, new Packet());
            images.add(packet);
            imagesAdapter.notifyDataSetChanged();
        }
    }

    // And to convert the image URI to the direct file system path of the image file
    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            results = cursor.getString(column_index);
        }
//                managedQuery( );
        cursor.moveToFirst();
        cursor.close();
        return results;
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }

    private void submit(Work activity, String message, String quantity, ArrayList<Packet> images, final AlertDialog alertDialog) throws JSONException {
        float q = Float.parseFloat(quantity);
//        float c = Float.parseFloat(activity.getQty_completed());
        float qo = Float.parseFloat(activity.getAssign_qty());
//        console.log("entry completed quanity "+q+" "+c+" "+qo+" " + (qo-c));
//        if (q <= (qo)) {
        HashMap<String, String> params = new HashMap<>();
        params.put("work_update", new JSONObject()
//                    .put("activity_list_id", activity.getActivityListId())
                        .put("planner_detail_id", activity.getPlanned_detail_id())
//                    .put("type", "work")
                        .put("comment", message)
                        .put("completed_qty", quantity)
//                    .put("units", activity.getUnits())
                        .put("user_id", App.userId)
                        .put("project_id", App.projectId).toString()
        );
//        work_update: planner_detail_id, user_id, completed_qty, comment
        console.log("Work" + params.toString());
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
            params.put("images", array.toString());
        }

        app.sendNetworkRequest(Config.UPDATE_PLANNED_WORK, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                utilityofActivity.dismissProgressDialog();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                utilityofActivity.dismissProgressDialog();
                if (response.equals("1")) {
                    Toast.makeText(context, "Status Updated", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    loadWorks(structureId);
//                    finish();
                }
            }
        });
    }


    private void setStructureSpinner() {

        String requestURl = Config.SendStructures;
        requestURl = requestURl.replace("[0]", App.userId);
        requestURl = requestURl.replace("[1]", App.projectId);

        structureList.clear();

        console.log(requestURl);
        app.sendNetworkRequest(requestURl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                utilityofActivity.showProgressDialog();
                console.error("Network request failed with error :" + error);
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                utilityofActivity.dismissProgressDialog();

                Type vendorType = new TypeToken<ArrayList<Structure>>() {
                }.getType();
                structureList = new Gson().fromJson(response, vendorType);

                structureSpinAdapter.notifyDataSetChanged();

                structureSpinAdapter = new StructureSpinAdapter(context, R.layout.custom_spinner, structureList);
                structureSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                structureSpinner.setAdapter(structureSpinAdapter);


            }
        });
    }


}
