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
import buildnlive.com.buildlive.elements.ShowWorkListItem;

public class ShowWorkListSpinnerAdapter extends ArrayAdapter<ShowWorkListItem> {

    private final ArrayList<ShowWorkListItem> item;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final int mResource;

    public ShowWorkListSpinnerAdapter(@NonNull Context context, int resource, ArrayList<ShowWorkListItem> items) {
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
    public ShowWorkListItem getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getId(int position) {
        return item.get(position).getProject_work_id();
    }


    public String getName(int position) {
        return item.get(position).getWork_list_name();
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

        ShowWorkListItem offerData = item.get(position);
        offTypeTv.setText(offerData.getWork_list_name());

        return view;
    }


}


