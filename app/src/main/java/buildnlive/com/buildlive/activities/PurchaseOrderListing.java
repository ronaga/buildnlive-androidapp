package buildnlive.com.buildlive.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import buildnlive.com.buildlive.adapters.PurchaseOrderListingAdapter;
import buildnlive.com.buildlive.adapters.SingleImageAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.OrderItem;
import buildnlive.com.buildlive.elements.Packet;
import buildnlive.com.buildlive.utils.AdvancedRecyclerView;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class PurchaseOrderListing extends AppCompatActivity {
    private App app;
    private String id, type_id;
    private RecyclerView list;
    private PurchaseOrderListingAdapter adapter;
    private List<OrderItem> itemList;
    private Button submit;
    private AlertDialog.Builder builder;
    public static final int QUALITY = 10;
    public static final int REQUEST_CAPTURE_IMAGE = 7190;
    private String imagePath;
    private ArrayList<Packet> images;
    private SingleImageAdapter imagesAdapter;
    private EditText challan, invoice, inward_no, vehicle_no;
    private ProgressBar progress;
    private TextView hider;
    public static final int REQUEST_GALLERY_IMAGE = 7191;
    private static String results;
    private Context context;
    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_purchase_order_listing);
        list = findViewById(R.id.items);

        utilityofActivity = new UtilityofActivity(appCompatActivity);
        utilityofActivity.configureToolbar(appCompatActivity);

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle = findViewById(R.id.toolbar_subtitle);
        toolbar_title.setText("Receive Items");
        toolbar_subtitle.setText(App.projectName);

        challan = findViewById(R.id.challan);
        invoice = findViewById(R.id.invoice);
        inward_no = findViewById(R.id.inward_no);
        vehicle_no = findViewById(R.id.vehicle_no);
        progress = findViewById(R.id.progress);
        hider = findViewById(R.id.hider);
        itemList = new ArrayList<>();
        submit = findViewById(R.id.submit);
        adapter = new PurchaseOrderListingAdapter(getApplicationContext(), itemList, new PurchaseOrderListingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {

            }
        });
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list.setAdapter(adapter);
        builder = new AlertDialog.Builder(this);
        app = (App) getApplication();
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        type_id = bundle.getString("type_id");
        fetchOrders();

        final AdvancedRecyclerView list = findViewById(R.id.images);
        images = new ArrayList<>();
        images.add(new Packet());
        imagesAdapter = new SingleImageAdapter(getApplicationContext(), images, new SingleImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Packet packet, int pos, View view) {
                if (pos == 0) {

                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.image_chooser, null);
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.PinDialog);
                    final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
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
                                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
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
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        list.setmMaxHeight(350);

        final boolean[] flag = {false};
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    /*for (int i = 0; i < itemList.size(); i++) {
                                        if (itemList.get(i).isIncluded()) {
                                            if (Float.parseFloat(itemList.get(i).getQuantity()) <= Float.parseFloat(itemList.get(i).getMax_qty())) {
                                                flag[0] = true;
                                            } else {
                                                flag[0] = false;
                                            }

                                        }
                                    }*/

//                                    if (flag[0]) {
                                    pushOrders(images);
//                                    } else {
//                                        Toast.makeText(context, "Enter Proper Quantity", Toast.LENGTH_LONG).show();
//                                    }

//                                    pushOrders(images);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Purchase Order");
                alert.show();


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


    private void pushOrders(ArrayList<Packet> images) throws JSONException {
        String url = Config.REQ_PURCHASE_ORDER_UPDATE;
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", App.userId);
        params.put("purchase_order_id", id);
        params.put("type_id", type_id);
        JSONArray array = new JSONArray();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).isIncluded()) {
                JSONObject obj = new JSONObject();
                obj.put("qty_received", itemList.get(i).getQuantity());
                obj.put("comments", "Done");
                obj.put("purchase_order_list_id", itemList.get(i).getOrderId());
                array.put(obj);
            }
        }
        params.put("purchase_order_list", array.toString());
        params.put("challan", challan.getText().toString());
        params.put("invoice", invoice.getText().toString());
        params.put("inward_no", inward_no.getText().toString());
        params.put("vehicle_no", vehicle_no.getText().toString());
        JSONArray imageArray = new JSONArray();
        for (Packet p : images) {
            if (p.getName() != null) {
                Bitmap bm = BitmapFactory.decodeFile(p.getName());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, QUALITY, baos);
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                imageArray.put(encodedImage);
            }
        }
        console.log("Params" + params);
        params.put("images", imageArray.toString());
        console.log("Image" + params);
        app.sendNetworkRequest(url, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
                if (response.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Request generated", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private void fetchOrders() {
        String url = Config.REQ_PURCHASE_ORDER_LISTING;
        url = url.replace("[0]", App.userId).replace("[1]", id);
        app.sendNetworkRequest(url, 0, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
                utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();

                try {
                    itemList.clear();
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        itemList.add(new OrderItem().parseFromJSON(array.getJSONObject(i)));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {

                }
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Packet packet = images.remove(0);
                packet.setName(imagePath);
//                Uri uri=data.getData();
//                packet.setName(getRealPathFromURI(uri));
                console.log("Image Path " + packet.getName() + "EXTRAS " + packet.getExtra());
                images.add(0, new Packet());
                images.add(packet);
                imagesAdapter.notifyDataSetChanged();
            } else if (resultCode == Activity.RESULT_CANCELED) {
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
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            results = cursor.getString(column_index);
        }
//                managedQuery( );
        cursor.moveToFirst();
        cursor.close();
        return results;
    }

}