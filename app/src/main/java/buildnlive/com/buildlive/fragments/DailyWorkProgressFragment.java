package buildnlive.com.buildlive.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

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
import buildnlive.com.buildlive.activities.DailyWorkProgressActivities;
import buildnlive.com.buildlive.adapters.ActivityImagesAdapter;
import buildnlive.com.buildlive.adapters.AttendanceAdapter;
import buildnlive.com.buildlive.adapters.DailyWorkAdapter;
import buildnlive.com.buildlive.adapters.ListAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Activity;
import buildnlive.com.buildlive.elements.Packet;
import buildnlive.com.buildlive.elements.Work;
import buildnlive.com.buildlive.elements.Worker;
import buildnlive.com.buildlive.utils.AdvancedRecyclerView;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.Utils;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.annotations.Index;

public class DailyWorkProgressFragment extends Fragment {
    private RecyclerView items;
    private ProgressBar progress;
    private TextView hider,filter,reset,no_content;
    private static RealmResults<Work> works;
    private static  ArrayList<Work> workslist=new ArrayList<>();
    private static App app;
    private DailyWorkAdapter adapter;
    private Realm realm;
    private boolean LOADING = true;
    public static final int QUALITY = 10;
    private ImageButton back;
    private String status_text,category_text;

    public static DailyWorkProgressFragment newInstance(App a) {
        app = a;
        return new DailyWorkProgressFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_work_progress, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadWorks();
        items = view.findViewById(R.id.items);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
        no_content = view.findViewById(R.id.no_content);
        back =view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        reset =view.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadWorks();
            }
        });
        filter = view.findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_filter, null);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.PinDialog);
                final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
                alertDialog.show();
                final Spinner status= dialogView.findViewById(R.id.status);
                final Spinner catfilt= dialogView.findViewById(R.id.category_filter);
                final EditText startDateDD= dialogView.findViewById(R.id.start_date_dd);
                final EditText startDateMM= dialogView.findViewById(R.id.start_date_mm);
                final EditText startDateYYYY= dialogView.findViewById(R.id.start_date_yyyy);
                final EditText endDateDD= dialogView.findViewById(R.id.end_date_dd);
                final EditText endDateMM= dialogView.findViewById(R.id.end_date_mm);
                final EditText endDateYYYY= dialogView.findViewById(R.id.end_date_yyyy);

                Button positive = dialogView.findViewById(R.id.positive);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                           if(!(status.getSelectedItem().toString().equals("Select Status")))
                           {
                               status_text = status.getSelectedItem().toString();
                               console.log(status_text);
                           }
                           else
                           {
                               status_text="";
                           }
                           if(!(catfilt.getSelectedItem().toString().equals("Select Category")))
                           {
                               category_text = catfilt.getSelectedItem().toString();
                               console.log(category_text);
                           }
                           else
                           {
                               category_text="";
                           }
                           String start_date=startDateDD.getText()+"/"+startDateMM.getText()+"/"+startDateYYYY.getText();
                           String end_date=endDateDD.getText()+"/"+endDateMM.getText()+"/"+endDateYYYY.getText();
                           console.log(start_date+" "+end_date);
                            filter(status_text,category_text,start_date,end_date);
                            alertDialog.dismiss();

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
        });
