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
import buildnlive.com.buildlive.elements.LabourTrade;

public class LabourTradeSpinAdapter extends ArrayAdapter<LabourTrade> {

    private final ArrayList<LabourTrade> item;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final int mResource;

    public LabourTradeSpinAdapter(@NonNull Context context, int resource, ArrayList<LabourTrade> items) {
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
    public LabourTrade getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getLabourTrade_id(int position) {
        return item.get(position).getTrade_id();
    }

    public String getName(int position) {
        return item.get(position).getTrade_name();
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

        LabourTrade offerData = item.get(position);
        offTypeTv.setText(offerData.getTrade_name());

        return view;
    }


}


