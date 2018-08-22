package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Packet;
import buildnlive.com.buildlive.utils.Utils;

public class ActivityImagesAdapter extends RecyclerView.Adapter<ActivityImagesAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Packet packet, int pos, View view);
    }

    private final List<Packet> items;
    private Context context;
    private final OnItemClickListener listener;

    public ActivityImagesAdapter(Context context, List<Packet> items, OnItemClickListener listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case 100:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_images, parent, false);
                return new ViewHolderAdd(v);
            case 101:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_images, parent, false);
                return new ViewHolderView(v);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(context, items.get(position), position, listener);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 100;
        else
            return 101;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolderView extends ViewHolder {

        private ImageView imageView;

        public ViewHolderView(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
        }

        @Override
        public void bind(final Context context, final Packet item, final int pos, final OnItemClickListener listener) {
            Glide.with(context).load(item.getName()).into(imageView);
        }
    }

    static class ViewHolderAdd extends ViewHolder {

        public ViewHolderAdd(View view) {
            super(view);
        }

        @Override
        public void bind(final Context context, final Packet item, final int pos, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, pos, itemView);
                }
            });
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final Context context, final Packet item, final int pos, final OnItemClickListener listener) {

        }
    }
}