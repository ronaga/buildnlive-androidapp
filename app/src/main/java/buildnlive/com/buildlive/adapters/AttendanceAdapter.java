package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Worker;
import buildnlive.com.buildlive.utils.PrefernceFile;
import buildnlive.com.buildlive.utils.Utils;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Worker worker, int pos, View view);
    }

    public static final int RESET_ATTENDANCE_LIMIT_MIN = 900;
//    public static final int CHECKOUT_TIME_GAP_MIN = 30;
    private final List<Worker> items;
    private static Context context;
    private final OnItemClickListener listener;

    public AttendanceAdapter(Context context, ArrayList<Worker> workers, OnItemClickListener listener) {
        this.items = workers;
        this.context = context;
        this.listener = listener;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_attendance, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(context, items.get(holder.getAdapterPosition()), holder.getAdapterPosition(), listener);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public static HashMap<String, Boolean> changedUsers = new HashMap<>();
        private TextView name, role;
        private   CheckBox check_in, check_out;
        private boolean reset = false;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            role = view.findViewById(R.id.role);
            check_in = view.findViewById(R.id.check_in);
            check_out = view.findViewById(R.id.check_out);
        }

        public void bind(final Context context, final Worker item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getName());
            role.setText(item.getRoleAssigned() + "(" + item.getType() + ")");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, pos, itemView);
                }
            });
            check_in.setOnCheckedChangeListener(null);
            check_out.setOnCheckedChangeListener(null);

//            int time_gap = Utils.differenceInMin(item.getCheckInTime(), System.currentTimeMillis());


            if(item.getLabour_present().equals("1"))
            {
                check_in.setChecked(true);
                check_in.setEnabled(false);
                check_out.setChecked(true);
                check_out.setEnabled(false);
            }
            else if(item.getLabour_present().equals("0"))
            {
                check_in.setChecked(true);
                check_in.setEnabled(false);
                check_out.setChecked(false);
                check_out.setEnabled(true);
            }
            else
            {
                check_in.setChecked(false);
                check_in.setEnabled(true);
                check_out.setChecked(false);
                check_out.setEnabled(true);
            }

//            if(!item.getStart_time().equals(""))
//            {
//                check_in.setChecked(true);
//                check_in.setEnabled(false);
//            }
//
//            if(!item.getEnd_time().equals(""))
//            {
//                check_out.setChecked(true);
//                check_out.setEnabled(false);
//            }



            check_in.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        changedUsers.put(item.getId(), false);
                        alertCheckIn(item,check_in);
                    }
                    else
                        changedUsers.remove(item.getId());
                }
            });

            check_out.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        if(!item.getStart_time().equals("")){
                            changedUsers.put(item.getId(), true);
                            alertCheckOut(item,check_out);
                        }
                        else
                        {
                            check_out.setChecked(false);
                            Toast.makeText(context,"Please Checkin First",Toast.LENGTH_LONG).show();
                        }

                    }
                         else
                        changedUsers.remove(item.getId());
                }
            });


        }

    }
    private static void alertCheckIn(Worker item, CheckBox check_in) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
        View dialogView = inflater.inflate(R.layout.alert_dialog_checkin, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.PinDialog);

        final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
        alertDialog.show();

        TextView title = dialogView.findViewById(R.id.alert_title);
        TextView hours = dialogView.findViewById(R.id.hours);
        TextView minutes = dialogView.findViewById(R.id.minutes);
        title.setText("Check In");

        Button positive = dialogView.findViewById(R.id.positive);
        Button negative = dialogView.findViewById(R.id.negative);

        Calendar cal= Calendar.getInstance();

        int hour= cal.get(Calendar.HOUR_OF_DAY);
        int min= cal.get(Calendar.MINUTE);

        hours.setText("09");
        minutes.setText("00");

        positive.setText("Done");
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hours.getText().toString().equals("")||minutes.getText().toString().equals("")){
                    Toast.makeText(context,"Enter time in 24 hrs format",Toast.LENGTH_LONG).show();
                }
                else {

//                    Integer.parseInt(hours.getText().toString())+ (Integer.parseInt(minutes.getText().toString())/60))

                    if(Integer.parseInt(hours.getText().toString())<=24&&Integer.parseInt(minutes.getText().toString())<=60)
                    {
                        PrefernceFile.Companion.getInstance(context).setString("CheckinTime"+item.getId(),hours.getText()+":"+minutes.getText());
                        alertDialog.dismiss();
                    }
                    else Toast.makeText(context,"Enter proper time in 24 hrs format",Toast.LENGTH_LONG).show();
                }

            }
        });
//        negative.setVisibility(View.GONE);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_in.setChecked(false);
                alertDialog.dismiss();
            }
        });
    }

    private static void alertCheckOut(Worker item, CheckBox check_out) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
        View dialogView = inflater.inflate(R.layout.alert_dialog_checkout, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.PinDialog);

        final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
        alertDialog.show();

        TextView title = dialogView.findViewById(R.id.alert_title);
        TextView hours = dialogView.findViewById(R.id.hours);
        TextView minutes = dialogView.findViewById(R.id.minutes);
        EditText overtime_hours = dialogView.findViewById(R.id.overtime_hours);
        EditText overtime_work = dialogView.findViewById(R.id.overtime_work);
        title.setText("Check Out");

        Button positive = dialogView.findViewById(R.id.positive);
        Button negative = dialogView.findViewById(R.id.negative);

        Calendar cal= Calendar.getInstance();

        int hour= cal.get(Calendar.HOUR_OF_DAY);
        int min= cal.get(Calendar.MINUTE);

        hours.setText("20");
        minutes.setText("00");

        overtime_hours.setText("0");

        positive.setText("Done");
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hours.getText().toString().equals("")||minutes.getText().toString().equals("")){
                    Toast.makeText(context,"Enter time in 24 hrs format",Toast.LENGTH_LONG).show();
                }
                else {
                    if (Integer.parseInt(hours.getText().toString()) <= 24 && Integer.parseInt(minutes.getText().toString()) <= 60
                            && Integer.parseInt(overtime_hours.getText().toString())<=24) {
                        PrefernceFile.Companion.getInstance(context).setString("CheckoutTime" + item.getId(), hours.getText() + ":" + minutes.getText());
                        PrefernceFile.Companion.getInstance(context).setString("OvertimeHours" + item.getId(), overtime_hours.getText().toString());
                        PrefernceFile.Companion.getInstance(context).setString("OvertimeWork" + item.getId(), overtime_work.getText().toString());
                        alertDialog.dismiss();
                    } else
                        Toast.makeText(context, "Enter proper time in 24 hrs format", Toast.LENGTH_LONG).show();
                }
            }
        });
//        negative.setVisibility(View.GONE);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_out.setChecked(false);
                alertDialog.dismiss();
            }
        });
    }

}