//        realm = Realm.getDefaultInstance();
//        works = realm.where(Work.class).equalTo("belongsTo", App.belongsTo).findAllAsync();

        if (LOADING) {
            progress.setVisibility(View.VISIBLE);
            hider.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
            hider.setVisibility(View.GONE);
        }
    }

    private void filter(String status,String category,String startdate,String enddate) {
        workslist.clear();
        progress.setVisibility(View.VISIBLE);
        hider.setVisibility(View.VISIBLE);
        String filterURL = Config.WORK_FILTERS;
        HashMap<String, String> params = new HashMap<>();
        params.put("status", status);
        params.put("category_filter",category);
        params.put("start_date",startdate);
        params.put("end_date",enddate);
        console.log("Params: "+params);
        app.sendNetworkRequest(filterURL, Request.Method.POST, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.log("Response:" + response);
                try{
                    JSONArray array = new JSONArray(response);
                    for (int i=0; i<array.length(); i++){
                        JSONObject par = array.getJSONObject(i);
                        JSONObject sch = par.getJSONObject("work_schedule");
                        final Work work = new Work().parseFromJSON(sch.getJSONObject("work_details"), par.getString("work_list_id"),
                                sch.getString("work_duration"), sch.getString("qty"), sch.getString("schedule_start_date"), sch.getString("schedule_finish_date")
                                , sch.getString("current_status"),sch.getString("qty_completed"));
                        workslist.add(work);
//                        , par.getString("completed_activities"), par.getString("total_activities")
                        //                        realm.executeTransaction(new Realm.Transaction() {
//                            @Override
//                            public void execute(Realm realm) {
//                                realm.copyToRealmOrUpdate(work);
//                            }
//                        });
                        console.log("Worklist"+workslist.get(i));
                    }
                    if(workslist.isEmpty()){
                        no_content.setVisibility(View.VISIBLE);
                    }
                    adapter = new DailyWorkAdapter(getContext(), workslist, new DailyWorkAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos, View view) {
                            Intent intent = new Intent(getContext(), DailyWorkProgressActivities.class);
                            intent.putExtra("id", workslist.get(pos).getWorkListId());
                            startActivity(intent);
                        }
                    }, new DailyWorkAdapter.OnButtonClickListener() {
                        @Override
                        public void onButtonClick(int pos,View view) {
                            menuUpdate(workslist.get(pos));
                        }
                    });
                    items.setLayoutManager(new LinearLayoutManager(getContext()));
                    items.setAdapter(adapter);

                } catch (JSONException e){

                }
            }
        });
    }

    private void loadWorks(){
        workslist.clear();
        String url = Config.REQ_DAILY_WORK;
        url = url.replace("[0]", App.userId);
        url = url.replace("[1]", App.projectId);
        app.sendNetworkRequest(url, 0, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                workslist.clear();
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.log("Response:"+response);
                try{
                    JSONArray array = new JSONArray(response);
                    for (int i=0; i<array.length(); i++){
                        JSONObject par = array.getJSONObject(i);
                        JSONObject sch = par.getJSONObject("work_schedule");
                        final Work work = new Work().parseFromJSON(sch.getJSONObject("work_details"), par.getString("work_list_id"),
                                sch.getString("work_duration"), sch.getString("qty"), sch.getString("schedule_start_date"), sch.getString("schedule_finish_date")
                                , sch.getString("current_status"),sch.getString("qty_completed"));
                        workslist.add(work);
//                        , par.getString("completed_activities"), par.getString("total_activities")
                        //                        realm.executeTransaction(new Realm.Transaction() {
//                            @Override
//                            public void execute(Realm realm) {
//                                realm.copyToRealmOrUpdate(work);
//                            }
//                        });
                        console.log("Worklist"+workslist.get(i));
                    }
                    if(workslist.isEmpty()){
                        no_content.setVisibility(View.VISIBLE);
                    }
                    adapter = new DailyWorkAdapter(getContext(), workslist, new DailyWorkAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos, View view) {
                            Intent intent = new Intent(getContext(), DailyWorkProgressActivities.class);
                            intent.putExtra("id", workslist.get(pos).getWorkListId());
                            startActivity(intent);
                        }
                    }, new DailyWorkAdapter.OnButtonClickListener() {
                        @Override
                        public void onButtonClick(int pos,View view) {
                            menuUpdate(workslist.get(pos));
                        }
                    });
                    items.setLayoutManager(new LinearLayoutManager(getContext()));
                    items.setAdapter(adapter);

                } catch (JSONException e){

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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.PinDialog);
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
        imagesAdapter = new ActivityImagesAdapter(getContext(), images, new ActivityImagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Packet packet, int pos, View view) {
                if (pos == 0) {
                    Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                        }
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            imagePath = photoFile.getAbsolutePath();
                            startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
                        }
                    }
                }
            }
        });
        list.setAdapter(imagesAdapter);
        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
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
                        Toast.makeText(getContext(), "Fill data properly!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Fill data properly!", Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }

    private void submit(Work activity, String message, String quantity, ArrayList<Packet> images, final AlertDialog alertDialog) throws JSONException {
        float q = Float.parseFloat(quantity);
        float c = Float.parseFloat(activity.getQty_completed());
        float qo = Float.parseFloat(activity.getQuantity());
        console.log("entry completed quanity "+q+" "+c+" "+qo+" " + (qo-c));
        if (q <= (qo - c)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("work_update", new JSONObject()
//                    .put("activity_list_id", activity.getActivityListId())
                    .put("work_list_id",activity.getWorkId())
                    .put("type", "work")
                    .put("project_comment", message)
                    .put("quantity_done", quantity)
                    .put("units", activity.getUnits())
                    .put("user_id", App.userId)
                    .put("project_id", App.projectId)
                    .put("percentage_work", q / qo).toString());
            console.log("Work"+params.toString());
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
                params.put("images",array.toString());
            }

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
                        Toast.makeText(getContext(), "Status Updated", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        getActivity().finish();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Put right quantity", Toast.LENGTH_SHORT).show();
        }
    }

}



/*
{
    "work_list_id":"1",
    "work_schedule":
        {
            "work_details":
                {
                    "work_id":"1",
                    "work_name":"Preparation of Site\/Prelims",
                    "work_units":"Meter",
                    "work_code":"BNL001"
                },
            "schedule_id":"1",
            "work_duration":"50 Days",
            "qty":"5 ft",
            "schedule_start_date":"2018-06-25",
            "schedule_finish_date":"2018-07-10",
            "current_status":"Delayed"
        }
}
*/
