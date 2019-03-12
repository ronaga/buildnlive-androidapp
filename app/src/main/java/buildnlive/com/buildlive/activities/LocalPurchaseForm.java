package buildnlive.com.buildlive.activities;

import android.app.AlertDialog;
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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Locale;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.BuildConfig;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.ActivityImagesAdapter;
import buildnlive.com.buildlive.adapters.CategorySpinAdapter;
import buildnlive.com.buildlive.adapters.ItemSpinAdapter;
import buildnlive.com.buildlive.adapters.SingleImageAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Category;
import buildnlive.com.buildlive.elements.IndentItem;
import buildnlive.com.buildlive.elements.Item;
import buildnlive.com.buildlive.elements.Packet;
import buildnlive.com.buildlive.utils.AdvancedRecyclerView;
import buildnlive.com.buildlive.utils.Config;

public class LocalPurchaseForm extends AppCompatActivity {

    private Button submit,upload;
    private ProgressBar progress;
    private boolean val=true;
    private TextView hider, checkout_text,item,unitsText;
    private EditText quantity_edit,total_edit,overheads_edit,rate_edit,tax_edit,vendor_details_edit,ship_no_edit,details_edit;
    //    name_edit name,
    private static String quantity,total,overheads,rate,tax,unit,vendor_details,ship_no,details,results;
    private boolean LOADING;
//    private Spinner unitspinner;
    private AlertDialog.Builder builder;
    public static final int QUALITY = 10;
    public static final int REQUEST_CAPTURE_IMAGE = 7190;
    private String imagePath;
    private ArrayList<Packet> images;
    private SingleImageAdapter imagesAdapter;
    private Context context;
    public static final int REQUEST_GALLERY_IMAGE = 7191;
    private Item selectedItem;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_purchase_form);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView textView=findViewById(R.id.toolbar_title);
        textView.setText("Local Purchase");
        Bundle bundle=getIntent().getExtras();

        if(bundle!=null){
            selectedItem= (Item) bundle.getSerializable("Items");
        }


        item= findViewById(R.id.item);
        progress= findViewById(R.id.progress);
        submit =  findViewById(R.id.submit);
        context=this;
//        name_edit =  findViewById(R.id.name);
        quantity_edit =  findViewById(R.id.quantity);
        total_edit =  findViewById(R.id.total);
        overheads_edit =  findViewById(R.id.overheads);
        rate_edit =  findViewById(R.id.rate);
        tax_edit =  findViewById(R.id.tax);
        vendor_details_edit =  findViewById(R.id.vendor_details);
        ship_no_edit =  findViewById(R.id.ship_no);
        details_edit =  findViewById(R.id.details);
        builder = new AlertDialog.Builder(context);
        unitsText= findViewById(R.id.unit);
//        unitspinner= findViewById(R.id.unit);


        item.setText(selectedItem.getName());
        unitsText.setText(selectedItem.getUnit());



