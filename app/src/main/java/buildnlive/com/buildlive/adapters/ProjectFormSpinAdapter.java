package buildnlive.com.buildlive.adapters;

        import android.content.Context;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

        import java.util.ArrayList;

        import buildnlive.com.buildlive.R;
        import buildnlive.com.buildlive.elements.Assets;
        import buildnlive.com.buildlive.elements.Category;
        import buildnlive.com.buildlive.elements.IssueVendor;
        import buildnlive.com.buildlive.elements.Project;
        import buildnlive.com.buildlive.elements.User;

public class ProjectFormSpinAdapter extends ArrayAdapter<Project> {

    private final ArrayList<Project> item;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final int mResource;

    public ProjectFormSpinAdapter(@NonNull Context context, int resource, ArrayList<Project> items) {
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
    public Project getItem(int position){
        return item.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    public String getProjectName(int position) {
        return item.get(position).getName();
    }
    public String getProjectId(int position) {
        return item.get(position).getId();
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

        Project offerData = item.get(position);
        offTypeTv.setText(offerData.getName());


        return view;
    }


}


