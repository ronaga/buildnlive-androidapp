package buildnlive.com.buildlive.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.adapters.CreateTransferAdapter;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.CreateTransferObject;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.UtilityofActivity;

public class CreateTransfer extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static App app;
    private ArrayList<CreateTransferObject> transferList;
    private ProgressBar progress;
    private TextView hider;
    private Context context;
    private LinearLayout search_view;
    private EditText search_text;
    private ImageButton search_close,search;
    private CreateTransferAdapter transferAdapter;
    private UtilityofActivity utilityofActivity;
    private AppCompatActivity appCompatActivity=this;





    private CreateTransferAdapter.OnItemClickListener listener= new CreateTransferAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(CreateTransferObject packet, int pos, View view) {
            Intent intent=new Intent(CreateTransfer.this, TransferForm.class);
            Bundle bundle= new Bundle();
            bundle.putSerializable("Items",packet);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    };


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
        setContentView(R.layout.activity_create_transfer);

        context=this;

        utilityofActivity=new UtilityofActivity(appCompatActivity);
        utilityofActivity.configureToolbar(appCompatActivity);

        TextView toolbar_title=findViewById(R.id.toolbar_title);
        TextView toolbar_subtitle=findViewById(R.id.toolbar_subtitle);
        toolbar_title.setText("Transfer");
        toolbar_subtitle.setText(App.projectName);


        app = (App) getApplication();

        recyclerView= findViewById(R.id.items);
        progress = findViewById(R.id.progress);
        hider = findViewById(R.id.hider);

        search_view = findViewById(R.id.search_view);
        search_text= findViewById(R.id.search_text);
        search_close= findViewById(R.id.search_close);
        search= findViewById(R.id.search);


        search.setOnClickListener(v -> {
            search_text.setVisibility(View.VISIBLE);
            search_close.setVisibility(View.VISIBLE);
            search_text.requestFocus();
            if(search_text.hasFocus()){
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(search_text, InputMethodManager.SHOW_IMPLICIT);
                }
            }

        });

        search_text.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                search(search_text.getText().toString());
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(search_text.getWindowToken(), 0);
                }
                return true;
            } else {
                return false;
            }

        });

        search_close.setOnClickListener(v -> {
            search_text.setText("");
            search_text.setVisibility(View.INVISIBLE);
            search_close.setVisibility(View.INVISIBLE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(search_close.getWindowToken(), 0);
            }
            refresh();
        });

        transferList = new ArrayList<>();


        transferAdapter=new CreateTransferAdapter(context,transferList,listener);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        DividerItemDecoration dividerItemDecoration= new DividerItemDecoration(context, LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(transferAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        refresh();

    }


    private void refresh() {
        transferList.clear();
        String requestUrl = Config.SEND_TRANSFER_LIST;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", App.projectId);

        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
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
                console.error("Network request failed with error :" + error);
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        transferList.add(new CreateTransferObject().parseFromJSON(array.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                transferAdapter.notifyDataSetChanged();
            }
        });
    }

    private void search(String keyword) {
        transferList.clear();
        String requestUrl = Config.SEARCH_TRANSFER;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", App.projectId);
        requestUrl = requestUrl.replace("[2]", keyword);
        console.log(requestUrl);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
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
                console.error("Network request failed with error :" + error);
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
//                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                utilityofActivity.dismissProgressDialog();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        transferList.add(new CreateTransferObject().parseFromJSON(array.getJSONObject(i)));
                    }
                    console.log("data set changed");

                    transferAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
