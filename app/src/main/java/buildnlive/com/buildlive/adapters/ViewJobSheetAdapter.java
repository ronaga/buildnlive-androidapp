package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.ViewJobSheet;

public class ViewJobSheetAdapter extends RecyclerView.Adapter<ViewJobSheetAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ViewJobSheet items, int pos, View view);
    }

    private final List<ViewJobSheet> items;
    private Context context;
    private final OnItemClickListener listener;

    public ViewJobSheetAdapter(Context context, List<ViewJobSheet> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_asset_sheet, parent, false);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(context, items.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, loginTime, logoutTime, serviceTime, description, startKms, endKms, update, date;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            date = view.findViewById(R.id.date);
            loginTime = view.findViewById(R.id.login_time);
            logoutTime = view.findViewById(R.id.logout_time);
            serviceTime = view.findViewById(R.id.service_time);
            description = view.findViewById(R.id.description);
            startKms = view.findViewById(R.id.startKms);
            endKms = view.findViewById(R.id.endKms);
            update = view.findViewById(R.id.update);
        }

        public void bind(final Context context, final ViewJobSheet item, final int pos, final OnItemClickListener listener) {

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item, pos, update);
                }
            });

            if (item.getName() != null)
                name.setText(item.getName());
            if (item.getDate() != null)
                date.setText(String.format(context.getString(R.string.dateHolder),item.getDate()));
            if (item.getLog_in_meter() != null)
                startKms.setText(item.getLog_in_meter());
            if (item.getLog_out_meter() != null)
                endKms.setText(item.getLog_out_meter());
            if (item.getName() != null)
                name.setText(item.getName());
            if (item.getLog_in_time() != null)
                loginTime.setText(item.getLog_in_time());
            if (item.getLog_out_time() != null)
                logoutTime.setText(item.getLog_out_time());
            if (item.getService_time() != null)
                serviceTime.setText(item.getService_time());
            if (item.getWork_description() != null)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    description.setText(Html.fromHtml("<b>Description: </b>" + item.getWork_description(), Html.FROM_HTML_MODE_LEGACY));
                } else
                    description.setText(Html.fromHtml("<b>Description: </b>" + item.getWork_description()));


        }
    }
}