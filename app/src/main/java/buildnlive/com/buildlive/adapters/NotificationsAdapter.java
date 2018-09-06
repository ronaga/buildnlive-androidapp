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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

import buildnlive.com.buildlive.App;
import buildnlive.com.buildlive.Interfaces;
import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.console;
import buildnlive.com.buildlive.elements.Notification;
import buildnlive.com.buildlive.elements.Plans;
import buildnlive.com.buildlive.elements.Project;
import buildnlive.com.buildlive.utils.Config;
import buildnlive.com.buildlive.utils.GlideApp;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class NotificationsAdapter extends RealmRecyclerViewAdapter<Notification,NotificationsAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Notification notification, int pos, View view);
    }

    private final List<Notification> items;
    private Context context;
    private final OnItemClickListener listener;

    public NotificationsAdapter(Context context, OrderedRealmCollection<Notification> users, OnItemClickListener listener) {
        super(users, true);
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_notification, parent, false);
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
        private TextView title,quantity,label,details,date;
        private ImageView imageView;
        private LinearLayout buttons,reply;
        private Button approve,reject,review,receive,notReceive;

        public ViewHolder(View view) {
            super(view);
            imageView= view.findViewById(R.id.level);
            details = view.findViewById(R.id.details);
            label = view.findViewById(R.id.name);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.date);
//            quantity = view.findViewById(R.id.quantity);
            buttons = view.findViewById(R.id.buttons);
            approve = view.findViewById(R.id.approve);
            reject = view.findViewById(R.id.reject);
            review = view.findViewById(R.id.review);
            receive = view.findViewById(R.id.receive);
            notReceive = view.findViewById(R.id.not_receive);
            reply = view.findViewById(R.id.reply);
        }

        public void bind(final Context context, final Notification item, final int pos, final OnItemClickListener listener) {
            if(item.getLevel().equals("urgent"))
            {
                imageView.setVisibility(View.VISIBLE);
            }
            switch (item.getType()){
                case "update":
                    reply.setVisibility(View.GONE);
                    buttons.setVisibility(View.GONE);
                    details.setText(item.getUser());
                    title.setText(item.getLabel());
                    label.setText(item.getText());
                    date.setText(item.getDate());

                    break;
                case "reply":
                    buttons.setVisibility(View.GONE);
                    details.setText(item.getUser());
                    title.setText(item.getLabel());
                    label.setText(item.getText());
                    date.setText(item.getDate());
                    notReceive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onItemClick(item, pos, view);
                        }

                    });
                    receive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onItemClick(item, pos, view);
                        }

                    });
                    break;
                case "approval":
                    reply.setVisibility(View.GONE);
                    details.setText(item.getUser());
                    title.setText(item.getLabel());
                    label.setText(item.getText());
                    date.setText(item.getDate());
                    approve.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onItemClick(item, pos, view);
                        }

                    });
                    reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onItemClick(item, pos, view);
                        }

                    });
                    review.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onItemClick(item, pos, view);
                        }

                    });
                    break;
            }

        }
    }

}