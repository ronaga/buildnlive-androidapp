package buildnlive.com.buildlive.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Plans;
import buildnlive.com.buildlive.utils.GlideApp;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class PlansAdapter extends RealmRecyclerViewAdapter<Plans, PlansAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Plans plans, int pos, View view);
    }

    private final List<Plans> items;
    private Context context;
    private final OnItemClickListener listener;

    public PlansAdapter(Context context, OrderedRealmCollection<Plans> users, OnItemClickListener listener) {
        super(users, true);
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_plan, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(context, items.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        private TextView text;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            text = view.findViewById(R.id.text);
        }

        public void bind(final Context context, final Plans item, final int pos, final OnItemClickListener listener) {
            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "BL");
            File f = new File(folder, item.getName());
            if (f.exists() && f.length() > 0)
                Glide.with(context).load(f).into(imageView);
            else {
                GlideApp.with(context).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).load(item.getUrl()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Glide.with(context).load(resource).into(imageView);
                        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "BL");
                        if (!folder.exists()) {
                            folder.mkdirs();
                        }
                        folder = new File(folder, item.getName());
                        if (folder.exists()) {
                            folder.delete();
                        }
                        try {
                            FileOutputStream fos = new FileOutputStream(folder);
                            resource.compress(Bitmap.CompressFormat.JPEG, 40, fos);
                            fos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            text.setText(item.getDescription());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, pos, itemView);
                }
            });
        }
    }
}