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
        import buildnlive.com.buildlive.elements.User;

public class UserFormSpinAdapter extends ArrayAdapter<User> {

    private final ArrayList<User> item;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final int mResource;

    public UserFormSpinAdapter(@NonNull Context context, int resource, ArrayList<User> items) {
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
    public User getItem(int position){
        return item.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    public String getUserName(int position) {
        return item.get(position).getUser_name();
    }
    public String getUserId(int position) {
        return item.get(position).getUser_id();
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

        User offerData = item.get(position);
            offTypeTv.setText(offerData.getUser_name());


        return view;
    }


}


