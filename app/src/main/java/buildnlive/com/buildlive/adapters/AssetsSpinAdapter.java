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
import buildnlive.com.buildlive.elements.Assets;

public class AssetsSpinAdapter extends ArrayAdapter<Assets> {

    private final ArrayList<Assets> item;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final int mResource;

    public AssetsSpinAdapter(@NonNull Context context, int resource, ArrayList<Assets> items) {
        super(context, resource,items);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        item = items;
    }

    @Override
    public int getCount(){
        return item.size();
    }

    @Override
    public Assets getItem(int position){
        return item.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public String getRentId(int position){
        return item.get(position).getItem_rent_id();
    }

    public String getAssetId(int position){
        return item.get(position).getAssets_id();
    }

    public String getName(int position) {
        return item.get(position).getName();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);

        TextView offTypeTv = (TextView) view.findViewById(R.id.title);

        Assets offerData = item.get(position);
        if(offerData.getReg_no().equals("")) {
            offTypeTv.setText(offerData.getName());
        }
        else {
            offTypeTv.setText(offerData.getReg_no());
        }

        return view;
    }


}


