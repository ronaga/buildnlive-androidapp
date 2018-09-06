package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Category;
import buildnlive.com.buildlive.elements.IndentItem;

public class ItemSpinAdapter extends ArrayAdapter<IndentItem> {

    private final ArrayList<IndentItem> item;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final int mResource;

    public ItemSpinAdapter(@NonNull Context context, int resource, ArrayList<IndentItem> items) {
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
    public IndentItem getItem(int position){
        return item.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public String getID(int position){
        return item.get(position).getId();
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

        IndentItem offerData = item.get(position);

        offTypeTv.setText(offerData.getName());

        return view;
    }


//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
//        TextView label = (TextView) super.getView(position, convertView, parent);
//        label.setTextColor(Color.BLACK);
//        // Then you can get the current item using the values array (Users array) and the current position
//        // You can NOW reference each method you has created in your bean object (User class)
//        label.setText(item.get(position).getName());
//
//        // And finally return your dynamic (or custom) view for each spinner item
//        return label;
//    }
//
//    // And here is when the "chooser" is popped up
//    // Normally is the same view, but you can customize it if you want
//    @Override
//    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
//        label.setTextColor(Color.BLACK);
//        label.setText(item.get(position).getName());
//
//        return label;
//    }


}


