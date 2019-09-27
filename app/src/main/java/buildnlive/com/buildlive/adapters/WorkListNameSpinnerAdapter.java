package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.WorkNameItem;

public class WorkListNameSpinnerAdapter extends ArrayAdapter<WorkNameItem> {

    private final ArrayList<WorkNameItem> item;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final int mResource;

    public WorkListNameSpinnerAdapter(@NonNull Context context, int resource, ArrayList<WorkNameItem> items) {
        super(context, resource, items);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        item = items;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public WorkNameItem getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getId(int position) {
        return item.get(position).getWork_activity_id();
    }


    public String getName(int position) {
        return item.get(position).getWork_activity_name();
    }

    public String getUnits(int position) {
        return item.get(position).getWork_activity_units();
    }
    public String getQty(int position) {
        return item.get(position).getWork_qty();
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);

        TextView offTypeTv = (TextView) view.findViewById(R.id.title);

        WorkNameItem offerData = item.get(position);
        offTypeTv.setText(offerData.getWork_activity_name());

        return view;
    }


}


