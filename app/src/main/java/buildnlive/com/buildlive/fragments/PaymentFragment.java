package buildnlive.com.buildlive.fragments;

import android.app.Activity;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import buildnlive.com.buildlive.adapters.SingleImageAdapter;
import buildnlive.com.buildlive.adapters.VendorOptionSpinAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Packet;
import buildnlive.com.buildlive.elements.VendorOption;
import buildnlive.com.buildlive.utils.AdvancedRecyclerView;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class PaymentFragment extends Fragment {
    private Button submit;
    private RadioButton payPrivate,payPublic;
    private ProgressBar progress;
    private boolean val = true;
    private TextView hider,balance;
    private EditText amount_edit, overheads_edit, to_edit,reason_edit, details_edit;
    private String amount, details, payment_type, payment_mode,type_of_payment="Public",purpose,to, reason;
//     overheads,
    private static String results;
    private boolean LOADING;
    private Spinner purposeSpinner,paymentTypeSpinner,paymentModeSpinner;
    private AlertDialog.Builder builder;
    private RadioGroup radioGroup;
    public static final int QUALITY = 10;
    public static final int REQUEST_CAPTURE_IMAGE = 7190;
    private String imagePath;
    private ArrayList<Packet> images;
    private SingleImageAdapter imagesAdapter;
    public static final int REQUEST_GALLERY_IMAGE = 7191;
    private Context context;
    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity;
    private VendorOptionSpinAdapter purposeAdapter;
    private ArrayList<VendorOption> vendorOptions= new ArrayList<>();



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.appCompatActivity = (AppCompatActivity) activity;
    }


    public static PaymentFragment newInstance() {
        return new PaymentFragment();
    }


    @Override
    public void onStart() {
        super.onStart();

        try {
            getVendorList();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        submit = view.findViewById(R.id.submit);
        utilityofActivity=new UtilityofActivity(appCompatActivity);

        details_edit = view.findViewById(R.id.payment_details);
        to_edit = view.findViewById(R.id.receiver);
        reason_edit = view.findViewById(R.id.reason);
//        overheads_edit = view.findViewById(R.id.overheads);
        amount_edit = view.findViewById(R.id.amount);

        payPrivate = view.findViewById(R.id.payPrivate);
        payPublic = view.findViewById(R.id.paypublic);

        progress = view.findViewById(R.id.progress);

        paymentModeSpinner=view.findViewById(R.id.paymentMode);
        paymentTypeSpinner=view.findViewById(R.id.paymentType);
        purposeSpinner=view.findViewById(R.id.purpose);
        balance=view.findViewById(R.id.balance);


        payPublic.setChecked(true);
        payPrivate.setChecked(false);

        radioGroup = (RadioGroup) view.findViewById(R.id.PaymentRadio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton) radioGroup.findViewById(i);
                if (null != rb) {
                    switch (i){
                        case R.id.payPrivate:
                            type_of_payment="Private";
                            break;
                        case R.id.paypublic:
                            type_of_payment="Public";
                    }
                }
            }
        });

        purposeAdapter  = new VendorOptionSpinAdapter(context, R.layout.custom_spinner, vendorOptions);
        purposeSpinner.setAdapter(purposeAdapter);

        purposeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                purpose=purposeAdapter.getVendorOption_id(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                payment_type=paymentTypeSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        paymentModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                payment_mode=paymentModeSpinner.getSelectedItem().toString();

                if(i>0)
                {
                    try {
                        getBalanceAmount(payment_mode);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                    balance.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        hider = view.findViewById(R.id.hider);
        builder = new AlertDialog.Builder(getContext());


        if (LOADING) {
            progress.setVisibility(View.VISIBLE);
            hider.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
            hider.setVisibility(View.GONE);
        }

        final AdvancedRecyclerView list = view.findViewById(R.id.images);
        images = new ArrayList<>();
        images.add(new Packet());
        imagesAdapter = new SingleImageAdapter(context, images, new SingleImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Packet packet, int pos, View view) {
                if (pos == 0) {

                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.image_chooser, null);
                    androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.PinDialog);
                    final androidx.appcompat.app.AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
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
        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        list.setmMaxHeight(350);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                to=to_edit.getText().toString();
                reason=reason_edit.getText().toString();
                amount=amount_edit.getText().toString();
//                overheads=overheads_edit.getText().toString();
                details=details_edit.getText().toString();
                builder.setMessage("Are you sure?") .setTitle("Payment");

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(validate(purpose,details,reason,amount,to,payment_mode,payment_type,type_of_payment))
                                {
                                    try {
                                        sendRequest(purpose,details,amount,to,reason,payment_mode,payment_type,type_of_payment,images);
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


    private boolean validate(String purpose,String details,String reason,String amount,String to,String payment_mode,String payment_type,String type_of_payment)
    {

        if(TextUtils.equals(payment_mode,"Select Payment Mode")){
            Toast.makeText(getContext(),"Please Payment Mode",Toast.LENGTH_LONG).show();
            val=false;
        }

        if(TextUtils.equals(payment_type,"Select Payment Type")){
            Toast.makeText(getContext(),"Please Payment Type",Toast.LENGTH_LONG).show();
            val=false;
        }


        if(TextUtils.isEmpty(details)){
            details_edit.setError("Enter Details");
            val=false;
        }
        if(TextUtils.isEmpty(reason)){
            reason_edit.setError("Enter Reason");
            val=false;
        }

        if(TextUtils.isEmpty(amount)){
            amount_edit.setError("Enter Amount");
            val=false;
        }
        if(TextUtils.isEmpty(to)){
            to_edit.setError("Enter Payee");
            val=false;
        }
        return val;
    }

    private void sendRequest(String purpose, String details, String amount,String payee,String reason, String payment_mode, String payment_type, String type_of_payment,ArrayList<Packet> images) throws JSONException {
        App app= ((App)getActivity().getApplication());
        HashMap<String, String> params = new HashMap<>();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("project_id", App.projectId).put("user_id", App.userId).put("purpose",purpose).put("details",details)
                .put("amount",amount).put("payee",payee).put("reason",reason).put("payment_mode",payment_mode).put("payment_type",payment_type)
                .put("type_of_payment",type_of_payment);
        params.put("site_payments", jsonObject.toString());
        console.log("Res:" + params);
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

        app.sendNetworkRequest(Config.SEND_SITE_PAYMENTS, 1, params, new Interfaces.NetworkInterfaceListener() {
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
                Toast.makeText(getContext(),"Something went wrong, Try again later",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
                console.log(response);
                if (response.equals("1")) {
                    Toast.makeText(getContext(), "Request Generated", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        });
    }


    private void getVendorList() throws JSONException {

        App app= ((App)getActivity().getApplication());

        String url = Config.SITE_PAYMENT_OPTION;

        url = url.replace("[0]", App.userId);
        url = url.replace("[1]", App.projectId);


        app.sendNetworkRequest(url, 1, null, new Interfaces.NetworkInterfaceListener() {
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
                Toast.makeText(getContext(),"Something went wrong, Try again later",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
                console.log(response);

                try {
                    vendorOptions.clear();

                    Type vendorType = new TypeToken<ArrayList<VendorOption>>() {
                    }.getType();
                    vendorOptions = new Gson().fromJson(response, vendorType);

                    purposeAdapter  = new VendorOptionSpinAdapter(context, R.layout.custom_spinner, vendorOptions);
                    purposeSpinner.setAdapter(purposeAdapter);


                    console.log("ERROR: "+vendorOptions.get(0).getOption_label());
                    purposeAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

  private void getBalanceAmount(String type) throws JSONException {

        App app= ((App)getActivity().getApplication());

        String url = Config.SendBalance;

        url = url.replace("[0]", App.userId);
        url = url.replace("[1]", App.projectId);
        url = url.replace("[2]", type);


        app.sendNetworkRequest(url, 1, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
            utilityofActivity.showProgressDialog();
            }

            @Override
            public void onNetworkRequestError(String error) {
                utilityofActivity.dismissProgressDialog();
                Toast.makeText(getContext(),"Something went wrong, Try again later",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                utilityofActivity.dismissProgressDialog();
                console.log(response);

                try {
                    balance.setVisibility(View.VISIBLE);
                    balance.setText(String.format(getString(R.string.balanceAmount),response));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }




    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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