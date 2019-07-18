package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.TransferMachine;

public class TransferOutAdapter extends RecyclerView.Adapter<TransferOutAdapter.ViewHolder> {


    public interface OnItemClickedListener {
        void onButtonClicked(TransferMachine transferMachine,int pos);
    }

    private List<TransferMachine> filteredItems;
    private Context context;
    private OnItemClickedListener listener;

    public TransferOutAdapter(Context context, List<TransferMachine> users, OnItemClickedListener listener) {
        this.filteredItems=users;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_transfer_out_item, parent, false);
        return new ViewHolder(v);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bind(context, filteredItems.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView materialName,machineName,quantity,site,receive,date,status;


        public ViewHolder(View view) {
            super(view);
            materialName = view.findViewById(R.id._material_name);
            machineName = view.findViewById(R.id.machine_name);
            quantity = view.findViewById(R.id.quantity);
            site = view.findViewById(R.id.site);
            receive = view.findViewById(R.id.receive);
            date = view.findViewById(R.id.date);
            status = view.findViewById(R.id.status);
        }

        public void bind(final Context context, final TransferMachine item, final int pos, final OnItemClickedListener listener) {

            if(item!=null){
                quantity.setText(item.getQty());
                site.setText(item.getProject_name());

                if(item.getItem_type().equals("material")){
                    materialName.setText(item.getItem_name());
                    materialName.setVisibility(View.VISIBLE);
                    machineName.setVisibility(View.GONE);
                }
                else
                {
                    machineName.setText(item.getItem_name());
                    machineName.setVisibility(View.VISIBLE);
                    materialName.setVisibility(View.GONE);
                }


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    date.setText((Html.fromHtml("<b>Date: </b>"+item.getDate(),Html.FROM_HTML_MODE_LEGACY)));
                }
                else {
                    date.setText((Html.fromHtml("<b>Date: </b>"+item.getDate())));
                }

                status.setText(item.getStatus());

                if(item.getStatus().equals("Pending"))
                status.setTextColor(ContextCompat.getColor(context,R.color.c4));

                if(item.getStatus().equals("Received"))
                status.setTextColor(ContextCompat.getColor(context,R.color.c1));

                if(item.getStatus().equals("Rejected"))
                status.setTextColor(ContextCompat.getColor(context,R.color.material_red));

            }

//            receive.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onButtonClicked(item,pos);
//                }
//            });

        }
    }

}