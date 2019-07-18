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
import buildnlive.com.buildlive.elements.WorkList;

public class WorkListSpinAdapter extends ArrayAdapter<WorkList> {

    private final ArrayList<WorkList> item;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final int mResource;

    public WorkListSpinAdapter(@NonNull Context context, int resource, ArrayList<WorkList> items) {
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
    public WorkList getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getId(int position) {
        return item.get(position).getPms_project_work_list();
    }


    public String getName(int position) {
        return item.get(position).getName();
    }

    public String getQuantity(int position) {
        return item.get(position).getQty_left();
    }
    public String getUnits(int position) {
        return item.get(position).getUnits();
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

        WorkList offerData = item.get(position);
        offTypeTv.setText(offerData.getName());

        return view;
    }


}