//        unitspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                unit=unitspinner.getSelectedItem().toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


        progress = findViewById(R.id.progress);
        hider = findViewById(R.id.hider);

        if (LOADING) {
            progress.setVisibility(View.VISIBLE);
            hider.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
            hider.setVisibility(View.GONE);
        }



        final AdvancedRecyclerView list = findViewById(R.id.images);
        images = new ArrayList<>();
        images.add(new Packet());
        imagesAdapter = new SingleImageAdapter(context, images, new SingleImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Packet packet, int pos, View view) {
                if (pos == 0) {

                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.image_chooser, null);
                    android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(context, R.style.PinDialog);
                    final android.support.v7.app.AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
                    alertDialog.show();
                    final TextView gallery= dialogView.findViewById(R.id.gallery);
                    final TextView camera= dialogView.findViewById(R.id.camera);

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

                } else{
                    images.remove(pos);
                    imagesAdapter.notifyItemRemoved(pos);
                    imagesAdapter.notifyDataSetChanged();
                }
            }
        });
        list.setAdapter(imagesAdapter);
        list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        list.setmMaxHeight(350);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                name=name_edit.getText().toString();
                quantity=quantity_edit.getText().toString();
                unit=unitsText.getText().toString();
                total=total_edit.getText().toString();
                overheads=overheads_edit.getText().toString();
                rate=rate_edit.getText().toString();
                tax=tax_edit.getText().toString();
                vendor_details=vendor_details_edit.getText().toString();
                ship_no=ship_no_edit.getText().toString();
                details=details_edit.getText().toString();

                builder.setMessage("Are you sure?") .setTitle("Payment");

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(validate(quantity,total,vendor_details))
                                { console.log("From Validate");
                                    try {
                                        sendRequest(selectedItem.getId(),quantity,unit,total,overheads,rate,tax,vendor_details,ship_no,details,images);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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
                alert.setTitle("Local Purchase");
                alert.show();


            }
        });




    }



    private boolean validate(String quantity,String total,String vendor_details)
    {
        val=true;

//        if(TextUtils.equals(unit,"Unit")){
//            Toast.makeText(context,"Select Unit",Toast.LENGTH_LONG).show();
//            val=false;
//
//        }

//        if(TextUtils.isEmpty(unit)){
//            Toast.makeText(context,"Select Unit",Toast.LENGTH_LONG).show();
//            val=false;
//
//        }

        if(TextUtils.isEmpty(quantity)){
            quantity_edit.setError("Enter Quantity");
            val=false;
        }

        if(TextUtils.isEmpty(total)){
            total_edit.setError("Enter Total");
            val=false;
        }
        if(TextUtils.isEmpty(vendor_details)){
            vendor_details_edit.setError("Enter Vendor Details");
            val=false;
        }

        return val;
    }

    private void sendRequest(String stockid,String quantity,String units,String total,
                             String overheads,String rate,String tax,String vendor_details,String ship_no,String details,ArrayList<Packet> images) throws JSONException {
        App app= ((App)getApplication());
        HashMap<String, String> params = new HashMap<>();
        params.put("local_purchase", App.userId);
//        JSONArray array = new JSONArray();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("stock_id", stockid).put("project_id", App.projectId).put("user_id", App.userId)
                .put("quantity",quantity).put("units",units).put("total_amount",total)
                .put("overheads",overheads).put("rate",rate).put("tax",tax).put("vendor_details",vendor_details).put("slip_no",ship_no)
                .put("details",details);
        params.put("local_purchase", jsonObject.toString());
        console.log("Local Purchase"+params);
        JSONArray array =new JSONArray();
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
        console.log("Image"+params);
        app.sendNetworkRequest(Config.SEND_LOCAL_PURCHASE, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);;
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                Toast.makeText(context,"Error"+error,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                if(response.equals("1")) {
                    Toast.makeText(context, "Request Generated", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(context, "Check Your Network", Toast.LENGTH_SHORT).show();

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
        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            if (resultCode == android.app.Activity.RESULT_OK) {
                Packet packet = images.remove(0);
                packet.setName(imagePath);
//                Uri uri=data.getData();
//                packet.setName(getRealPathFromURI(uri));
                console.log("Image Path "+packet.getName()+"EXTRAS "+packet.getExtra());
                images.add(0, new Packet());
                images.add(packet);
                imagesAdapter.notifyDataSetChanged();
            } else if (resultCode == android.app.Activity.RESULT_CANCELED) {
                console.log("Canceled");
            }
        }
        else if(requestCode == REQUEST_GALLERY_IMAGE){
            Packet packet = images.remove(0);
//            packet.setName(imagePath);
            Uri uri=data.getData();
            packet.setName(getRealPathFromURI(uri));
            console.log("Image Path "+packet.getName()+"EXTRAS "+packet.getExtra());
            images.add(0, new Packet());
            images.add(packet);
            imagesAdapter.notifyDataSetChanged();
        }
    }
    // And to convert the image URI to the direct file system path of the image file
    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor =context.getContentResolver().query(contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            results = cursor.getString(column_index);
        }
//                managedQuery( );
        cursor.moveToFirst();
        cursor.close();
        return results;
    }





}
