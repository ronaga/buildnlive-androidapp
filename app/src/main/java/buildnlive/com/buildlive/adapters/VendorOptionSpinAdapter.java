package buildnlive.com.buildlive.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.VendorOption;

public class VendorOptionSpinAdapter extends ArrayAdapter<VendorOption> {

    private final ArrayList<VendorOption> item;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final int mResource;

    public VendorOptionSpinAdapter(@NonNull Context context, int resource, ArrayList<VendorOption> items) {
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
    public VendorOption getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getVendorOption_id(int position) {
        return item.get(position).getOption_id();
    }

    public String getName(int position) {
        return item.get(position).getOption_label();
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

        VendorOption offerData = item.get(position);
            offTypeTv.setText(offerData.getOption_label());

        return view;
    }


}


