package buildnlive.com.buildlive.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import java.util.HashMap;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.utils.Config;

public class ProfileFragment extends Fragment {
    private static App app;
    public static ProfileFragment newInstance(App a) {
        app=a;
        return new ProfileFragment();
    }
    private EditText mob,old_pass,new_pass;
    private Button change_password;
    private ProgressBar progress;
    private TextView hider;
    private AlertDialog.Builder builder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mob= view.findViewById(R.id.mobile);
        old_pass= view.findViewById(R.id.old_password);
        new_pass= view.findViewById(R.id.new_password);
        builder = new AlertDialog.Builder(getContext());
        progress= view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
        change_password= view.findViewById(R.id.change_password);
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("Are you sure?") .setTitle("Forgot Password?");

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String m = mob.getText().toString();
                                String o = old_pass.getText().toString();
                                String n = new_pass.getText().toString();
                                if (m.length() > 3 && o.length()>3 && n.length()>3) {
                                    resetPassword(m,o,n);
                                } else {
                                    Toast.makeText(getContext(), "Please enter valid details", Toast.LENGTH_LONG).show();
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
                alert.setTitle("Forgot Password?");
                alert.show();


            }
        });

    }
    private void resetPassword(String m,String o,String n) {
        progress.setVisibility(View.VISIBLE);
        hider.setVisibility(View.VISIBLE);
        String resetPass = Config.RESET_PASSWORD;
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile_no", m);
        params.put("password",o);
        params.put("new_password",n);
        app.sendNetworkRequest(resetPass, Request.Method.POST, params, new Interfaces.NetworkInterfaceListener() {
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
                console.log("Response:" + response);
                if (response.equals("0")) {
                    Toast.makeText(getContext(), "Error, Please Try Again Later", Toast.LENGTH_LONG).show();
                } else if(response.equals("1")) {
                    Toast.makeText(getContext(),"Password Changed Successfully",Toast.LENGTH_LONG).show();
                }
                else if(response.equals("-1")){
                    Toast.makeText(getContext(),"Incorrect Credentials",Toast.LENGTH_LONG).show();
                }
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
            }
        });
    }
}
