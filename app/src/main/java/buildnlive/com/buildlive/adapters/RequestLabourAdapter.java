package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.LabourInfo;

public class RequestLabourAdapter extends RecyclerView.Adapter<RequestLabourAdapter.ViewHolder> implements Filterable {
    public interface OnItemSelectedListener {
        void onItemCheck(boolean checked);

        void onItemInteract(int pos, int flag);
    }

    private List<LabourInfo> items,filteredItems;
    private Context context;
    private RequestLabourAdapter.OnItemSelectedListener listener;

    public RequestLabourAdapter(Context context, List<LabourInfo> users, RequestLabourAdapter.OnItemSelectedListener listener) {
        this.items = users;
        this.filteredItems=users;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestLabourAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_labour_item, parent, false);
        return new RequestLabourAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (RequestLabourAdapter.ViewHolder.CHECKOUT) {
            if (items.get(position).isUpdated())
                holder.bind(context, filteredItems.get(position), position, listener);
        } else {
            holder.bind(context, filteredItems.get(position), position, listener);
        }
        }



    @Override
    public int getItemCount(){
        return filteredItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private static int checkCount = 0;
        public static boolean CHECKOUT = false;
        private TextView name, availabilty;
        private EditText quantity;
        private CheckBox check;
        private ImageButton close;


        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            availabilty = view.findViewById(R.id.available);
            check = view.findViewById(R.id.check);
            quantity = view.findViewById(R.id.quantity);
            close = view.findViewById(R.id.close);

        }

        public void bind(final Context context, final LabourInfo item, final int pos, final RequestLabourAdapter.OnItemSelectedListener listener) {
            name.setText(item.getName());
            if (CHECKOUT) {
                availabilty.setVisibility(View.GONE);
                check.setChecked(true);
                check.setEnabled(false);
                quantity.setEnabled(false);
                quantity.setText(item.getQuantity());
                item.setUpdated(false);
                check.setVisibility(View.GONE);
                close.setVisibility(View.VISIBLE);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemInteract(pos, 100);
                    }
                });
            } else {
                availabilty.setText(item.getQuantity());
                check.setVisibility(View.VISIBLE);
                close.setVisibility(View.GONE);
                check.setChecked(false);
                check.setEnabled(true);
                quantity.setEnabled(true);
                check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (quantity.getText().toString().length() > 0) {
                                final int a= Integer.parseInt(quantity.getText().toString());
                                final int b=Integer.parseInt(item.getQuantity());

                                if(a>b){
                                    Toast.makeText(context, "Please enter correct quantity", Toast.LENGTH_SHORT).show();
                                    buttonView.setChecked(false);
                                }
                                else {
                                    item.setQuantity(quantity.getText().toString());
                                    checkCount++;
                                    listener.onItemCheck(true);
                                    item.setUpdated(true);
                                }
                            } else {
                                Toast.makeText(context, "Please enter quantity", Toast.LENGTH_SHORT).show();
                                buttonView.setChecked(false);
                            }
                        } else {
                            checkCount--;
                            item.setUpdated(false);
                            if (checkCount > 0) {
                                listener.onItemCheck(true);
                            } else {
                                listener.onItemCheck(false);
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredItems = items;
                } else {
                    List<LabourInfo> filteredList = new ArrayList<>();
                    for (LabourInfo row : items) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
//                            Log.i("Filter",row.getName());
                        }
                    }

                    filteredItems = filteredList;
//                    Log.i("filteredItems",filteredItems.get(0).getName());
//                    Log.i("filteredItems",filteredItems.get(1).getName());

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredItems;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredItems = (ArrayList<LabourInfo>) filterResults.values;
                notifyDataSetChanged();
            }

        };
    }
}
