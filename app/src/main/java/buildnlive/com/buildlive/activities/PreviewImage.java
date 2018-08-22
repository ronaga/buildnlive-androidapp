package buildnlive.com.buildlive.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.byox.drawview.enums.BackgroundScale;
import com.byox.drawview.enums.BackgroundType;
import com.byox.drawview.enums.DrawingCapture;
import com.byox.drawview.views.DrawView;
import com.github.chrisbanes.photoview.PhotoView;
import com.raodevs.touchdraw.TouchDrawView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.BuildConfig;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.ActivityImagesAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Packet;
import buildnlive.com.buildlive.utils.AdvancedRecyclerView;
import buildnlive.com.buildlive.utils.Config;


public class PreviewImage extends AppCompatActivity {
    private ImageButton edit, undo, redo;
    private FloatingActionButton button;
    private DrawView drawView;
    private String overview = "", ID = "";
    private int step = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.HolyBlack);
        setContentView(R.layout.activity_preview_image);
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "BL");
        final File f = new File(folder, getIntent().getStringExtra("image"));
        ID = getIntent().getStringExtra("id");
        edit = findViewById(R.id.edit);
        undo = findViewById(R.id.undo);
        redo = findViewById(R.id.redo);
        button = findViewById(R.id.floating_button);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawView.canUndo()) {
                    step--;
                    drawView.undo();
                }
            }
        });
        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawView.canRedo()) {
                    step++;
                    drawView.redo();
                }
            }
        });
        Bitmap bm = BitmapFactory.decodeFile(f.getAbsolutePath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] b = baos.toByteArray();
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert();
            }
        });

        drawView = findViewById(R.id.draw_view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawView.setBackgroundImage(b, BackgroundType.BYTES, BackgroundScale.CENTER_INSIDE);
            }
        }, 500);

        drawView.setOnDrawViewListener(new DrawView.OnDrawViewListener() {
            @Override
            public void onStartDrawing() {
            }

            @Override
            public void onEndDrawing() {
                step++;
            }

            @Override
            public void onClearDrawing() {
            }

            @Override
            public void onRequestText() {

            }

            @Override
            public void onAllMovesPainted() {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (step > 1) {
                    Object[] img = drawView.createCapture(DrawingCapture.BYTES);
                    byte[] b = (byte[]) img[0];
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    App app = (App) getApplication();
                    HashMap<String, String> params = new HashMap<>();
                    try {
                        params.put("project_plan", new JSONObject()
                                .put("id", ID)
                                .put("detail", overview.toString())
                                .put("user_id", App.userId)
                                .put("image", encodedImage)
                                .toString());
                    } catch (JSONException e) {

                    }
                    app.sendNetworkRequest(Config.REQ_PLAN_MARKUP, 1, params, new Interfaces.NetworkInterfaceListener() {
                        @Override
                        public void onNetworkRequestStart() {

                        }

                        @Override
                        public void onNetworkRequestError(String error) {

                        }

                        @Override
                        public void onNetworkRequestComplete(String response) {
                            if (response.equals("1")) {
                                Toast.makeText(getApplicationContext(), "Plan Updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Edit Something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void alert() {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_plan, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.PinDialog);
        final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
        alertDialog.show();
        final TextView disable = dialogView.findViewById(R.id.disableView);
        final ProgressBar progress = dialogView.findViewById(R.id.progress);
        final EditText message = dialogView.findViewById(R.id.message);
        message.setText(overview);
        TextView title = dialogView.findViewById(R.id.alert_title);
        title.setText("Plan Overview");
        final TextView alert_message = dialogView.findViewById(R.id.alert_message);
        alert_message.setText("Please explain markings of plan.");
        Button positive = dialogView.findViewById(R.id.positive);
        Button negative = dialogView.findViewById(R.id.negative);
        positive.setText("Done");
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overview = message.getText().toString();
                alertDialog.dismiss();
            }
        });
        negative.setVisibility(View.GONE);
    }
}
